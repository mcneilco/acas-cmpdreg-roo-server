// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto;

import com.labsynch.cmpdreg.dto.SearchFormReturnDTO;
import com.labsynch.cmpdreg.dto.SearchResultExportRequestDTO;

privileged aspect SearchResultExportRequestDTO_Roo_JavaBean {
    
    public String SearchResultExportRequestDTO.getFilePath() {
        return this.filePath;
    }
    
    public void SearchResultExportRequestDTO.setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public SearchFormReturnDTO SearchResultExportRequestDTO.getSearchFormResultsDTO() {
        return this.searchFormResultsDTO;
    }
    
    public void SearchResultExportRequestDTO.setSearchFormResultsDTO(SearchFormReturnDTO searchFormResultsDTO) {
        this.searchFormResultsDTO = searchFormResultsDTO;
    }
    
}
