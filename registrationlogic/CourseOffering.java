package registrationlogic;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

/**
 * A section/Offering of a course
 * @author Luke Hollinda
 *
 */
public class CourseOffering implements Serializable{
	

	private static final long serialVersionUID = -6329292364997854895L;
	
	/**
	 * The offering section number.
	 */
	private int secNum;
	
	/**
	 * The offering maximum student capacity.
	 */
	private int secCap;
	
	/**
	 * The course of which this object if an offering of.
	 */
	private Course theCourse;
	
	/**
	 * List of students.
	 */
	private Vector<Student> studentList;
	
	/**
	 * List of registrations.
	 */
	private Vector <Registration> offeringRegList;
	
	/**
	 * Initializes a course with the given section number and cap.
	 * @param secNum The section number.
	 * @param secCap The section maximum student capacity.
	 */
	public CourseOffering (int secNum, int secCap) {
		this.setSecNum(secNum);
		this.setSecCap(secCap);
		offeringRegList = new Vector <Registration>();
		studentList     = new Vector <Student>();
	}
	public int getSecNum() {
		return secNum;
	}
	public void setSecNum(int secNum) {
		this.secNum = secNum;
	}
	public int getSecCap() {
		return secCap;
	}
	public void setSecCap(int secCap) {
		this.secCap = secCap;
	}
	public Course getTheCourse() {
		return theCourse;
	}
	public void setTheCourse(Course theCourse) {
		this.theCourse = theCourse;
	}
	
	/**
	 * To string function for CourseOffering
	 */
	@Override
	public String toString () {
		String st = "\n";
		st += getTheCourse().getCourseName() + " " + getTheCourse().getCourseNum() + "\n";
		st += "Section Num: " + getSecNum() + ", section cap: "+ getSecCap() +"\n";

		for (Student c : studentList) {
			st += c; 
			st += "\n";
		}
		return st;
	}
	
	/**
	 * Adds a registration to the registration list. Also adds student to the list of students.
	 * @param registration The registration.
	 */
	public void addRegistration(Registration registration) {
		offeringRegList.add(registration);
		studentList.add(registration.getTheStudent());
	}
	
	/**
	 * Removes a given registration and the student found within that registration from registrationList and studentList.
	 * @param registration The registation to be removed.
	 */
	public void removeRegistration(Registration registration)
	{
		Iterator<Registration> regItr = offeringRegList.iterator();
		while(regItr.hasNext())
		{
			Registration reg =regItr.next();
			if(registration.equals(reg))
			{
				regItr.remove();
			}
		}
		
		Iterator<Student> studentItr = studentList.iterator();
		while(studentItr.hasNext())
		{
			Student student = studentItr.next();
			if(student.equals(registration.getTheStudent()))
			{
				studentItr.remove();
			}
		}
	}
	

}
