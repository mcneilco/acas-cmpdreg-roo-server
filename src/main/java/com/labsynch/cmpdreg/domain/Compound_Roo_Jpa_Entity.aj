// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.Compound;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect Compound_Roo_Jpa_Entity {
    
    declare @type: Compound: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Compound.id;
    
    @Version
    @Column(name = "version")
    private Integer Compound.version;
    
    public Long Compound.getId() {
        return this.id;
    }
    
    public void Compound.setId(Long id) {
        this.id = id;
    }
    
    public Integer Compound.getVersion() {
        return this.version;
    }
    
    public void Compound.setVersion(Integer version) {
        this.version = version;
    }
    
}
