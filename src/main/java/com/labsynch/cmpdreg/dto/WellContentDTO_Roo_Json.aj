// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto;

import com.labsynch.cmpdreg.dto.WellContentDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect WellContentDTO_Roo_Json {
    
    public String WellContentDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String WellContentDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static WellContentDTO WellContentDTO.fromJsonToWellContentDTO(String json) {
        return new JSONDeserializer<WellContentDTO>()
        .use(null, WellContentDTO.class).deserialize(json);
    }
    
    public static String WellContentDTO.toJsonArray(Collection<WellContentDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String WellContentDTO.toJsonArray(Collection<WellContentDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<WellContentDTO> WellContentDTO.fromJsonArrayToWellCoes(String json) {
        return new JSONDeserializer<List<WellContentDTO>>()
        .use("values", WellContentDTO.class).deserialize(json);
    }
    
}
