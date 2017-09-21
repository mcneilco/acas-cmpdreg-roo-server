package com.labsynch.cmpdreg.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.cmpdreg.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class CreatePlateRequestDTO {

	private String barcode;
	
	private String definition;
	
	private String template;
	
	private String description;
	
	private String recordedBy;
	
	private String createdUser;
	
	private Date createdDate;
	
	private String physicalState;
	
	private String batchConcentrationUnits;
	
	private Collection<WellContentDTO> wells = new ArrayList<WellContentDTO>();
	
	public String toJson() {
        return new JSONSerializer().include("wells").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
}
