package com.labsynch.cmpdreg.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class RegSearchParamsDTO {

    private String corpName;

    private String molStructure;
    
	private String parentCorpName;
	
	private String saltFormCorpName;
	
	private String lotCorpName;
    

}
