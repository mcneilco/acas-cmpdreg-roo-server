package com.labsynch.cmpdreg.db.migration.postgres;

import java.sql.Connection;
import java.sql.SQLException;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chemaxon.jchem.db.UpdateHandlerException;
import chemaxon.jchem.db.Updater;
import chemaxon.util.ConnectionHandler;

public class V1_0_2_0__Upgrade_Jchem_Tables implements JdbcMigration {
 
	Logger logger = LoggerFactory.getLogger(V1_0_2_0__Upgrade_Jchem_Tables.class);


	public void migrate(Connection conn) throws Exception {
		logger.info("ATTEMPTING TO UPGRADE JCHEM TABLES");
		conn.setAutoCommit(true);
		logger.info("connection autocommit mode: " + conn.getAutoCommit());
		logger.info("getTransactionIsolation  " + conn.getTransactionIsolation());

		recalculateJChemTable(conn);

		conn.setAutoCommit(false);
		logger.info("connection autocommit mode: " + conn.getAutoCommit());

	}	

	private boolean recalculateJChemTable(Connection conn) throws UpdateHandlerException, SQLException {
		ConnectionHandler ch = new ConnectionHandler();
		ch.setConnection(conn);

		String message = "";
		Updater ud = new Updater(ch);
		Updater.UpdateInfo ui = null;
		while ((ui = ud.getNextUpdateInfo()) != null) {
			logger.info("\n" + ui.processingMessage + "\n");
			logger.info("Is structure change required: " + ui.isStructuralChange);
			message = ud.performCurrentUpdate();
			logger.info(message);
		}

		logger.info("updated the Jchem structure tables " );

		return false;
	}




}

