package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import model.Task;

public class MySQLCommunicator {
	//Private variables
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
	
	/**
	 * Adding task to the database
	 * @param task
	 */
	public void addTask(Task task){
		StringBuilder update = new StringBuilder("insert into tasks(reporter,assignee,`status`,description,priority,`completion`,start_date,deadline,title) values(");
		if(task.getReporter()!=null && task.getReporter().getUserName()!=null) 
			update.append("\""+task.getReporter().getUserName()+"\",");
		if(task.getAssignee()!=null && task.getAssignee().getUserName()!=null) 
			update.append("\""+task.getAssignee().getUserName()+"\",");
		if(task.getStatus()!=null && task.getStatus().getStatusName()!=null) 
			update.append("\""+task.getStatus().getStatusName()+"\",");
		if(task.getDescription()!=null) 
			update.append("\""+task.getDescription()+"\",");
		
		update.append(""+task.getPriority()+",");
		update.append(""+task.getCompletionPercent()+",");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss",java.util.Locale.getDefault());
		String date;
		if(task.getStartDate()!=null){
			date = formatter.format(task.getStartDate().getTime());
			update.append("\""+date+"\",");
		}
		if(task.getDeadLine()!=null) {
			date = formatter.format(task.getDeadLine().getTime());
			update.append("\""+date+"\",");
		}
		update.append("\""+task.getTitle()+"\")");
		System.out.println(update);
		execUpdate(update.toString());
	}
	
}
