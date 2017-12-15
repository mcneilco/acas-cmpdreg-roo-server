package com.labsynch.cmpdreg.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.labsynch.cmpdreg.dto.RegSearchDTO;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.service.RegSearchService;

//@RooWebScaffold(path = "regsearches", formBackingObject = RegSearch.class)
@RequestMapping(value ={"/regsearches", "/regSearches"})
@Controller
public class RegSearchController {
	
	@Autowired
	private RegSearchService regSearchService;
	
	Logger logger = LoggerFactory.getLogger(RegSearchController.class);


	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		}
		catch (UnsupportedEncodingException uee) {}
		return pathSegment;
	}


	@RequestMapping(value = "/parent", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> getParentsbyParamsPost(@RequestBody String searchParams) {
		logger.debug("using the /regsearches/parent controller" );
		
		HttpHeaders headers= new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		
		RegSearchDTO regSearchDTO = null;
		try {
			regSearchDTO = regSearchService.getParentsbyParams(searchParams);
		} catch (IOException | CmpdRegMolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<String>("ERROR", headers, HttpStatus.BAD_REQUEST);

		}

		return new ResponseEntity<String>(regSearchDTO.toJson(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/parent", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> getParentsbyParams(@RequestParam String searchParams) {
		logger.debug("using the /regsearches/parent controller" );
		
		HttpHeaders headers= new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		
		RegSearchDTO regSearchDTO = null;
		try {
			regSearchDTO = regSearchService.getParentsbyParams(searchParams);
		} catch (IOException | CmpdRegMolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<String>("ERROR", headers, HttpStatus.BAD_REQUEST);

		}

		return new ResponseEntity<String>(regSearchDTO.toJson(), headers, HttpStatus.OK);
	}


	@RequestMapping(method = RequestMethod.OPTIONS)
	public ResponseEntity<String> getOptions() {
		HttpHeaders headers= new HttpHeaders();
		headers.add("Content-Type", "application/text, text/html");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Max-Age", "86400");

		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}
}
