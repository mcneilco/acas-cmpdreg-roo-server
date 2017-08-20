package com.labsynch.cmpdreg.chemclasses.jchem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFReader;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;

public class CmpdRegSDFReaderJChemImpl implements CmpdRegSDFReader {

	private MolImporter molImporter;
	
	public CmpdRegSDFReaderJChemImpl(String fileName) throws CmpdRegMolFormatException, FileNotFoundException, IOException{
		FileInputStream fis;
		fis = new FileInputStream (fileName);
		try{
			this.molImporter = new MolImporter(fis);
		}catch (MolFormatException e) {
			throw new CmpdRegMolFormatException(e);
		}
	};
	
	@Override
	public void close() throws IOException{
		this.molImporter.close();
	}
	
	@Override
	public CmpdRegMoleculeJChemImpl readNextMol() throws IOException{
		Molecule mol = this.molImporter.read();
		CmpdRegMoleculeJChemImpl molecule = new CmpdRegMoleculeJChemImpl(mol);
		return molecule;
	}
}
