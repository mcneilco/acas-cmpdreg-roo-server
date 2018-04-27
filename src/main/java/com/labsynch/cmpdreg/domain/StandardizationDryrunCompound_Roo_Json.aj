// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.StandardizationDryRunCompound;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect StandardizationDryRunCompound_Roo_Json {
    
    public String StandardizationDryRunCompound.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String StandardizationDryRunCompound.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static StandardizationDryRunCompound StandardizationDryRunCompound.fromJsonToStandardizationDryRunCompound(String json) {
        return new JSONDeserializer<StandardizationDryRunCompound>()
        .use(null, StandardizationDryRunCompound.class).deserialize(json);
    }
    
    public static String StandardizationDryRunCompound.toJsonArray(Collection<StandardizationDryRunCompound> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String StandardizationDryRunCompound.toJsonArray(Collection<StandardizationDryRunCompound> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<StandardizationDryRunCompound> StandardizationDryRunCompound.fromJsonArrayToStandardizationDryRunCompounds(String json) {
        return new JSONDeserializer<List<StandardizationDryRunCompound>>()
        .use("values", StandardizationDryRunCompound.class).deserialize(json);
    }
    
}