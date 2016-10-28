package com.labsynch.cmpdreg.dto;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.cmpdreg.domain.LotAlias;
import com.labsynch.cmpdreg.domain.ParentAlias;

@RooJavaBean
@RooToString
@RooJson
public class LotAliasDTO {

    private String lotCorpName;
    
    private String lsType;
    
    private String lsKind;
    
    private String aliasName;
    
    private boolean preferred;
    
    public LotAliasDTO(){
    	
    }
    
    public LotAliasDTO(LotAlias lotAlias){
    	this.lotCorpName = lotAlias.getLot().getCorpName();
    	this.lsType = lotAlias.getLsType();
    	this.lsKind = lotAlias.getLsKind();
    	this.aliasName = lotAlias.getAliasName();
    	this.preferred = lotAlias.isPreferred();
    }
    
    public boolean getPreferred(){
    	return this.preferred;
    }
    
    public static LotAliasDTO getLotByAlias(LotAliasDTO lotAliasDTO) {
		LotAlias lotAlias;
		try {
			lotAlias = LotAlias.findLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(lotAliasDTO.getAliasName(), lotAliasDTO.getLsType(), lotAliasDTO.getLsKind()).getSingleResult();
		} catch (EmptyResultDataAccessException e){
			lotAlias = null;
		}
		return new LotAliasDTO(lotAlias);
	}

	public static Collection<LotAliasDTO> getLotsByAlias(Collection<LotAliasDTO> lotAliasDTOs) {
		Collection<LotAliasDTO> results = new HashSet<LotAliasDTO>();
		Collection<LotAlias> lotAliases = null;
		for (LotAliasDTO lotAliasDTO : lotAliasDTOs){
			lotAliases = LotAlias.findLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(lotAliasDTO.getAliasName(), lotAliasDTO.getLsType(), lotAliasDTO.getLsKind()).getResultList();
			for (LotAlias alias : lotAliases){
				results.add(new LotAliasDTO(alias));
			}
		}
		return results;
	}
    
   
}
