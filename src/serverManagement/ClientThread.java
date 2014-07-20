package serverManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import database.MySQLCommunicator;
import database.MySQLDBInfo;


public class ClientThread extends Thread{
	//Constants
	private static final String FINISHING_MESSAGE = "FINISH";
	
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
				String inputMessage = inputStream.readLine();
				if(inputMessage.equals(FINISHING_MESSAGE)){
					break;
				}
				if(inputMessage!=null){
					if(inputMessage.startsWith("update") || inputMessage.startsWith("insert") || inputMessage.startsWith("delete")){
						synchronized (this) {
							//Updating Database. Inserting, updating or deleting raws
							communicator.execUpdate(inputMessage); 
							System.out.println("update done");
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
				for(int i=0; i<clientTheads.size();i++){
					if(clientTheads.get(i) == this){
						clientTheads.remove(i);
					}
				}
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
