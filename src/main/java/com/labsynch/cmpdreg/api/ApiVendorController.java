package com.labsynch.cmpdreg.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.cmpdreg.domain.Vendor;
import com.labsynch.cmpdreg.service.ErrorMessage;

@RequestMapping(value = {"/api/v1/vendors"})
@Controller
public class ApiVendorController {
	
	Logger logger = LoggerFactory.getLogger(ApiVendorController.class);
	
	//check if vendor already exists by code
	// if exists -- return false else true
    @RequestMapping(value = "/validate", method = RequestMethod.GET, headers = "Accept=application/json")
	    @ResponseBody
	    public ResponseEntity<Boolean> validate(@RequestParam(value="code", required = true) String code) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/json; charset=utf-8");
	        headers.add("Access-Control-Allow-Headers", "Content-Type");
	        headers.add("Access-Control-Allow-Origin", "*");
	        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
	        headers.add("Pragma", "no-cache"); //HTTP 1.0
	        headers.setExpires(0); // Expire the cache
	        
	        List<Vendor> vendors = Vendor.findVendorsByCodeEquals(code).getResultList();
	        int vendorCount = vendors.size();
	        logger.info("number of vendors found: " + vendorCount);
	        if (vendorCount > 0){
	        	return new ResponseEntity<Boolean>(false, headers,HttpStatus.CONFLICT );
	        } else {
	        	return new ResponseEntity<Boolean>(true, headers,HttpStatus.ACCEPTED );	        	
	        }
	    }	

	//validate vendor before saving
    // is vendor code still unique?
    // is vendor name unique? (optional)
    @RequestMapping(value = "/validateBeforeSave", method = RequestMethod.POST, headers = "Accept=application/json")
	    @ResponseBody
	    public ResponseEntity<String> validateNewVendor(@RequestBody String json) {
    	   logger.info("validateNewVendor -- incoming json: " + json);
    	    Vendor queryVendor = Vendor.fromJsonToVendor(json);
    	    if (queryVendor.getCode() == null) {
    	    	logger.info("creating the missing code name");
    	    	queryVendor.setCode(queryVendor.getName().toLowerCase());
    	    }
    	    logger.info("validateNewVendor -- query vendor: " + queryVendor.toJson());

            ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
            boolean errorsFound = false;
            
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/json; charset=utf-8");
	        headers.add("Access-Control-Allow-Headers", "Content-Type");
	        headers.add("Access-Control-Allow-Origin", "*");
	        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
	        headers.add("Pragma", "no-cache"); //HTTP 1.0
	        headers.setExpires(0); // Expire the cache
	        
	        Long vendorByCodeCount = 0L;
	        if (queryVendor.getCode() != null){
		        vendorByCodeCount = Vendor.countFindVendorsByCodeEquals(queryVendor.getCode());
		        logger.info("number of vendors found by code: " + vendorByCodeCount);
		        Long vendorCountByName = Vendor.countFindVendorsByNameEquals(queryVendor.getName());
		        logger.info("number of vendors found by name: " + vendorCountByName);
	        }	        
	        if (vendorByCodeCount > 0  ){
	        	ErrorMessage error = new ErrorMessage();
	        	error.setLevel("ERROR");
	        	error.setMessage("Found another vendor with the same code name");
	        	errors.add(error);
	        	return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers,HttpStatus.CONFLICT );
	        } else {
	        	return new ResponseEntity<String>(queryVendor.toJson(), headers,HttpStatus.OK );	        	
	        }
	    }
    
    @RequestMapping(value = "/findByCodeEquals", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> findByCodeEquals(@RequestParam(value="code", required = true) String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        List<Vendor> vendors = Vendor.findVendorsByCodeEquals(code).getResultList();
        logger.debug("number of vendors found: " + vendors.size());
        if (vendors.size() != 1){
        	
            return new ResponseEntity<String>("[]", headers, HttpStatus.CONFLICT);       	
        } else {
            return new ResponseEntity<String>(vendors.get(0).toJson(), headers, HttpStatus.OK);
        }
               
    }	 
    
	    @RequestMapping(value = "/findByCodeLike", method = RequestMethod.GET, headers = "Accept=application/json")
	    @ResponseBody
	    public ResponseEntity<String> findByCodeLike(@RequestParam(value="code", required = true) String code) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/json; charset=utf-8");
	        headers.add("Access-Control-Allow-Headers", "Content-Type");
	        headers.add("Access-Control-Allow-Origin", "*");
	        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
	        headers.add("Pragma", "no-cache"); //HTTP 1.0
	        headers.setExpires(0); // Expire the cache
	        return new ResponseEntity<String>(Vendor.toJsonArray(Vendor.findVendorsByCodeLike(code).getResultList()), headers, HttpStatus.OK);
	    }	 

	    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	    @ResponseBody
	    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
	        Vendor vendor = Vendor.findVendor(id);
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/text; charset=utf-8");
	        headers.add("Access-Control-Allow-Headers", "Content-Type");
	        headers.add("Access-Control-Allow-Origin", "*");
	        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
	        headers.add("Pragma", "no-cache"); //HTTP 1.0
	        headers.setExpires(0); // Expire the cache
	        if (vendor == null) {
	            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<String>(vendor.toJson(), headers, HttpStatus.OK);
	    }

	    @RequestMapping(headers = "Accept=application/json")
	    @ResponseBody
	    public ResponseEntity<String> listJson() {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/text; charset=utf-8");
	        headers.add("Access-Control-Allow-Headers", "Content-Type");
	        headers.add("Access-Control-Allow-Origin", "*");
	        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
	        headers.add("Pragma", "no-cache"); //HTTP 1.0
	        headers.setExpires(0); // Expire the cache
	        return new ResponseEntity<String>(Vendor.toJsonArray(Vendor.findAllVendors("name", "ASC")), headers, HttpStatus.OK);
	    }

	    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	    public ResponseEntity<String> createFromJson(@RequestBody String json) {
	    	Vendor newVendor = Vendor.fromJsonToVendor(json);
    	    if (newVendor.getCode() == null) {
    	    	logger.info("creating the missing code name");
    	    	newVendor.setCode(newVendor.getName().toLowerCase());
    	    }
	    	newVendor.persist();
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/text; charset=utf-8");
	        headers.add("Access-Control-Allow-Headers", "Content-Type");
	        headers.add("Access-Control-Allow-Origin", "*");
	        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
	        headers.add("Pragma", "no-cache"); //HTTP 1.0
	        headers.setExpires(0); // Expire the cache
	        return new ResponseEntity<String>(newVendor.toJson(), headers, HttpStatus.CREATED);
	    }

	    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
	        for (Vendor vendor : Vendor.fromJsonArrayToVendors(json)) {
	            vendor.persist();
	        }
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/text; charset=utf-8");
	        headers.add("Access-Control-Allow-Headers", "Content-Type");
	        headers.add("Access-Control-Allow-Origin", "*");
	        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
	        headers.add("Pragma", "no-cache"); //HTTP 1.0
	        headers.setExpires(0); // Expire the cache
	        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	    }

	    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/text; charset=utf-8");
	        headers.add("Access-Control-Allow-Headers", "Content-Type");
	        headers.add("Access-Control-Allow-Origin", "*");
	        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
	        headers.add("Pragma", "no-cache"); //HTTP 1.0
	        headers.setExpires(0); // Expire the cache
	        if (Vendor.fromJsonToVendor(json).merge() == null) {
	            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<String>(headers, HttpStatus.OK);
	    }

	    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/text; charset=utf-8");
	        headers.add("Access-Control-Allow-Headers", "Content-Type");
	        headers.add("Access-Control-Allow-Origin", "*");
	        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
	        headers.add("Pragma", "no-cache"); //HTTP 1.0
	        headers.setExpires(0); // Expire the cache
	        for (Vendor vendor : Vendor.fromJsonArrayToVendors(json)) {
	            if (vendor.merge() == null) {
	                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	            }
	        }
	        return new ResponseEntity<String>(headers, HttpStatus.OK);
	    }

	    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
	        Vendor vendor = Vendor.findVendor(id);
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/text; charset=utf-8");
	        headers.add("Access-Control-Allow-Headers", "Content-Type");
	        headers.add("Access-Control-Allow-Origin", "*");
	        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
	        headers.add("Pragma", "no-cache"); //HTTP 1.0
	        headers.setExpires(0); // Expire the cache
	        if (vendor == null) {
	            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	        }
	        vendor.remove();
	        return new ResponseEntity<String>(headers, HttpStatus.OK);
	    }

	    @RequestMapping(value = "/search", method = RequestMethod.GET, headers = "Accept=application/json")
	    @ResponseBody
	    public ResponseEntity<String> searchBySearchTerms(@RequestParam(value="searchTerm", required = true) String searchTerm) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/json; charset=utf-8");
	        headers.add("Access-Control-Allow-Headers", "Content-Type");
	        headers.add("Access-Control-Allow-Origin", "*");
	        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
	        headers.add("Pragma", "no-cache"); //HTTP 1.0
	        headers.setExpires(0); // Expire the cache
	        return new ResponseEntity<String>(Vendor.toJsonArray(Vendor.findVendorsBySearchTerm(searchTerm).getResultList()), headers, HttpStatus.OK);
	    }	
	    
	    @RequestMapping(method = RequestMethod.OPTIONS)
	    public ResponseEntity<String> getOptions() {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Type", "application/text");
	        headers.add("Access-Control-Allow-Headers", "Content-Type");
	        headers.add("Access-Control-Allow-Origin", "*");
	        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
	        headers.add("Pragma", "no-cache"); //HTTP 1.0
	        headers.setExpires(0); // Expire the cache
	        return new ResponseEntity<String>(headers, HttpStatus.OK);
	    }	 
	 
}
