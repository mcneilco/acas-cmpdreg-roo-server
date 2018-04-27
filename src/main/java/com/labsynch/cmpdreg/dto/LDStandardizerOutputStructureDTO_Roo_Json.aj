// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto;

import com.labsynch.cmpdreg.dto.LDStandardizerOutputStructureDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect LDStandardizerOutputStructureDTO_Roo_Json {
    
    public String LDStandardizerOutputStructureDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String LDStandardizerOutputStructureDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static LDStandardizerOutputStructureDTO LDStandardizerOutputStructureDTO.fromJsonToLDStandardizerOutputStructureDTO(String json) {
        return new JSONDeserializer<LDStandardizerOutputStructureDTO>()
        .use(null, LDStandardizerOutputStructureDTO.class).deserialize(json);
    }
    
    public static String LDStandardizerOutputStructureDTO.toJsonArray(Collection<LDStandardizerOutputStructureDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String LDStandardizerOutputStructureDTO.toJsonArray(Collection<LDStandardizerOutputStructureDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<LDStandardizerOutputStructureDTO> LDStandardizerOutputStructureDTO.fromJsonArrayToLDStandardizerOutputStructureDTO(String json) {
        return new JSONDeserializer<List<LDStandardizerOutputStructureDTO>>()
        .use("values", LDStandardizerOutputStructureDTO.class).deserialize(json);
    }
    
}