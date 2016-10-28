// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.PreDef_CorpName;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect PreDef_CorpName_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager PreDef_CorpName.entityManager;
    
    public static final List<String> PreDef_CorpName.fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "corpNumber", "corpName", "comment", "used", "skip");
    
    public static final EntityManager PreDef_CorpName.entityManager() {
        EntityManager em = new PreDef_CorpName().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long PreDef_CorpName.countPreDef_CorpNames() {
        return entityManager().createQuery("SELECT COUNT(o) FROM PreDef_CorpName o", Long.class).getSingleResult();
    }
    
    public static List<PreDef_CorpName> PreDef_CorpName.findAllPreDef_CorpNames() {
        return entityManager().createQuery("SELECT o FROM PreDef_CorpName o", PreDef_CorpName.class).getResultList();
    }
    
    public static List<PreDef_CorpName> PreDef_CorpName.findAllPreDef_CorpNames(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM PreDef_CorpName o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, PreDef_CorpName.class).getResultList();
    }
    
    public static PreDef_CorpName PreDef_CorpName.findPreDef_CorpName(Long id) {
        if (id == null) return null;
        return entityManager().find(PreDef_CorpName.class, id);
    }
    
    public static List<PreDef_CorpName> PreDef_CorpName.findPreDef_CorpNameEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM PreDef_CorpName o", PreDef_CorpName.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<PreDef_CorpName> PreDef_CorpName.findPreDef_CorpNameEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM PreDef_CorpName o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, PreDef_CorpName.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void PreDef_CorpName.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void PreDef_CorpName.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            PreDef_CorpName attached = PreDef_CorpName.findPreDef_CorpName(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void PreDef_CorpName.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void PreDef_CorpName.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public PreDef_CorpName PreDef_CorpName.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        PreDef_CorpName merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
