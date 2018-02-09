// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.cmpdreg.domain;

import com.labsynch.cmpdreg.domain.StandardizationSettings;
import com.labsynch.cmpdreg.domain.StandardizationSettingsDataOnDemand;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

privileged aspect StandardizationSettingsDataOnDemand_Roo_DataOnDemand {
    
    declare @type: StandardizationSettingsDataOnDemand: @Component;
    
    private Random StandardizationSettingsDataOnDemand.rnd = new SecureRandom();
    
    private List<StandardizationSettings> StandardizationSettingsDataOnDemand.data;
    
    public StandardizationSettings StandardizationSettingsDataOnDemand.getNewTransientStandardizationSettings(int index) {
        StandardizationSettings obj = new StandardizationSettings();
        setCurrentSettings(obj, index);
        setCurrentSettingsHash(obj, index);
        setModifiedDate(obj, index);
        setNeedsStandardization(obj, index);
        return obj;
    }
    
    public void StandardizationSettingsDataOnDemand.setCurrentSettings(StandardizationSettings obj, int index) {
        String currentSettings = "currentSettings_" + index;
        obj.setCurrentSettings(currentSettings);
    }
    
    public void StandardizationSettingsDataOnDemand.setCurrentSettingsHash(StandardizationSettings obj, int index) {
        int currentSettingsHash = index;
        obj.setCurrentSettingsHash(currentSettingsHash);
    }
    
    public void StandardizationSettingsDataOnDemand.setModifiedDate(StandardizationSettings obj, int index) {
        Date modifiedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setModifiedDate(modifiedDate);
    }
    
    public void StandardizationSettingsDataOnDemand.setNeedsStandardization(StandardizationSettings obj, int index) {
        Boolean needsStandardization = Boolean.TRUE;
        obj.setNeedsStandardization(needsStandardization);
    }
    
    public StandardizationSettings StandardizationSettingsDataOnDemand.getSpecificStandardizationSettings(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        StandardizationSettings obj = data.get(index);
        Long id = obj.getId();
        return StandardizationSettings.findStandardizationSettings(id);
    }
    
    public StandardizationSettings StandardizationSettingsDataOnDemand.getRandomStandardizationSettings() {
        init();
        StandardizationSettings obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return StandardizationSettings.findStandardizationSettings(id);
    }
    
    public boolean StandardizationSettingsDataOnDemand.modifyStandardizationSettings(StandardizationSettings obj) {
        return false;
    }
    
    public void StandardizationSettingsDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = StandardizationSettings.findStandardizationSettingsEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'StandardizationSettings' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<StandardizationSettings>();
        for (int i = 0; i < 10; i++) {
            StandardizationSettings obj = getNewTransientStandardizationSettings(i);
            try {
                obj.persist();
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
