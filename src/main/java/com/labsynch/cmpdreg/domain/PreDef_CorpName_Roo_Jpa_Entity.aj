// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.PreDef_CorpName;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect PreDef_CorpName_Roo_Jpa_Entity {
    
    declare @type: PreDef_CorpName: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long PreDef_CorpName.id;
    
    @Version
    @Column(name = "version")
    private Integer PreDef_CorpName.version;
    
    public Long PreDef_CorpName.getId() {
        return this.id;
    }
    
    public void PreDef_CorpName.setId(Long id) {
        this.id = id;
    }
    
    public Integer PreDef_CorpName.getVersion() {
        return this.version;
    }
    
    public void PreDef_CorpName.setVersion(Integer version) {
        this.version = version;
    }
    
}
