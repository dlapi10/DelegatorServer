package serverManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import database.MySQLCommunicator;
import database.MySQLDBInfo;


public class ClientThread extends Thread{
	//Constants
	public static final String FINISHING_MESSAGE = "FINISH";
	public static final String MSG_SYNC_ALL="SYNC_ALL";
	public static final String MSG_ADD_TASK="ADD_TASK";
	public static final String MSG_ADD_GROUP="ADD_GROUP";
	public static final String MSG_SYNC_OVER="SYNC_OVER";

	//Private variables
	private BufferedReader inputStream = null;
	private PrintStream outputStream = null;
	private Socket clientSocket = null;
	private final ArrayList<ClientThread> clientTheads;
	private Connection dbConnection;
	private MySQLCommunicator communicator;


	public ClientThread(Socket clientSocket, ArrayList<ClientThread> clientTheads) {
		this.clientSocket = clientSocket;
		this.clientTheads = clientTheads;
		dbConnection = MySQLDBInfo.getConnection();
		try {
			MySQLDBInfo.startUpStatement(dbConnection);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		communicator = new MySQLCommunicator(dbConnection);
	}

	/**
	 * Sends message to the client
	 * @param message
	 */
	public void sendMessage(ClientThread  th, String header, String message) {
		if (th.outputStream != null && !th.outputStream.checkError()) {
			th.outputStream.println(header);
			th.outputStream.println(message);
			System.out.println("sent Message from server: header "+header+" message "+message);
		}
	}

	/**
	 * Sending Message to everyone
	 * @param header
	 * @param message
	 */
	public void sendMessageToEveryOne(String header, String message){
		for(int i=0;i<clientTheads.size();i++){
			sendMessage(clientTheads.get(i),header, message);
		}
	}

	public void syncAllTasks(ClientThread th){
		ArrayList<Task> allTasks=null;
		try {
			allTasks = communicator.getAlltasks();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(int i=0;i<allTasks.size();i++){
			Gson gson = new GsonBuilder().create();
			String message = gson.toJson(allTasks.get(i));
			sendMessage(th,MSG_SYNC_ALL, message);
		}
		sendMessage(th,MSG_SYNC_OVER, null);
	}

	public void run() {
		ArrayList<ClientThread> clientTheads = this.clientTheads; 

		try {
			/*
			 * Create input and output streams for this client.
			 */
			inputStream = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
			outputStream = new PrintStream(clientSocket.getOutputStream());


			/*
			 * Messages
			 * 
			 */

			while(true){
				String inputHeader;
				String inputMessage;
				inputHeader = inputStream.readLine();
				inputMessage = inputStream.readLine();

				if(inputHeader!=null){
					if(inputHeader.equals(FINISHING_MESSAGE)){
						break;
					}
					System.out.println(inputHeader);
					if(inputMessage!=null){
						if (inputHeader.startsWith(MSG_ADD_TASK)){
							Gson gson = new Gson();
							Task task = gson.fromJson(inputMessage, Task.class);
							communicator.addTask(task);
							sendMessageToEveryOne(MSG_ADD_TASK, inputMessage);
						}else if(inputHeader.startsWith(MSG_SYNC_ALL)){
							syncAllTasks(this);
						}
					}else{

					}
				}
			}

			/*
			 * Clean up. Set the current thread variable to null so that a new client
			 * could be accepted by the server.
			 */
			synchronized (this) {
				clientTheads.remove(this);
			}
			/*
			 * Close the output stream, close the input stream, close the socket.
			 */
			inputStream.close();
			outputStream.close();
			clientSocket.close();
		} catch (IOException e) {
		}
	}
}
