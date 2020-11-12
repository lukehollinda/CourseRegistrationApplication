package client;


import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.*;

import registrationlogic.Course;
import registrationlogic.Student;

/**
 * Main client view class. Houses other panels within a cardLayout. 
 * Also holds an object of ClientController to communicate with the socket and student model.
 */
@SuppressWarnings("serial")
public class ClientView extends JFrame{
	
	/**
	 * Main client logic
	 */
	ClientController myClient;
	
	/**
	 * CardLayout Panel
	 */
	JPanel cards;
	
	/**
	 *  CardLayout Object
	 */
	CardLayout cardLayout;
	
	/**
	 * CourseSelectionPanel CARD 1 in CardLayout
	 */
	CourseSelectionPanel courseSelectionPanel;
	
	/**
	 * MyCoursePanel CARD 2 in CardLayout
	 */
	MyCoursesPanel myCoursesPanel;
	
	/**
	 * StudentInformationPanel CARD 3 in CardLayout
	 */
	StudentInformationPanel studentInformationPanel;
	

	
	
	/**
	 * Constructor for Client view, initializes all frame components.
	 * @param title Title of Frame
	 */
	public ClientView(String title, ClientController myClient, Student theStudent) 
	{
		super (title);
		
		//Connection to server
		this.myClient = myClient;
		this.myClient.setStudent(theStudent);
		
		
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		this.setLayout(new FlowLayout());

		
		//Set close method for when a user quits the application
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//Set closing action to predefined exit procedure.
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent event) {
		        exitProcedure();
		    }
		});
		
		
		//Initialize Panel and Layout for the cardLayout used
		cards = new JPanel(new CardLayout());
		cardLayout = (CardLayout)cards.getLayout();
		
		//Initialize and add courseSelectionPanel (CARD PAGE 1)
		courseSelectionPanel = new CourseSelectionPanel(this);
		courseSelectionPanel.setSize(new Dimension(601,511));
		cards.add(courseSelectionPanel, "COURSE_SELECTION");
		
		//Initialize and add myCoursesPanel  (CARD PAGE 2)
		myCoursesPanel = new MyCoursesPanel(this);
		myCoursesPanel.setSize(new Dimension(601,511));
		cards.add(myCoursesPanel, "MY_COURSES");
		
		//Initialize and add studentInfromationPanel (CARD PAGE 3)
		studentInformationPanel = new StudentInformationPanel(this);
		studentInformationPanel.setSize(new Dimension(601,511));
		cards.add(studentInformationPanel, "STUDENT_INFO");
		
		this.add(cards);
		this.setVisible(true);
		this.pack();
	}

	/**
	 * Returns the students courseList.
	 */
	public Vector<Course> getCourseList()
	{
		Vector<Course> courseList = myClient.requestCourseList();
		return courseList;
	}
	
	/**
	 * Displays CourseSelectionPanel.
	 */
	public void goToCourseSelection()
	{
		cardLayout.show(cards, "COURSE_SELECTION");
		this.pack();
	}	
	
	/**
	 * Displays MyCoursesPanel.
	 */
	public void goToMyCourses()
	{
		cardLayout.show(cards, "MY_COURSES");
		this.pack();
	}
		
	/**
	 * Displays student Information Panel.
	 */
	public void goToStudentInfo()
	{
		cardLayout.show(cards, "STUDENT_INFO");
		this.pack();
	}
	
	/**
	 * Closes ClientView properly and tells server that Client is done.
	 */
	public void exitProcedure()
	{
		myClient.quitProcedure();
		this.setVisible(false);
		this.dispose();
	}

}
