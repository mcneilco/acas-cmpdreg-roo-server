package com.labsynch.cmpdreg.db.migration.sqlserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.labsynch.cmpdreg.dto.configuration.MainConfigDTO;
import com.labsynch.cmpdreg.utils.Configuration;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class V1_0_1_4__set_defaults implements SpringJdbcMigration {

	Logger logger = LoggerFactory.getLogger(V1_0_1_4__set_defaults.class);
	
	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();
	
	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		savePhysicalStates(jdbcTemplate);
		saveStereoCategorys(jdbcTemplate);
		saveIsotopes(jdbcTemplate);
		saveOperators(jdbcTemplate);
		saveUnits(jdbcTemplate);
		saveFileTypes(jdbcTemplate);
		savePurityMeasuredBys(jdbcTemplate);
		loadCorpNames(jdbcTemplate);
				
	}
	
	private void savePhysicalStates(JdbcTemplate jdbcTemplate){
		String json = "["
				+"{ \"code\": \"solid\", \"name\": \"solid\"},"
				+"{ \"code\": \"liquid\", \"name\": \"liquid\"},"
				+"{ \"code\": \"glass\", \"name\": \"glass\"},"
				+"{ \"code\": \"oil\", \"name\": \"oil\"},"
				+"{ \"code\": \"resin\", \"name\": \"resin\"},"
				+"{ \"code\": \"solution\", \"name\": \"solution\"}"
				+"]";
		String objectTable = "physical_state";
		Collection<CodeAndNameObject> objects = CodeAndNameObject.fromJsonArrayToCodeAndNameObjects(json);
		for (CodeAndNameObject object : objects){
			try{
				String select = "SELECT * FROM " + objectTable
						+ " WHERE code = '"+object.getCode()+"' "
						+ "AND  name = '"+object.getName()+"'";
				CodeAndNameObject foundObject = jdbcTemplate.queryForObject(select, new CodeAndNameRowMapper());
			}catch(EmptyResultDataAccessException e){
				String insert = "INSERT INTO " + objectTable
						+ " (code, name, version) VALUES ("
						//+ "(NEXT VALUE FOR hibernate_sequence, "
						+ "'"+object.getCode()+"', "
						+ "'"+object.getName()+"', "
						+ " 0)";
				jdbcTemplate.update(insert);
			}
		}
		
	}
	
	private void saveStereoCategorys(JdbcTemplate jdbcTemplate){
		String json = "["
				+"{\"code\": \"scalemic\", \"name\": \"Scalemic\"},"
				+"{\"code\": \"racemic\", \"name\": \"Racemic\"},"
				+"{\"code\": \"achiral\", \"name\": \"Achiral\"},"
				+"{\"code\": \"see_comments\", \"name\": \"See Comments\"},"
				+"{\"code\": \"unknown\", \"name\": \"Unknown\"}"
				+"]";
		String objectTable = "stereo_category";
		Collection<CodeAndNameObject> objects = CodeAndNameObject.fromJsonArrayToCodeAndNameObjects(json);
		for (CodeAndNameObject object : objects){
			try{
				String select = "SELECT * FROM " + objectTable
						+ " WHERE code = '"+object.getCode()+"' "
						+ "AND  name = '"+object.getName()+"'";
				CodeAndNameObject foundObject = jdbcTemplate.queryForObject(select, new CodeAndNameRowMapper());
			}catch(EmptyResultDataAccessException e){
				String insert = "INSERT INTO " + objectTable
						+ " (code, name, version) VALUES "
						+ "("
						+ "'"+object.getCode()+"', "
						+ "'"+object.getName()+"', "
						+ " 0)";
				jdbcTemplate.update(insert);
			}
		}
	}
	
	private void saveIsotopes(JdbcTemplate jdbcTemplate){
		String json =  "["
				+"{\"name\":\"Deuterium labeled\",\"abbrev\":\"2H\",\"massChange\":1},"
				+"{\"name\":\"Tritium labeled\",\"abbrev\":\"3H\",\"massChange\":2},"
				+"{\"name\":\"13C labeled\",\"abbrev\":\"13C\",\"massChange\":1},"
				+"{\"name\":\"14C labeled\",\"abbrev\":\"14C\",\"massChange\":2}"
				+"]";
		String objectTable = "isotope";
		Collection<IsotopeObject> objects = IsotopeObject.fromJsonArrayToIsotopeObjects(json);
		for (IsotopeObject object : objects){
			try{
				String select = "SELECT * FROM " + objectTable
						+ " WHERE abbrev = '"+object.getAbbrev()+"' "
						+ "AND  name = '"+object.getName()+"'";
				IsotopeObject foundObject = jdbcTemplate.queryForObject(select, new IsotopeRowMapper());
			}catch(EmptyResultDataAccessException e){
				String insert = "INSERT INTO " + objectTable
						+ " (abbrev, ignore, mass_change, name, version) VALUES "
						+ "("
						+ "'"+object.getAbbrev()+"', "
						+ " 0, "
						+ object.getMassChange()+", "
						+ "'"+object.getName()+"', "
						+ " 0)";
				jdbcTemplate.update(insert);
			}
		}
	}
	
	private void saveOperators(JdbcTemplate jdbcTemplate){
		String json = "["
				+"{\"code\": \"=\", \"name\": \"=\"},"
				+"{\"code\": \"<\", \"name\": \"<\"},"
				+"{\"code\": \">\", \"name\": \">\"}"
				+"]";
		String objectTable = "operator";
		Collection<CodeAndNameObject> objects = CodeAndNameObject.fromJsonArrayToCodeAndNameObjects(json);
		for (CodeAndNameObject object : objects){
			try{
				String select = "SELECT * FROM " + objectTable
						+ " WHERE code = '"+object.getCode()+"' "
						+ "AND  name = '"+object.getName()+"'";
				CodeAndNameObject foundObject = jdbcTemplate.queryForObject(select, new CodeAndNameRowMapper());
			}catch(EmptyResultDataAccessException e){
				String insert = "INSERT INTO " + objectTable
						+ " (code, name, version) VALUES "
						+ "( "
						+ "'"+object.getCode()+"', "
						+ "'"+object.getName()+"', "
						+ " 0)";
				jdbcTemplate.update(insert);
			}
		}
	}
	
	private void saveUnits(JdbcTemplate jdbcTemplate){
		String json = "["
				+"{\"code\": \"mg\", \"name\": \"mg\"},"
				+"{\"code\": \"g\", \"name\": \"g\"},"
				+"{\"code\": \"kg\", \"name\": \"kg\"},"
				+"{\"code\": \"mL\", \"name\": \"mL\"}"
				+"]";
		String objectTable = "unit";
		Collection<CodeAndNameObject> objects = CodeAndNameObject.fromJsonArrayToCodeAndNameObjects(json);
		for (CodeAndNameObject object : objects){
			try{
				String select = "SELECT * FROM " + objectTable
						+ " WHERE code = '"+object.getCode()+"' "
						+ "AND  name = '"+object.getName()+"'";
				CodeAndNameObject foundObject = jdbcTemplate.queryForObject(select, new CodeAndNameRowMapper());
			}catch(EmptyResultDataAccessException e){
				String insert = "INSERT INTO " + objectTable
						+ " (code, name, version) VALUES "
						+ "( "
						+ "'"+object.getCode()+"', "
						+ "'"+object.getName()+"', "
						+ " 0)";
				jdbcTemplate.update(insert);
			}
		}
	}
	
	private void saveFileTypes(JdbcTemplate jdbcTemplate){
		String json = "["
				+"{\"code\":\"HPLC\",\"name\":\"HPLC\",\"version\":0},"
				+"{\"code\":\"LCMS\",\"name\":\"LCMS\",\"version\":0},"
				+"{\"code\":\"NMR\",\"name\":\"NMR\",\"version\":0}"
				+"]";
		String objectTable = "file_type";
		Collection<CodeAndNameObject> objects = CodeAndNameObject.fromJsonArrayToCodeAndNameObjects(json);
		for (CodeAndNameObject object : objects){
			try{
				String select = "SELECT * FROM " + objectTable
						+ " WHERE code = '"+object.getCode()+"' "
						+ "AND  name = '"+object.getName()+"'";
				CodeAndNameObject foundObject = jdbcTemplate.queryForObject(select, new CodeAndNameRowMapper());
			}catch(EmptyResultDataAccessException e){
				String insert = "INSERT INTO " + objectTable
						+ " (code, name, version) VALUES "
						+ "( "
						+ "'"+object.getCode()+"', "
						+ "'"+object.getName()+"', "
						+ " 0)";
				jdbcTemplate.update(insert);
			}
		}
		
	}
	
	private void savePurityMeasuredBys(JdbcTemplate jdbcTemplate){
		String json = "["
				+"{\"code\":\"HPLC\",\"name\":\"HPLC\",\"version\":0},"
				+"{\"code\":\"NMR\",\"name\":\"NMR\",\"version\":0},"
				+"{\"code\":\"GC\",\"name\":\"GC\",\"version\":0},"
				+"{\"code\":\"Not Done\",\"name\":\"Not Done\",\"version\":0}"
				+"]";
		String objectTable = "purity_measured_by";
		Collection<CodeAndNameObject> objects = CodeAndNameObject.fromJsonArrayToCodeAndNameObjects(json);
		for (CodeAndNameObject object : objects){
			try{
				String select = "SELECT * FROM " + objectTable
						+ " WHERE code = '"+object.getCode()+"' "
						+ "AND  name = '"+object.getName()+"'";
				CodeAndNameObject foundObject = jdbcTemplate.queryForObject(select, new CodeAndNameRowMapper());
			}catch(EmptyResultDataAccessException e){
				String insert = "INSERT INTO " + objectTable
						+ " (code, name, version) VALUES "
						+ "( "
						+ "'"+object.getCode()+"', "
						+ "'"+object.getName()+"', "
						+ " 0)";
				jdbcTemplate.update(insert);
			}
		}
	}
	
	private void loadCorpNames(JdbcTemplate jdbcTemplate) throws FileNotFoundException {
		// 2 options
		// load the corpName from a predefined List
		// or generate it from a simple counter
		int numCorpnames = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM pre_def_corp_name");
		if (numCorpnames < 1L){
			boolean usePredefinedList = mainConfig.getServerSettings().isUsePredefinedList();
			long numberOfCorpNamesToGenerate = 50000;
			
			if (usePredefinedList && numCorpnames < 1L){
				logger.debug("load up preDefined corpNames ");
				
				String fileName = "src/test/resources/predef_corpname.csv";
			    File inputFile = new File(fileName); 
				Scanner scanner = new Scanner(new FileReader(inputFile));
				
				//skip the header line
				String header = scanner.nextLine();
				logger.debug("header line: " + header);
				
				int lineCount = 0;
				while ( scanner.hasNextLine() && lineCount < 500 ){
					processLine( scanner.nextLine() , jdbcTemplate);
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
					corpName = Configuration.getConfigInfo().getServerSettings().getCorpPrefix()
							.concat(Configuration.getConfigInfo().getServerSettings().getCorpSeparator())
							.concat(String.format(formatCorpDigits, corpNumber));	    		
//					boolean used = false;
//					boolean skip = false;
					int used = 0;
					int skip = 0;
					String objectTable = "pre_def_corp_name";
					String insert = "INSERT INTO " + objectTable
							+ " (corp_name, corp_number, used, skip, version) VALUES "
							+ "( "
							+ "'"+corpName+"', "
							+ corpNumber+", "
							+ used+", "
							+ skip+", "
							+ " 0)";
					jdbcTemplate.update(insert);
				}
			}
		}
	}

	
	protected void processLine(String aLine, JdbcTemplate jdbcTemplate){
		//use a second Scanner to parse the content of each line 
		Scanner scanner = new Scanner(aLine);
		scanner.useDelimiter(",");
		if ( scanner.hasNext() ){
			
			//"id","corpname","used","skip"
			String id = scanner.next();
			String corpName = scanner.next().replaceAll("\"", "");
			long corpNumber = convertToParentNumber(corpName);
			String usedString = scanner.next();
			String skipString = scanner.next();
			int used = 0;
			int skip= 0;
			
			if (usedString.equalsIgnoreCase("1")){
				used = 1;
			} else {
				used = 0;
			}
			
			if (skipString.equalsIgnoreCase("1")){
				skip = 1;
			} else {
				skip = 0;
			}
			
			
			String objectTable = "pre_def_corp_name";
			String insert = "INSERT INTO " + objectTable
					+ " (corp_name, corp_number, used, skip, version) VALUES "
					+ "( "
					+ "'"+corpName+"', "
					+ corpNumber+", "
					+ used+", "
					+ skip+", "
					+ " 0)";
			jdbcTemplate.update(insert);
		}
		else {
			logger.error("Empty or invalid line. Unable to process.");
		}
	}
	
	private static class CodeAndNameObject{
		private long id;
		private String code;
		private String name;
		private int version;
		
		public long getId(){
			return this.id;
		}
		
		public void setId(long id){
			this.id = id;
		}
		
		public String getCode(){
			return this.code;
		}
		
		public void setCode(String code){
			this.code = code;
		}
		
		public String getName(){
			return this.name;
		}
		
		public void setName(String name){
			this.name = name;
		}
		
		public int getVersion(){
			return this.version;
		}
		
		public void setVersion(int version){
			this.version = version;
		}
		
		CodeAndNameObject fromJsonToCodeAndNameObject(String json) {
	        return new JSONDeserializer<CodeAndNameObject>()
	        .use(null, CodeAndNameObject.class).deserialize(json);
	    }
	    
	    String toJsonArray(Collection<CodeAndNameObject> collection) {
	        return new JSONSerializer()
	        .exclude("*.class").serialize(collection);
	    }
	    
	    String toJsonArray(Collection<CodeAndNameObject> collection, String[] fields) {
	        return new JSONSerializer()
	        .include(fields).exclude("*.class").serialize(collection);
	    }
	    
	    static Collection<CodeAndNameObject> fromJsonArrayToCodeAndNameObjects(String json) {
	        return new JSONDeserializer<List<CodeAndNameObject>>()
	        .use("values", CodeAndNameObject.class).deserialize(json);
	    }
	}
	
	@SuppressWarnings("rawtypes")
	public class CodeAndNameRowMapper implements RowMapper
	{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			CodeAndNameObject object = new CodeAndNameObject();
			object.setId(rs.getLong("id"));
			object.setCode(rs.getString("code"));
			object.setName(rs.getString("name"));
			object.setVersion(rs.getInt("version"));
			return object;
		}
	}
	
	private static class IsotopeObject{
		private long id;
		private String name;
		private String abbrev;
		private Double massChange;
		private int version;
		
		public long getId(){
			return this.id;
		}
		
		public void setId(long id){
			this.id = id;
		}
		
		public String getAbbrev(){
			return this.abbrev;
		}
		
		public void setAbbrev(String abbrev){
			this.abbrev = abbrev;
		}
		
		public String getName(){
			return this.name;
		}
		
		public void setName(String name){
			this.name = name;
		}
		
		public Double getMassChange(){
			return this.massChange;
		}
		
		public void setMassChange(Double massChange){
			this.massChange = massChange;
		}
		
		public int getVersion(){
			return this.version;
		}
		
		public void setVersion(int version){
			this.version = version;
		}
		
		IsotopeObject fromJsonToIsotopeObject(String json) {
	        return new JSONDeserializer<IsotopeObject>()
	        .use(null, IsotopeObject.class).deserialize(json);
	    }
	    
	    String toJsonArray(Collection<IsotopeObject> collection) {
	        return new JSONSerializer()
	        .exclude("*.class").serialize(collection);
	    }
	    
	    String toJsonArray(Collection<IsotopeObject> collection, String[] fields) {
	        return new JSONSerializer()
	        .include(fields).exclude("*.class").serialize(collection);
	    }
	    
	    static Collection<IsotopeObject> fromJsonArrayToIsotopeObjects(String json) {
	        return new JSONDeserializer<List<IsotopeObject>>()
	        .use("values", IsotopeObject.class).deserialize(json);
	    }
	}
	
	@SuppressWarnings("rawtypes")
	public class IsotopeRowMapper implements RowMapper
	{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			IsotopeObject object = new IsotopeObject();
			object.setId(rs.getLong("id"));
			object.setAbbrev(rs.getString("abbrev"));
			object.setName(rs.getString("name"));
			object.setMassChange(rs.getDouble("mass_change"));
			object.setVersion(rs.getInt("version"));
			return object;
		}
	}
	
	Long convertToParentNumber(String corpName) {
		corpName = corpName.trim();
		Pattern corpNamePattern = Pattern.compile("^" + ".*?" + "([0-9]{1,9})" + ".*?$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = corpNamePattern.matcher(corpName);
		logger.debug("found: " + matcher.replaceFirst("$1"));
		String corpNumberString = matcher.replaceFirst("$1");
		Long corpNumber = parseCorpNumber(corpNumberString);
		return corpNumber;
	}
	
	Long parseCorpNumber(String corpName) {
		corpName = corpName.trim();
		long corpNumber;
		try {
			corpNumber = Long.parseLong(corpName);
		} catch (Exception e){
			logger.debug("caught an exception parsing the corp number. set to default 0");
			corpNumber = 0L;
		}
		return corpNumber;
	}
	

	
}

