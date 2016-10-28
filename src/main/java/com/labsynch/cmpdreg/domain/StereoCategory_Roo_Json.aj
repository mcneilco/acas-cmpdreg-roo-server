// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.StereoCategory;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect StereoCategory_Roo_Json {
    
    public String StereoCategory.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String StereoCategory.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static StereoCategory StereoCategory.fromJsonToStereoCategory(String json) {
        return new JSONDeserializer<StereoCategory>()
        .use(null, StereoCategory.class).deserialize(json);
    }
    
    public static String StereoCategory.toJsonArray(Collection<StereoCategory> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String StereoCategory.toJsonArray(Collection<StereoCategory> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<StereoCategory> StereoCategory.fromJsonArrayToStereoCategorys(String json) {
        return new JSONDeserializer<List<StereoCategory>>()
        .use("values", StereoCategory.class).deserialize(json);
    }
    
}
