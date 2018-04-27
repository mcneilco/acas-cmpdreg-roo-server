package com.labsynch.cmpdreg.web;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.domain.StandardizationDryRunCompound;
import com.labsynch.cmpdreg.service.StructureImageService;

@RequestMapping("/structureimage")
@Controller
@Transactional
public class StructureImageController {

	Logger logger = LoggerFactory.getLogger(StructureImageController.class);
	
	@Autowired
	private StructureImageService structureImageService;
	
//    @RequestMapping
//    public void get(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
//    }

	@RequestMapping(value = "/convertMol", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<byte[]> convertMol(	@RequestBody String molStructure,
			@RequestParam(value = "hSize", required=false) Integer hSize,
			@RequestParam(value = "wSize", required=false) Integer wSize,
			@RequestParam(value = "format", required=false) String format) {

    	byte[] image = structureImageService.convertMolToImage(molStructure, hSize, wSize, format);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

       return new ResponseEntity<byte[]>(image, headers, HttpStatus.OK);

    }
    

	@RequestMapping(value = "/parent/{corpName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> displayParentImage(@PathVariable("corpName") String corpName,
    											@RequestParam(value = "hSize", required=false) Integer hSize,
    											@RequestParam(value = "wSize", required=false) Integer wSize,
    											@RequestParam(value = "format", required=false) String format) {
        Parent parent = Parent.findParentsByCorpNameEquals(corpName).getSingleResult();      
        byte[] image = structureImageService.convertMolToImage(parent.getMolStructure(), hSize, wSize, format);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

       return new ResponseEntity<byte[]>(image, headers, HttpStatus.OK);

    }

	@RequestMapping(value = "/saltForm/{corpName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> displaySaltFormImage(@PathVariable("corpName") String corpName,
			@RequestParam(value = "hSize", required=false) Integer hSize,
			@RequestParam(value = "wSize", required=false) Integer wSize,
			@RequestParam(value = "format", required=false) String format) {
    	
    	String molStructure = null;
        List<SaltForm> saltForms = SaltForm.findSaltFormsByCorpNameEquals(corpName).getResultList();
    	SaltForm saltForm = saltForms.get(0);
    	if (saltForms.size() > 1){
    		logger.error("found multiple saltForms for : " + corpName);
    	}

// TODO: work out issue with SaltForm structures    	
//    	if (saltForm.getMolStructure() != null){
//    		molStructure = saltForm.getMolStructure();
//    	} else {
////            Parent parent = Parent.findParentsByCorpNameEquals(corpName).getSingleResult();
//    		Parent parent = saltForm.getParent();
//            molStructure = parent.getMolStructure();
//    	}

		Parent parent = saltForm.getParent();
        molStructure = parent.getMolStructure();
        
        byte[] image = structureImageService.convertMolToImage(molStructure, hSize, wSize, format);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

       return new ResponseEntity<byte[]>(image, headers, HttpStatus.OK);

    }
    
	@RequestMapping(value = "/lot/{corpName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> displayLotImage(@PathVariable("corpName") String corpName,
			@RequestParam(value = "hSize", required=false) Integer hSize,
			@RequestParam(value = "wSize", required=false) Integer wSize,
			@RequestParam(value = "format", required=false) String format) {
    	
        Lot lot = Lot.findLotsByCorpNameEquals(corpName).getSingleResult();
        String molStructure = null; //do not display the as drawn structure -- may be incorrect before standardization
        
        if ((molStructure == null || molStructure.length() < 1) && lot.getSaltForm().getMolStructure() != null){
        	molStructure = lot.getSaltForm().getMolStructure();
        }
        if (molStructure == null || molStructure.length() < 1){
        	molStructure = lot.getSaltForm().getParent().getMolStructure();
        }
        
        byte[] image = structureImageService.convertMolToImage(molStructure, hSize, wSize, format);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

       return new ResponseEntity<byte[]>(image, headers, HttpStatus.OK);

    }
    
    
	@RequestMapping(value = "/{corpName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> displayImage(@PathVariable("corpName") String corpName) {
        Parent parent = Parent.findParentsByCorpNameEquals(corpName).getSingleResult();      
        byte[] image = structureImageService.displayImage(parent.getMolStructure());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

       return new ResponseEntity<byte[]>(image, headers, HttpStatus.OK);

    }
    
	@RequestMapping(value = "/standardization/{corpName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> displayDryrunCompoundImage(@PathVariable("corpName") String corpName,
			@RequestParam(value = "hSize", required=false) Integer hSize,
			@RequestParam(value = "wSize", required=false) Integer wSize,
			@RequestParam(value = "format", required=false) String format) {
        StandardizationDryRunCompound stndznCompound = StandardizationDryRunCompound.findStandardizationDryRunCompoundsByCorpNameEquals(corpName).getSingleResult();
        byte[] image = structureImageService.convertMolToImage(stndznCompound.getMolStructure(), hSize, wSize, format);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

       return new ResponseEntity<byte[]>(image, headers, HttpStatus.OK);

    }

	@RequestMapping(value = "/originallydrawnas/{corpName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> displayOriginallyDrawnAsCompoundImage(@PathVariable("corpName") String corpName,
			@RequestParam(value = "hSize", required=false) Integer hSize,
			@RequestParam(value = "wSize", required=false) Integer wSize,
			@RequestParam(value = "format", required=false) String format) {
		Parent parent = Parent.findParentsByCorpNameEquals(corpName).getSingleResult();
		List<Lot> queryLots;
		String asDrawnStruct;
		queryLots = Lot.findLotByParentAndLowestLotNumber(parent).getResultList();
		if (queryLots.size() > 0 && queryLots.get(0).getAsDrawnStruct() != null){
			asDrawnStruct = queryLots.get(0).getAsDrawnStruct();
		} else {
			asDrawnStruct = parent.getMolStructure();
		}
        byte[] image = structureImageService.convertMolToImage(asDrawnStruct, hSize, wSize, format);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

       return new ResponseEntity<byte[]>(image, headers, HttpStatus.OK);

    }

    @RequestMapping
    public String index() {
        return "structureimage/index";
    }
    
    
	@RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getIsotopeOptions() {
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}
