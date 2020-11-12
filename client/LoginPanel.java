package client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import registrationlogic.Student;

/**
 * Login page of client side of application. Users have the option to open a frame to create a student account or login with an existing account.
 * @author Luke Hollinda
 */
@SuppressWarnings("serial")
public class LoginPanel extends JFrame{

	/**
	 * JButton components
	 */
	JButton loginButton, createAccountButton;
	
	/**
	 * JLabel components
	 */
	JLabel usernameLabel, passwordLabel, topLabel, usernameHint, passwordHint;
	
	/**
	 * JTextField components. Password and username will be read out of these.
	 */
	JTextField usernameText, passwordText;
	
	/**
	 * Connection to socket.
	 */
	ClientController client;
	
	/**
	 * Initializes all frame components and clientController.
	 */
	public LoginPanel()
	{
		//Link to socket for communication with server.
		client = new ClientController("localhost", 9898);
		
		//Set close method for when a user quits the application
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				
		//Set closing action to predefined exit procedure.
		this.addWindowListener(new WindowAdapter() 
		{
		 @Override
		public void windowClosing(WindowEvent event) 
		 	{
				 exitProcedure();
		 	}
		});
		
		//Set bounds and absolute layout
		this.setBounds(100, 100, 450, 300);
		this.setLayout(null);
		
		//Top Label
		topLabel = new JLabel("Course Registration Login Page");
		topLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 17));
		topLabel.setBounds(91, 11, 252, 20);
		this.add(topLabel);
		
		//Username Label
		usernameLabel = new JLabel("Username"); 
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		usernameLabel.setBounds(36, 67, 73, 20);
		this.add(usernameLabel);
		
		//Password Label
		passwordLabel = new JLabel("Password");
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		passwordLabel.setBounds(36, 117, 73, 14);
		this.add(passwordLabel);
		
		//Username text
		usernameText = new JTextField();
		usernameText.setBounds(119, 69, 258, 20);
		usernameText.setColumns(10);
		this.add(usernameText);
		
		//Password text
		passwordText = new JTextField();
		passwordText.setBounds(119, 116, 258, 20);
		passwordText.setColumns(10);
		this.add(passwordText);
		
		//Login button
		loginButton = new JButton("Login");
		loginButton.setBounds(36, 189, 148, 23);
		this.add(loginButton);
		
		//Create account Button
		createAccountButton = new JButton("Create New Account");
		createAccountButton.setBounds(230, 189, 160, 23);
		this.add(createAccountButton);
		
		//Username hint
		usernameHint = new JLabel("Hint: userLuke");
		usernameHint.setBounds(119, 91, 89, 14);
		this.add(usernameHint);
		
		//Password Hint
		passwordHint = new JLabel("Hint: password");
		passwordHint.setBounds(119, 137, 90, 14);
		this.add(passwordHint);
		
		this.setVisible(true);
		
		//Add listeners
		loginButton.addActionListener(new loginListener(client));
		createAccountButton.addActionListener(new createAccountListener(client));
	
	}
	
	/**
	 * Exit procedure for LoginPanel
	 */
	public void exitProcedure()
	{
		client.quitProcedure();
		this.setVisible(false);
		this.dispose();
	}
	
	/**
	 * ActionListner that will return the user back to My Courses page.
	 */
	class loginListener implements ActionListener
	{ 
		ClientController client;
		
		public loginListener(ClientController controller)
		{
			this.client = controller;
			
		}
		
		public void actionPerformed(ActionEvent e) {
				String username = usernameText.getText();
				String password = passwordText.getText();
				Student found = client.requestStudentInformation(username, password);
				
				if(found != null)
				{
					//Open client view
					@SuppressWarnings("unused")
					ClientView acceptedView = new ClientView("ClientView", client, found);
					//dispose of frame without closing the cientController passed to acceptedView
					setVisible(false);
					dispose();
				}
				else
				{
					  JFrame temp =new JFrame();  
					  JOptionPane.showMessageDialog(temp,"The credentials you have entered are not found within our database."); 
				}
		}
	
	}
	
	
	
	
	
	/**
	 * ActionListner that will prompt student to input student data and create new student account
	 */
	class createAccountListener implements ActionListener
	{ 
		ClientController client;
		
		public createAccountListener(ClientController controller)
		{
			this.client = controller;
			
		}
		
		public void actionPerformed(ActionEvent e) {
			
				//Open create account frame
				@SuppressWarnings("unused")
				CreateAccountFrame createAccount = new CreateAccountFrame(client);
				//Delete frame
				setVisible(false);
				dispose();
				
		}
	
	}
	

}
