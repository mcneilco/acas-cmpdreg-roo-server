package com.labsynch.cmpdreg.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.cmpdreg.domain.FileList;
import com.labsynch.cmpdreg.domain.IsoSalt;
import com.labsynch.cmpdreg.domain.Lot;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@RooJavaBean
@RooToString
@RooJson

public class Metalot {
	
	private boolean skipParentDupeCheck = false;

	private Lot lot;
	
	private Set<IsoSalt> isosalts = new HashSet<IsoSalt>();

	private Set<FileList> fileList = new HashSet<FileList>();

	

	public String toJson() {
        String json = new JSONSerializer().include("isosalts", "fileList", "lot", "lot.lotAliases", "lot.saltForm.parent.parentAliases").exclude("*.class", "isosalts.saltForm", "fileList.lot")
        		.transform( new DateTransformer( "MM/dd/yyyy"), Date.class)
        		.serialize(this);
        System.out.println("fromMetaLotToJson");
        System.out.println(json);
        return json;
	}


	public static Metalot fromJsonToMetalot(String json) {
        System.out.println("fromJsonToMetalot");
        System.out.println(json);
        return new JSONDeserializer<Metalot>().use(null, Metalot.class)
        		.use( Date.class, new DateTransformer( "MM/dd/yyyy"))
        		.deserialize(json);
    }

	public static String toJsonArray(Collection<Metalot> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<Metalot> fromJsonArrayToMetalots(String json) {
        return new JSONDeserializer<List<Metalot>>().use(null, ArrayList.class).use("values", Metalot.class).deserialize(json);
    }
}
