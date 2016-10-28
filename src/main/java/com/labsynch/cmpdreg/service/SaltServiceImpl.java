package com.labsynch.cmpdreg.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Salt;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.utils.Configuration;
import com.labsynch.cmpdreg.utils.MoleculeUtil;

import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

@Service
public class SaltServiceImpl implements SaltService {

	Logger logger = LoggerFactory.getLogger(SaltServiceImpl.class);

	public static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

	@Autowired
	private ChemStructureService saltStructServ;

	@Transactional
	@Override
	public int loadSalts(String saltSD_fileName) {
		//simple utility to load salts
		//fileName = "src/test/resources/Initial_Salts.sdf";
		FileInputStream fis;	
		int savedSaltCount = 0;
		try {
			// Open an input stream
			fis = new FileInputStream (saltSD_fileName);
			MolImporter mi = new MolImporter(fis);
			Molecule mol = null;
			Long saltCount = 0L;
			while ((mol = mi.read()) != null) {
				// save salt if no existing salt with the same Abbrev -- could do match by other properties
				saltCount = Salt.countFindSaltsByAbbrevLike(MoleculeUtil.getMolProperty(mol, "code"));
				if (saltCount < 1){
					saveSalt(mol);		
					savedSaltCount++;			
				}
			}	
			mi.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return savedSaltCount;
	}

	@Transactional
	private void saveSalt(Molecule mol) throws IOException {
		Salt salt = new Salt();
		salt.setMolStructure(MoleculeUtil.exportMolAsText(mol, "mol"));
		salt.setOriginalStructure(MoleculeUtil.exportMolAsText(mol, "mol"));
		salt.setAbbrev(MoleculeUtil.getMolProperty(mol, "code"));
		salt.setName(MoleculeUtil.getMolProperty(mol, "Name"));
		salt.setFormula(mol.getFormula());
		salt.setMolWeight(mol.getMass());

		logger.debug("salt code: " + salt.getAbbrev());
		logger.debug("salt name: " + salt.getName());
		logger.debug("salt structure: " + salt.getMolStructure());

		int[] queryHits = saltStructServ.searchMolStructures(salt.getMolStructure(), "Salt_Structure", "salt", "DUPLICATE_NO_TAUTOMER");
		Integer cdId = 0;
		if (queryHits.length > 0){
			cdId = 0;
		} else {
			cdId = saltStructServ.saveStructure(salt.getMolStructure(), "Salt_Structure");			
		}
		salt.setCdId(cdId);


		if (salt.getCdId() > 0 && salt.getCdId() != -1){
			salt.persist();
		} else {
			logger.error("Could not save the salt: " + salt.getAbbrev());
		}		
	}

}

