// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.Vendor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Vendor_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Vendor.entityManager;
    
    public static final List<String> Vendor.fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "code");
    
    public static final EntityManager Vendor.entityManager() {
        EntityManager em = new Vendor().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Vendor.countVendors() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Vendor o", Long.class).getSingleResult();
    }
    
    public static List<Vendor> Vendor.findAllVendors() {
        return entityManager().createQuery("SELECT o FROM Vendor o", Vendor.class).getResultList();
    }
    
    public static List<Vendor> Vendor.findAllVendors(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Vendor o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Vendor.class).getResultList();
    }
    
    public static Vendor Vendor.findVendor(Long id) {
        if (id == null) return null;
        return entityManager().find(Vendor.class, id);
    }
    
    public static List<Vendor> Vendor.findVendorEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Vendor o", Vendor.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Vendor> Vendor.findVendorEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Vendor o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Vendor.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Vendor.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Vendor.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Vendor attached = Vendor.findVendor(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Vendor.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Vendor.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Vendor Vendor.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Vendor merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
