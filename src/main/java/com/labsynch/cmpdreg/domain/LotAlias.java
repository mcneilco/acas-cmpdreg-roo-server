package com.labsynch.cmpdreg.domain;

import javax.persistence.ManyToOne;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.cmpdreg.dto.LotAliasDTO;
import com.labsynch.cmpdreg.exceptions.LotNotFoundException;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders={"findLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", "findLotAliasesByLot"})
public class LotAlias {

	private static final Logger logger = LoggerFactory.getLogger(LotAlias.class);
	
	@ManyToOne
    @org.hibernate.annotations.Index(name="LotAlias_Parent_IDX")
	private Lot lot;
	
    private String lsType;

    private String lsKind;
    
    private String aliasName;
    
	private boolean preferred;
	
	private boolean ignored;
	
	private boolean deleted;
    
	public LotAlias(){
	}
	
	public LotAlias(Lot lot, String lsType, String lsKind, String aliasName, boolean preferred){
		this.lot = lot;
		this.lsType = lsType;
		this.lsKind = lsKind;
		this.aliasName = aliasName;
		this.preferred = preferred;
	}
	
	public LotAlias(LotAliasDTO lotAliasDTO) throws LotNotFoundException{
		try{
			Lot lot = Lot.findLotsByCorpNameEquals(lotAliasDTO.getLotCorpName()).getSingleResult();
			this.lot = lot;
		}catch (Exception e){
			logger.error("Lot "+lotAliasDTO.getLotCorpName()+" could not be found.",e);
			throw new LotNotFoundException("Lot "+lotAliasDTO.getLotCorpName()+" could not be found.");
		}
		this.lsType = lotAliasDTO.getLsType();
		this.lsKind = lotAliasDTO.getLsKind();
		this.aliasName = lotAliasDTO.getAliasName();
		this.preferred = lotAliasDTO.isPreferred();
	}
}
