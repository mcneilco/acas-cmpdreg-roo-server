package com.labsynch.cmpdreg.utils;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.web.util.UriComponentsBuilder;

public class SimpleUtil {
		
	private static int PARAMETER_LIMIT = 999;
	
	public static boolean isNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) return false;
		}
		return true;
	}
	
	public static boolean isDecimalNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c) && c != '-' && c!= '.') return false;
		}
		return true;
	}
	
	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	
	public static List<String> splitSearchString(String searchString){
		List<String> list = new ArrayList<String>();
		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(searchString);
		while (m.find()){
			list.add(m.group(1).replace("\"",""));
		}
		return list;
	}
	
	public static String toAlphabetic(int i){
		if( i<0 ) {
	        return "-"+toAlphabetic(-i-1);
	    }

	    int quot = i/26;
	    int rem = i%26;
	    char letter = (char)((int)'A' + rem);
	    if( quot == 0 ) {
	        return ""+letter;
	    } else {
	        return toAlphabetic(quot-1) + letter;
	    }
	}
	
	public static Collection<Query> splitHqlInClause(EntityManager em, String queryString, String attributeName, List<String> matchStrings){
		Map<String, Collection<String>> sqlCurveIdMap = new HashMap<String, Collection<String>>();
    	List<String> allCodes = new ArrayList<String>();
    	allCodes.addAll(matchStrings);
    	int startIndex = 0;
    	Collection<Query> allQueries = new ArrayList<Query>();
    	while (startIndex < matchStrings.size()){
    		int endIndex;
    		if (startIndex+PARAMETER_LIMIT < matchStrings.size()) endIndex = startIndex+PARAMETER_LIMIT;
    		else endIndex = matchStrings.size();
    		List<String> nextCodes = allCodes.subList(startIndex, endIndex);
    		String groupName = "strings"+startIndex;
    		String sqlClause = " "+attributeName+" IN (:"+groupName+")";
    		sqlCurveIdMap.put(sqlClause, nextCodes);
    		startIndex=endIndex;
    	}
    	for (String sqlClause : sqlCurveIdMap.keySet()){
			String completeQueryString = queryString + sqlClause + " )";
			Query q = em.createQuery(completeQueryString);
			String groupName = sqlClause.split(":")[1].replace(")","");
        	q.setParameter(groupName, sqlCurveIdMap.get(sqlClause));
        	allQueries.add(q);
    	}
    	return allQueries;
	}
	
	public static Query addHqlInClause(EntityManager em, String queryString, String attributeName, List<String> matchStrings){
		Map<String, Collection<String>> sqlCurveIdMap = new HashMap<String, Collection<String>>();
    	List<String> allCodes = new ArrayList<String>();
    	allCodes.addAll(matchStrings);
    	int startIndex = 0;
    	while (startIndex < matchStrings.size()){
    		int endIndex;
    		if (startIndex+PARAMETER_LIMIT < matchStrings.size()) endIndex = startIndex+PARAMETER_LIMIT;
    		else endIndex = matchStrings.size();
    		List<String> nextCodes = allCodes.subList(startIndex, endIndex);
    		String groupName = "strings"+startIndex;
    		String sqlClause = " "+attributeName+" IN (:"+groupName+")";
    		sqlCurveIdMap.put(sqlClause, nextCodes);
    		startIndex=endIndex;
    	}
    	int numClause = 1;
    	for (String sqlClause : sqlCurveIdMap.keySet()){
    		if (numClause == 1){
    			queryString = queryString + sqlClause;
    		}else{
    			queryString = queryString + " OR " + sqlClause;
    		}
    		numClause++;
    	}
    	queryString = queryString + " )";
    	Query q = em.createQuery(queryString);
		for (String sqlClause : sqlCurveIdMap.keySet()){
        	String groupName = sqlClause.split(":")[1].replace(")","");
        	q.setParameter(groupName, sqlCurveIdMap.get(sqlClause));
        }
    	return q;
	}
	
	public static Predicate buildInPredicate(CriteriaBuilder cb, Expression<String> property, List<String> values) {
		Predicate predicate = null;
        int listSize = values.size();
        for (int i = 0; i < listSize; i += PARAMETER_LIMIT) {
            List<String> subList;
            if (listSize > i + PARAMETER_LIMIT) {
                subList = values.subList(i, (i + PARAMETER_LIMIT));
            } else {
                subList = values.subList(i, listSize);
            }
            if (predicate != null) {
            	predicate = cb.or(predicate, property.in(subList));
            } else {
            	predicate = property.in(subList);
            }
        }
        return predicate;
    }
	
	public static final List<Long> getIdsFromSequence(JdbcTemplate jdbcTemplate, String sequenceName, int numberOfIds){
		String databaseType = null;
		try{
			databaseType = (String) JdbcUtils.extractDatabaseMetaData(jdbcTemplate.getDataSource(), "getDatabaseProductName");
		}catch (MetaDataAccessException e){}
		List<Long> idList = new ArrayList<Long>();
		if (databaseType.equalsIgnoreCase("Oracle")){
			String getIdsSql = "SELECT "+sequenceName+".nextval as id from dual connect by level <"+(numberOfIds+1);
			idList = jdbcTemplate.query(getIdsSql, new RowMapper<Long>(){
				public Long mapRow(ResultSet rs, int rowNum) throws SQLException{
					return rs.getLong(1);
				}
			});
		}
		else{ 
			String getIdsSql = "SELECT nextval('"+sequenceName+"') as id from generate_series(1,"+numberOfIds+")";
			idList = jdbcTemplate.query(getIdsSql, new RowMapper<Long>(){
				public Long mapRow(ResultSet rs, int rowNum) throws SQLException{
					return rs.getLong(1);
				}
			});
		}
		return idList;
	}
	
	public static String postRequestToExternalServer(String url, String jsonContent, Logger logger) throws MalformedURLException, IOException {
		String charset = "UTF-8";
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Accept-Charset", charset);
		connection.setRequestProperty("Content-Type", "application/json");		
		logger.info("Sending request to: "+url);
		logger.info("with data: "+jsonContent);
		try{
			OutputStream output = connection.getOutputStream();
			output.write(jsonContent.getBytes());
		}catch (Exception e){
			logger.error("Error occurred in making HTTP Request to external server",e);
		}
		InputStream input = connection.getInputStream();
		byte[] bytes = IOUtils.toByteArray(input);
		String responseJson = new String(bytes);
		return responseJson;
	}
	
	public static String getFromExternalServer(String url, Map<String, String> queryParams, Logger logger) throws MalformedURLException, IOException {
		String charset = "UTF-8";
		UriComponentsBuilder ub = UriComponentsBuilder.fromHttpUrl(url);
		if (queryParams != null){
			for (String param : queryParams.keySet()) {
				ub.queryParam(param, URLEncoder.encode(queryParams.get(param), charset));
			}
		}
		String fullUrl = ub.build().toUriString();
		HttpURLConnection connection = (HttpURLConnection) new URL(fullUrl).openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Accept-Charset", charset);
		logger.info("Sending request to: "+fullUrl);
		int responseCode = connection.getResponseCode();
		logger.info("Response Code: "+responseCode);
		BufferedReader inStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = inStream.readLine()) != null) {
			response.append(inputLine);
		}
		inStream.close();
		return response.toString();
	}
}