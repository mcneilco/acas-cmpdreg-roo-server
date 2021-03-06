// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.CompoundType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CompoundType_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager CompoundType.entityManager;
    
    public static final List<String> CompoundType.fieldNames4OrderClauseFilter = java.util.Arrays.asList("code", "name", "displayOrder", "comment");
    
    public static final EntityManager CompoundType.entityManager() {
        EntityManager em = new CompoundType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long CompoundType.countCompoundTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CompoundType o", Long.class).getSingleResult();
    }
    
    public static List<CompoundType> CompoundType.findAllCompoundTypes() {
        return entityManager().createQuery("SELECT o FROM CompoundType o", CompoundType.class).getResultList();
    }
    
    public static List<CompoundType> CompoundType.findAllCompoundTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CompoundType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CompoundType.class).getResultList();
    }
    
    public static CompoundType CompoundType.findCompoundType(Long id) {
        if (id == null) return null;
        return entityManager().find(CompoundType.class, id);
    }
    
    public static List<CompoundType> CompoundType.findCompoundTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CompoundType o", CompoundType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<CompoundType> CompoundType.findCompoundTypeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CompoundType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CompoundType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void CompoundType.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void CompoundType.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            CompoundType attached = CompoundType.findCompoundType(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void CompoundType.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void CompoundType.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public CompoundType CompoundType.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CompoundType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
