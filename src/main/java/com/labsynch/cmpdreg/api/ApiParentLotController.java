package com.labsynch.cmpdreg.api;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.cmpdreg.domain.Lot;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.dto.CodeTableDTO;
import com.labsynch.cmpdreg.dto.LotDTO;
import com.labsynch.cmpdreg.dto.ParentEditDTO;
import com.labsynch.cmpdreg.dto.ParentLotCodeDTO;
import com.labsynch.cmpdreg.dto.ReparentLotDTO;
import com.labsynch.cmpdreg.service.LotService;
import com.labsynch.cmpdreg.service.ParentLotService;
import com.labsynch.cmpdreg.utils.SecurityUtil;

@RequestMapping(value = {"/api/v1/parentLot"})
@Controller
public class ApiParentLotController {
	
	Logger logger = LoggerFactory.getLogger(ApiParentLotController.class);
	
	@Autowired
	private ParentLotService parentLotService;

	@Autowired
	private LotService lotService;
	
	@Transactional
	@RequestMapping(value = "/getLotsByParent", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getParentByAliasName(@RequestParam String parentCorpName){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			Collection<CodeTableDTO> codeTableLots = parentLotService.getCodeTableLotsByParentCorpName(parentCorpName);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(codeTableLots), headers, HttpStatus.OK);
		}catch(Exception e){
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
	}
	
	@Transactional
	@RequestMapping(value = "/getLotsByParentAlias", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getLotCodesByParentAlias(@RequestBody Collection<ParentLotCodeDTO> requestDTOs){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			Collection<ParentLotCodeDTO> responseDTO = parentLotService.getLotCodesByParentAlias(requestDTOs);
			return new ResponseEntity<String>(ParentLotCodeDTO.toJsonArray(responseDTO), headers, HttpStatus.OK);
		}catch(Exception e){
			logger.error("Caught exception searching for lots by parent alias",e);
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
	}
	
	@Transactional
	@RequestMapping(value = "/updateLot", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> updateLotAndParent(@RequestBody Collection<ParentLotCodeDTO> requestDTOs){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			Collection<ParentLotCodeDTO> responseDTO = parentLotService.getLotCodesByParentAlias(requestDTOs);
			return new ResponseEntity<String>(ParentLotCodeDTO.toJsonArray(responseDTO), headers, HttpStatus.OK);
		}catch(Exception e){
			logger.error("Caught exception searching for lots by parent alias",e);
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	@RequestMapping(value = "/updateLot/metadata", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> updateLotMetadata(@RequestBody String json){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			String modifiedByUser = SecurityUtil.getLoginUser().getCode();
			LotDTO lotDTO = LotDTO.fromJsonToLotDTO(json);
			Lot lot = lotService.updateLotMeta(lotDTO, modifiedByUser);
			return new ResponseEntity<String>(lot.toJsonIncludeAliases(), headers, HttpStatus.OK);
		}catch(Exception e){
			logger.error("Caught exception updating lot metadata",e);
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
	}
	
	@Transactional
	@RequestMapping(value = "/updateLot/metadata/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> updateLotMetaArray(@RequestBody String json){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			String modifiedByUser = SecurityUtil.getLoginUser().getCode();
			String results = lotService.updateLotMetaArray(json, modifiedByUser);
			return new ResponseEntity<String>(results, headers, HttpStatus.OK);
		}catch(Exception e){
			logger.error("Caught error trying to update lot metadata",e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Transactional
	@RequestMapping(value = "/reparentLot", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> reparentLot(@RequestBody String json){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			String modifiedByUser = SecurityUtil.getLoginUser().getCode();
			ReparentLotDTO lotDTO = ReparentLotDTO.fromJsonToReparentLotDTO(json);
			Lot lot = lotService.reparentLot(lotDTO.getLotCorpName(), lotDTO.getParentCorpName(), modifiedByUser);
			return new ResponseEntity<String>(lot.toJsonIncludeAliases(), headers, HttpStatus.OK);
		}catch(Exception e){
			logger.error("Caught exception updating lot metadata",e);
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	@RequestMapping(value = "/reparentLot/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> reparentLotArray(@RequestBody String json){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		int lotCount = 0;
		try{
			String modifiedByUser = SecurityUtil.getLoginUser().getCode();
			Collection<ReparentLotDTO> lotDTOs = ReparentLotDTO.fromJsonArrayToReparentLoes(json);
			for (ReparentLotDTO lotDTO : lotDTOs){
				lotService.reparentLot(lotDTO.getLotCorpName(), lotDTO.getParentCorpName(), modifiedByUser);
				lotCount++;
			}
			return new ResponseEntity<String>("number of lots reparented: " + lotCount, headers, HttpStatus.OK);
		}catch(Exception e){
			logger.error("Caught exception updating lot metadata",e);
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
	}

}
