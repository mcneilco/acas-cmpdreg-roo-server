// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.ParentAliasKind;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect ParentAliasKind_Roo_Jpa_Entity {
    
    declare @type: ParentAliasKind: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long ParentAliasKind.id;
    
    @Version
    @Column(name = "version")
    private Integer ParentAliasKind.version;
    
    public Long ParentAliasKind.getId() {
        return this.id;
    }
    
    public void ParentAliasKind.setId(Long id) {
        this.id = id;
    }
    
    public Integer ParentAliasKind.getVersion() {
        return this.version;
    }
    
    public void ParentAliasKind.setVersion(Integer version) {
        this.version = version;
    }
    
}
