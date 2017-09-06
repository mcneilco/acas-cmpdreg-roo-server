package com.labsynch.cmpdreg.chemclasses;

import java.io.IOException;

import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

public interface CmpdRegMolecule {
		
	public void setProperty(String key, String value);
	
	public String getProperty(String key);
	
	public String[] getPropertyKeys();
	
	public String getPropertyType(String key);
	
	public String getMolStructure() throws CmpdRegMolFormatException;

	public String getFormula();

	public Double getExactMass();

	public Double getMass();

	public int getTotalCharge();

	public String getSmiles();
	
	public CmpdRegMolecule replaceStructure(String newStructure) throws CmpdRegMolFormatException;

	public String getMrvStructure();
	
	public byte[] toBinary(CmpdRegMolecule molecule, String format) throws IOException;

	public void dearomatize();

}
