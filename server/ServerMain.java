package server;

import java.io.IOException;

/**
 * Main function to be run for the Server side of this application. Houses a ServerControlHub object.
 * @author Luke Hollinda
 *
 */
public class ServerMain {

	/**
	 * Main thread where the ServerControlHub object is held. 
	 * @param args Unused command line arguments
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		ServerControlHub myServer = new ServerControlHub(9898);
		myServer.runServer();
	}


}
