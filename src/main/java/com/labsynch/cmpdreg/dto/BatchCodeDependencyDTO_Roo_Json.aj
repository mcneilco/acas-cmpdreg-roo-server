// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.dto;

import com.labsynch.cmpdreg.dto.BatchCodeDependencyDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect BatchCodeDependencyDTO_Roo_Json {
    
    public static BatchCodeDependencyDTO BatchCodeDependencyDTO.fromJsonToBatchCodeDependencyDTO(String json) {
        return new JSONDeserializer<BatchCodeDependencyDTO>()
        .use(null, BatchCodeDependencyDTO.class).deserialize(json);
    }
    
    public static String BatchCodeDependencyDTO.toJsonArray(Collection<BatchCodeDependencyDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String BatchCodeDependencyDTO.toJsonArray(Collection<BatchCodeDependencyDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<BatchCodeDependencyDTO> BatchCodeDependencyDTO.fromJsonArrayToBatchCoes(String json) {
        return new JSONDeserializer<List<BatchCodeDependencyDTO>>()
        .use("values", BatchCodeDependencyDTO.class).deserialize(json);
    }
    
}
