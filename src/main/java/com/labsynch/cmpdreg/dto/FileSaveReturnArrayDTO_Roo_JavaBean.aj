// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto;

import com.labsynch.cmpdreg.dto.FileSaveReturnArrayDTO;
import com.labsynch.cmpdreg.dto.FileSaveReturnDTO;
import java.util.List;

privileged aspect FileSaveReturnArrayDTO_Roo_JavaBean {
    
    public List<FileSaveReturnDTO> FileSaveReturnArrayDTO.getReturnFileList() {
        return this.returnFileList;
    }
    
    public void FileSaveReturnArrayDTO.setReturnFileList(List<FileSaveReturnDTO> returnFileList) {
        this.returnFileList = returnFileList;
    }
    
}
