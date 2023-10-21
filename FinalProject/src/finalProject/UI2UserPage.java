package finalProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import finalProject.enums.Department;

//User Interface after logging in
public class UI2UserPage extends JPanel {
	private JPanel pages;
	StaffAndProjectManagementService staffAndProjectManagementService = StaffAndProjectManagementService.getInstance();
	Staff staff;
	
	public UI2UserPage(JPanel pages, int staffID) {
		this.pages = pages;
		staff = staffAndProjectManagementService.getStaffByStaffID(staffID);
		
		this.setLayout(null);
		this.setBounds(150, 150, 600, 600);
		
		JLabel welcomeLabel = new JLabel("<html>Welcome!<br><br>The following tasks are due: </html>");
		welcomeLabel.setBounds(60, 20, 260, 60);
		this.add(welcomeLabel);
		
		JButton logOutButton = new JButton("Log out");              
		logOutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				 CardLayout layout = (CardLayout)pages.getLayout();
				 layout.show(pages, "loginPage");
			}
		});
		logOutButton.setBounds(380, 20, 110, 30);
		this.add(logOutButton);
		
		String alert = staff.displayAlerts();
		JTextArea alertTextArea = new JTextArea(alert);
		JScrollPane alertPane = new JScrollPane(alertTextArea);
		alertPane.setBounds(60, 80, 450, 150);
		this.add(alertPane);
		
		JTextArea infoTextArea = new JTextArea();
		JScrollPane infoPane = new JScrollPane(infoTextArea);
		infoPane.setBounds(60, 350, 450, 150);
		this.add(infoPane);
		
		JButton DisplayAllProjBtn = new JButton("Display all ongoing projects");
		DisplayAllProjBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				infoTextArea.setText(staff.displayAllOngingProjects());
			}
		});
		DisplayAllProjBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		DisplayAllProjBtn.setBounds(60, 280, 450, 20);
		this.add(DisplayAllProjBtn);
		
		//Only GeneralManager has the displayProjectsByDepartment and displayProjectByStaffID options
		if(staff.position.equals("General Manager")) {
			String[] departments= new String[] {"Road Project Department", "Building Project Department", "Financial Department"};
			JComboBox<String> departmentComboBox = new JComboBox<>(departments);
			departmentComboBox.setBounds(55, 305, 220, 25); 
			this.add(departmentComboBox);
		
			JButton DisplayProjectsBtn1 = new JButton("Display projects by department");
			DisplayProjectsBtn1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Department department = Department.values()[departmentComboBox.getSelectedIndex()];
					String info = ((GeneralManager)staff).displayProjectsByDepartment(department);
					infoTextArea.setText(info);
				}
			});
			DisplayProjectsBtn1.setBounds(275, 308, 235, 16);
			this.add(DisplayProjectsBtn1);
			
			JLabel staffIDLabel = new JLabel("Please enter staffID:");
			staffIDLabel.setBounds(60, 330, 135, 16);
			this.add(staffIDLabel);
			
			JTextField staffIDTextField = new JTextField();
			staffIDTextField.setBounds(190, 330, 60, 16);
			this.add(staffIDTextField);
			staffIDTextField.setColumns(10);
			
			JButton DisplayProjectsBtn2 = new JButton("Display projects by staff ID");
			DisplayProjectsBtn2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					String info;
					try {
						int inquiredStaffID = Integer.parseInt(staffIDTextField.getText());
						Staff inquiredStaff = staffAndProjectManagementService.getStaffByStaffID(inquiredStaffID);
						if(inquiredStaff != null) {
							info = ((GeneralManager)staff).displayProjectsByStaffID(inquiredStaffID);
						} else {
							info = "Please enter a valid staff ID";
						}
					} catch (NumberFormatException exc) {
						info = "Please enter a valid staff ID";
					}
					infoTextArea.setText(info);
				}
			});
			DisplayProjectsBtn2.setActionCommand("Display projects by staff ID");
			DisplayProjectsBtn2.setBounds(275, 330, 235, 16);
			this.add(DisplayProjectsBtn2);
		}
	}
}





