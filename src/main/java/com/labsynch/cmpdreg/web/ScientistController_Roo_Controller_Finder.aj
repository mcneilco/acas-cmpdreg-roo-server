// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.web;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.labsynch.cmpdreg.domain.Scientist;

privileged aspect ScientistController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByCodeEquals", "form" }, method = RequestMethod.GET)
    public String ScientistController.findScientistsByCodeEqualsForm(Model uiModel) {
        return "scientists/findScientistsByCodeEquals";
    }
    
    @RequestMapping(params = "find=ByCodeEquals", method = RequestMethod.GET)
    public String ScientistController.findScientistsByCodeEquals(@RequestParam("code") String code, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("scientists", Scientist.findScientistsByCodeEquals(code, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Scientist.countFindScientistsByCodeEquals(code) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("scientists", Scientist.findScientistsByCodeEquals(code, sortFieldName, sortOrder).getResultList());
        }
        return "scientists/list";
    }
    
}
