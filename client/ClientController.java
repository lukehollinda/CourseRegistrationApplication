package client;

import tools.*;
import java.net.*;
import java.util.Vector;

import registrationlogic.Course;
import registrationlogic.CourseOffering;
import registrationlogic.Registration;
import registrationlogic.Student;

import java.io.*;

/**
 * ClientController houses the ClientModel and most functions used on the Client end of application. 
 * @author Luke Hollinda
 *
 */
public class ClientController{
	
	/**
	 * Socket connection, used for initializing input/output streams.
	 */
	private Socket aSocket;
	
	/**
	 * Output stream for communication with server.
	 */
	private PrintWriter socketOut;
	
	/**
	 * Input stream for communication with server.
	 */
	@SuppressWarnings("unused")
	private BufferedReader socketIn;
	
	/**
	 * Object input stream for receiving data from server.
	 */
	private ObjectInputStream socketObjectIn;
	
	/**
	 * Object output stream for receiving data from server.
	 */
	private ObjectOutputStream socketObjectOut;
	
	/**
	 * Client model object, houses student information and available courses received from server.
	 */
	private ClientModel clientModel;
	
	/**
	 * Initialize socket connection and relevant input/output streams.
	 * @param serverName Name of the socket.
	 * @param portNumber Port number of the socket.
	 */
	public ClientController(String serverName, int portNumber) {
		

		try {
			//Initialize input/output streams
			aSocket = new Socket (serverName, portNumber);
			socketIn = new BufferedReader (new InputStreamReader (aSocket.getInputStream()));
			socketObjectIn = new ObjectInputStream(aSocket.getInputStream());
			socketObjectOut = new ObjectOutputStream(aSocket.getOutputStream());
			socketOut = new PrintWriter((aSocket.getOutputStream()), true);
			
			//Initialize clientModel
			clientModel = new ClientModel();
			clientModel.setCourseList(this.requestCourseList());
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Requests the server to send the Student Object with a matching name.
	 * @param name The name of the student to find.
	 * @return The student that was found.
	 */
	public Student requestStudentInformation(String username, String password)
	{
		//Send instruction to server that we will be requesting a students data
		String instruction = InstructionEnum.REQUEST_STUDENT_INFORMATION.name();
		socketOut.println(instruction);
		
		//Send the server the name of the student.
		socketOut.println(username);
		
		//Send the server the students password
		socketOut.println(password);
		
		Student sendBack = null;
		try
		{
			sendBack = (Student)socketObjectIn.readObject();
		}
		catch(Exception e)
		{
			System.out.println("Exeption reached during request for: " + username + "'s information. ");
		}
		
		return sendBack;
	}
	
	/**
	 * Posts a request to the server for the total available courses, receives the courses, and returns them from the function.
	 * @return The course vector from the server.
	 */
	@SuppressWarnings("unchecked")
	public Vector<Course> requestCourseList()
	{
		//Send instruction to server
		String instruction = InstructionEnum.REQUEST_COURSELIST.name();
		socketOut.println(instruction);
		
		Vector<Course> courseList = null;
		try
		{	//Read CourseList from server
			 courseList = (Vector<Course>)socketObjectIn.readObject();
		}
		catch (ClassNotFoundException e) 
		{
			System.out.println("Issue with recieved serialized object...");
			e.printStackTrace();
		} catch (IOException e)
		{
			System.out.println("IOException thrown with serilialized object retrevial");
			e.printStackTrace();
		}
		
		return courseList;
	}
	
	/**
	 * Calls the server to create a new student account in its database.
	 * @param newStudent The students information.
	 * @param password The account password
	 */
	public void createStudentAccount(Student newStudent,String password)
	{
		String instruction = InstructionEnum.CREATE_STUDENT_ACCOUNT.name();
		socketOut.println(instruction);
		
		try
		{
			socketOut.println(password);   //Send server the password
		
			socketObjectOut.writeObject(newStudent); //Send server the student information
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Sends student information to server to be changed in database
	 */
	public void updateUserInformation()
	{
		String instruction = InstructionEnum.UPDATE_STUDENT_INFORMATION.name();
		try 
		{
			socketOut.println(instruction);

			socketObjectOut.writeObject(clientModel.getTheStudent());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Indicates to the SeverRunnable that the client is finished with the application.
	 */
	public void quitProcedure()
	{
		String instruction = InstructionEnum.QUIT.name();
		socketOut.println(instruction);
	}
	
	/**
	 * Searches for a course within the client Model offered Course List.
	 * @param courseName 
	 * @param courseID
	 * @return The course searched for, returns null if no course was found.
	 */
	public Course findCourse(String courseName, int courseID)
	{
		return this.clientModel.findCourse(courseName, courseID);
	}

	/**
	 * Registers the student in the CourseOffering.
	 */
	public void regesterStudent(CourseOffering theOffer)
	{
		Registration theReg = new Registration();
		theReg.completeRegistration(clientModel.getTheStudent(), theOffer);
	}

	/**
	 * Will return the list of classes the student is registered for.
	 * @return Returns the list of students from the student within ClientModel
	 */
	public Vector<Registration> getStudentsRegistration()
	{
		return clientModel.getStudentRegistrations();
	}
	
	
	/**
	 * Will return if or if not a student is already registered for a course with the same name and course number.
	 */
	public boolean isStudentAlreadyRegistered(CourseOffering desiredOffering)
	{
		Vector<Registration> regList = clientModel.getStudentRegistrations();
		
		for(Registration reg : regList)
		{
			if(reg.getTheOffering().getTheCourse().getCourseName().equals(desiredOffering.getTheCourse().getCourseName())
					&& reg.getTheOffering().getTheCourse().getCourseNum() == desiredOffering.getTheCourse().getCourseNum())
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Calls clientModel to unregister a student from a course.
	 * @param courseName The name of the course
	 * @param ID The course ID 
	 */
	public void unregisterCourse(String courseName, int courseId)
	{
		clientModel.unregisterCourse(courseName, courseId);
	}
	
	public void setStudent(Student theStudent)
	{
		clientModel.setTheStudent(theStudent);
	}
	
	public Student getStudent()
	{
		return clientModel.getTheStudent();
	}
	
	public void setCourseList(Vector<Course> theList)
	{
		clientModel.setCourseList(theList);
	}
}
