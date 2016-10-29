package com.labsynch.cmpdreg.api;

import java.net.URLDecoder;
import java.util.Collection;

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

import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.dto.CodeTableDTO;
import com.labsynch.cmpdreg.dto.ParentDTO;
import com.labsynch.cmpdreg.dto.ParentValidationDTO;
import com.labsynch.cmpdreg.service.ParentService;

@RequestMapping(value = {"/api/v1/parents"})
@Controller
public class ApiParentController {

	Logger logger = LoggerFactory.getLogger(ApiParentController.class);

	@Autowired
	public ParentService parentService;

	@Transactional
	@RequestMapping(value = "/validateParent", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> validateParent(@RequestBody String json){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			Parent queryParent = Parent.fromJsonToParent(json);
			ParentValidationDTO validationDTO = parentService.validateUniqueParent(queryParent);
			if (validationDTO.isParentUnique() && validationDTO.getErrors().isEmpty()){
				return new ResponseEntity<String>(CodeTableDTO.toJsonArray(validationDTO.getAffectedLots()), headers, HttpStatus.OK);
			}else{
				return new ResponseEntity<String>(validationDTO.toJson(), headers, HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			logger.error("Caught error trying to validate parent",e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@RequestMapping(value = "/updateParent", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> updateParent(@RequestBody String json){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			Parent parent = Parent.fromJsonToParent(json);
			Collection<CodeTableDTO> affectedLots = parentService.updateParent(parent);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(affectedLots), headers, HttpStatus.OK);
		}catch(Exception e){
			logger.error("Caught error trying to update parent",e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@RequestMapping(value = "/standardizeParents", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> standardizeParent(
			@RequestParam(value="adminCode", required = true) String adminCode,
			@RequestParam(value="onlyDisplayChanges", required = false) String onlyDisplayChanges){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		boolean displayChanges = false;
		if (onlyDisplayChanges != null && onlyDisplayChanges.equalsIgnoreCase("true")){
			displayChanges = true;
		}
		if (adminCode.equalsIgnoreCase("lajolla-standardize")){
			logger.info("standardizing parent structs");
			try{
				int count = 0;
				if (displayChanges) {
					count = parentService.restandardizeParentStructsWithDisplayChanges();
				} else {
					count = parentService.restandardizeAllParentStructures();					
				}
				return new ResponseEntity<String>("Parent structures restandardized: " + count, headers, HttpStatus.OK);
			}catch(Exception e){
				logger.error("Caught error trying to standardize parent structures: ",e);
				return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			//do nothing
			return new ResponseEntity<String>("NO Response", headers, HttpStatus.BAD_REQUEST);
		}
	}
	
	@Transactional
	@RequestMapping(value = "/findParentDupeStructs", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> findParentDupeStructs(
			@RequestParam(value="adminCode", required = true) String adminCode,
			@RequestBody String reportFile
			){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		if (adminCode.equalsIgnoreCase("lajolla-check")){
			if (reportFile == null || reportFile.equalsIgnoreCase("")){
				reportFile = "/tmp/dupeCheckParents.sdf";
			}
			// generate report of dupe structures in parent table if the correct code is sent (basic guard)
			logger.info("decoded report file: " + URLDecoder.decode(reportFile));
			logger.info("checking parent structs and generating report file: " + reportFile);
			int numberOfPotentialDupes = parentService.findDupeParentStructures(reportFile);
			logger.info("number of dupes: " + numberOfPotentialDupes);
			return new ResponseEntity<String>("Parent dupe check done: " + numberOfPotentialDupes, headers, HttpStatus.OK);
		} else {
			//do nothing
			return new ResponseEntity<String>("NO Response", headers, HttpStatus.BAD_REQUEST);
		}
	}
	

	@Transactional
	@RequestMapping(value = "/findPotentialParentDupeStructs", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> findPotenitalParentDupeStructs(
			@RequestParam(value="adminCode", required = true) String adminCode,
			@RequestBody String reportFile
			){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		if (adminCode.equalsIgnoreCase("lajolla-check")){
			if (reportFile == null || reportFile.equalsIgnoreCase("")){
				reportFile = "/tmp/potentialParentDupes.sdf";
			}
			// generate report of potential dupe structures in parent table if the correct code is sent (basic guard)
			// does not look at stereo code and comments
			logger.info("checking parent structs and saving to QC table");
			int numberOfPotentialDupes = parentService.findPotentialDupeParentStructures(reportFile);
			logger.info("number of potential dupes: " + numberOfPotentialDupes);
			return new ResponseEntity<String>("Parent dupe check done: " + numberOfPotentialDupes, headers, HttpStatus.OK);
		} else {
			//do nothing
			return new ResponseEntity<String>("NO Response", headers, HttpStatus.BAD_REQUEST);
		}
	}
	

}
