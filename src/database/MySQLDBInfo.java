package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLDBInfo {
	//ბაზის პარამეტრები
	public static final String MYSQL_USERNAME = "root";
	public static final String MYSQL_PASSWORD = "Jomolungma8848";
	public static final String MYSQL_DATABASE_HOST = "localhost";
	public static final String MYSQL_DATABASE_PORT = "3306";
	public static final String MYSQL_DATABASE_NAME = "delegator";
	public static final String MYSQL_DATABASE_DRIVER = "com.mysql.jdbc.Driver";
	private static Connection con;

	/**
	 * Base Initialization
	 * @return
	 */
	public static Connection initConnection(){
		String url = "";
		if(con==null){
			url = "jdbc:mysql://" + MYSQL_DATABASE_HOST + ":" + MYSQL_DATABASE_PORT + "/" + MYSQL_DATABASE_NAME;
			try {
				Class.forName(MYSQL_DATABASE_DRIVER);
				con = DriverManager.getConnection(url, MYSQL_USERNAME, MYSQL_PASSWORD);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return con;
	}

	/**
	 * Returns connection to the base
	 * @return
	 */
	public static Connection getConnection() {
		if (con == null) {
			initConnection();
		}
		return con;
	}

	/**
	 * Starts statement
	 * @throws SQLException 
	 */
	public static Statement startUpStatement(Connection con) throws SQLException{
		Statement st = con.createStatement();
		st.executeQuery("USE " +MySQLDBInfo.MYSQL_DATABASE_NAME);
		return st;
	}
}
