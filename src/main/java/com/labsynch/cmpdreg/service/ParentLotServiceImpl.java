package com.labsynch.cmpdreg.service;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.ParentAlias;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.dto.CodeTableDTO;
import com.labsynch.cmpdreg.dto.ParentLotCodeDTO;

@Service
public class ParentLotServiceImpl implements ParentLotService {

	Logger logger = LoggerFactory.getLogger(ParentLotServiceImpl.class);

	@Override
	@Transactional
	public Collection<CodeTableDTO> getCodeTableLotsByParentCorpName(String parentCorpName) throws NoResultException{
		Parent parent = Parent.findParentsByCorpNameEquals(parentCorpName).getSingleResult();
		Collection<Lot> lots = new HashSet<Lot>();
		for (SaltForm saltForm : parent.getSaltForms()){
			lots.addAll(saltForm.getLots());
		}
		Collection<CodeTableDTO> codeTableLots = new HashSet<CodeTableDTO>();
		for (Lot lot : lots){
			CodeTableDTO codeTable = new CodeTableDTO();
			codeTable.setCode(lot.getCorpName());
			codeTable.setName(lot.getCorpName());
			codeTable.setComments(lot.getComments());
			codeTableLots.add(codeTable);
		}
		return codeTableLots;
	}

	@Override
	@Transactional
	public Collection<ParentLotCodeDTO> getLotCodesByParentAlias(
			Collection<ParentLotCodeDTO> requestDTOs) {
		Collection<ParentLotCodeDTO> responseDTOs = new HashSet<ParentLotCodeDTO>();
		for (ParentLotCodeDTO requestDTO : requestDTOs){
			Collection<Parent> foundParents = new HashSet<Parent>();
			try{
				Collection<ParentAlias> foundAliases = ParentAlias.findParentAliasesByAliasNameEquals(requestDTO.getRequestName()).getResultList();
				for (ParentAlias foundAlias : foundAliases){
					if (foundAlias.getParent() != null) foundParents.add(foundAlias.getParent());
				}
				foundParents.addAll(Parent.findParentsByCorpNameEquals(requestDTO.getRequestName()).getResultList());
			}catch(Exception e){
				//do nothing
			}
			if (!foundParents.isEmpty()){
				for (Parent foundParent : foundParents){
					ParentLotCodeDTO responseDTO = new ParentLotCodeDTO();
					responseDTO.setRequestName(requestDTO.getRequestName());
					responseDTO.setReferenceCode(foundParent.getCorpName());
					responseDTO.setLotCodes(new HashSet<String>());
					for (SaltForm saltForm : foundParent.getSaltForms()){
						for (Lot lot : saltForm.getLots()){
							responseDTO.getLotCodes().add(lot.getCorpName());
						}
					}
					responseDTOs.add(responseDTO);
				}
			}else{
				ParentLotCodeDTO responseDTO = new ParentLotCodeDTO();
				responseDTO.setRequestName(requestDTO.getRequestName());
				responseDTOs.add(responseDTO);
			}
		}
		return responseDTOs;
	}

	
}

