package com.labsynch.cmpdreg.chemclasses.jchem;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.labsynch.cmpdreg.chemclasses.CmpdRegMolecule;
import com.labsynch.cmpdreg.chemclasses.CmpdRegSDFWriter;
import com.labsynch.cmpdreg.exceptions.CmpdRegMolFormatException;

import chemaxon.formats.MolExporter;
import chemaxon.marvin.io.MolExportException;
import chemaxon.struc.Molecule;

public class CmpdRegSDFWriterJChemImpl implements CmpdRegSDFWriter {

	private MolExporter molExporter;
	
	private ByteArrayOutputStream outstream;
	
	public CmpdRegSDFWriterJChemImpl(String fileName) throws IllegalArgumentException, IOException{
		FileOutputStream outStream = new FileOutputStream (fileName);
		this.molExporter = new MolExporter(outStream, "sdf");
	};
	
	public CmpdRegSDFWriterJChemImpl(FileOutputStream outStream) throws IllegalArgumentException, IOException{
		this.molExporter = new MolExporter(outStream, "sdf");
	}
	
	public public CmpdRegSDFWriterJChemImpl() {
		this.outputStream = new ByteArrayOutputStream();
		this.molExporter = new MolExporter(outputStream, "sdf");
	}
	
	@Override
	public void close() throws IOException{
		this.molExporter.close();
	}
	
	@Override
	public boolean writeMol(CmpdRegMolecule molecule) throws CmpdRegMolFormatException, IOException{
		CmpdRegMoleculeJChemImpl molWrapper = (CmpdRegMoleculeJChemImpl) molecule;
		Molecule mol = molWrapper.molecule;
		try{
			return this.molExporter.write(mol);
		}catch (MolExportException e) {
			throw new CmpdRegMolFormatException(e);
		}
	}
	
	@Override
	public String getBufferString() {
		return this.outstream.toString();
	}
}
