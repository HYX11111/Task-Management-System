package finalProject;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;

public class UI0LoginPage extends JFrame {
	CardLayout layout = new CardLayout();
	JPanel pages = new JPanel(layout);
	private StaffAndProjectManagementService staffAndProjectManagementService = StaffAndProjectManagementService.getInstance();

	public UI0LoginPage() {
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(150, 150, 600, 600);
		this.setContentPane(pages);
		
		JPanel page0 = new JPanel();
		page0.setBounds(150, 150, 600, 600);
		page0.setBorder(new EmptyBorder(5, 5, 5, 5));
		page0.setLayout(null);
		pages.add(page0, "loginPage");
		layout.show(pages, "loginPage");
		
		JLabel staffIDLabel = new JLabel("Staff ID");
		staffIDLabel.setBounds(150, 115, 80, 15);
		page0.add(staffIDLabel);
		
		JTextField passwordField = new JTextField();
		passwordField.setBounds(280, 150, 170, 25);
		page0.add(passwordField);
		passwordField.setColumns(15);
		
		JLabel staffIDLabel_1 = new JLabel("Password");
		staffIDLabel_1.setBounds(150, 155, 80, 15);
		page0.add(staffIDLabel_1);
				
		JTextField staffIDField = new JTextField();
		staffIDField.setColumns(15);
		staffIDField.setBounds(280, 110, 170, 25);
		page0.add(staffIDField);
		
		JLabel message = new JLabel("Please enter your staff ID and password to login.");
		message.setBounds(150, 225, 330, 15);
		page0.add(message);
		
		JButton loginButton = new JButton("Login");
		loginButton.setBounds(225, 330, 110, 30);
		page0.add(loginButton);
		
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String password = passwordField.getText();
				if(password == null) {
					message.setText("Please fill in the required information.");
				} else {
					try{
						int staffID = Integer.parseInt(staffIDField.getText());
				
						if(staffID == 0) {
							if(staffID == Administrator.STAFF_ID && password.equals(Administrator.PASSSWORD)) {
								UI1AdminPage page1 = new UI1AdminPage(pages);
								pages.add(page1, "adminPage");
								layout.show(pages, "adminPage");
							} else {
								message.setText("Invalid Staff ID or password. Please try again."); 
							}
						} else {
							if (checkPassword(staffID, password)) {
								UI2UserPage page2= new UI2UserPage(pages, staffID);
								pages.add(page2, "userPage");
							    layout.show(pages, "userPage");
							} else {			
								message.setText("Invalid Staff ID or Password. Please try again.");
							}
						}
					} catch (NumberFormatException exc) {
						message.setText("Invalid Staff ID. Please try again."); 
					}
				}
			}

			private boolean checkPassword(int staffID, String password) {
				Staff staff = staffAndProjectManagementService.getStaffByStaffID(staffID);
				if(staff == null) {
					return false; 
				} else {
					return staff.getPassword().equals(password);
				}
			}
		});
	}
}
