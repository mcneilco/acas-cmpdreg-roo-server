package com.labsynch.cmpdreg.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.labsynch.cmpdreg.domain.CorpName;
import com.labsynch.cmpdreg.domain.PreDef_CorpName;
import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.utils.Configuration;


@Service
public class SetupServiceImpl implements SetupService {
	
	Logger logger = LoggerFactory.getLogger(SetupServiceImpl.class);
	
	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

	@Override
	public void loadCorpNames(String corpFileName) throws FileNotFoundException {
		// 2 options
		// load the corpName from a predefined List
		// or generate it from a simple counter

		boolean usePredefinedList = mainConfig.getServerSettings().isUsePredefinedList();
		long numberOfCorpNamesToGenerate = 50000;
		
		if (usePredefinedList && PreDef_CorpName.countPreDef_CorpNames() < 1L){
			logger.debug("load up preDefined corpNames ");
			
//			String fileName = "src/test/resources/predef_corpname.csv";
		    File inputFile = new File(corpFileName); 
			Scanner scanner = new Scanner(new FileReader(inputFile));
			
			//skip the header line
			String header = scanner.nextLine();
			logger.debug("header line: " + header);
			
			int lineCount = 0;
			while ( scanner.hasNextLine() && lineCount < 500 ){
				processLine( scanner.nextLine() );
				lineCount++;
			}

			scanner.close();			
		} else {
			long corpNumber = mainConfig.getServerSettings().getStartingCorpNumber(); //starting number
			int corpDigits = mainConfig.getServerSettings().getNumberCorpDigits();
			String formatCorpDigits = "%0" + corpDigits + "d";
			while ( corpNumber < numberOfCorpNamesToGenerate ){
				corpNumber++;
				String corpName = null;
				corpName = CorpName.prefix.concat(CorpName.separator).concat(String.format(formatCorpDigits, corpNumber));	    		
				boolean used = false;
				boolean skip = false;
				PreDef_CorpName preDefCorpName = new PreDef_CorpName();
				preDefCorpName.setCorpName(corpName);
				preDefCorpName.setCorpNumber(corpNumber);
				preDefCorpName.setUsed(used);
				preDefCorpName.setSkip(skip);
				preDefCorpName.persist();
				logger.debug(corpName);
			}
		}

	}

	
	protected void processLine(String aLine){
		//use a second Scanner to parse the content of each line 
		Scanner scanner = new Scanner(aLine);
		scanner.useDelimiter(",");
		if ( scanner.hasNext() ){
			
			//"id","corpname","used","skip"
			String id = scanner.next();
			String corpName = scanner.next().replaceAll("\"", "");
			long corpNumber = CorpName.convertToParentNumber(corpName);
			String usedString = scanner.next();
			String skipString = scanner.next();
			boolean used;
			boolean skip;
			
			if (usedString.equalsIgnoreCase("1")){
				used = true;
			} else {
				used = false;
			}
			
			if (skipString.equalsIgnoreCase("1")){
				skip = true;
			} else {
				skip = false;
			}
			
			
			PreDef_CorpName preDefCorpName = new PreDef_CorpName();
			preDefCorpName.setCorpName(corpName);
			preDefCorpName.setCorpNumber(corpNumber);
			preDefCorpName.setUsed(used);
			preDefCorpName.setSkip(skip);
			preDefCorpName.persist();
		}
		else {
			logger.error("Empty or invalid line. Unable to process.");
		}
	}


	@Override
	public void loadCorpNames() throws FileNotFoundException {
		String noFile = null;
		loadCorpNames(noFile);
	}


	
	


}
