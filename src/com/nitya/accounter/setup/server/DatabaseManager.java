package com.nitya.accounter.setup.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.setup.client.core.DatabaseConnection;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * It will be using for only Desktop Application.
 * 
 * @author Prasanna Kumar G
 * 
 */
public class DatabaseManager {

	public static final String DB_FILE = "dbconfig.xml";

	public static final String POSTGRESQL = "postgresql";
	public static final String H2 = "h2";
	public static final String MYSQL = "mysql";
	public static final String ORACLE = "oracle";

	private static DatabaseManager manager = null;

	public static DatabaseManager getInstance() {
		if (manager == null) {
			manager = new DatabaseManager();
		}
		return manager;
	}

	public static boolean isDBConfigured() {
		if (!ServerConfiguration.isDesktopApp()) {
			return true;
		}
		File file = new File(ServerConfiguration.getAttachmentsDir(), DB_FILE);
		return file.exists();
	}

	public void deleteDBConfig() {
		String attachmentsDir = ServerConfiguration.getAttachmentsDir();
		File dbConfig = new File(attachmentsDir, DB_FILE);
		if (dbConfig != null) {
			dbConfig.deleteOnExit();
		}
	}

	public void saveDBConnection(DatabaseConnection conn) throws IOException {

		String attachmentsDir = ServerConfiguration.getAttachmentsDir();
		File dbConfig = new File(attachmentsDir, DB_FILE);
		if (!dbConfig.exists()) {
			dbConfig.createNewFile();
		}

		BufferedWriter out = new BufferedWriter(new FileWriter(dbConfig));

		DbConfig config = new DbConfig();
		config.type = getDbNameByType(conn.getDbType());
		config.url = getURL(conn);
		config.driver = getDriver(conn.getDbType());
		config.dialect = getDialect(conn.getDbType());
		config.username = conn.getUsername();
		config.password = conn.getPassword();
		config.schema = conn.getSchema();
		XStream xStream = getXStream();
		String xml = xStream.toXML(config);

		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.newLine();
		out.write(xml);
		out.close();
	}

	public void loadDbConfig() throws FileNotFoundException {

		DbConfig config = getDbConfig();
		if (config == null) {
			return;
		}
		System.setProperty("db.driver", config.driver);
		System.setProperty("db.url", config.url);
		System.setProperty("db.user", config.username);
		System.setProperty("db.pass", config.password);
		System.setProperty("dialect", config.dialect);

	}

	public DbConfig getDbConfig() {
		String attachmentsDir = ServerConfiguration.getAttachmentsDir();
		File dbConfig = new File(attachmentsDir, DB_FILE);
		if (!dbConfig.exists()) {
			return null;
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(dbConfig));
			XStream xStream = getXStream();
			DbConfig config = (DbConfig) xStream.fromXML(reader);
			return config;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	public String getDbType() {
		DbConfig dbConfig = getDbConfig();
		if (dbConfig != null) {
			return dbConfig.type;
		}
		return null;
	}

	private XStream getXStream() {
		XStream xStream = new XStream(new DomDriver());
		xStream.alias("accounter-database-config", DbConfig.class);
		xStream.aliasField("database-type", DbConfig.class, "type");
		xStream.aliasField("schema-name", DbConfig.class, "schema");
		xStream.aliasField("driver-class", DbConfig.class, "driver");
		xStream.aliasField("pool-size", DbConfig.class, "poolize");
		return xStream;
	}

	private String getDbNameByType(int dbType) {
		switch (dbType) {
		case DatabaseConnection.DB_TYPE_POSTGRES:
			return POSTGRESQL;
		case DatabaseConnection.DB_TYPE_MYSQL:
			return MYSQL;
		case DatabaseConnection.DB_TYPE_H2:
			return H2;
		}
		return "";
	}

	public String getDriver(int dbType) {
		switch (dbType) {
		case DatabaseConnection.DB_TYPE_POSTGRES:
			return "org.postgresql.Driver";
		case DatabaseConnection.DB_TYPE_MYSQL:
			return "com.mysql.jdbc.Driver";
		case DatabaseConnection.DB_TYPE_H2:
			return "org.h2.Driver";
		case DatabaseConnection.DB_TYPE_ORACLE:
			return "oracle.jdbc.driver.OracleDriver";
		}
		return "";
	}

	public String getURL(DatabaseConnection conn) {
		switch (conn.getDbType()) {
		case DatabaseConnection.DB_TYPE_POSTGRES:
			return "jdbc:postgresql://" + conn.getHostname() + ":"
					+ conn.getPort() + "/" + conn.getDbName();
		case DatabaseConnection.DB_TYPE_MYSQL:
			return "jdbc:mysql://" + conn.getHostname() + ":" + conn.getPort()
					+ "/" + conn.getDbName();
		case DatabaseConnection.DB_TYPE_H2:
			return "jdbc:h2:" + conn.getDbName();
		}
		return "";
	}

	public String getDialect(int dbType) {
		switch (dbType) {
		case DatabaseConnection.DB_TYPE_POSTGRES:
			return "org.hibernate.dialect.AccounterPgSQLDialect";
		case DatabaseConnection.DB_TYPE_MYSQL:
			return "org.hibernate.dialect.AccounterMySQLDialect";
		case DatabaseConnection.DB_TYPE_H2:
			return "org.hibernate.dialect.H2Dialect";
		}
		return "";
	}

	public void checkConnection(DatabaseConnection connDetail) throws Exception {
		Connection connection = null;
		try {
			try {
				Class.forName(getDriver(connDetail.getDbType()));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new Exception(
						"Connection refused. Check that the hostname and port are correct");
			}

			connection = DriverManager.getConnection(getURL(connDetail),
					connDetail.getUsername(), connDetail.getPassword());
			if (connection == null) {
				throw new Exception("Cann't connect to database");
			}

			String selecttables = getSelectTablesCommand(connDetail);
			if (selecttables != null) {
				Statement statement = connection.createStatement();
				statement.execute(selecttables);
				ResultSet resultSet = statement.getResultSet();
				while (resultSet.next()) {
					long noOfTable = resultSet.getLong("count");
					if (noOfTable > 0) {
						throw new Exception("Database is not Empty");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

	}

	private String getSelectTablesCommand(DatabaseConnection conn) {
		switch (conn.getDbType()) {
		case DatabaseConnection.DB_TYPE_POSTGRES:
			return "select count(*) from pg_stat_user_tables WHERE schemaname='"
					+ conn.getSchema() + "'";
		case DatabaseConnection.DB_TYPE_H2:
			return "SELECT count(*) as count FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = SCHEMA()";
		default:
			break;
		}
		return null;
	}

	public DatabaseConnection getInternalDBConnection() throws IOException {
		DatabaseConnection conn = new DatabaseConnection();
		String attachmentsDir = ServerConfiguration.getAttachmentsDir();
		File file = new File(attachmentsDir, "db");
		if (!file.exists()) {
			file.mkdir();
		} else {
			if (file.listFiles() != null) {
				for (File child : file.listFiles()) {
					child.delete();
				}
			}
		}
		conn.setDbName(file.getPath() + File.separator + "accounter");
		conn.setUsername("sa");
		conn.setPassword("");
		conn.setDbType(DatabaseConnection.DB_TYPE_H2);
		return conn;
	}

}
