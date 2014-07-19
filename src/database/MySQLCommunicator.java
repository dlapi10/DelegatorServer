package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLCommunicator {
	//Private variables
	private ResultSet resultSet;
	private Connection connection;
	private Statement statement;
	
	
	public MySQLCommunicator(Connection con){
		this.connection = con;
	}
	
	/**
	 * Updating database with given update
	 * @param update
	 */
	public void execUpdate(String update){
		try {
			statement = connection.createStatement();
			statement.executeUpdate(update);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
