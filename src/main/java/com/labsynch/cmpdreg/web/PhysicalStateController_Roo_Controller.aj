// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.web;

import org.springframework.ui.Model;

import com.labsynch.cmpdreg.domain.PhysicalState;

privileged aspect PhysicalStateController_Roo_Controller {
    
    void PhysicalStateController.populateEditForm(Model uiModel, PhysicalState physicalState) {
        uiModel.addAttribute("physicalState", physicalState);
    }
    
}
