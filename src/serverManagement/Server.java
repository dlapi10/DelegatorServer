package serverManagement;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;



public class Server {

	/*
	 * Constants 
	 */
	// The server socket.
	private static ServerSocket serverSocket = null;
	// The client socket. 
	private static Socket clientSocket = null;
	// Socket port number to use
	private static int port = 2728;
	// All the client Threads
	private static final ArrayList<ClientThread> clientTheads = new ArrayList<ClientThread>();
	
	public static void main(String[] args) {
		/*
		 * Open a server socket on the portNumber (default 2222). Note that we can
		 * not choose a port less than 1023 if we are not privileged users (root).
		 */
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("new ServerSocket initializinig problem"+e);
		}
		
		/*
	     * Create a client socket for each connection and pass it to a new client
	     * thread.
	     */
	    while (true) {
	      try {
	        clientSocket = serverSocket.accept();
	        ClientThread newClient = new ClientThread(clientSocket, clientTheads);
	        newClient.start();
	        clientTheads.add(newClient); // Starts when new client will join
	      } catch (IOException e) {
	        System.out.println(e);
	      }
	    }
	}
}
