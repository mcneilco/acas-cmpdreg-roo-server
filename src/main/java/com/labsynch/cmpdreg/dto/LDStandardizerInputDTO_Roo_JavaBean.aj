// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto;

import com.labsynch.cmpdreg.dto.LDStandardizerActionDTO;
import com.labsynch.cmpdreg.dto.LDStandardizerInputDTO;
import java.util.Collection;
import java.util.HashMap;

privileged aspect LDStandardizerInputDTO_Roo_JavaBean {
    
    public Collection<LDStandardizerActionDTO> LDStandardizerInputDTO.getActions() {
        return this.actions;
    }
    
    public void LDStandardizerInputDTO.setActions(Collection<LDStandardizerActionDTO> actions) {
        this.actions = actions;
    }
    
    public HashMap<String, String> LDStandardizerInputDTO.getStructures() {
        return this.structures;
    }
    
    public void LDStandardizerInputDTO.setStructures(HashMap<String, String> structures) {
        this.structures = structures;
    }
    
    public String LDStandardizerInputDTO.getAuth_token() {
        return this.auth_token;
    }
    
    public void LDStandardizerInputDTO.setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }
    
    public Integer LDStandardizerInputDTO.getTimeout() {
        return this.timeout;
    }
    
    public void LDStandardizerInputDTO.setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
    
    public String LDStandardizerInputDTO.getOutput_format() {
        return this.output_format;
    }
    
    public void LDStandardizerInputDTO.setOutput_format(String output_format) {
        this.output_format = output_format;
    }
    
}