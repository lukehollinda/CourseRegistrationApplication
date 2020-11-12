package client;
import java.util.Iterator;
import java.util.Vector;

import registrationlogic.*;

/**
 * Client model houses the users student information and the list of courses available to take.
 */
public class ClientModel {
	
	/**
	 * The student information held by client side.
	 */
	Student theStudent;
	
	/**
	 * The list of courses held by the client.
	 */
	Vector<Course> courseList;
	
	/**
	 * Creates an empty client model.
	 */
	public ClientModel()
	{
		theStudent = null;
		courseList = new Vector<Course>();
	}

	
	/**
	 * Searches and returns a desired course within the offered courses list.
	 * @param courseName The name of the searched course.
	 * @param courseID The ID of the searched course.
	 * @return The course searched for. If it is not found will return null.
	 */
	public Course findCourse(String courseName, int courseID)
	{
			for(Course course : courseList)
			{
				if(course.getCourseName().equals(courseName) && course.getCourseNum() == courseID)
					return course;
			}
			
			return null;
	}
	
	/**
	 * Removes a registration from a students registration list 
	 * and removes them from the class offerings lists.
	 */
	public void unregisterCourse(String courseName, int ID)
	{
		Vector<Registration> regList = theStudent.getStudentRegistrations();
		
		Registration foundReg = null;
		
		//Remove registration from student registration list.
		Iterator<Registration> regItr = regList.iterator();
		while(regItr.hasNext())
		{
			Registration reg = regItr.next();
			if(reg.getTheOffering().getTheCourse().getCourseName().contentEquals(courseName)
					&& reg.getTheOffering().getTheCourse().getCourseNum() == ID)
			{
				foundReg = reg;
				regItr.remove();
			}
		}
		
		CourseOffering offering = foundReg.getTheOffering();
		
		//Remove registration from offering
		offering.removeRegistration(foundReg);
		
		
		
		
	}
	/**
	 * Sets the student.
	 */
	public void setTheStudent(Student student)
	{
		this.theStudent = student;
	}
	
	/**
	 * Returns the student.
	 */
	public Student getTheStudent() {return this.theStudent; }
	
	/**
	 * Sets the courseList.
	 */
	public void setCourseList(Vector<Course> courseList)
	{
		this.courseList = courseList;
	}
	
	/**
	 * Returns the courseList.
	 */
	public Vector<Registration> getStudentRegistrations()
	{
		return theStudent.getStudentRegistrations();
	}
}

