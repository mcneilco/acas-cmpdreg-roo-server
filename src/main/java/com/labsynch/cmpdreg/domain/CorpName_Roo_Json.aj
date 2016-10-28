// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.CorpName;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect CorpName_Roo_Json {
    
    public String CorpName.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String CorpName.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static CorpName CorpName.fromJsonToCorpName(String json) {
        return new JSONDeserializer<CorpName>()
        .use(null, CorpName.class).deserialize(json);
    }
    
    public static String CorpName.toJsonArray(Collection<CorpName> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String CorpName.toJsonArray(Collection<CorpName> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<CorpName> CorpName.fromJsonArrayToCorpNames(String json) {
        return new JSONDeserializer<List<CorpName>>()
        .use("values", CorpName.class).deserialize(json);
    }
    
}
