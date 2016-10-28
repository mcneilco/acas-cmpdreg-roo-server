// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.CorpName;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CorpName_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager CorpName.entityManager;
    
    public static final List<String> CorpName.fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "prefix", "separator", "saltSeparator", "batchSeparator", "isFancyCorpNumberFormat", "numberCorpDigits", "saltBeforeLot", "appendSaltCodeToLotName", "corpParentFormat", "corpBatchFormat", "databaseType", "parentCorpName", "comment", "ignore");
    
    public static final EntityManager CorpName.entityManager() {
        EntityManager em = new CorpName().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long CorpName.countCorpNames() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CorpName o", Long.class).getSingleResult();
    }
    
    public static List<CorpName> CorpName.findAllCorpNames() {
        return entityManager().createQuery("SELECT o FROM CorpName o", CorpName.class).getResultList();
    }
    
    public static List<CorpName> CorpName.findAllCorpNames(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CorpName o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CorpName.class).getResultList();
    }
    
    public static CorpName CorpName.findCorpName(Long id) {
        if (id == null) return null;
        return entityManager().find(CorpName.class, id);
    }
    
    public static List<CorpName> CorpName.findCorpNameEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CorpName o", CorpName.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<CorpName> CorpName.findCorpNameEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CorpName o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CorpName.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void CorpName.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void CorpName.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            CorpName attached = CorpName.findCorpName(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void CorpName.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void CorpName.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public CorpName CorpName.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CorpName merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
