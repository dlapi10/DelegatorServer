package database;

import java.io.IOException;
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
		
		
	}
}
