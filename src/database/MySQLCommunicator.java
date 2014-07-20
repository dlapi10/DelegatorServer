package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import model.Task;
import model.TaskStatus;
import model.User;

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
			if(statement!=null)
				statement.close();
			statement = connection.createStatement();
			statement.executeUpdate(update);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Returning query answer, if wrong query then null
	 * @param query
	 * @return
	 */
	public ResultSet executeQuery(String query){
		try {
			if(statement!=null)statement.close();
			statement = connection.createStatement();
			ResultSet ans =statement.executeQuery(query);
			return ans;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Adding task to the database
	 * @param task
	 */
	public void addTask(Task task){
		StringBuilder update = new StringBuilder("insert into tasks(reporter,assignee,`status`,description,priority,`completion`,start_date,deadline,title) values(");
		if(task.getReporter()!=null && task.getReporter().getUserName()!=null) 
			update.append("\""+task.getReporter().getUserName()+"\",");
		else update.append("\"\",");
		if(task.getAssignee()!=null && task.getAssignee().getUserName()!=null) 
			update.append("\""+task.getAssignee().getUserName()+"\",");
		else update.append("\"\",");
		if(task.getStatus()!=null && task.getStatus().getStatusName()!=null) 
			update.append("\""+task.getStatus().getStatusName()+"\",");
		else update.append("\"\",");
		if(task.getDescription()!=null) 
			update.append("\""+task.getDescription()+"\",");
		else update.append("\"\",");
		
		update.append(""+task.getPriority()+",");
		update.append(""+task.getCompletionPercent()+",");

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss",java.util.Locale.getDefault());
		String date;
		if(task.getStartDate()!=null){
			date = formatter.format(task.getStartDate().getTime());
			update.append("\""+date+"\",");
		}
		else update.append("\"\",");
		if(task.getDeadLine()!=null) {
			date = formatter.format(task.getDeadLine().getTime());
			update.append("\""+date+"\",");
		}
		else update.append("\"\",");
		update.append("\""+task.getTitle()+"\")");
		System.out.println(update);
		execUpdate(update.toString());
	}

	/**
	 * Returns all tasks which are in database
	 * @return
	 * @throws SQLException 
	 */
	public ArrayList<Task> getAlltasks() throws SQLException {
		ArrayList<Task> allTasks = new ArrayList<Task>();
		ResultSet rs = executeQuery("select * from tasks");
		while(rs.next()){
			Task task = new Task(rs.getInt("id"));
			task.setReporter(new User(rs.getString("reporter")));
			task.setAssignee(new User(rs.getString("assignee")));
			task.setStatus(new TaskStatus(rs.getString("status")));
			task.setDescription(rs.getString("description"));
			task.setPriority(rs.getInt("priority"));
			task.setCompletionPercent(rs.getInt("completion"));
			task.setTitle(rs.getString("title"));
			String startDate = rs.getString("start_date");
			String deadline = rs.getString("deadline");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss",java.util.Locale.getDefault());
			try {
				if(startDate!=null)
					task.setStartDate(dateToCalendar((Date)formatter.parse(startDate)));
				if(deadline!=null)
					task.setDeadLine(dateToCalendar((Date)formatter.parse(deadline)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			allTasks.add(task);
		}
		return allTasks;
	}
	
	/**
	 * Date format to Calendar
	 * @param date
	 * @return
	 */
	public static Calendar dateToCalendar(Date date){ 
		if(date==null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
}
