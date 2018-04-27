// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto;

import com.labsynch.cmpdreg.dto.LDStandardizerInputDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect LDStandardizerInputDTO_Roo_Json {
    
    public static LDStandardizerInputDTO LDStandardizerInputDTO.fromJsonToLDStandardizerInputDTO(String json) {
        return new JSONDeserializer<LDStandardizerInputDTO>()
        .use(null, LDStandardizerInputDTO.class).deserialize(json);
    }
    
    public static String LDStandardizerInputDTO.toJsonArray(Collection<LDStandardizerInputDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String LDStandardizerInputDTO.toJsonArray(Collection<LDStandardizerInputDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<LDStandardizerInputDTO> LDStandardizerInputDTO.fromJsonArrayToLDStandardizerInputDTO(String json) {
        return new JSONDeserializer<List<LDStandardizerInputDTO>>()
        .use("values", LDStandardizerInputDTO.class).deserialize(json);
    }
    
}