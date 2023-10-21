package finalProject;

import java.time.temporal.ChronoUnit;
import java.util.*;
import finalProject.enums.*;

public abstract class Staff {
    static int staffCounter = 0;
    static StaffAndProjectManagementService staffAndProjectManagementService = StaffAndProjectManagementService.getInstance();
    static RiskCodeAssessor riskCodeAssessor = RiskCodeAssessor.getInstance();

    int staffID;
    String password;
    String staffName;
    String position;
    Department department;

    public static void setStaffCounter(int staffCounter) { Staff.staffCounter = staffCounter; }

    //Method to display Tasks due, available to all Staff but with different implementations depending on the position of each Staff
    public abstract String displayAlerts();
    //Method to display all ongoing Projects(including information of related Tasks),available to all Staff but with different implementations depending on the position of each Staff
    public abstract String displayAllOngingProjects();
    public String getPassword() { return password; }

    //Method to transform information of the Projects that are returned after inqury into String
    String projectsToString(ArrayList<Project> projects) {
        if(projects.isEmpty()) {
            return "There is no project that meets the selection criteria";
        } else {
            StringBuilder sb = new StringBuilder();
            for(Project project : projects) { sb.append(project.toString()).append(System.lineSeparator()); }
            return sb.toString();
        }
    }
}

class Employee extends Staff {
    public Employee (String password, String staffName, Department department) {
        this.staffID = ++staffCounter;
        this.password = password;
        this.staffName = staffName;
        this.position = "Employee";
        this.department = department;
    }

    //For Employee, all overdue tasks with YELLOW, ORANGE or RED RiskCode will be displayed, and Employee only has access to information regarding his or her Projects
    public String displayAlerts() {
        StringBuilder alertSB = new StringBuilder();
        ArrayList<Project> projects = staffAndProjectManagementService.getOngoingProjectsByStaffID(staffID);
        for(Project project : projects) {
            ArrayList<ProjectTask> tasks = project.tasks;
            //Sort the Tasks by dueDate so that the due Tasks of each Project will be displayed in aß chronological order
            tasks.sort((ProjectTask task1, ProjectTask task2) -> (int) ChronoUnit.DAYS.between(task2.dueDate, task1.dueDate));
            for(ProjectTask task : tasks) {
                if(riskCodeAssessor.getRiskCode(task).equals(RiskCode.YELLOW) || riskCodeAssessor.getRiskCode(task).equals(RiskCode.ORANGE) || riskCodeAssessor.getRiskCode(task).equals(RiskCode.RED)) {
                    alertSB.append(task.toString()).append(System.lineSeparator());
                }
            }
        }
        return alertSB.toString();
    }

    //Display the ongoing Projects of the Employee
    public String displayAllOngingProjects() {
        ArrayList<Project> projects = staffAndProjectManagementService.getOngoingProjectsByStaffID(staffID);
        return projectsToString(projects);
    }
}

class Manager extends Staff {
    public Manager(String password, String staffName, Department department) {
        this.staffID = ++staffCounter;
        this.password = password;
        this.staffName = staffName;
        this.position = "Manager";
        this.department = department;
    }
    
    //For Manager, only tasks with ORANGE or RED RiskCode will be displayed, and Manager has access to information regarding Projects in his or her department
    public String displayAlerts() {
        StringBuilder alertSB = new StringBuilder();
        ArrayList<Project> projects = staffAndProjectManagementService.getOngoingProjectsByDepartment(department);
        for(Project project : projects) {
            ArrayList<ProjectTask> tasks = project.tasks;
            tasks.sort((ProjectTask task1, ProjectTask task2) -> (int) ChronoUnit.DAYS.between(task2.dueDate, task1.dueDate));
            for(ProjectTask task : tasks) {
                if(riskCodeAssessor.getRiskCode(task).equals(RiskCode.ORANGE) || riskCodeAssessor.getRiskCode(task).equals(RiskCode.RED)) {
                    alertSB.append(task.toString()).append(System.lineSeparator());
                }
            }
        }
        return alertSB.toString();
    }

    //Display ongoing Projects in his or her Department
    public String displayAllOngingProjects() {
        ArrayList<Project> projects = staffAndProjectManagementService.getOngoingProjectsByDepartment(department);
        return projectsToString(projects);
    }
}

class GeneralManager extends Staff {
    public GeneralManager(String password, String staffName) {
        this.staffID = ++staffCounter;
        this.password = password; 
        this.staffName = staffName;
        this.position = "General Manager";
        this.department = Department.OTHER;
    }
    
    //For GeneralManager, only tasks with RED RiskCode will be displayed, and Manager has access to information regarding all Projects in the company
    public String displayAlerts() {
        StringBuilder alertSB = new StringBuilder();
        ArrayList<Project> projects = new ArrayList<Project>();

        for(Department department : Department.values()) {
            projects.addAll(staffAndProjectManagementService.getOngoingProjectsByDepartment(department));
        }
        for(Project project : projects) {
            ArrayList<ProjectTask> tasks = project.tasks;
            tasks.sort((ProjectTask task1, ProjectTask task2) -> (int) ChronoUnit.DAYS.between(task2.dueDate, task1.dueDate));
            for(ProjectTask task : tasks) {
                if(riskCodeAssessor.getRiskCode(task).equals(RiskCode.RED)) {
                    alertSB.append(task.toString()).append(System.lineSeparator());
                }
            }
        }
        return alertSB.toString();
    }

    //Dispaly all ongoing projects of the company
    public String displayAllOngingProjects() {
        ArrayList<Project> projects = new ArrayList<Project>();
        for(Department department : Department.values()) {
            projects.addAll(staffAndProjectManagementService.getOngoingProjectsByDepartment(department));
        }
        return projectsToString(projects);
    }

    //Methods for GeneralManager to look up projects of a certain Staff or by Department
    public String displayProjectsByStaffID(int staffID) {
        ArrayList<Project> projects = staffAndProjectManagementService.getAllProjectsByStaffID(staffID);
        return projectsToString(projects);
    }

    public String displayProjectsByDepartment(Department department) {
        ArrayList<Project> projects = staffAndProjectManagementService.getAllProjectsByDepartment(department);
        return projectsToString(projects);
    }
}
