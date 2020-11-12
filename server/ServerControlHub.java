package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;


/**
 * Main class to be run for server side of application. Houses thread pool and accepts and connects threads to new users. 
 * @author Luke Hollinda
 */
public class ServerControlHub{


	/**
	 * Used in creating socket connection
	 */
	private Socket aSocket;
	
	/**
	 * ServerSocket
	 */
	private ServerSocket serverSocket;
	
	/**
	 * Output stream passed to threads.
	 */
	private PrintWriter socketOut;
	
	/**
	 * Input stream passed to threads.
	 */
	private BufferedReader socketIn;
	
	/**
	 * ObjectOutputStream passed to threads.
	 */
	private ObjectOutputStream socketObjectOut;
	
	/**
	 * ObjectInputStream passed to thread.
	 */
	private ObjectInputStream socketObjectIn;
	
	/**
	 * Main database controller
	 */
	private Database database;
	
	/**
	 * Fixed thread pool.
	 */
	private ExecutorService pool;

	/**
	 * Initialize thread pool, socket connection, and DataBaseManager. 
	 * @param port The socket port number.
	 */
	public ServerControlHub(int portNumber) {
		
		this.database = new Database();
		
		try {
			this.serverSocket = new ServerSocket(portNumber);
			this.pool = Executors.newFixedThreadPool(10);    //Since the number of users will be small ten threads should not be an issue.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Initializes proper input output streams for thread after accepting a new client.
	 */
	public void runServer () {
		try {
			
			System.out.println("ServerControlHub now running...");
			while (true) 
			{
				
				//Accept client
				aSocket = serverSocket.accept();
				System.out.println("Connection accepted by server!");
				
				//Initialize input/output streams.
			    socketIn = new BufferedReader(new InputStreamReader(aSocket.getInputStream()));
				socketOut = new PrintWriter((aSocket.getOutputStream()), true);
				socketObjectOut = new ObjectOutputStream(aSocket.getOutputStream());
				socketObjectIn  = new ObjectInputStream(aSocket.getInputStream());
				
				//Create runnable
				ServerControlRunnable threadRunnable= new ServerControlRunnable(socketIn, socketOut, socketObjectOut, socketObjectIn);
				threadRunnable.linkWithDataBase(database);
				//Execute
				pool.execute(threadRunnable);
			}
		} catch (IOException e) {
			e.getStackTrace();
		}
		//Close all active sockets and terminate running threads.
		closeConnection();
		pool.shutdown();
		
	}
	/**
	 * Closes the Input/output streams used to attach threads.
	 */
	private void closeConnection() {
		try {
			socketIn.close();
			socketOut.close();
			socketObjectOut.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
	}


}
