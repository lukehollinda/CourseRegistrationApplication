package server;

import registrationlogic.*;
import java.sql.*;
import java.util.Vector;

import registrationlogic.Course;

/**
 * Database is the server side class responsible for all communication with the SQL database.
 * Responsible for reading student information, updating student information, and reading available courses from SQL database.
 * @author Luke Hollinda
 *
 */
public class Database {

	/**
	 * SQL connection
	 */
	private Connection connection;
	
	/**
	 * SQL statement
	 */
	private Statement statement;
	
	/**
	 * SQL ResultSet
	 */
	private ResultSet resultSet;
	
	/**
	 * List of available courses
	 */
	private Vector<Course> courseList;
	
	/**
	 * Constructs a Database object with connections to the SQL database.
	 */
	public Database()
	{
		try 
		{
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demoschema", "LukeHollinda", "D5rluhcp");
		
			courseList = readCourseListSQL();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new student account within the database. 
	 * The new account will have the information provided in the argument theStudent and a password of the argument password.
	 * @param theStudent The student information
	 * @param password The account password
	 */
	public void createStudentAccount(Student theStudent, String password)
	{
		
		String idString = Integer.toString(theStudent.getStudentId());
		String yearString = Integer.toString(theStudent.getStudentYear());
		
		String usernameString = appendQuotes(theStudent.getStudentName());
		String firstNameString = appendQuotes(theStudent.getFirstName());
		String lastNameString = appendQuotes(theStudent.getLastName());
		String majorString = appendQuotes(theStudent.getStudentMajor());
		String emailString = appendQuotes(theStudent.getStudentEmail());
		String passwordString = appendQuotes(password);
		
		try
		{
			statement = connection.createStatement();
			String query = "INSERT INTO students(id, username, password, first_name, last_name, major, year, email) VALUES (" + idString 
					+ "," + usernameString + ", " + passwordString + ", " + firstNameString +", " + lastNameString + ", " 
					+ majorString + ", " +  yearString + "," + emailString + ")";
			
			statement.execute(query);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Will return the list of available courses read from SQL database.
	 * @return The list of courses.
	 */
	public Vector<Course> readCourseListSQL() {
		
		Vector<Course> courseList = new Vector<Course>();
		
		try 
		{
			statement = connection.createStatement();
			String query = "SELECT * FROM courses";
			resultSet = statement.executeQuery(query);
			
			while(resultSet.next())
			{
				Course course = new Course(resultSet.getString("course_name"), resultSet.getInt("course_num"));
				
				int courseLimit1 = resultSet.getInt("course_offering1");
				int courseLimit2 = resultSet.getInt("course_offering2");
				int courseLimit3 = resultSet.getInt("course_offering3");
				int courseLimit4 = resultSet.getInt("course_offering4");

				//Kind of a gross way to do this
				if(courseLimit1 != 0)
				{
					CourseOffering courseOffering = new CourseOffering(1, courseLimit1);
					course.addOffering(courseOffering);
				}
				if(courseLimit2 != 0)
				{
					CourseOffering courseOffering = new CourseOffering(2, courseLimit2);
					course.addOffering(courseOffering);
				}
				if(courseLimit3 != 0)
				{
					CourseOffering courseOffering = new CourseOffering(3, courseLimit3);
					course.addOffering(courseOffering);
				}
				if(courseLimit4 != 0)
				{
					CourseOffering courseOffering = new CourseOffering(4, courseLimit4);
					course.addOffering(courseOffering);
				}
				
				courseList.add(course);	
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
				
		return courseList;		
	}
	
	/**
	 * Will update a students information in the SQL database. 
	 * Essentially overwrites a students information and course registrations.
	 */
	public void updateStudentInfo(Student student)
	{
		String id        = Integer.toString(student.getStudentId());
		String username  = student.getStudentName();
		String firstName = student.getFirstName();
		String lastName  = student.getLastName();
		String major 	 = student.getStudentMajor();
		String year 	 = Integer.toString(student.getStudentYear());
		String email 	 = student.getStudentEmail();
		
		//Append sql quotes on non integer values
		username  = appendQuotes(username);
		firstName = appendQuotes(firstName);
		lastName  = appendQuotes(lastName);
		major     = appendQuotes(major);
		email     = appendQuotes(email);

		
		//Initialize array of course information to null
		String[] courseNameList = new String[6];
		String[] courseNumList  = new String[6];
		String[] courseSecList  = new String[6];
		
		for(int i = 0; i < 6; i++)
		{
			courseNameList[i] = "NULL";
			courseNumList[i]  = "NULL";
			courseSecList[i]  = "NULL";
		}
		
		//Fill in values from student registrations
		Vector<Registration> regList = student.getStudentRegistrations();

		for(int i = 0; i < regList.size(); i++)
		{
			Course theCourse  = regList.elementAt(i).getTheOffering().getTheCourse();
			String courseName = theCourse.getCourseName();
			String courseNum  = Integer.toString(theCourse.getCourseNum());
			String courseSec  = Integer.toString(regList.elementAt(i).getTheOffering().getSecNum());
			
			courseNameList[i] = appendQuotes(courseName);
			courseNumList[i]  = courseNum;
			courseSecList[i]  = courseSec;
			
		}
		
		
		try
		{
			statement = connection.createStatement();
		
			String query = "UPDATE students SET username = " + username + 
						", first_name = " + firstName + ", major = " + major + ", year = " + year + ", email = " + email	
						+ ", course1_name = " + courseNameList[0] + ", course1_num = " + courseNumList[0] 
						+ ", course1_section = " + courseSecList[0]
						+ ", course2_name = " + courseNameList[1] + ", course2_num = " + courseNumList[1] 
						+ ", course2_section = " + courseSecList[1]
						+ ", course3_name = " + courseNameList[2] + ", course3_num = " + courseNumList[2] 
						+ ", course3_section =" + courseSecList[2]
						+ ", course4_name = " + courseNameList[3] + ", course4_num = " + courseNumList[3] 
						+ ", course4_section =" + courseSecList[3]
						+ ", course5_name = " + courseNameList[4] + ", course5_num = " + courseNumList[4] 
						+ ", course5_section =" + courseSecList[4]
						+ ", course6_name = " + courseNameList[5] + ", course6_num = " + courseNumList[5] 
						+ ", course6_section =" + courseSecList[5]
							
						+ " WHERE id = " + id;
			
			statement.execute(query);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Appends quotes for SQL statement formatting
	 * @param string The string to be formatted
	 * @return The formatted string
	 */
	private String appendQuotes(String string)
	{
		String st = "'";
		st += string;
		st += "'";
		return st;
	}
	
	/**
	 * Searches the SQL database for a student with matching username and password. Constructs Student object with that students information and returns the Student object.
	 * @param username The username to search for
	 * @param password The password to search for
	 * @return The found student object, null if no matching credentials can be found.
	 */
	public Student findStudent(String username, String password)
	{
		
		System.out.println("Searching Database for user: " + username);
		Student found = null;
		
		try
		{
			statement = connection.createStatement();
			String query = "SELECT * FROM students WHERE username = '" + username 
						+"' and password = '" +  password + "'";
			resultSet = statement.executeQuery(query);
			
			if(resultSet.next())
			{
				found = new Student(resultSet.getString("username"),resultSet.getInt("id"));
				
				//find classes the student has registered for 
				String course1_name = resultSet.getString("course1_name");
				int course1_num = resultSet.getInt("course1_num");
				int course1_section = resultSet.getInt("course1_section");
				
				String course2_name = resultSet.getString("course2_name");
				int course2_num = resultSet.getInt("course2_num");
				int course2_section = resultSet.getInt("course2_section");
				
				String course3_name = resultSet.getString("course3_name");
				int course3_num = resultSet.getInt("course3_num");
				int course3_section = resultSet.getInt("course3_section");

				String course4_name = resultSet.getString("course4_name");
				int course4_num = resultSet.getInt("course4_num");
				int course4_section = resultSet.getInt("course4_section");

				String course5_name = resultSet.getString("course5_name");
				int course5_num = resultSet.getInt("course5_num");
				int course5_section = resultSet.getInt("course5_section");

				String course6_name = resultSet.getString("course6_name");
				int course6_num = resultSet.getInt("course6_num");
				int course6_section = resultSet.getInt("course6_section");

				Vector<String> nameList = new Vector<String>();
				Vector<Integer> numList = new Vector<Integer>();
				Vector<Integer> sectionList = new Vector<Integer>();
				
				
				//Add number of registered courses to list
				if(course1_name != null && course1_num != 0)
				{
					nameList.add(course1_name);
					numList.add(course1_num);
					sectionList.add(course1_section);
				}
				
				if(course2_name != null && course2_num != 0)
				{
					nameList.add(course2_name);
					numList.add(course2_num);
					sectionList.add(course2_section);
				}
				
				if(course3_name != null && course3_num != 0)
				{
					nameList.add(course3_name);
					numList.add(course3_num);
					sectionList.add(course3_section);
				}
				
				if(course4_name != null && course4_num != 0)
				{
					nameList.add(course4_name);
					numList.add(course4_num);
					sectionList.add(course4_section);
				}
				
				if(course5_name != null && course5_num != 0)
				{
					nameList.add(course5_name);
					numList.add(course5_num);
					sectionList.add(course5_section);
				}
				
				if(course6_name != null && course6_num != 0)
				{
					nameList.add(course6_name);
					numList.add(course6_num);
					sectionList.add(course6_section);
				}
				
				
				
				
				//For each course go through course list and add the matching course to student registrations
				for(int i = 0; i < nameList.size(); i++)
				{
					for(Course course : courseList)
					{
						
						//If it is the correct course
						if(course.getCourseName().equals(nameList.elementAt(i)) && course.getCourseNum() == numList.elementAt(i))
						{
							Vector<CourseOffering> offeringList = course.getOfferingList();
							for(CourseOffering offer : offeringList)
							{
								if(offer.getSecNum() == sectionList.elementAt(i))
								{
									Registration reg = new Registration();
									reg.completeRegistration(found, offer);
								}
							}
							
						}
						
					}
				}
				
				String major = resultSet.getString("major");
				int year = resultSet.getInt("year");
				String email = resultSet.getString("email");
				String firstName = resultSet.getString("first_name");
				String lastName = resultSet.getString("last_name");
				
				//Set student information
				found.setStudentEmail(email);
				found.setStudentYear(year);
				found.setStudentMajor(major);
				found.setFirstName(firstName);
				found.setLastName(lastName);
			}
			
			//Return constructed student.
			return found;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		//If no student record is found return null
		return null;
	}
	
	
}
