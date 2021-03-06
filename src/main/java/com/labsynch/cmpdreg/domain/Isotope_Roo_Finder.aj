// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.Isotope;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Isotope_Roo_Finder {
    
    public static Long Isotope.countFindIsotopesByAbbrevEquals(String abbrev) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        EntityManager em = Isotope.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Isotope AS o WHERE o.abbrev = :abbrev", Long.class);
        q.setParameter("abbrev", abbrev);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Isotope.countFindIsotopesByAbbrevEqualsAndNameEquals(String abbrev, String name) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Isotope.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Isotope AS o WHERE o.abbrev = :abbrev  AND o.name = :name", Long.class);
        q.setParameter("abbrev", abbrev);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Isotope.countFindIsotopesByNameEquals(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Isotope.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Isotope AS o WHERE o.name = :name", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<Isotope> Isotope.findIsotopesByAbbrevEquals(String abbrev, String sortFieldName, String sortOrder) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        EntityManager em = Isotope.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Isotope AS o WHERE o.abbrev = :abbrev");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Isotope> q = em.createQuery(queryBuilder.toString(), Isotope.class);
        q.setParameter("abbrev", abbrev);
        return q;
    }
    
    public static TypedQuery<Isotope> Isotope.findIsotopesByAbbrevEqualsAndNameEquals(String abbrev, String name, String sortFieldName, String sortOrder) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Isotope.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Isotope AS o WHERE o.abbrev = :abbrev  AND o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Isotope> q = em.createQuery(queryBuilder.toString(), Isotope.class);
        q.setParameter("abbrev", abbrev);
        q.setParameter("name", name);
        return q;
    }
    
    public static TypedQuery<Isotope> Isotope.findIsotopesByNameEquals(String name, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Isotope.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Isotope AS o WHERE o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Isotope> q = em.createQuery(queryBuilder.toString(), Isotope.class);
        q.setParameter("name", name);
        return q;
    }
    
}
