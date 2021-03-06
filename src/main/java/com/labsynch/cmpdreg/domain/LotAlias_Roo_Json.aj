// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.LotAlias;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect LotAlias_Roo_Json {
    
    public String LotAlias.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String LotAlias.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static LotAlias LotAlias.fromJsonToLotAlias(String json) {
        return new JSONDeserializer<LotAlias>()
        .use(null, LotAlias.class).deserialize(json);
    }
    
    public static String LotAlias.toJsonArray(Collection<LotAlias> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String LotAlias.toJsonArray(Collection<LotAlias> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<LotAlias> LotAlias.fromJsonArrayToLotAliases(String json) {
        return new JSONDeserializer<List<LotAlias>>()
        .use("values", LotAlias.class).deserialize(json);
    }
    
}
