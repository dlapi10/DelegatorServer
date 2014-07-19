package database;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

	/*
	 * Constants 
	 */
	// The server socket.
	private static ServerSocket serverSocket = null;
	// The client socket. We don't need it now
	private static Socket clientSocket = null;
	// Socket port number to use
	private static int port = 2728;
	// This chat server can accept up to maxClientsCount clients' connections.
	private static final int MAX_CLIENT_NUMBER = 10;
	private static final ClientThread[] threads = new ClientThread[MAX_CLIENT_NUMBER];

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
	        int i = 0;
	        for (i = 0; i < MAX_CLIENT_NUMBER; i++) {
	          if (threads[i] == null) {
	            (threads[i] = new ClientThread(clientSocket, threads)).start();
	            break;
	          }
	        }
	        if (i == MAX_CLIENT_NUMBER) {
	          PrintStream os = new PrintStream(clientSocket.getOutputStream());
	          os.println("Server too busy. Try later.");
	          os.close();
	          clientSocket.close();
	        }
	      } catch (IOException e) {
	        System.out.println(e);
	      }
	    }
	}
}
