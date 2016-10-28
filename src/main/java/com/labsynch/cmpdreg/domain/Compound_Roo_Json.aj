// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.Compound;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect Compound_Roo_Json {
    
    public String Compound.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String Compound.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static Compound Compound.fromJsonToCompound(String json) {
        return new JSONDeserializer<Compound>()
        .use(null, Compound.class).deserialize(json);
    }
    
    public static String Compound.toJsonArray(Collection<Compound> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String Compound.toJsonArray(Collection<Compound> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<Compound> Compound.fromJsonArrayToCompounds(String json) {
        return new JSONDeserializer<List<Compound>>()
        .use("values", Compound.class).deserialize(json);
    }
    
}
