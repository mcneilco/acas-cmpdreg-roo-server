package com.labsynch.cmpdreg.web;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.gvnix.addon.datatables.GvNIXDatatables;
import org.gvnix.addon.web.mvc.addon.jquery.GvNIXWebJQuery;
import org.gvnix.web.datatables.query.SearchResults;
import org.gvnix.web.datatables.util.DatatablesUtils;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.github.dandelion.datatables.extras.spring3.ajax.DatatablesParams;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.domain.StereoCategory;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;
import com.labsynch.cmpdreg.service.ChemStructureService;
import com.labsynch.cmpdreg.service.ErrorMessage;
import com.labsynch.cmpdreg.service.ParentStructureService;
import com.labsynch.cmpdreg.utils.MoleculeUtil;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.path.PathBuilder;

@RooWebScaffold(path = "parents", formBackingObject = Parent.class)
@RequestMapping("/parents")
@Controller
@Transactional
@GvNIXWebJQuery
@GvNIXDatatables(ajax = false)
@RooWebFinder
public class ParentController {

	Logger logger = LoggerFactory.getLogger(ParentController.class);

	@Autowired
	public ConversionService conversionService_dtt;

	@Autowired
	public MessageSource messageSource_dtt;

	@Autowired
	private ChemStructureService service;

