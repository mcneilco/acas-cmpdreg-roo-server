package com.labsynch.cmpdreg.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.labsynch.cmpdreg.dto.MolConvertOutputDTO;
import com.labsynch.cmpdreg.dto.StrippedSaltDTO;

import chemaxon.formats.MolFormatException;
import chemaxon.struc.Molecule;

@Service
public interface ChemStructureService {


	public int getCount(String structureTable);

	public int saveStructure(String molfile, String structureTable);

	public int[] searchMolStructures(String molfile, String structureTable, String searchType, Float simlarityPercent);

	void closeConnection();

	public int[] searchMolStructures(String molfile, String structureTable, String searchType);

	public int[] searchMolStructures(String molfile, String structureTable,
			String plainTable, String searchType, Float simlarityPercent);

	public boolean dropJChemTable(String tableName);

	public int[] searchMolStructures(String molfile, String structureTable,
			String plainTable, String searchType);

	public boolean createJChemTable(String tableName, boolean tautomerDupe);

	public int saveStructure(String molfile, String structureTable, boolean checkForDupes);

	public Molecule[] searchMols(String molfile, String structureTable,
			int[] cdHitList, String plainTable, String searchType,
			Float simlarityPercent);

	public double getMolWeight(String molStructure);

	public Molecule toMolecule(String molStructure);

	public String toMolfile(String molStructure);

	public String toSmiles(String molStructure);

	public boolean createJchemPropertyTable();


	public int[] checkDupeMol(String molStructure, String structureTable, String plainTable);

	public String toInchi(String molStructure);

	public boolean updateStructure(String molStructure, String structureTable, int cdId);

	public String getMolFormula(String molStructure);

	public boolean deleteAllJChemTableRows(String tableName);

	public boolean deleteJChemTableRows(String tableName, int[] cdIds);

	boolean checkForSalt(String molfile) throws MolFormatException;

	public boolean updateStructure(Molecule mol, String structureTable, int cdId);
	double getExactMass(String molStructure);

	boolean deleteStructure(String structureTable, int cdId);

	public Molecule[] searchMols(String molfile, String structureTable,
			int[] inputCdIdHitList, String plainTable, String searchType,
			Float simlarityPercent, int maxResults);

	public int[] searchMolStructures(String molfile, String structureTable,
			String plainTable, String searchType, Float simlarityPercent,
			int maxResults);

	MolConvertOutputDTO toFormat(String structure, String inputFormat, String outputFormat) throws IOException;

	MolConvertOutputDTO cleanStructure(String structure, int dim, String opts) throws IOException;

	String hydrogenizeMol(String structure, String inputFormat, String method) throws IOException;

	String getCipStereo(String structure) throws IOException;

	StrippedSaltDTO stripSalts(Molecule inputStructure);



}
