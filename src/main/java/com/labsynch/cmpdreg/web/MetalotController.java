package com.labsynch.cmpdreg.web;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.labsynch.cmpdreg.domain.FileList;
import com.labsynch.cmpdreg.domain.IsoSalt;
import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.dto.Metalot;
import com.labsynch.cmpdreg.dto.MetalotReturn;
import com.labsynch.cmpdreg.service.ChemStructureService;
import com.labsynch.cmpdreg.service.ErrorMessage;
import com.labsynch.cmpdreg.service.MetalotService;

@RooWebScaffold(path = "metalots", formBackingObject = Metalot.class)
@RequestMapping("/metalots")
@Controller
//@Transactional
public class MetalotController {
		
	@Autowired
	private MetalotService metalotService;
	
	@Autowired
	private ChemStructureService chemStructService;
	
	private static final Logger logger = LoggerFactory.getLogger(MetalotController.class);

	@Transactional
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		headers.setContentType(MediaType.APPLICATION_JSON);
		Metalot metaLot = new Metalot();
		
		try {
			Lot lot = Lot.findLot(id); 

			if (lot == null){
				return new ResponseEntity<String>(metaLot.toJson(), headers, HttpStatus.NOT_FOUND);
			} else {
				metaLot.setLot(lot);

				SaltForm saltForm = SaltForm.findSaltForm(metaLot.getLot().getSaltForm().getId());
				List<IsoSalt> isoSalts = IsoSalt.findIsoSaltsBySaltForm(saltForm).getResultList();
				metaLot.getIsosalts().addAll(isoSalts);

				List<FileList> fileLists = FileList.findFileListsByLot(lot).getResultList();
				metaLot.getFileList().addAll(fileLists);

				System.out.println(metaLot.toJson());
				return new ResponseEntity<String>(metaLot.toJson(), headers, HttpStatus.OK);
			}			
			
		} catch (Exception e){
			logger.error("hit an exception: " + e);
			
		}
		
		return new ResponseEntity<String>("ERROR", headers, HttpStatus.OK);

	}


	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		HttpHeaders headers= new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

		logger.debug("here is the original json: " + json);

		
		MetalotReturn mr = metalotService.save(Metalot.fromJsonToMetalot(json));
		boolean hasError = false;
		for (ErrorMessage error : mr.getErrors()){
			if (error.getLevel().equals("error")) hasError = true;
		}
		if (hasError){
			headers.add("lot save error", "true");
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(mr.getErrors()), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
//			MetalotReturnDTO miniLot = new MetalotReturnDTO();
//			miniLot.setId(mr.getMetalot().getLot().getId());
//			miniLot.setCorpName(mr.getMetalot().getLot().getCorpName());
//			miniLot.setBuid(mr.getMetalot().getLot().getBuid());
//			Metalot returnMetaLot = mr.getMetalot();
			return new ResponseEntity<String>(mr.toJson(), headers, HttpStatus.OK);        	
		}
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		Parent parent = Parent.findParent(id);
		HttpHeaders headers= new HttpHeaders();
		headers.add("Content-Type", "application/text");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		if (parent == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		parent.remove();
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/corpName/{corpName}", method = RequestMethod.GET, headers = "Accept=application/json, application/text, text/html")
	@ResponseBody
	public ResponseEntity<String> jsonFindMetalotsByCorpNameEquals(@PathVariable("corpName") String corpName) {
		logger.debug("search lot corpName = " + corpName);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		Metalot metaLot = new Metalot();
		List<Lot> lots = Lot.findLotsByCorpNameEquals(corpName).getResultList(); 
		System.out.println("Number of lots found = " + lots.size());	

		if (lots.size() == 0) {
			System.out.println("Did not find a lot with corpName = " + corpName);	
		}

		else {
			Lot lot = lots.get(0);
			metaLot.setLot(lot);

			SaltForm saltForm = SaltForm.findSaltForm(metaLot.getLot().getSaltForm().getId());
			List<IsoSalt> isoSalts = IsoSalt.findIsoSaltsBySaltForm(saltForm).getResultList();
			metaLot.getIsosalts().addAll(isoSalts);

			List<FileList> fileLists = FileList.findFileListsByLot(lot).getResultList();
			metaLot.getFileList().addAll(fileLists);
			System.out.println("From corpName/corpName --- here is the metalot for corpName = " + corpName);	
			System.out.println(metaLot.toJson());	    		
		}

		return new ResponseEntity<String>(metaLot.toJson(), headers, HttpStatus.OK);
	}


	@Transactional
	@RequestMapping(params = "corpName", method = RequestMethod.GET, headers = "Accept=application/json, application/text, text/html")
	@ResponseBody
	public ResponseEntity<String> jsonFindMetalotsByCorpNameParam(@RequestParam("corpName") String corpName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Content-Type", "application/text");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

		Metalot metaLot = new Metalot();
		List<Lot> lots = Lot.findLotsByCorpNameEquals(corpName).getResultList(); 
		System.out.println("Number of lots found = " + lots.size());	

		if (lots.size() == 0) {
			System.out.println("Did not find a lot with corpName = " + corpName);	
		}

		else {
			Lot lot = lots.get(0);
			metaLot.setLot(lot);

			SaltForm saltForm = SaltForm.findSaltForm(metaLot.getLot().getSaltForm().getId());
			List<IsoSalt> isoSalts = IsoSalt.findIsoSaltsBySaltForm(saltForm).getResultList();
			metaLot.getIsosalts().addAll(isoSalts);

			List<FileList> fileLists = FileList.findFileListsByLot(lot).getResultList();
			metaLot.getFileList().addAll(fileLists);
			System.out.println("here is the metalot for corpName = " + corpName);	
			System.out.println(metaLot.toJson());	    		
		}


		return new ResponseEntity<String>(metaLot.toJson(), headers, HttpStatus.OK);
	}


	@Transactional
	@RequestMapping(params = "find=ByCorpNameEquals", method = RequestMethod.GET)
	public String findMetalotsByCorpNameEqualsForm(@RequestParam("corpName") String corpName, Model uiModel) {
		uiModel.addAttribute("metalots", Parent.findParentsByCorpNameEquals(corpName).getResultList());
		return "metalots/list";
	}

	@Transactional
	@RequestMapping(params = "find=ByCorpNameLike", method = RequestMethod.GET)
	public String findParentsByCorpNameLike(@RequestParam("corpName") String corpName, Model uiModel) {
		uiModel.addAttribute("parents", Parent.findParentsByCorpNameLike(corpName).getResultList());
		return "metalots/list";
	}


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
