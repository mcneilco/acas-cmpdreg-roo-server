package com.labsynch.cmpdreg.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.ParentAlias;
import com.labsynch.cmpdreg.domain.QcCompound;
import com.labsynch.cmpdreg.dto.CodeTableDTO;
import com.labsynch.cmpdreg.dto.ParentDTO;
import com.labsynch.cmpdreg.dto.ParentValidationDTO;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.utils.Configuration;
import com.labsynch.cmpdreg.utils.MoleculeUtil;

import chemaxon.formats.MolExporter;
import chemaxon.formats.MolFormatException;
import chemaxon.struc.Molecule;
import chemaxon.util.MolHandler;

@Service
public class ParentServiceImpl implements ParentService {

	Logger logger = LoggerFactory.getLogger(ParentServiceImpl.class);

	public static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

	@Autowired
	public ChemStructureService chemStructureService;

	@Autowired
	public ParentLotService parentLotService;

	@Autowired
	public ParentStructureService parentStructureService;

	@Autowired
	public ParentAliasService parentAliasService;

	@Override
	@Transactional
	public ParentValidationDTO validateUniqueParent(Parent queryParent) throws MolFormatException {
		ParentValidationDTO validationDTO = new ParentValidationDTO();

		if (queryParent.getCorpName() == null) validationDTO.getErrors().add(new ErrorMessage("error","Must provide corpName for parent to be validated"));
		if (queryParent.getStereoCategory() == null) validationDTO.getErrors().add(new ErrorMessage("error","Must provide stereo category for parent to be validated"));
		if (queryParent.getStereoCategory().getCode().equalsIgnoreCase("see_comments") && (queryParent.getStereoComment() == null || queryParent.getStereoComment().length()==0)){
			validationDTO.getErrors().add(new ErrorMessage("error","Stereo category is See Comments, but no stereo comment provided"));
		}
		if (chemStructureService.checkForSalt(queryParent.getMolStructure())){
			if (queryParent.getIsMixture() != null){
				if (!queryParent.getIsMixture()){
					validationDTO.getErrors().add(new ErrorMessage("error","Multiple fragments found. Please register the neutral base parent or mark as a Mixture"));
				}
			}else{
				validationDTO.getErrors().add(new ErrorMessage("error","Multiple fragments found. Please register the neutral base parent or mark as a Mixture"));
			}
		}
		if (!validationDTO.getErrors().isEmpty()){
			for (ErrorMessage error : validationDTO.getErrors()){
				logger.error(error.getMessage());
			}
			return validationDTO;
		}
		Collection<ParentDTO> dupeParents = new HashSet<ParentDTO>();
		int[] dupeParentList = chemStructureService.checkDupeMol(queryParent.getMolStructure(), "Parent_Structure", "Parent");
		if (dupeParentList.length > 0){
			searchResultLoop:
				for (int foundParentCdId : dupeParentList){
					List<Parent> foundParents = Parent.findParentsByCdId(foundParentCdId).getResultList();
					for (Parent foundParent : foundParents){
						//same structure hits
						if (queryParent.getCorpName().equals(foundParent.getCorpName())){
							//corpName match => this is the parent we're searching on. ignore this match.
							continue;
						}else{
							//same structure, different corpName => check stereo category
							if(queryParent.getStereoCategory().getCode().equalsIgnoreCase(foundParent.getStereoCategory().getCode())){
								//same structure and stereo category => check stereo comment
								if (queryParent.getStereoComment() == null && foundParent.getStereoComment() == null){
									//both null - stereo comments match => this is a dupe
									ParentDTO foundDupeParentDTO = new ParentDTO();
									foundDupeParentDTO.setCorpName(foundParent.getCorpName());
									foundDupeParentDTO.setStereoCategory(foundParent.getStereoCategory());
									foundDupeParentDTO.setStereoComment(foundParent.getStereoComment());
									dupeParents.add(foundDupeParentDTO);
								}else if (queryParent.getStereoComment() == null || foundParent.getStereoComment() == null){
									//one null, the other is not => not a dupe
									continue;
								}else if (queryParent.getStereoComment().equalsIgnoreCase(foundParent.getStereoComment())){
									//same stereo comment => this is a dupe
									ParentDTO foundDupeParentDTO = new ParentDTO();
									foundDupeParentDTO.setCorpName(foundParent.getCorpName());
									foundDupeParentDTO.setStereoCategory(foundParent.getStereoCategory());
									foundDupeParentDTO.setStereoComment(foundParent.getStereoComment());
									dupeParents.add(foundDupeParentDTO);
								}else{
									//different stereo comment => not a dupe
									continue;
								}
							}else{
								//different stereo category => not a dupe
								continue;
							}
						}
					}
				}
		}
		if (!dupeParents.isEmpty()){
			validationDTO.setParentUnique(false);
			validationDTO.setDupeParents(dupeParents);
			return validationDTO;
		}else{
			validationDTO.setParentUnique(true);
			validationDTO.setAffectedLots(parentLotService.getCodeTableLotsByParentCorpName(queryParent.getCorpName()));
			return validationDTO;
		}
	}

