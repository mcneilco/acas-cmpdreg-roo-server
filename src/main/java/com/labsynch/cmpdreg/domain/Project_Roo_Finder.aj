// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.Project;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Project_Roo_Finder {
    
    public static Long Project.countFindProjectsByCodeEquals(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Project.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Project AS o WHERE o.code = :code", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<Project> Project.findProjectsByCodeEquals(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Project.entityManager();
        TypedQuery<Project> q = em.createQuery("SELECT o FROM Project AS o WHERE o.code = :code", Project.class);
        q.setParameter("code", code);
        return q;
    }
    
    public static TypedQuery<Project> Project.findProjectsByCodeEquals(String code, String sortFieldName, String sortOrder) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Project.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Project AS o WHERE o.code = :code");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Project> q = em.createQuery(queryBuilder.toString(), Project.class);
        q.setParameter("code", code);
        return q;
    }
    
}
