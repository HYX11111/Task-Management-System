package finalProject;

import java.util.ArrayList;
import java.util.HashMap;
import finalProject.enums.Department;

//The class that connects the main part of the program with data storing service 
public class StaffAndProjectManagementService {
    private static StaffAndProjectManagementService instance;
    private static DataStoringService dataStoringService = DataStoringService.getInstance();

    public static StaffAndProjectManagementService getInstance() {
        if(instance == null) { instance = new StaffAndProjectManagementService();}
        return instance; 
    }

    //Method to update counters when the program is restarted
    public void updateCounters() {
        HashMap<String, Integer> counters = dataStoringService.getCounters();
        System.out.println(counters.get("staffCounter"));
        System.out.println(counters.get("projectCounter"));
        System.out.println(counters.get("taskCounter"));

        Staff.setStaffCounter(counters.get("staffCounter"));
        Project.setProjectCounter(counters.get("projectCounter"));
        ProjectTask.setTaskCounter(counters.get("taskCounter"));
    }

    //Method to add Staff, and methods to add Projects and related Tasks to database
    public void addStaff(Staff staff) {
        dataStoringService.addStaff(staff);
    }

    public void addProjectAndTasks(Project project) {
        dataStoringService.addProject(project);
        for(ProjectTask task : project.tasks) {
            dataStoringService.addConstruacitonProjTask((ConstructionProjectTask)task);
        }
    }

    //Methods to update Projects and Tasks
    public void updateProject(Project project) {
        dataStoringService.updateProject(project);
        //If a Project is marked as aborted, all of its Tasks will also be marked as aborted so the status of the Tasks also need to be updated
        for(ProjectTask task : project.tasks) { updateTask(task); }
    }

    public void updateTask(ProjectTask task) {
        dataStoringService.updateConstruacitonProjTask((ConstructionProjectTask)task);
    }

    //Methods to read from database
    public Staff getStaffByStaffID(int staffID) {
        return dataStoringService.loadStaffByID(staffID);
    }
    
    public ArrayList<Project> getAllProjectsByDepartment(Department department) {
        String query = dataStoringService.createProjectQueryStatement(department);
        return dataStoringService.loadSelectedProjects(query);
    }
    
    public ArrayList<Project> getAllProjectsByStaffID(int staffID) {
        String query = dataStoringService.createProjectQueryStatement(staffID);
        return dataStoringService.loadSelectedProjects(query);
    }

    public Project getProjetByProjectID(int projectID) {
        String query = dataStoringService.createSelectProjectQueryStatement(projectID);
        ArrayList<Project> projects = dataStoringService.loadSelectedProjects(query);
        if(!projects.isEmpty() && projects!= null) { return projects.get(0); }
        else { return null; }
    }

    public ArrayList<Project> getOngoingProjectsByDepartment(Department department) {
        String query = dataStoringService.createOngoingProjQueryStatement(department);
        return dataStoringService.loadSelectedProjects(query);
    }
    
    public ArrayList<Project> getOngoingProjectsByStaffID(int staffID) {
        String query = dataStoringService.createOngoingProjQueryStatement(staffID);
        return dataStoringService.loadSelectedProjects(query);
    }

    public ProjectTask getTaskByTaskID(int taskID) {
        String query = dataStoringService.createSelectTaskQueryStatement(taskID);
        ArrayList<ProjectTask> tasks = dataStoringService.loadConstructionProjTasks(query);
        if(!tasks.isEmpty() && tasks!= null) { return tasks.get(0); }
        else { return null; }
    }
}