	@Override
	public Collection<CodeTableDTO> updateParent(Parent parent){
		Set<ParentAlias> parentAliases = parent.getParentAliases();
		parent = parentStructureService.update(parent);
		//save parent aliases
		logger.info("--------- Number of parentAliases to save: " + parentAliases.size());
		parent = parentAliasService.updateParentAliases(parent, parentAliases);
		if (logger.isDebugEnabled()) logger.debug("Parent aliases after save: "+ ParentAlias.toJsonArray(parent.getParentAliases()));
		Collection<CodeTableDTO> affectedLots = parentLotService.getCodeTableLotsByParentCorpName(parent.getCorpName());
		return affectedLots;
	}

	@Override
	public int restandardizeAllParentStructures() throws MolFormatException, IOException{
		List<Long> parentIds = Parent.getParentIds();
		Parent parent;
		List<Lot> lots;
		Lot lot;
		String originalStructure = null;
		String result;
		logger.info("number of parents to restandardize: " + parentIds.size());
		for  (Long parentId : parentIds){
			parent = Parent.findParent(parentId);
			lots = Lot.findLotsByParent(parent).getResultList();
			lot = lots.get(0);
			if (lots.size() > 0 && lot.getAsDrawnStruct() != null){
				originalStructure = lot.getAsDrawnStruct();				
			} else {
				logger.warn("Did not find the asDrawnStruct for parent: " + parentId + "  " + parent.getCorpName());
				originalStructure = parent.getMolStructure();
			}
			result = chemStructureService.standardizeStructure(originalStructure);
			parent.setMolStructure(result);
			parent = parentStructureService.update(parent);
		}

		return parentIds.size();
	}
	
	@Override
	public int restandardizeParentStructures(List<Long> parentIds) throws MolFormatException, IOException{
		Parent parent;
		List<Lot> lots;
		Lot lot;
		String originalStructure = null;
		String result;
		logger.info("number of parents to restandardize: " + parentIds.size());
		for  (Long parentId : parentIds){
			parent = Parent.findParent(parentId);
			lots = Lot.findLotsByParent(parent).getResultList();
			lot = lots.get(0);
			if (lots.size() > 0 && lot.getAsDrawnStruct() != null){
				originalStructure = lot.getAsDrawnStruct();				
			} else {
				logger.warn("Did not find the asDrawnStruct for parent: " + parentId + "  " + parent.getCorpName());
				originalStructure = parent.getMolStructure();
			}
			result = chemStructureService.standardizeStructure(originalStructure);
			parent.setMolStructure(result);
			parent = parentStructureService.update(parent);
		}

		return parentIds.size();
	}
	
	@Override
	public int restandardizeParentStructsWithDisplayChanges() throws MolFormatException, IOException{
		List<Long> parentIds = QcCompound.findParentsWithDisplayChanges().getResultList();
		int result = restandardizeParentStructures(parentIds);
		return result;
	}


