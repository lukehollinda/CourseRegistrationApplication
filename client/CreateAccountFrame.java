package client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client.ClientController;
import client.LoginPanel;
import registrationlogic.Student;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.JTextField;
import javax.swing.JButton;

/**
 * Frame that prompts users to enter a new students information, then on submission requests server to create a new student account with that information. 
 * It is also apparent that I only learned of the majesty of WindowBuilder after I finished all other frames besides CreateAccountFrame.
 */
@SuppressWarnings("serial")
public class CreateAccountFrame extends JFrame {

	/**
	 * JPanel components
	 */
	private JPanel contentPane;
	
	/**
	 * JTextField components. Student information will be retrieved upon submission from here. 
	 */
	private JTextField usernameText, passwordText, firstNameText, lastNameText, majorText, emailText, yearText;

	/**
	 * JButton components
	 */
	private JButton submitButton;
	
	/**
	 * Reference to ClientController
	 */
	private ClientController client;

	/**
	 * Initialize all frame components
	 */
	public CreateAccountFrame(ClientController client) {
		
		this.client = client;
		
	
		//Set close method for when a user quits the application
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//Set closing action to predefined exit procedure.
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent event) {
		        exitProcedure();
		    }
		});
		
		
		
		//Size and default close operations
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 452, 457);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Please fill in student information and press submit.");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(26, 11, 377, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Username");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(26, 51, 54, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPassword.setBounds(26, 90, 54, 14);
		contentPane.add(lblPassword);
		
		JLabel lblFirstName = new JLabel("First name");
		lblFirstName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblFirstName.setBounds(26, 123, 67, 14);
		contentPane.add(lblFirstName);
		
		JLabel lblLastName = new JLabel("Last name");
		lblLastName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblLastName.setBounds(26, 159, 67, 14);
		contentPane.add(lblLastName);
		
		JLabel lblMajor = new JLabel("Major");
		lblMajor.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMajor.setBounds(26, 196, 37, 14);
		contentPane.add(lblMajor);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmail.setBounds(26, 231, 67, 14);
		contentPane.add(lblEmail);
		
		JLabel lblYearOfStudy = new JLabel("Year of Study");
		lblYearOfStudy.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblYearOfStudy.setBounds(26, 264, 97, 14);
		contentPane.add(lblYearOfStudy);
		
		usernameText = new JTextField();
		usernameText.setBounds(159, 49, 244, 20);
		contentPane.add(usernameText);
		usernameText.setColumns(10);
		
		passwordText = new JTextField();
		passwordText.setBounds(159, 88, 244, 20);
		contentPane.add(passwordText);
		passwordText.setColumns(10);
		
		firstNameText = new JTextField();
		firstNameText.setBounds(159, 123, 244, 20);
		contentPane.add(firstNameText);
		firstNameText.setColumns(10);
		
		lastNameText = new JTextField();
		lastNameText.setBounds(159, 157, 244, 20);
		contentPane.add(lastNameText);
		lastNameText.setColumns(10);
		
		majorText = new JTextField();
		majorText.setBounds(159, 194, 244, 20);
		contentPane.add(majorText);
		majorText.setColumns(10);
		
		emailText = new JTextField();
		emailText.setBounds(159, 229, 244, 20);
		contentPane.add(emailText);
		emailText.setColumns(10);
		
		yearText = new JTextField();
		yearText.setBounds(159, 262, 244, 20);
		contentPane.add(yearText);
		yearText.setColumns(10);
		
		submitButton = new JButton("Submit");
		submitButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		submitButton.setBounds(26, 331, 377, 23);
		contentPane.add(submitButton);
		
		submitButton.addActionListener(new submitListener(client));
		
		this.setVisible(true);
	}
	
	/**
	 * Exit procedure for CreateAccountFrame
	 */
	public void exitProcedure()
	{
		client.quitProcedure();
		this.setVisible(false);
		this.dispose();
	}
	
	
	/**
	 * ActionListner that submits user information and creates new account for user.
	 * This is accomplished by sending the student information to the server.
	 */
	class submitListener implements ActionListener
	{
		
		ClientController parent;
		public submitListener(ClientController parent)
		{
			this.parent = parent;
		}
		
		public void actionPerformed(ActionEvent e) {
				
			Random r = new Random();
			int id = r.nextInt((40000000 - 30000000) + 1) + 30000000;   //Create a random ID between 30000000 and 40000000
																		//With the small number of users this should not be a problem
			String yearString = yearText.getText();
			
			int year = -1; 
			
			try
			{
				year = Integer.parseInt(yearString);
			}
			catch(NumberFormatException e1)  //Catch number formating issue for year text box.
			{

				  JFrame temp =new JFrame();  
				  JOptionPane.showMessageDialog(temp,"Please format your entry for the YEAR input to be in the form of an integer."); 
				  return;
			}
			
			String major = majorText.getText();
			String username = usernameText.getText();
			String password = passwordText.getText();
			String email = emailText.getText();
			String firstName = firstNameText.getText();
			String lastName = lastNameText.getText();
			
			Student newStudent = new Student(username, id);
			newStudent.setFirstName(firstName);
			newStudent.setLastName(lastName);
			newStudent.setStudentEmail(email);
			newStudent.setStudentMajor(major);
			newStudent.setStudentYear(year);
			
			//Create student account
			client.createStudentAccount(newStudent, password);
			
			//Prompt them that a student account has been made.
			JFrame temp =new JFrame();  
			JOptionPane.showMessageDialog(temp,"A new account has been made for you. To make a schedule for this student please login."); 
			
			//Open login screen
			@SuppressWarnings("unused")
			LoginPanel login = new LoginPanel();
			
			exitProcedure();
		}
	
	}
	



}
