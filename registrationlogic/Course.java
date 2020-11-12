package registrationlogic;

import java.io.Serializable;
import java.util.Vector;

/**
 * Course class that holds course information.
 * @author Luke Hollinda
 */
public class Course implements Serializable{


	private static final long serialVersionUID = -3356087256706040315L;
	
	/**
	 * The course name
	 */
	private String courseName;
	
	/**
	 * The course number
	 */
	private int courseNum;
	
	/**
	 * The courseOffering list. All available sections of the course.
	 */
	@SuppressWarnings("unused")
	private Vector<CourseOffering> offeringList;

	
	/**
	 * Constructs a course with the given course name and number.
	 * @param courseName The name of the course
	 * @param courseNum The course number.
	 */
	public Course(String courseName, int courseNum) {
		this.setCourseName(courseName);
		this.setCourseNum(courseNum);
		offeringList = new Vector<CourseOffering>();
	}

	/**
	 * Adds an offering to the offeringList.
	 * @param offering The CourseOffering to be added.
	 */
	public void addOffering(CourseOffering offering) {
		if (offering != null && offering.getTheCourse() == null) {
			offering.setTheCourse(this);
			if (!offering.getTheCourse().getCourseName().equals(courseName)
					|| offering.getTheCourse().getCourseNum() != courseNum) {
				System.err.println("Error! This section belongs to another course!");
				return;
			}
			
			offeringList.add(offering);
		}
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getCourseNum() {
		return courseNum;
	}

	public void setCourseNum(int courseNum) {
		this.courseNum = courseNum;
	}
	
	/**
	 * To string function for Course
	 */
	@Override
	public String toString () {
		String st = "\n";
		st += getCourseName() + " " + getCourseNum ();
		st += "\nAll course sections:\n";
		for (CourseOffering c : offeringList)
			st += c;
		st += "\n-------\n";
		return st;
	}
	
	/**
	 * Return the courseOffering list
	 * @return The course offering list.
	 */
	public Vector<CourseOffering> getOfferingList() { return offeringList; }
	
	/**
	 * To string function when wanting to display data of course and course offering number.
	 * @return The formated data.
	 */
	public String formatCourseData()
	{
		String st = "";
		st += getCourseName() + " " + getCourseNum() + " || Number of available sections: " + offeringList.size() + "\n"; 
		return st;
	}
	
	/**
	 * Returns a courseOffering at a given index.
	 */
	public CourseOffering getCourseOfferingAt(int i) {
		
		if (i < 0 || i >= offeringList.size() )
			return null;
		else
			return offeringList.get(i);
	}

}
