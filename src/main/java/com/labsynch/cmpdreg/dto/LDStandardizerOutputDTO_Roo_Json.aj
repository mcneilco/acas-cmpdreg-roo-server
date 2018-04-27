// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto;

import com.labsynch.cmpdreg.dto.LDStandardizerOutputDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect LDStandardizerOutputDTO_Roo_Json {
    
    public static LDStandardizerOutputDTO LDStandardizerOutputDTO.fromJsonToLDStandardizerOutputDTO(String json) {
        return new JSONDeserializer<LDStandardizerOutputDTO>()
        .use(null, LDStandardizerOutputDTO.class).deserialize(json);
    }
    
    public static String LDStandardizerOutputDTO.toJsonArray(Collection<LDStandardizerOutputDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String LDStandardizerOutputDTO.toJsonArray(Collection<LDStandardizerOutputDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<LDStandardizerOutputDTO> LDStandardizerOutputDTO.fromJsonArrayToLDStandardizerOutputDTO(String json) {
        return new JSONDeserializer<List<LDStandardizerOutputDTO>>()
        .use("values", LDStandardizerOutputDTO.class).deserialize(json);
    }
    
}