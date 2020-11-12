package registrationlogic;

import java.io.Serializable;

/**
 * A registration. Serves as a connection/account for a student and a course they are registered for.
 * @author Luke Hollinda
 *
 */
public class Registration implements Serializable{

	private static final long serialVersionUID = -84116223415095771L;
	
	/**
	 * The student registered.
	 */
	private Student theStudent;
	
	/**
	 * The offering that the student is registered for.
	 */
	private CourseOffering theOffering;
	
	/**
	 * Registers the input student for the input offering.
	 * @param st The student to be registered.
	 * @param of The courseOffering to be registered in.
	 */
	public void completeRegistration (Student st, CourseOffering of) {
		theStudent = st;
		theOffering = of;
		addRegistration ();
	}
	
	/**
	 * Calls the Student and the CourseOffering to add this registration to their registration lists.
	 */
	private void addRegistration () {
		theStudent.addRegistration(this);
		theOffering.addRegistration(this);
	}
	
	
	public Student getTheStudent() {
		return theStudent;
	}
	public void setTheStudent(Student theStudent) {
		this.theStudent = theStudent;
	}
	public CourseOffering getTheOffering() {
		return theOffering;
	}
	public void setTheOffering(CourseOffering theOffering) {
		this.theOffering = theOffering;
	}

	/**
	 * toString function for Registration.
	 */
	@Override
	public String toString () {
		String st = "\n";
		st += "Student Name: " + getTheStudent() + "\n";
		st += "The Offering: " + getTheOffering () + "\n";
		st += "\n-----------\n";
		return st;
		
	}
	

}
