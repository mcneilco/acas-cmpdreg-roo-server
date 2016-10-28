// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.labsynch.cmpdreg.domain.LotAliasType;

privileged aspect LotAliasTypeController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String LotAliasTypeController.create(@Valid LotAliasType lotAliasType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lotAliasType);
            return "lotaliastypes/create";
        }
        uiModel.asMap().clear();
        lotAliasType.persist();
        return "redirect:/lotaliastypes/" + encodeUrlPathSegment(lotAliasType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String LotAliasTypeController.createForm(Model uiModel) {
        populateEditForm(uiModel, new LotAliasType());
        return "lotaliastypes/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String LotAliasTypeController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("lotaliastype", LotAliasType.findLotAliasType(id));
        uiModel.addAttribute("itemId", id);
        return "lotaliastypes/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String LotAliasTypeController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lotaliastypes", LotAliasType.findLotAliasTypeEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LotAliasType.countLotAliasTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lotaliastypes", LotAliasType.findAllLotAliasTypes(sortFieldName, sortOrder));
        }
        return "lotaliastypes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String LotAliasTypeController.update(@Valid LotAliasType lotAliasType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lotAliasType);
            return "lotaliastypes/update";
        }
        uiModel.asMap().clear();
        lotAliasType.merge();
        return "redirect:/lotaliastypes/" + encodeUrlPathSegment(lotAliasType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String LotAliasTypeController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LotAliasType.findLotAliasType(id));
        return "lotaliastypes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String LotAliasTypeController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LotAliasType lotAliasType = LotAliasType.findLotAliasType(id);
        lotAliasType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lotaliastypes";
    }
    
    void LotAliasTypeController.populateEditForm(Model uiModel, LotAliasType lotAliasType) {
        uiModel.addAttribute("lotAliasType", lotAliasType);
    }
    
    String LotAliasTypeController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
