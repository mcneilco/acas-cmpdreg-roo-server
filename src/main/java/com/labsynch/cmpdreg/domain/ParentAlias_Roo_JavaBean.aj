// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.ParentAlias;

privileged aspect ParentAlias_Roo_JavaBean {
    
    public Parent ParentAlias.getParent() {
        return this.parent;
    }
    
    public void ParentAlias.setParent(Parent parent) {
        this.parent = parent;
    }
    
    public String ParentAlias.getLsType() {
        return this.lsType;
    }
    
    public void ParentAlias.setLsType(String lsType) {
        this.lsType = lsType;
    }
    
    public String ParentAlias.getLsKind() {
        return this.lsKind;
    }
    
    public void ParentAlias.setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }
    
    public String ParentAlias.getAliasName() {
        return this.aliasName;
    }
    
    public void ParentAlias.setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
    
    public boolean ParentAlias.isPreferred() {
        return this.preferred;
    }
    
    public void ParentAlias.setPreferred(boolean preferred) {
        this.preferred = preferred;
    }
    
    public boolean ParentAlias.isIgnored() {
        return this.ignored;
    }
    
    public void ParentAlias.setIgnored(boolean ignored) {
        this.ignored = ignored;
    }
    
    public boolean ParentAlias.isDeleted() {
        return this.deleted;
    }
    
    public void ParentAlias.setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    public Integer ParentAlias.getSortId() {
        return this.sortId;
    }
    
    public void ParentAlias.setSortId(Integer sortId) {
        this.sortId = sortId;
    }
    
}
