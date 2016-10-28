package com.labsynch.cmpdreg.api;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.cmpdreg.dto.PreferredNameDTO;
import com.labsynch.cmpdreg.dto.SearchCdIdReturnDTO;
import com.labsynch.cmpdreg.dto.SearchFormDTO;
import com.labsynch.cmpdreg.service.RegSearchService;
import com.labsynch.cmpdreg.service.SearchFormService;

@RequestMapping(value = {"/api/v1/structuresearch"})
@Controller
public class ApiCmpdSearchController {
	
	Logger logger = LoggerFactory.getLogger(ApiCmpdSearchController.class);

	@Autowired
	private RegSearchService regSearchService;
	
	@Autowired
	private SearchFormService searchFormService;
	
	
	
	@Transactional
	@RequestMapping(value = "/getPreferredName/parent", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonArrayCheckParentCorpNameExists(@RequestBody String json){
		logger.debug("incoming json from getPreferredName: " + json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<PreferredNameDTO> preferredNameDTOs = PreferredNameDTO.fromJsonArrayToPreferredNameDTO(json);
		preferredNameDTOs = PreferredNameDTO.getParentPreferredNames(preferredNameDTOs);
		return new ResponseEntity<String>(PreferredNameDTO.toJsonArray(preferredNameDTOs), headers, HttpStatus.OK);
	}
	


	@RequestMapping(value = "/parents", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> searchStructures (	
			@RequestBody String molStructure,

//    		@RequestParam(value = "molStructure", required=true) String molStructure,
			@RequestParam(value = "maxResults", required=false) Integer maxResults,
			@RequestParam(value = "similarity", required=false) Float similarity,
			@RequestParam(value = "searchType", required=false) String searchType,
			@RequestParam(value = "outputFormat", required=false) String outputFormat) throws IOException {

    	//options for outputFormat -- corpname, cdid, corpname-cdid, sdf ; default is cdid
    	//    ./jcsearch --maxResults:$4 -q $9 -f sdf:Tcd_id DB:compound.parent_structure
			
		
		String searchResults = searchFormService.findParentIds(molStructure, maxResults, similarity, searchType, outputFormat);
    	
		String results = null;
		        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

       return new ResponseEntity<String>(searchResults, headers, HttpStatus.OK);

    }
	
	@RequestMapping(value = "/parents/form", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> searchStructuresForm (	
    		@RequestParam(value = "molStructure", required=true) String molStructure,
			@RequestParam(value = "maxResults", required=false) Integer maxResults,
			@RequestParam(value = "similarity", required=false) Float similarity,
			@RequestParam(value = "searchType", required=false) String searchType,
			@RequestParam(value = "outputFormat", required=false) String outputFormat) throws IOException {

    	//options for outputFormat -- corpname, cdid, corpname-cdid, sdf ; default is cdid
    	//    ./jcsearch --maxResults:$4 -q $9 -f sdf:Tcd_id DB:compound.parent_structure
			
		if (outputFormat == null || outputFormat.equalsIgnoreCase("")) outputFormat = "cdid";
		if (searchType == null || searchType.equalsIgnoreCase("")) searchType = "SUBSTRUCTURE";

		String searchResults = searchFormService.findParentIds(molStructure, maxResults, similarity, searchType, outputFormat);
    	
		String results = null;
		        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

       return new ResponseEntity<String>(searchResults, headers, HttpStatus.OK);

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
