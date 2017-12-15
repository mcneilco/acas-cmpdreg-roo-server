package com.labsynch.cmpdreg.web;
import javax.servlet.http.HttpServletRequest;

import org.gvnix.addon.datatables.GvNIXDatatables;
import org.gvnix.addon.web.mvc.addon.jquery.GvNIXWebJQuery;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.cmpdreg.domain.PurityMeasuredBy;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.utils.Configuration;


@RequestMapping({"/puritymeasuredbys","/purityMeasuredBys"})
@Controller
@GvNIXWebJQuery
@GvNIXDatatables(ajax = false)
@RooWebJson(jsonObject = PurityMeasuredBy.class)
@RooWebScaffold(path = "puritymeasuredbys", formBackingObject = PurityMeasuredBy.class)
@RooWebFinder
public class PurityMeasuredByController {


	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        PurityMeasuredBy puritymeasuredby = PurityMeasuredBy.findPurityMeasuredBy(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

        if (puritymeasuredby == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(puritymeasuredby.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

		if (mainConfig.getServerSettings().isOrderSelectLists()){
	        return new ResponseEntity<String>(PurityMeasuredBy.toJsonArray(PurityMeasuredBy.findAllPurityMeasuredBys("name", "ASC")), headers, HttpStatus.OK);
		} else {
	        return new ResponseEntity<String>(PurityMeasuredBy.toJsonArray(PurityMeasuredBy.findAllPurityMeasuredBys()), headers, HttpStatus.OK);
		}
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        PurityMeasuredBy.fromJsonToPurityMeasuredBy(json).persist();
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (PurityMeasuredBy purityMeasuredBy: PurityMeasuredBy.fromJsonArrayToPurityMeasuredBys(json)) {
            purityMeasuredBy.persist();
        }
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        if (PurityMeasuredBy.fromJsonToPurityMeasuredBy(json).merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
	}


	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        for (PurityMeasuredBy purityMeasuredBy: PurityMeasuredBy.fromJsonArrayToPurityMeasuredBys(json)) {
            if (purityMeasuredBy.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        PurityMeasuredBy puritymeasuredby = PurityMeasuredBy.findPurityMeasuredBy(id);
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");       
        if (puritymeasuredby == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        puritymeasuredby.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
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

	
	
	
    @RequestMapping(produces = "text/html", value = "/list")
    public String listDatatablesDetail(Model uiModel, HttpServletRequest request, @ModelAttribute PurityMeasuredBy purityMeasuredBy) {
        // Do common datatables operations: get entity list filtered by request parameters
        listDatatables(uiModel, request, purityMeasuredBy);
        // Show only the list fragment (without footer, header, menu, etc.) 
        return "forward:/WEB-INF/views/puritymeasuredbys/list.jspx";
    }

    
}
