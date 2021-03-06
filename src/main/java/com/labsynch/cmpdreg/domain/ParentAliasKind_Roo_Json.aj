// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.ParentAliasKind;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect ParentAliasKind_Roo_Json {
    
    public String ParentAliasKind.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String ParentAliasKind.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static ParentAliasKind ParentAliasKind.fromJsonToParentAliasKind(String json) {
        return new JSONDeserializer<ParentAliasKind>()
        .use(null, ParentAliasKind.class).deserialize(json);
    }
    
    public static String ParentAliasKind.toJsonArray(Collection<ParentAliasKind> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String ParentAliasKind.toJsonArray(Collection<ParentAliasKind> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<ParentAliasKind> ParentAliasKind.fromJsonArrayToParentAliasKinds(String json) {
        return new JSONDeserializer<List<ParentAliasKind>>()
        .use("values", ParentAliasKind.class).deserialize(json);
    }
    
}
