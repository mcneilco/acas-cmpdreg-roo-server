// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.ParentAlias;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect ParentAlias_Roo_Jpa_Entity {
    
    declare @type: ParentAlias: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long ParentAlias.id;
    
    @Version
    @Column(name = "version")
    private Integer ParentAlias.version;
    
    public Long ParentAlias.getId() {
        return this.id;
    }
    
    public void ParentAlias.setId(Long id) {
        this.id = id;
    }
    
    public Integer ParentAlias.getVersion() {
        return this.version;
    }
    
    public void ParentAlias.setVersion(Integer version) {
        this.version = version;
    }
    
}
