package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import registrationlogic.Course;
import registrationlogic.CourseOffering;

/**
 * This class is one of the cards within the cardLayout of ClientView. 
 * Students are able to view and register for the available courses.
 */
@SuppressWarnings("serial")
public class CourseSelectionPanel extends JPanel{
	/**
	 * Reference to ClientView
	 */
	@SuppressWarnings("unused")
	private ClientView parent;
	
	/**
	 * JPanel components
	 */
	private JPanel topButtonBar;
	
	/**
	 * JButton components
	 */
	private JButton goToCourseSelection, goToMyCourses, goToStudentInfo, addToMyCourses;
	
	/**
	 * List model which organizes available courses
	 */
	private DefaultListModel<String> courseListModel;
	
	/**
	 * JList which houses and displays the available courses
	 */
	private JList<String> courseList;
	
	/**
	 * JScrollPane components
	 */
	private JScrollPane listScrollPane;
	
	/**
	 * Initializes all member components of the panel.
	 * @param parent Reference to ClientView.
	 */
	public CourseSelectionPanel(ClientView parent)
	{
		this.parent = parent;
		this.setLayout(new BorderLayout());
		
		//Create button bar with goToCourseSelection, goToStudentInfo, and goToMyCourses buttons. (NORTH)
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
		Vector<Course> courseObjectList = parent.getCourseList();
		this.courseListModel = new DefaultListModel<String>();
				
		for(Course course: courseObjectList)
		{
			String courseInformation = course.formatCourseData();
			courseListModel.addElement(courseInformation);
		}
		this.courseList = new JList<String>(courseListModel);
		courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		courseList.setLayoutOrientation(JList.VERTICAL);
		courseList.setVisibleRowCount(-1);
				
		this.listScrollPane = new JScrollPane(courseList);
		listScrollPane.setPreferredSize(new Dimension(400,400));
		this.add("Center", listScrollPane);
				
		//CREATE ADDCOURSE BUTTON (SOUTH)
		addToMyCourses = new JButton("Add Selected Course to MyCourse List");
		addToMyCourses.addActionListener(new addToMyCoursesListener(courseList, courseListModel, parent));
		this.add("South", addToMyCourses);
		
		
		
		this.setVisible(true);
	}
	
	/**
	 * This action listener will add the selected course and section to the students registered courses
	 * and display the course within the list in MyCoursesPanel.
	 */
	public class addToMyCoursesListener implements ActionListener
	{
		JList<String> theList;
		DefaultListModel<String> theListModel;
		ClientView parent;
		public addToMyCoursesListener(JList<String> theList, DefaultListModel<String> theListModel, ClientView parent)
		{
			this.theList = theList;
			this.theListModel = theListModel;
			this.parent = parent;
		}
		
		public void actionPerformed(ActionEvent e) 
		{
		    int index = theList.getSelectedIndex();
		    
		    if(index != -1)  //If an item was selected
		    {
		    	String theCourseFormated = theListModel.get(index);
		    	String[] splitCourseInformation = theCourseFormated.split(" ");
		    	
		    	
		    	//Break selected course from displayed list into its course name and ID.
		    	String courseName = splitCourseInformation[0];

		    	int courseID   = Integer.parseInt(splitCourseInformation[1]);
		    	
		    	//Find the course from within the offered course list.
		    	Course foundCourse = parent.myClient.findCourse(courseName, courseID);
		    	
		    	//All of this mess prompts the user to choose a section
		    	Vector<CourseOffering> foundCourseOfferingList = foundCourse.getOfferingList();
		    	
		    	String[] optionList = new String[foundCourseOfferingList.size()];
		    	for(int i = 0; i < foundCourseOfferingList.size(); i++)
		    	{
		    		String st = "";
		    		st += foundCourseOfferingList.elementAt(i).getTheCourse().getCourseName();
		    		st += " " + foundCourseOfferingList.elementAt(i).getTheCourse().getCourseNum();
		    		st += " Section number: " + foundCourseOfferingList.elementAt(i).getSecNum();
		    		st += " Section cap: " + foundCourseOfferingList.elementAt(i).getSecCap();
		    		optionList[i] = st;
		    	}
		    	
		    	int choice = JOptionPane.showOptionDialog(null, "Please choose a section to join. This will then be added to your class list.",
		                "Click a button",
		                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, optionList, optionList[0]);
		    	
		    	if(choice == -1	)  //If no item is selected return.
		    	{  
		    		return;
		    	}
		    	
		    	CourseOffering desiredOffering = foundCourseOfferingList.elementAt(choice);
		    	
		    	
		    	if(parent.myClient.isStudentAlreadyRegistered(desiredOffering)) //If they are already in a different section of that class
		    	{
		    		JFrame tempFrame = new JFrame();
		    		JOptionPane.showMessageDialog(tempFrame, "You are already registered in a section of this course.");
		    	}
		    	else if(parent.myClient.getStudent().getStudentRegistrations().size() >= 6) //If they are already registered for 6 courses
		    	{
		    		JFrame tempFrame = new JFrame();
		    		JOptionPane.showMessageDialog(tempFrame, "You are already registered in six courses.");
		    	}
		    	else  //Register student in View list and in Model
		    	{
		    		//Register student 
			    	parent.myClient.regesterStudent(desiredOffering);
			    	
			    	//Add to myCoursePanel list
			    	parent.myCoursesPanel.addCourse(desiredOffering);

		    	}
		    }
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
