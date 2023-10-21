package finalProject;

import java.util.ArrayList;
import finalProject.enums.Department;

class Project {
    static int projectCounter = 0; 

    int projectID;
    String projectName;
    ArrayList<ProjectTask> tasks;
    Department department;
    int staffID;
    boolean isCompleted;
    boolean isAborted;   

    public Project(String projectName, Department department, int staffID) {
        this.projectID = ++projectCounter;
        this.projectName = projectName;
        tasks = new ArrayList<ProjectTask>();
        this.department = department;
        this.staffID = staffID;
        isCompleted = false;
        isAborted = false;
    }

    public static void setProjectCounter(int projectCounter) {
        Project.projectCounter = projectCounter;
    }

    public void addTask(ProjectTask task) {
       tasks.add(task);
    }

    public String markAsCompleted() {
        //If any of the Task is not completed and not aborted, the Project cannot be marked as completed
        for(ProjectTask task : tasks) {
            if(task.isCompleted == false && task.isAborted == false) {
                return "There are still pending tasks. Cannot mark the project as completed.";
            }
        }
        isCompleted = true; 
        return "The project is completed!";
    }

    public String markAsAborted() {
        isAborted = true;
        //If a Project is aborted, all of its Tasks will also be marked as aborted 
        for(ProjectTask task : tasks) { 
            task.markAsAborted();
        }
        return "The project is aborted.";
    }

    @Override
    public String toString() { 
        StringBuilder sb = new StringBuilder();
        sb.append("Project ID: " + projectID + "\nProject Name: " + projectName + "\nDepartment: " + department + "\nResponsible staff ID: " + + staffID + "\nStatus - is completed? " + isCompleted + "\nStatus - is aborted? " + isAborted  +"\nThis project includes the following tasks:\n");
        
        for(ProjectTask task: tasks) { 
            sb.append(task.toString()); 
        }
        return sb.toString();
    }
}

