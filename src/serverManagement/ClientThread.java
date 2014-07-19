package serverManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;


public class ClientThread extends Thread{
	private BufferedReader inputStream = null;
	private PrintStream outputStream = null;
	private Socket clientSocket = null;
	private final ArrayList<ClientThread> clientTheads;
	
	

	public ClientThread(Socket clientSocket, ArrayList<ClientThread> clientTheads) {
		this.clientSocket = clientSocket;
		this.clientTheads = clientTheads;
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
