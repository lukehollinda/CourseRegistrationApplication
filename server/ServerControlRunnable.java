package server;

import tools.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Vector;

import registrationlogic.Course;
import registrationlogic.Student;

/**
 * Runnable class for the server thread pool. Communicates with DataBase and with clients. 
 * @author Luke Hollinda
 *
 */
public class ServerControlRunnable implements Runnable {
	
	@SuppressWarnings("unused")
	/**
	 * Output stream for communication with client.
	 */
	private PrintWriter socketOut;
	
	/**
	 * Input stream for communication with client.
	 */
	private BufferedReader socketIn;
	
	/**
	 * Object output stream for sending data to client.
	 */
	private ObjectOutputStream socketObjectOut;
	
	/**
	 * Object input stream for receiving data from client. 
	 */
	private ObjectInputStream socketObjectIn;
	
	/**
	 * Reference to dataBaseManager
	 */
	private Database database;
	
	/**
	 * Constructor for ServerControlRunnable
	 * @param socketIn Socket input stream
	 * @param socketOut Socket output stream
	 * @param socketObjectOut Socket object output stream.
	 */
	public ServerControlRunnable(BufferedReader socketIn, PrintWriter socketOut, ObjectOutputStream socketObjectOut, ObjectInputStream socketObjectIn)
	{
		this.socketOut       = socketOut;
		this.socketIn        = socketIn;
		this.socketObjectOut = socketObjectOut;
		this.socketObjectIn  = socketObjectIn;
	}

	
	/**
	 * Superloop of ServerControlRunnable. Will alternate between accepting an instruction from the socket, 
	 * and executing the instruction by calling other functions. 
	 */
	@Override
	public void run() 
	{	
		String line = null;
		boolean quitFlag = false;
		while (true) 
		{	
			
			if(quitFlag)
			{
				break;
			}
			
			
			try 
			{		
				line = socketIn.readLine();
				InstructionEnum instruction = InstructionEnum.valueOf(line);
				
				switch(instruction)
				{
				case REQUEST_COURSELIST:
					System.out.println("Request for courseList recognized by serverRunnable.");
					sendCourseList();
					break;
				case REQUEST_STUDENT_INFORMATION:
					System.out.println("Request for student Information recognized by serverRunnable.");
					sendStudentInformation();
					break;
				case UPDATE_STUDENT_INFORMATION:
					System.out.println("Request to update student Information recognized by serverRunnable.");
					updateStudentInformation();
					break;
				case CREATE_STUDENT_ACCOUNT:
					System.out.println("Request to create student Account recognized by serverRunnable.");
					createStudentAccount();
					break;
				case QUIT:
					System.out.println("Client has exited. serverRunnable closing...");
					quitFlag = true;
					break;
				default:
					System.out.println("Default case reached in serverRunnable.");
					break;	
				}
			} 
			catch(Exception e)
			{
				System.out.println("Exception thrown in ServerRunnable...");
				break;
			}
				
		} 
	}

	/**
	 * Calls the data base to create a student account with the given student data and password.
	 */
	private void createStudentAccount()
	{
		
		try
		{
			String password = socketIn.readLine();
			Student theStudent = (Student)socketObjectIn.readObject();
			database.createStudentAccount( theStudent, password );
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Calls data base to update a student information
	 */
	private void updateStudentInformation()
	{
		try 
		{
			Student theStudent = (Student)socketObjectIn.readObject();
			database.updateStudentInfo(theStudent);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * A method for the server to send the client controller a students information. 
	 * When reaching this method a REQUEST_STUDENT_INFORMATION instruction will have been
	 * recognized. It is expected that the following line within the socket stream will be 
	 * the name of the desired student's information.
	 */
	private void sendStudentInformation()
	{
		try
		{
			String username = socketIn.readLine();
			String password = socketIn.readLine();
			Student found = database.findStudent(username, password);
			socketObjectOut.writeObject(found);
			socketObjectOut.flush();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Will read the list of available courses from the data base and send them to the client.
	 */
	private void sendCourseList()
	{
		Vector<Course> courseList = database.readCourseListSQL();
		try
		{
			socketObjectOut.writeObject(courseList);
			socketObjectOut.flush();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	


	/**
	 * Initializes a reference for each thread to the DataBase.
	 */
	public void linkWithDataBase(Database DB)
	{
		this.database = DB;
	}
	
}
