package com.labsynch.cmpdreg.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFReader;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFReaderFactory;
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.domain.StereoCategory;
import com.labsynch.cmpdreg.dto.Metalot;
import com.labsynch.cmpdreg.dto.MetalotReturn;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.service.MetalotService;
import com.labsynch.cmpdreg.service.SetupService;

@RequestMapping("/adminsetup")
@Controller
public class SetupDBController {

	private static final Logger logger = LoggerFactory.getLogger(SetupDBController.class);

	@Autowired
	private MetalotService metalotServ;
	
	@Autowired
	private SetupService setupServ;
	
	@Autowired
	CmpdRegMoleculeFactory cmpdRegMoleculeFactory;

	@Autowired
	CmpdRegSDFReaderFactory sdfReaderFactory;
	
//	@RequestMapping(value = "loadplainsdf", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<String> loadCorpNames(@RequestBody String fileNamePath) {
//		HttpHeaders headers= new HttpHeaders();
//        headers.add("Content-Type", "application/text");
//        
//        try {
//			setupServ.loadCorpNames();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//	        return new ResponseEntity<String>("error saving new corpNames. File not found.", headers, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//        
//        return new ResponseEntity<String>("saved new corpNames", headers, HttpStatus.OK);
//
//	}
//	
	@RequestMapping(value = "loadplainsdf", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> loadCmpdsFromSDF(@RequestBody String fileNamePath) {
		
    	//simple utility to load compounds without any properties

    	String fileName = fileNamePath;
    	
	    int molCount = 0;
	    
		    // Open an input stream
		    try {
			    CmpdRegSDFReader mi = sdfReaderFactory.getCmpdRegSDFReader(fileName);
			    CmpdRegMolecule mol = null;

			    while ((mol = mi.readNextMol()) != null) {
			    	
			    	Metalot metaLot = new Metalot();
			    	Lot lot = new Lot();
			    	SaltForm saltForm = new SaltForm();
			    	Parent parent = new Parent();		
			    	
			    	lot.setAsDrawnStruct(mol.getMolStructure());

//			    	mol.clearExtraLabels();
			    	parent.setMolStructure(mol.getMolStructure());
			    	Scientist chemist = Scientist.findScientistsByCodeEquals("cchemist").getSingleResult();
			    	String noteBookInfo = "1234-123";
			    	StereoCategory stereoCategory = StereoCategory.findStereoCategorysByCodeEquals("achiral").getSingleResult();
			    	
			    	parent.setChemist(chemist);
			    	parent.setStereoCategory(stereoCategory);
			    	
			    	saltForm.setParent(parent);
			    	saltForm.setChemist(chemist);
			    	saltForm.setMolStructure("");

			    	lot.setSynthesisDate(new Date());
			    	lot.setChemist(chemist);
			    	lot.setNotebookPage(noteBookInfo);
			    	lot.setSaltForm(saltForm);
			    	lot.setLotMolWeight(mol.getMass());
			    	lot.setLotMolWeight(mol.getMass());
			    	lot.setIsVirtual(false);

			    	metaLot.setLot(lot);
			    	
			    	MetalotReturn results = metalotServ.save(metaLot);
			    	logger.debug("lot saved: " + results.toJson());
			    	
			    	molCount++;
			    }	
			    
			    mi.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CmpdRegMolFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
		HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");

        return new ResponseEntity<String>("saved " + molCount + " new structures", headers, HttpStatus.CREATED);
    }


}
