// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.FileType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect FileType_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager FileType.entityManager;
    
    public static final List<String> FileType.fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "code");
    
    public static final EntityManager FileType.entityManager() {
        EntityManager em = new FileType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long FileType.countFileTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM FileType o", Long.class).getSingleResult();
    }
    
    public static List<FileType> FileType.findAllFileTypes() {
        return entityManager().createQuery("SELECT o FROM FileType o", FileType.class).getResultList();
    }
    
    public static List<FileType> FileType.findAllFileTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM FileType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, FileType.class).getResultList();
    }
    
    public static FileType FileType.findFileType(Long id) {
        if (id == null) return null;
        return entityManager().find(FileType.class, id);
    }
    
    public static List<FileType> FileType.findFileTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM FileType o", FileType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<FileType> FileType.findFileTypeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM FileType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, FileType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void FileType.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void FileType.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            FileType attached = FileType.findFileType(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void FileType.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void FileType.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public FileType FileType.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        FileType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
