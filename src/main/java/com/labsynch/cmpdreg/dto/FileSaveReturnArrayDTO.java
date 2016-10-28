package com.labsynch.cmpdreg.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class FileSaveReturnArrayDTO{
			 
	private List<FileSaveReturnDTO> returnFileList = new ArrayList<FileSaveReturnDTO>();



	public String toJson() {
        return new JSONSerializer().exclude("*.class")
        		.exclude("returnFileList.file")
        		.serialize(this);
    }

	public static FileSaveReturnArrayDTO fromJsonToFileSaveReturnArrayDTO(String json) {
        return new JSONDeserializer<FileSaveReturnArrayDTO>().use(null, FileSaveReturnArrayDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<FileSaveReturnArrayDTO> collection) {
        return new JSONSerializer().exclude("*.class")
        		.exclude("returnFileList.file")
        		.serialize(collection);
    }

	public static Collection<FileSaveReturnArrayDTO> fromJsonArrayToFileSaveReturnArrayDTO(String json) {
        return new JSONDeserializer<List<FileSaveReturnArrayDTO>>().use(null, ArrayList.class).use("values", FileSaveReturnArrayDTO.class).deserialize(json);
    }
}
