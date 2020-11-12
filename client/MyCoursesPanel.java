package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import registrationlogic.CourseOffering;
import registrationlogic.Registration;



/**
 * This class is one of the cards within the cardlayout of ClientView. 
 * Students are able to view the classes they are registered for and drop selected classes.
 */
@SuppressWarnings("serial")
public class MyCoursesPanel extends JPanel{
	private ClientView parent;
	private JPanel topButtonBar;
	private JButton goToCourseSelection, goToMyCourses, dropButton, goToStudentInfo;
	private DefaultListModel<String> courseListModel;
	private JList<String> courseList;
	private JScrollPane listScrollPane;
	

	public MyCoursesPanel(ClientView parent)
	{
		this.parent = parent;
		this.setLayout(new BorderLayout());
		
		//Create button bar with goToCourseSelection, goToMyCourses, and goToStudentInfo buttons. (NORTH)
		this.topButtonBar = new JPanel();
		this.goToCourseSelection = new JButton("Go to Course Selection View");
		this.goToMyCourses = new JButton("Go to My Courses View");
		this.goToStudentInfo = new JButton("Go to My Student Information");
		goToCourseSelection.addActionListener(new goToCourseSelectionListener(parent));
		goToMyCourses.addActionListener(new goToMyCoursesListener(parent));
		goToStudentInfo.addActionListener(new goToStudentInfoListener(parent));
		topButtonBar.add(goToCourseSelection);
		topButtonBar.add(goToMyCourses);
		topButtonBar.add(goToStudentInfo);
		this.add("North", topButtonBar);
		
		
		
		//CREATE COURSE LIST and SCROLLPANE (CENTER)
		Vector<Registration> courseRegList = parent.myClient.getStudentsRegistration();
		this.courseListModel = new DefaultListModel<String>();
		
		for(Registration reg: courseRegList)
		{
			String st = formatOffering(reg.getTheOffering());
			courseListModel.addElement(st);
		}
		this.courseList = new JList<String>(courseListModel);
		courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		courseList.setLayoutOrientation(JList.VERTICAL);
		courseList.setVisibleRowCount(-1);
		
		this.listScrollPane = new JScrollPane(courseList);
		listScrollPane.setPreferredSize(new Dimension(400,400));
		this.add("Center", listScrollPane);
		
		//CREATE DROP BUTTON (SOUTH)
		dropButton = new JButton("Drop Selected Course");
		dropButton.addActionListener(new dropButtonListener(courseList, courseListModel));
		this.add("South", dropButton);
		
		
		
		this.setVisible(true);
		
	}
	
	/**
	 * Adds a desired CourseOffering to the list of student courses displayed.
	 * @param course The CourseOffering to be added.
	 */
	public void addCourse(CourseOffering course)	
	{
		
		String st = formatOffering(course);
		courseListModel.addElement(st);
	}
	
	/**
	 * Will format CourseOfferings in the proper way to be displayed in the visible list.
	 */
	private String formatOffering(CourseOffering course)
	{
		String st = "";
		st += course.getTheCourse().getCourseName();
		st += " " + course.getTheCourse().getCourseNum();
		st += " Section: " + course.getSecNum();
		return st;
	}
	
	/**
	 * This is the listener class for the drop selected class button of the panel. 
	 * Upon pressing this button the selected class from the above list will be removed from the list
	 * and dropped from the student model.
	 */
	public class dropButtonListener implements ActionListener
	{
		JList<String> theList;
		DefaultListModel<String> theListModel;
		public dropButtonListener(JList<String> theList, DefaultListModel<String> theListModel)
		{
			this.theList = theList;
			this.theListModel = theListModel;
		}
		
		public void actionPerformed(ActionEvent e) 
		{
		    int index = theList.getSelectedIndex();
		    
		    if(index != -1) //If an Item was selected.
		    {
		    	//parse information and unregister student
		    	
		    	String selected = theListModel.elementAt(index);
		    	String[] splitSelected = selected.split(" ");
		    	
		    	String courseName = splitSelected[0];
		    	int ID = Integer.parseInt(splitSelected[1]);
		    	
		    	parent.myClient.unregisterCourse(courseName, ID);
		    	//remove course from displayed list
		    	theListModel.remove(index);
		    }
		}
	}

	
	
	/**
	 * ActionListener that will return the user back to Course Selection page.
	 */
	class goToCourseSelectionListener implements ActionListener
	{
		
		ClientView parent;
		public goToCourseSelectionListener(ClientView parent)
		{
			this.parent = parent;
		}
		
		public void actionPerformed(ActionEvent e) {
				parent.goToCourseSelection();
		}
	
	}
	
	/**
	 * ActionListner that will return the user back to My Courses page.
	 */
	class goToMyCoursesListener implements ActionListener
	{
		
		ClientView parent;
		public goToMyCoursesListener(ClientView parent)
		{
			this.parent = parent;
		}
		
		public void actionPerformed(ActionEvent e) {
				parent.goToMyCourses();
		}
	
	}
	
	/**
	 * ActionListner that will return the user back to student information page.
	 */
	class goToStudentInfoListener implements ActionListener
	{
		
		ClientView parent;
		public goToStudentInfoListener(ClientView parent)
		{
			this.parent = parent;
		}
		
		public void actionPerformed(ActionEvent e) {
				parent.goToStudentInfo();
		}
	
	}
}

