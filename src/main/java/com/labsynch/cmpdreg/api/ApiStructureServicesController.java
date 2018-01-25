package com.labsynch.cmpdreg.api;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.cmpdreg.domain.ParentAliasKind;
import com.labsynch.cmpdreg.domain.ParentAliasType;
import com.labsynch.cmpdreg.dto.HydrogenizeMolInputDTO;
import com.labsynch.cmpdreg.dto.MolCleanInputDTO;
import com.labsynch.cmpdreg.dto.MolConvertInputDTO;
import com.labsynch.cmpdreg.dto.MolConvertOutputDTO;
import com.labsynch.cmpdreg.dto.MolInputDTO;
import com.labsynch.cmpdreg.dto.ParentAliasDTO;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.exceptions.StandardizerException;
import com.labsynch.cmpdreg.service.ChemStructureService;
import com.labsynch.cmpdreg.service.SearchFormService;

@RequestMapping(value = {"/api/v1/structureServices"})
@Controller
public class ApiStructureServicesController {
	
	Logger logger = LoggerFactory.getLogger(ApiStructureServicesController.class);

	@Autowired
	private ChemStructureService chemStructureService;

	@RequestMapping(value = "/standardize", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> standardize(@RequestBody String json){
		if (logger.isDebugEnabled()) logger.debug("incoming json from molconvert: " + json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		MolConvertInputDTO inputDTO = MolConvertInputDTO.fromJsonToMolConvertInputDTO(json);
		MolConvertOutputDTO output = new MolConvertOutputDTO();
		String standardizedMol= "" ;
		try {
			standardizedMol = chemStructureService.standardizeStructure(inputDTO.getStructure());
		} catch (IOException e) {
			logger.error(e.toString());
			return new ResponseEntity<String>("IO ERROR", headers, HttpStatus.BAD_REQUEST);
		} catch (CmpdRegMolFormatException e) {
			logger.error(e.toString());
			return new ResponseEntity<String>("Cannot read input molfile: "+e.toString(), headers, HttpStatus.BAD_REQUEST);
		} catch (StandardizerException e) {
			logger.error(e.toString());
			return new ResponseEntity<String>("Standardization ERROR: "+e.toString(), headers, HttpStatus.BAD_REQUEST);
		}
		output.setStructure(standardizedMol);
		output.setFormat("mol");

		return new ResponseEntity<String>(output.toJson(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/molconvert", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> molconvert(@RequestBody String json){
		if (logger.isDebugEnabled()) logger.debug("incoming json from molconvert: " + json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		MolConvertInputDTO inputDTO = MolConvertInputDTO.fromJsonToMolConvertInputDTO(json);
		MolConvertOutputDTO output = null;
		try {
			output = chemStructureService.toFormat(inputDTO.getStructure(), inputDTO.getInputFormat(), inputDTO.getParameters());
		} catch (IOException e) {
			logger.error(e.toString());
			return new ResponseEntity<String>("IO ERROR", headers, HttpStatus.BAD_REQUEST);
		} catch (CmpdRegMolFormatException e) {
			logger.error(e.toString());
			return new ResponseEntity<String>("Cannot read input molfile: "+e.toString(), headers, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>(output.toJson(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/clean", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> cleanMol(@RequestBody String json){
		if (logger.isDebugEnabled()) logger.debug("incoming json from mol clean: " + json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "plain/text");		
		MolCleanInputDTO inputDTO = MolCleanInputDTO.fromJsonToMolCleanInputDTO(json);
		MolConvertOutputDTO output = null;
		try {
			output = chemStructureService.cleanStructure(inputDTO.getStructure(), inputDTO.getParameters().getDim(), inputDTO.getParameters().getOpts());
		} catch (IOException e) {
			logger.error(e.toString());
			return new ResponseEntity<String>("IO ERROR", headers, HttpStatus.BAD_REQUEST);
		} catch (CmpdRegMolFormatException e) {
			logger.error(e.toString());
			return new ResponseEntity<String>("Cannot read input molfile: "+e.toString(), headers, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(output.getStructure(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/hydrogenizer", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> hydrogenizeMol(@RequestBody String json){
		if (logger.isDebugEnabled()) logger.debug("incoming json from mol hydrogenize: " + json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "plain/text");		
		HydrogenizeMolInputDTO inputDTO = HydrogenizeMolInputDTO.fromJsonToHydrogenizeMolInputDTO(json);
		String output = null;
		try {
			output = chemStructureService.hydrogenizeMol(inputDTO.getStructure(), inputDTO.getInputFormat(), inputDTO.getParameters().getMethod());
		} catch (IOException e) {
			logger.error(e.toString());
			return new ResponseEntity<String>("IO ERROR", headers, HttpStatus.BAD_REQUEST);
		} catch (CmpdRegMolFormatException e) {
			logger.error(e.toString());
			return new ResponseEntity<String>("Cannot read input molfile: "+e.toString(), headers, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(output, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/cipStereoInfo", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> cipStereoInfo(@RequestBody String json){
		if (logger.isDebugEnabled()) logger.debug("incoming json from mol get cipStereoInfo: " + json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "plain/text");		
		MolInputDTO inputDTO = MolInputDTO.fromJsonToMolInputDTO(json);
		String output = null;
		try {
			output = chemStructureService.getCipStereo(inputDTO.getStructure());
		} catch (IOException e) {
			logger.error(e.toString());
			return new ResponseEntity<String>("IO ERROR", headers, HttpStatus.BAD_REQUEST);
		} catch (CmpdRegMolFormatException e) {
			logger.error(e.toString());
			return new ResponseEntity<String>("Cannot read input molfile: "+e.toString(), headers, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(output, headers, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.OPTIONS)
	public ResponseEntity<String> getOptions() {
		HttpHeaders headers= new HttpHeaders();
		headers.add("Content-Type", "application/text");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}
	

}
