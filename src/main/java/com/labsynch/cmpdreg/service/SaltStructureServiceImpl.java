package com.labsynch.cmpdreg.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.domain.Salt;
import com.labsynch.cmpdreg.utils.Configuration;

@Service
public class SaltStructureServiceImpl implements SaltStructureService {

	@Autowired
	private ChemStructureService chemStructureService;

	Logger logger = LoggerFactory.getLogger(SaltStructureService.class);



	@Override
	public Salt saveStructure(Salt salt) {		

		CmpdRegMolecule mol = chemStructureService.toMolecule(salt.getMolStructure());
		salt.setOriginalStructure(salt.getMolStructure());
		salt.setMolStructure(mol.getMolStructure());
		salt.setFormula(mol.getFormula());
		if (Configuration.getConfigInfo().getMetaLot().isUseExactMass()){
			salt.setMolWeight(mol.getExactMass());
		}else{
			salt.setMolWeight(mol.getMass());
		}
		salt.setCharge(mol.getTotalCharge());

		logger.debug("salt code: " + salt.getAbbrev());
		logger.debug("salt name: " + salt.getName());
		logger.debug("salt structure: " + salt.getMolStructure());

		// check if existing salt with the same name and code
		
		List<Salt> salts = Salt.findSaltsByAbbrevEqualsAndNameEquals(salt.getAbbrev(), salt.getName()).getResultList();
		if (salts.size() > 0){
			logger.error("found another salt with the same name and abbrev");
			salt.setCdId(0);
			int[] dupeMols = chemStructureService.checkDupeMol(salt.getMolStructure(), "Salt_Structure", "salt");
			logger.debug("number of matching salt structures: " + dupeMols.length);
		} else {
			boolean checkForDupes = true;
			int cdId = chemStructureService.saveStructure(salt.getMolStructure(), "Salt_Structure", checkForDupes);			
			salt.setCdId(cdId);			
		}

		return salt;

	}



	@Override
	public Salt update(Salt salt) {
		CmpdRegMolecule mol = chemStructureService.toMolecule(salt.getMolStructure());
		salt.setOriginalStructure(salt.getMolStructure());
		salt.setMolStructure(mol.getMolStructure());
		salt.setFormula(mol.getFormula());
		salt.setMolWeight(mol.getMass());
		salt.setCharge(mol.getTotalCharge());

		logger.debug("salt code: " + salt.getAbbrev());
		logger.debug("salt name: " + salt.getName());
		logger.debug("salt structure: " + salt.getMolStructure());

		boolean updated = chemStructureService.updateStructure(mol, "Salt_Structure", salt.getCdId());
		salt.merge();
		
		if (updated){
			return Salt.findSalt(salt.getId());
		} else {
			return null;
		}
	}

}