	@Autowired
	private ParentStructureService parentService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
		Parent parent = Parent.findParent(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		if (parent == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(parent.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		return new ResponseEntity<String>(Parent.toJsonArray(Parent.findAllParents()), headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		Parent parent = Parent.fromJsonToParent(json);
		parent = this.saveParentStructure(parent);
		parent.persist();
		if (parent.getCdId() == -1) {
			ErrorMessage error = new ErrorMessage();
			error.setLevel("error");
			error.setMessage("Bad molformat. Please fix the molfile: " + parent.getMolStructure());
			return new ResponseEntity<String>(error.toJson(), headers, HttpStatus.BAD_REQUEST);
		} else if (parent.getCdId() > 0) {
			try{
				String molfile = service.toMolfile(parent.getMolStructure());
				parent.setMolStructure(molfile);
				parent.persist();
				return showJson(parent.getId());
			}catch (CmpdRegMolFormatException e) {
				return new ResponseEntity<String>("Cannot parse input mol: "+e.toString(), headers, HttpStatus.BAD_REQUEST);
			}
		} else {
			ErrorMessage error = new ErrorMessage();
			error.setLevel("warning");
			error.setMessage("Duplicate parent found. Please select existing parent.");
			return new ResponseEntity<String>(error.toJson(), headers, HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		for (Parent parent : Parent.fromJsonArrayToParents(json)) {
			parent = this.saveParentStructure(parent);
			parent.persist();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text");
		if (Parent.fromJsonToParent(json).merge() == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text");
		for (Parent parent : Parent.fromJsonArrayToParents(json)) {
			if (parent.merge() == null) {
				return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
			}
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		Parent parent = Parent.findParent(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		if (parent == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		parent.remove();
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid Parent parent, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("parent", parent);
			return "parents/create";
		}
		uiModel.asMap().clear();
		parent = this.saveParentStructure(parent);
		String molfile;
		try {
			molfile = service.toMolfile(parent.getMolStructure());
			parent.setMolStructure(molfile);
			parent.persist();
			return "redirect:/parents/" + encodeUrlPathSegment(parent.getId().toString(), httpServletRequest);
		} catch (CmpdRegMolFormatException e) {
			logger.error("Cannot parse input mol: "+e.toString());
		}
		return "redirect:/error/";
	}

	private Parent saveParentStructure(Parent parent) {
		int parentCdId = service.saveStructure(parent.getMolStructure(), "Parent_Structure");
		parent.setCdId(parentCdId);
		return parent;
	}

	@RequestMapping(params = "form", method = RequestMethod.GET)
	public String createForm(Model uiModel) {
		uiModel.addAttribute("parent", new Parent());
		return "parents/create";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("parent", Parent.findParent(id));
		uiModel.addAttribute("itemId", id);
		return "parents/show";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			uiModel.addAttribute("parents", Parent.findParentEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
			float nrOfPages = (float) Parent.countParents() / sizeNo;
			uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		} else {
			uiModel.addAttribute("parents", Parent.findParentEntries(0, 10));
		}
		return "parents/list";
	}

	@Transactional
	@RequestMapping(method = RequestMethod.PUT)
	public String update(@Valid Parent parent, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("parent", parent);
			return "parents/update";
		}
		uiModel.asMap().clear();
		try {
			logger.debug("Parent weight: " + service.getMolWeight(parent.getMolStructure()));
		} catch (CmpdRegMolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parentService.update(parent);
		//		  parent.merge();
		return "redirect:/parents/" + encodeUrlPathSegment(parent.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") Long id, Model uiModel) {
		uiModel.addAttribute("parent", Parent.findParent(id));
		return "parents/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		Parent.findParent(id).remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/parents";
	}

	@ModelAttribute("parents")
	public Collection<Parent> populateParents() {
		return Parent.findParentEntries(0, 2);
	}

	@ModelAttribute("saltforms")
	public Collection<SaltForm> populateSaltForms() {
		return SaltForm.findSaltFormEntries(0, 2);
	}

	@ModelAttribute("scientists")
	public Collection<Scientist> populateScientists() {
		return Scientist.findAllScientists();
	}

	@ModelAttribute("stereocategorys")
	public Collection<StereoCategory> populateStereoCategorys() {
		return StereoCategory.findAllStereoCategorys();
	}

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (UnsupportedEncodingException uee) {
		}
		return pathSegment;
	}

	@RequestMapping(method = RequestMethod.OPTIONS)
	public ResponseEntity<String> getOptions() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text, text/html");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	//	  @Transactional
	//	  @RequestMapping(headers = "Accept=application/json", value = "/datatables/ajax", produces = "application/json")
	//	  @ResponseBody
	//	  public DatatablesResponse<Map<String, String>> findAllParents(@DatatablesParams DatatablesCriterias criterias, @ModelAttribute Parent parent, HttpServletRequest request) {
	//		  // URL parameters are used as base search filters
	//		  Map<String, Object> baseSearchValuesMap = getPropertyMap(parent, request);
	//		  setDatatablesBaseFilter(baseSearchValuesMap);
	//		  SearchResults<Parent> searchResult = DatatablesUtils.findByCriteria(Parent.class, Parent.entityManager(), criterias, baseSearchValuesMap, conversionService_dtt, messageSource_dtt);
	//		  // Get datatables required counts
	//		  long totalRecords = searchResult.getTotalCount();
	//		  long recordsFound = searchResult.getResultsCount();
	//		  // Entity pk field name
	//		  String pkFieldName = "id";
	//		  org.springframework.ui.Model uiModel = new org.springframework.ui.ExtendedModelMap();
	//		  addDateTimeFormatPatterns(uiModel);
	//		  Map<String, Object> datePattern = uiModel.asMap();
	//		  DataSet<Map<String, String>> dataSet = DatatablesUtils.populateDataSet(searchResult.getResults(), pkFieldName, totalRecords, recordsFound, criterias.getColumnDefs(), datePattern, conversionService_dtt);
	//		  return DatatablesResponse.build(dataSet, criterias);
	//	  }
	@RequestMapping(headers = "Accept=application/json", value = "/datatables/ajax", params = "ajax_find=BySaltForms", produces = "application/json")
	@ResponseBody
	public DatatablesResponse<Map<String, String>> findParentsBySaltForms(@DatatablesParams DatatablesCriterias criterias, @RequestParam("saltForms") Set<SaltForm> saltForms) {
		BooleanBuilder baseSearch = new BooleanBuilder();
		// Base Search. Using BooleanBuilder, a cascading builder for
		// Predicate expressions
		PathBuilder<Parent> entity = new PathBuilder<Parent>(Parent.class, "entity");
		if (saltForms != null) {
			baseSearch.and(entity.get("saltForms").eq(saltForms));
		} else {
			baseSearch.and(entity.get("saltForms").isNull());
		}
		SearchResults<Parent> searchResult = DatatablesUtils.findByCriteria(entity, Parent.entityManager(), criterias, baseSearch);
		// Get datatables required counts
		long totalRecords = searchResult.getTotalCount();
		long recordsFound = searchResult.getResultsCount();
		// Entity pk field name
		String pkFieldName = "id";
		org.springframework.ui.Model uiModel = new org.springframework.ui.ExtendedModelMap();
		addDateTimeFormatPatterns(uiModel);
		Map<String, Object> datePattern = uiModel.asMap();
		DataSet<Map<String, String>> dataSet = DatatablesUtils.populateDataSet(searchResult.getResults(), pkFieldName, totalRecords, recordsFound, criterias.getColumnDefs(), datePattern, conversionService_dtt);
		return DatatablesResponse.build(dataSet, criterias);
	}

	@RequestMapping(headers = "Accept=application/json", value = "/datatables/ajax", params = "ajax_find=ByCdId", produces = "application/json")
	@ResponseBody
	public DatatablesResponse<Map<String, String>> findParentsByCdId(@DatatablesParams DatatablesCriterias criterias, @RequestParam("CdId") int CdId) {
		BooleanBuilder baseSearch = new BooleanBuilder();
		// Base Search. Using BooleanBuilder, a cascading builder for
		// Predicate expressions
		PathBuilder<Parent> entity = new PathBuilder<Parent>(Parent.class, "entity");
		if (CdId > 0) {
			baseSearch.and(entity.getNumber("CdId", int.class).eq(CdId));
		} else {
			baseSearch.and(entity.getNumber("CdId", int.class).isNull());
		}
		SearchResults<Parent> searchResult = DatatablesUtils.findByCriteria(entity, Parent.entityManager(), criterias, baseSearch);
		// Get datatables required counts
		long totalRecords = searchResult.getTotalCount();
		long recordsFound = searchResult.getResultsCount();
		// Entity pk field name
		String pkFieldName = "id";
		org.springframework.ui.Model uiModel = new org.springframework.ui.ExtendedModelMap();
		addDateTimeFormatPatterns(uiModel);
		Map<String, Object> datePattern = uiModel.asMap();
		DataSet<Map<String, String>> dataSet = DatatablesUtils.populateDataSet(searchResult.getResults(), pkFieldName, totalRecords, recordsFound, criterias.getColumnDefs(), datePattern, conversionService_dtt);
		return DatatablesResponse.build(dataSet, criterias);
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute("parent_registrationdate_date_format", DateTimeFormat.patternForStyle("S-", LocaleContextHolder.getLocale()));
		uiModel.addAttribute("parent_modifieddate_date_format", DateTimeFormat.patternForStyle("S-", LocaleContextHolder.getLocale()));
	}
}
