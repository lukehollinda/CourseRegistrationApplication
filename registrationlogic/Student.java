package registrationlogic;

import java.io.Serializable;
import java.util.Vector;

/**
 * Houses all student information and courseOfferings the student is registered for.
 * @author Luke Hollinda
 */
public class Student implements Serializable{
	

	private static final long serialVersionUID = 1267570190607163250L;
	
	/**
	 * Student's username
	 */
	private String studentName;
	
	/**
	 * Student's first name
	 */
	private String firstName;
	
	/**
	 * Student's last name
	 */
	private String lastName;
	
	/**
	 * Student's school id number
	 */
	private int studentId;
	
	/**
	 * The list of courseOfferings a student is registered for
	 */
	@SuppressWarnings("unused")
	private Vector<Registration> studentRegList;
	
	/**
	 * Student's email address
	 */
	private String studentEmail;
	
	/**
	 * Student's major
	 */
	private String studentMajor;
	
	/**
	 * Student's year of study.
	 */
	private int    studentYear;
	
	
	/**
	 * Constructs a new student object with the given student name and id.
	 * @param studentName Student username
	 * @param studentId Student id number
	 */
	public Student (String studentName, int studentId) {
		this.setStudentName(studentName);
		this.setStudentId(studentId);
		studentRegList = new Vector<Registration>();
	}
	
	public Vector<Registration> getStudentRegistrations()
	{
		return studentRegList;
	}
	
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	
	/**
	 * toString function for Student
	 */
	@Override
	public String toString () {
		String st = "Student Name: " + getStudentName() + "\n" +
				"Student Id: " + getStudentId() + "\n\n";
		return st;
	}

	/**
	 * Adds an input registration to the students list of registrations.
	 * @param registration The registration to be added.
	 */
	public void addRegistration(Registration registration) {
		studentRegList.add(registration);
	}

	/**
	 * DEBUGGING FUNCTION. 
	 * Used to display a student and the courses they are registered to the console.
	 */
	public void displayCourses()
	{
		if(studentRegList.size() == 0)
		{
			System.out.println(studentName + " is taking no courses at the moment");
			return;
		}
		System.out.println(studentName + " is taking the following courses");
		for(Registration reg: studentRegList)
		{
			System.out.println("Course Name:   " + reg.getTheOffering().getTheCourse().getCourseName() + " " + reg.getTheOffering().getTheCourse().getCourseNum());
			System.out.println("");
		}
	}
	
	/**
	 * Removes a course from the student registration list if it matched input courseName and courseNumber.
	 * @param courseName The name of the course to be removed.
	 * @param courseNum The course number of the course to be removed.
	 */
	public void removeCourse(String courseName, int courseNum)
	{
		int i = 0;
		for(Registration course : studentRegList)
		{
			String regCourseName = course.getTheOffering().getTheCourse().getCourseName();
			int regCourseID = course.getTheOffering().getTheCourse().getCourseNum();
			if(courseName.equals(regCourseName) && regCourseID == courseNum)
			{
				break;
			}
			i++;
		}
		studentRegList.remove(i);
	}

	public String getStudentEmail() {
		return studentEmail;
	}

	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}

	public String getStudentMajor() {
		return studentMajor;
	}

	public void setStudentMajor(String studentMajor) {
		this.studentMajor = studentMajor;
	}

	public int getStudentYear() {
		return studentYear;
	}

	public void setStudentYear(int studentYear) {
		this.studentYear = studentYear;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
