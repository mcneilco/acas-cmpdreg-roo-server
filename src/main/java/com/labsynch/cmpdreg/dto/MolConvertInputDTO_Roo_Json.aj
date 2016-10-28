// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto;

import com.labsynch.cmpdreg.dto.MolConvertInputDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect MolConvertInputDTO_Roo_Json {
    
    public String MolConvertInputDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String MolConvertInputDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static MolConvertInputDTO MolConvertInputDTO.fromJsonToMolConvertInputDTO(String json) {
        return new JSONDeserializer<MolConvertInputDTO>()
        .use(null, MolConvertInputDTO.class).deserialize(json);
    }
    
    public static String MolConvertInputDTO.toJsonArray(Collection<MolConvertInputDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String MolConvertInputDTO.toJsonArray(Collection<MolConvertInputDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<MolConvertInputDTO> MolConvertInputDTO.fromJsonArrayToMolCoes(String json) {
        return new JSONDeserializer<List<MolConvertInputDTO>>()
        .use("values", MolConvertInputDTO.class).deserialize(json);
    }
    
}
