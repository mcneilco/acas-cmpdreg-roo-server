// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto;

import com.labsynch.cmpdreg.dto.ParentValidationDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect ParentValidationDTO_Roo_Json {
    
    public static ParentValidationDTO ParentValidationDTO.fromJsonToParentValidationDTO(String json) {
        return new JSONDeserializer<ParentValidationDTO>()
        .use(null, ParentValidationDTO.class).deserialize(json);
    }
    
    public static String ParentValidationDTO.toJsonArray(Collection<ParentValidationDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String ParentValidationDTO.toJsonArray(Collection<ParentValidationDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<ParentValidationDTO> ParentValidationDTO.fromJsonArrayToParentValidatioes(String json) {
        return new JSONDeserializer<List<ParentValidationDTO>>()
        .use("values", ParentValidationDTO.class).deserialize(json);
    }
    
}