	@Override
	public int findPotentialDupeParentStructures(String dupeCheckFile){
		List<Long> parentIds = Parent.getParentIds();
		Parent parent;
		List<Parent> dupeParents = new ArrayList<Parent>();
		int[] hits;
		logger.info("number of parents to check: " + parentIds.size());
		FileOutputStream dupeOutputStream = null;
		try {
			dupeOutputStream = new FileOutputStream (dupeCheckFile, false);
			MolExporter dupeMolExporter = new MolExporter(dupeOutputStream, "sdf");
			for  (Long parentId : parentIds){
				parent = Parent.findParent(parentId);
				hits = chemStructureService.searchMolStructures(parent.getMolStructure(), "Parent_Structure", "DUPLICATE_TAUTOMER");
				if (hits.length == 0){
					logger.error("did not find a match for parentId: " + parentId + "   parent: " + parent.getCorpName());
				} 
				for (int hit:hits){
					List<Parent> searchResultParents = Parent.findParentsByCdId(hit).getResultList();
					for (Parent searchResultParent : searchResultParents){
						if (searchResultParent.getCorpName().equalsIgnoreCase(parent.getCorpName())){
							logger.debug("found the same parent" + parent.getCorpName());
						} else {
							logger.info("found dupe parents");
							logger.info("query: " + parent.getCorpName() + "     dupe: " + searchResultParent.getCorpName());
							dupeParents.add(searchResultParent);
							MolHandler mh = new MolHandler(parent.getMolStructure());
							Molecule parentMol = mh.getMolecule();
							MoleculeUtil.setMolProperty(parentMol, "corpName", parent.getCorpName());
							MoleculeUtil.setMolProperty(parentMol, "stereoCategory", parent.getStereoCategory().getName());
							MoleculeUtil.setMolProperty(parentMol, "stereoComment", parent.getStereoComment());
							MoleculeUtil.setMolProperty(parentMol, "dupeCorpName", searchResultParent.getCorpName());
							MoleculeUtil.setMolProperty(parentMol, "dupeStereoCategory", searchResultParent.getStereoCategory().getName());
							MoleculeUtil.setMolProperty(parentMol, "dupeStereoComment", searchResultParent.getStereoComment());

							//							MoleculeUtil.setMolProperty(parentMol, "dupeMolStructure", searchResultParent.getMolStructure());
							MoleculeUtil.setMolProperty(parentMol, "dupeMolSmiles", chemStructureService.toSmiles(searchResultParent.getMolStructure()));

							dupeMolExporter.write(parentMol);
						}
					}
				}
			}
			dupeOutputStream.close();
			dupeMolExporter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		logger.info("total number of potential dupes found: " + dupeParents.size());

		return (dupeParents.size());
		//export dupes to a SDF file for review

	}


	@Override
	public int findDupeParentStructures(String dupeCheckFile){
		List<Long> parentIds = Parent.getParentIds();
		Parent parent;
		List<Parent> dupeParents = new ArrayList<Parent>();
		FileOutputStream dupeOutputStream = null;
		int[] hits;
		logger.info("number of parents to check: " + parentIds.size());
		try {
			dupeOutputStream = new FileOutputStream (dupeCheckFile, false);
			MolExporter dupeMolExporter = new MolExporter(dupeOutputStream, "sdf");
			for  (Long parentId : parentIds){
				parent = Parent.findParent(parentId);
				hits = chemStructureService.searchMolStructures(parent.getMolStructure(), "Parent_Structure", "DUPLICATE_TAUTOMER");
				if (hits.length == 0){
					logger.error("did not find a match for parentId: " + parentId + "   parent: " + parent.getCorpName());
				} 
				String dupeCorpNames = "";
				boolean firstDupeHit = true;
				for (int hit:hits){
					List<Parent> searchResultParents = Parent.findParentsByCdId(hit).getResultList();
					for (Parent searchResult : searchResultParents){
						if (searchResult.getCorpName().equalsIgnoreCase(parent.getCorpName())){
							logger.debug("found the same parent: " + parent.getCorpName());
						} else {
							if (searchResult.getStereoCategory() == parent.getStereoCategory()
									&& searchResult.getStereoComment().equalsIgnoreCase(parent.getStereoComment())){
								logger.info("found dupe parents -- matching structure, stereo category, and stereo comments");
								logger.info("query: " + parent.getCorpName() + "     dupe: " + searchResult.getCorpName());
								if (!firstDupeHit) dupeCorpNames = dupeCorpNames.concat(";");
								dupeCorpNames = dupeCorpNames.concat(searchResult.getCorpName());
								firstDupeHit = false;
								dupeParents.add(searchResult);							
								MolHandler mh = new MolHandler(parent.getMolStructure());
								Molecule parentMol = mh.getMolecule();
								MoleculeUtil.setMolProperty(parentMol, "corpName", parent.getCorpName());
								MoleculeUtil.setMolProperty(parentMol, "dupeCorpName", searchResult.getCorpName());
								MoleculeUtil.setMolProperty(parentMol, "stereoCategory", searchResult.getStereoCategory().getName());
								MoleculeUtil.setMolProperty(parentMol, "stereoComment", searchResult.getStereoComment());
								MoleculeUtil.setMolProperty(parentMol, "dupeMolSmiles", chemStructureService.toSmiles(searchResult.getMolStructure()));
								dupeMolExporter.write(parentMol);

							} else {
								logger.info("found different stereo codes and comments");
							}
						}
					}
				}
			}
			dupeOutputStream.close();
			dupeMolExporter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("total number of dupes found: " + dupeParents.size());
		return (dupeParents.size());
	}

	// code to check the structures -- original as drawn mol --> standardized versus the original struct
	// may need a qc_compound table to store the mols and a list of identified dupes
	// id, runNumber, date, parent_id, corpName, dupeCount, dupeCorpName, asDrawnStruct, preMolStruct, postMolStruct, comment

	@Override
	@Transactional
	public void qcCheckParentStructures() throws MolFormatException, IOException{
		List<Long> parentIds = Parent.getParentIds();
		Parent parent;
		QcCompound qcCompound;
		List<Parent> dupeParents = new ArrayList<Parent>();
		int[] hits;
		int nonMatchingCmpds = 0;
		logger.info("number of parents to check: " + parentIds.size());
		Date qcDate = new Date();
		String asDrawnStruct;
		for  (Long parentId : parentIds){
			parent = Parent.findParent(parentId);
			qcCompound = new QcCompound();
			qcCompound.setQcDate(qcDate);
			qcCompound.setParentId(parent.getId());
			qcCompound.setCorpName(parent.getCorpName());
			List<Lot> queryLots = Lot.findLotByParentAndLowestLotNumber(parent).getResultList();
			if (queryLots.size() != 1) logger.error("!!!!!!!!!!!!  odd lot number size   !!!!!!!!!  " + queryLots.size() + "  saltForm: " + parent.getId());
			if (queryLots.size() > 0){
				asDrawnStruct = queryLots.get(0).getAsDrawnStruct();
			} else {
				asDrawnStruct = parent.getMolStructure();
			}
			qcCompound.setMolStructure(chemStructureService.standardizeStructure(asDrawnStruct));				
			boolean matching = chemStructureService.compareStructures(asDrawnStruct, qcCompound.getMolStructure(), "DUPLICATE");
			if (!matching){
				qcCompound.setDisplayChange(true);
				logger.info("the compounds are NOT matching: " + parent.getCorpName());
				nonMatchingCmpds++;
			}
			qcCompound.persist();
		}
		logger.info("total number of nonMatching compounds: " + nonMatchingCmpds);
	}

	@Override
	public void dupeCheckQCStructures(){

		List<Long> qcIds = QcCompound.findAllIds().getResultList();
		logger.info("number of qcCompounds found: " + qcIds.size());
		if (qcIds.size() > 0){
			int[] hits;	
			List<Long> testQcIds = qcIds.subList(0, 10);
			logger.info("size of test subList" + testQcIds.size());
			QcCompound qcCompound;
			Parent queryParent;
			for (Long qcId : testQcIds){
				qcCompound = QcCompound.findQcCompound(qcId);
				queryParent = Parent.findParent(qcCompound.getParentId());
				logger.info("query compound: " + qcCompound.getCorpName());
				hits = chemStructureService.searchMolStructures(qcCompound.getMolStructure(), "Parent_Structure", "DUPLICATE_TAUTOMER");
				for (int hit:hits){
					List<Parent> searchResultParents = Parent.findParentsByCdId(hit).getResultList();
					for (Parent searchResultParent : searchResultParents){
						if (searchResultParent.getCorpName().equalsIgnoreCase(queryParent.getCorpName())){
							//logger.info("found the same parent");
						} else {
							if (searchResultParent.getStereoCategory() == queryParent.getStereoCategory()
									&& searchResultParent.getStereoComment().contentEquals(queryParent.getStereoComment())){
								logger.info("found dupe parents");
								logger.info("query: " + qcCompound.getCorpName() + "     dupe: " + searchResultParent.getCorpName());
								//								dupeParents.add(searchResultParent);							
							} else {
								logger.info("found different stereo codes and comments");
							}

						}
					}

				}

			}			
		}


	}


}

