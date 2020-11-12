package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import registrationlogic.Student;


/**
 * StudentInformationPanel is one of the cards held in the CardLayout found within ClientView. 
 * Students can view and edit their account information.
 */
@SuppressWarnings("serial")
public class StudentInformationPanel extends JPanel{

	/**
	 * Reference to clientView (Parent component)
	 */
	@SuppressWarnings("unused")
	private ClientView parent;
	
	/**
	 * JButton components
	 */
	private JButton goToCourseSelection, goToMyCourses, goToStudentInfo, logoutButton, changeInformationButton;
	
	/**
	 * JPanel components
	 */
	private JPanel topButtonBar, centerPanel, bottomButtonBar;
	
	/**
	 * JTextField components, edits to student information will be read from here.
	 */
	JTextField usernameText, passwordText, firstNameText, lastNameText, emailText, majorText, yearText, idText;
	
	
	/**
	 * Initializes all panel components.
	 * @param parent Reference to parent component.
	 */
	public StudentInformationPanel(ClientView parent)
	{
		this.parent = parent;
		
		this.setLayout(new BorderLayout());
		
		//Create button bar with goToCourseSelection, goToMyCourses and goToStudentInfo buttons. (NORTH)
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
		
		
		//Retrieve Student information to display upon first visiting the StudentInformationPanel	
		Student theStudent = parent.myClient.getStudent();
		
		//Initialize centerPanel and components.
		GridLayout grid = new GridLayout(7, 2);
		centerPanel = new JPanel();
		centerPanel.setLayout(grid);
		
		JLabel usernameLabel = new JLabel("Username");
		centerPanel.add(usernameLabel);
		usernameText = new JTextField(theStudent.getStudentName());
		centerPanel.add(usernameText);
		
		JLabel firstNameLabel = new JLabel("First name");
		centerPanel.add(firstNameLabel);
		firstNameText = new JTextField(theStudent.getFirstName());
		centerPanel.add(firstNameText);
		
		JLabel lastNameLabel = new JLabel("Last name");
		centerPanel.add(lastNameLabel);
		lastNameText = new JTextField(theStudent.getLastName());
		centerPanel.add(lastNameText);
		
		JLabel emailLabel = new JLabel("Email");
		centerPanel.add(emailLabel);
		emailText = new JTextField(theStudent.getStudentEmail());
		centerPanel.add(emailText);
		
		JLabel majorLabel = new JLabel("Major");
		centerPanel.add(majorLabel);
		majorText = new JTextField(theStudent.getStudentMajor());
		centerPanel.add(majorText);
		
		JLabel yearLabel = new JLabel("Year");
		centerPanel.add(yearLabel);
		yearText = new JTextField(Integer.toString(theStudent.getStudentYear()));
		centerPanel.add(yearText);
		
		JLabel idLabel = new JLabel("ID");
		centerPanel.add(idLabel);
		idText = new JTextField(Integer.toString(theStudent.getStudentId()));
		idText.setEditable(false);
		centerPanel.add(idText);
		
		//Add centerPanel containing student information (CENTER)
		this.add("Center",centerPanel);
		
		//Bottom button bar and logoutButton (SOUTH)
		this.logoutButton = new JButton("Logout");
		
		this.changeInformationButton = new JButton("Save student information");
		
		this.bottomButtonBar = new JPanel(new BorderLayout());
		bottomButtonBar.add("West", changeInformationButton);
		bottomButtonBar.add("East", logoutButton);

		
		this.add("South", bottomButtonBar);
		
		logoutButton.addActionListener(new logoutListener(parent));
		changeInformationButton.addActionListener(new changeInformationListener(parent));
	}
	
	/**
	 * ActionListner tell the server to update student information
	 */
	class changeInformationListener implements ActionListener
	{
		
		ClientView parent;
		public changeInformationListener(ClientView parent)
		{
			this.parent = parent;
		}
		
		public void actionPerformed(ActionEvent e) 
		{
			JFrame temp = new JFrame();
			int choice = JOptionPane.showConfirmDialog(temp,"Are you sure that you want to update your student information. \n Your courses will be saved.");  
			if(choice == JOptionPane.YES_OPTION)
			{  
				//Get student information
				Student tempStudent = parent.myClient.getStudent();		
				
				//Edit student information
				tempStudent.setFirstName(firstNameText.getText());
				tempStudent.setLastName(lastNameText.getText());
				tempStudent.setStudentEmail(emailText.getText());
				tempStudent.setStudentMajor(majorText.getText());
				tempStudent.setStudentName(usernameText.getText());
				
				try
				{
					int yearNum = Integer.parseInt(yearText.getText());
					tempStudent.setStudentYear(yearNum);
					
				}
				catch(NumberFormatException e1)
				{
					//Intentional empty try block to account for improperly formatted year input
				}
				
				parent.myClient.setStudent(tempStudent);
				parent.myClient.updateUserInformation();
				
				
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
	
	/**
	 * ActionListner that will close application and open a login page.
	 */
	class logoutListener implements ActionListener
	{
		
		ClientView parent;
		public logoutListener(ClientView parent)
		{
			this.parent = parent;
		}
		
		public void actionPerformed(ActionEvent e) {
				parent.exitProcedure();
				@SuppressWarnings("unused")
				LoginPanel login = new LoginPanel();
		}
	
	}
}
