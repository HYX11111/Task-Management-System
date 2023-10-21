package finalProject;

import java.time.LocalDate;

public abstract class ProjectTask {
    static int taskCounter = 0; 

    int projectID;
    int taskID;
    String taskType;
    String taskName; 
    LocalDate dueDate;
    boolean isCompleted;
    boolean isAborted;

    public static void setTaskCounter(int taskCounter) { ProjectTask.taskCounter = taskCounter;}
    public LocalDate getDueDate() { return dueDate; }

    public String markAsCompleted() { 
        this.isCompleted = true;
        return "Task completed!";
    }

    public String markAsAborted() {
        this.isAborted = true;
        return "The task is aborted.";
    }
}


class ConstructionProjectTask extends ProjectTask {
    double projectValue;

    public ConstructionProjectTask (int projectID, String taskName, double projectValue, LocalDate dueDate) {
        this.projectID = projectID;
        this.taskID = ++taskCounter; 
        this.taskType = "Construction Project Task";
        this.taskName = taskName;
        this.projectValue = projectValue;
        this.dueDate = dueDate;
        this.isCompleted = false;
        this.isAborted = false;
    }

    public double getProjectValue() { return projectValue; }

    @Override
    public String toString() { 
        return "Project ID: " + projectID + "\nTask ID: " + taskID + "\nType of Task: " + taskType + "\nTask Name: " + taskName + "\nProject Value: " + projectValue + "\nDue Date: " + dueDate + "\nStatus - Is Completed: " + isCompleted + "\nStatus - Is Aborted: " + isAborted + "\n";
    }
}

