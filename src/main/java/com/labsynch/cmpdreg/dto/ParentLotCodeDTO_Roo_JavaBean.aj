// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto;

import com.labsynch.cmpdreg.dto.ParentLotCodeDTO;
import java.util.Collection;

privileged aspect ParentLotCodeDTO_Roo_JavaBean {
    
    public String ParentLotCodeDTO.getRequestName() {
        return this.requestName;
    }
    
    public void ParentLotCodeDTO.setRequestName(String requestName) {
        this.requestName = requestName;
    }
    
    public String ParentLotCodeDTO.getReferenceCode() {
        return this.referenceCode;
    }
    
    public void ParentLotCodeDTO.setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }
    
    public Collection<String> ParentLotCodeDTO.getLotCodes() {
        return this.lotCodes;
    }
    
    public void ParentLotCodeDTO.setLotCodes(Collection<String> lotCodes) {
        this.lotCodes = lotCodes;
    }
    
}
