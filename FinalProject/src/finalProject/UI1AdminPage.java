package finalProject;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import finalProject.enums.Department;

public class UI1AdminPage extends JPanel {
	private JPanel pages;
	static StaffAndProjectManagementService staffAndProjectManagementService = StaffAndProjectManagementService.getInstance();
	final String HINT = "Please enter all necessary project and task information correctly. The responsible staff must be an exisiting staff and his/her position must be Employee. Project Value needs to be a number and Due Date should be in correct format." +  
		"\nIMPORTANT: After filling in the task table and before clicking the register button, please do not forget to move the cursor away and click somewhere else; otherwise, if the cell is still seleted, the information will not be recorded.";	
	JList<String> optionList;
	JTextArea infoTextArea;
	JTextField idField;

	public UI1AdminPage(JPanel pages) {
		this.pages = pages;
			
		this.setLayout(null);
		this.setBounds(150, 150, 600, 600);
		
		JLabel welcomeLabel = new JLabel("<html>Welcome!<br><br>You can register projects and related tasks,<br> look up projects and tasks, and change their status </html>");
		welcomeLabel.setBounds(50, 5, 330, 64);
		this.add(welcomeLabel);
		
		JButton logOutButton = new JButton("Log out");              
		logOutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				 CardLayout layout = (CardLayout)pages.getLayout();
				 layout.show(pages, "loginPage");
			}
		});
		logOutButton.setBounds(400, 20, 110, 30);
		this.add(logOutButton);
		
		JLabel projNameLabel = new JLabel("Project Name:");
		projNameLabel.setBounds(50, 88, 95, 22);
		this.add(projNameLabel);

		JTextField projNameField = new JTextField();
		projNameField.setColumns(10);
		projNameField.setBounds(150, 88, 160, 22);
		this.add(projNameField);
		
		JLabel StaffIDLabel = new JLabel("Staff ID:");
		StaffIDLabel.setBounds(50, 110, 95, 22);
		this.add(StaffIDLabel);
		
		JTextField staffIDField = new JTextField();
		staffIDField.setColumns(10);
		staffIDField.setBounds(150, 110, 160, 22);
		this.add(staffIDField);

		String[] departments = new String[] {"Road Project Department", "Building Project Department", "Financial Department"};
		JList<String> departmentList = new JList<>(departments);
		departmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		departmentList.setBounds(330, 80, 180, 55);
		this.add(departmentList);
		
		String[] columnNames = {"Task Name", "Project Value", "Due Date (yyyy-MM-dd)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 5);
		JTable table = new JTable(tableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
	    table.getColumnModel().getColumn(1).setPreferredWidth(100);
	    table.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 150, 460, 105);
        this.add(scrollPane);
		
		infoTextArea = new JTextArea();
		infoTextArea.setLineWrap(true);
		infoTextArea.setWrapStyleWord(true);
		infoTextArea.setText(HINT);
	
		JScrollPane infoPane = new JScrollPane(infoTextArea);
		infoPane.setBounds(50, 380, 460, 150);
		this.add(infoPane);

		JButton registerProjBtn = new JButton("Register new project and tasks");
		registerProjBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try{
					String projectName = projNameField.getText();
					Department department = Department.values()[departmentList.getSelectedIndex()];
					int staffID = Integer.parseInt(staffIDField.getText());
					//To keep track of the number of Tasks created, so that the taskCounter can be set in case of unsuccssful registration due to inalid task information
					int taskCreated = 0;
					if(projectName == null || projectName.isEmpty() || departmentList.getSelectedIndex() == -1) {
						infoTextArea.setText("Cannot register. Invalid project information.\n" + HINT);
						return;
					}
					if(staffAndProjectManagementService.getStaffByStaffID(staffID) == null || !staffAndProjectManagementService.getStaffByStaffID(staffID).position.equals("Employee")) {
						infoTextArea.setText("Cannot register. Invalid staff ID.\n" + HINT);
						return;
					}
					Project project = new Project(projectName, department, staffID);
					DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					for(int row = 0; row < tableModel.getRowCount(); row++) {
						try {
							String taskName = (String)tableModel.getValueAt(row, 0);
							String projectValueString = (String)tableModel.getValueAt(row, 1);
							String dueDateString = (String)tableModel.getValueAt(row, 2);
							if((taskName == null || taskName.isEmpty()) && (projectValueString == null || projectValueString.isEmpty()) && (dueDateString == null || dueDateString.isEmpty())) {
								break;
							}

							double projectValue = Double.parseDouble(projectValueString);
							LocalDate dueDate = LocalDate.parse(dueDateString, dateformatter);

							project.addTask(new ConstructionProjectTask(project.projectID, taskName, projectValue, dueDate));
							taskCreated++;
						} catch (Exception exc) {
							infoTextArea.setText("Cannot register. Invalid task information. \n" + HINT);
							//Set the projectCounter and the taskCounter in case of unsuccssful registration due to inalid task information
							Project.projectCounter--;
							ProjectTask.taskCounter -= taskCreated; 
							return;
						}
					}
					if(!project.tasks.isEmpty()) {
						staffAndProjectManagementService.addProjectAndTasks(project);
						infoTextArea.setText("The project and tasks have been registered successfully! Please remember the ProjectID and taskID.\n" + project.toString());
						// Reset the table after successfully registering the information
						for (int row = 0; row < tableModel.getRowCount(); row++) {
							for (int col = 0; col < tableModel.getColumnCount(); col++) {
								tableModel.setValueAt("", row, col); 
							}
						}
					} else {
						infoTextArea.setText("Cannot register. Invalid task information. \n" + HINT);
						Project.projectCounter--; 
						ProjectTask.taskCounter -= taskCreated; 
					}
				} catch (Exception exc) {
					infoTextArea.setText("Cannot register. Invalid project information.\n" + HINT);
					return;
				}
			}
		});
		registerProjBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		registerProjBtn.setBounds(50, 260, 460, 20);
		this.add(registerProjBtn);
		
		String[] options = new String[] {"Project ID", "Task ID"};
		optionList = new JList<String>(options);
        optionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        optionList.setBounds(50, 330, 100, 35);
        this.add(optionList);

		idField = new JTextField();
		idField.setColumns(10);
		idField.setBounds(150, 330, 80, 30);
		add(idField);

		JButton displayInfoBtn = new JButton("Display information");
		displayInfoBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(optionList.getSelectedIndex() == -1) {
					infoTextArea.setText("Please select Project ID or Task ID");
				} else if (optionList.getSelectedIndex() == 0){
					Project project = getProject(idField.getText());
					if(project == null) {
						infoTextArea.setText("Invalid Project ID");
					} else { 
						infoTextArea.setText(project.toString());
					}
				} else if(optionList.getSelectedIndex() == 1) {
					ProjectTask task = getTask(idField.getText());
					if(task == null) {
						infoTextArea.setText("Invalid Task ID");
					} else {
						infoTextArea.setText(task.toString());
					}
				}
			}  
		});
		displayInfoBtn.setBounds(242, 330, 268, 16);
		this.add(displayInfoBtn);

        JButton completedBtn = new JButton("Mark as completed");
		addMouseAdapter(completedBtn, this);
		completedBtn.setBounds(242, 350, 125, 16);
        add(completedBtn);

		JButton abortedBtn = new JButton("Mark as aborted");
		addMouseAdapter(abortedBtn, this);
		abortedBtn.setBounds(385, 350, 125, 16);
		add(abortedBtn);
	}

	private void addMouseAdapter(JButton button, JPanel panel) {
		button.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String message = "Are you sure you want to mark the project/task as completed or aborted? This operation cannot be undone";
					int proceed = JOptionPane.showConfirmDialog(panel, message, "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(proceed == JOptionPane.YES_OPTION) {
						if(optionList.getSelectedIndex() == -1) {
							infoTextArea.setText("Please select Project ID or Task ID");
						} else if (optionList.getSelectedIndex() == 0){
							Project project = getProject(idField.getText());
							if(project == null) {
								infoTextArea.setText("Invalid Project ID");
							} else {
								String info = button.getText().equals("Mark as completed") ? project.markAsCompleted() : project.markAsAborted();
								staffAndProjectManagementService.updateProject(project);
								infoTextArea.setText(info + "\n" + project.toString());
							}
						} else if(optionList.getSelectedIndex() == 1) {
							ProjectTask task = getTask(idField.getText());
							if(task == null) {
								infoTextArea.setText("Invalid Task ID");
							} else {
								String info = button.getText().equals("Mark as completed") ? task.markAsCompleted() : task.markAsAborted();
								staffAndProjectManagementService.updateTask(task);
								infoTextArea.setText(info + "\n" + task.toString());
							}
						}
					} else {
						return; 
					}
				}
		});	
	}

	private Project getProject(String idFieldText) {
		try {
			Project project = staffAndProjectManagementService.getProjetByProjectID(Integer.parseInt(idFieldText));
			return project;
		} catch(NumberFormatException exc) {
			infoTextArea.setText("Invalid Project ID");
			return null;
		}
	}
 
	private ProjectTask getTask(String idFieldText) {
		try {
			ProjectTask task = staffAndProjectManagementService.getTaskByTaskID(Integer.parseInt(idFieldText));
			return task;
		} catch(NumberFormatException exc) {
			infoTextArea.setText("Invalid Task ID");
			return null;
		}
	}
}
