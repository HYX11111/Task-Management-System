package finalProject;

import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import finalProject.enums.Department;

public class DataStoringService {
    private static final String DB_PATH = "jdbc:sqlite:programData.db";
    private static DataStoringService instance;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static DataStoringService getInstance() {
        if(instance == null) { instance = new DataStoringService(); }
        return instance; 
    }
    
    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_PATH);
    }

    //Method to create a table for Staff, a table for Projects and a table for ProjectTasks
    public void createTable() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            String staffTableQuery = "CREATE TABLE IF NOT EXISTS Staff ("
                + "staffID INTEGER PRIMARY KEY,"
                + "password TEXT NOT NULL,"
                + "staffName TEXT NOT NULL,"
                + "position TEXT NOT NULL,"
                + "department TEXT NOT NULL"
                + ");";
            statement.executeUpdate(staffTableQuery);
            
            String projectsTableQuery = "CREATE TABLE IF NOT EXISTS Projects ("
                + "projectID INTEGER PRIMARY KEY,"
                + "projectName TEXT NOT NULL,"
                + "department TEXT NOT NULL,"
                + "staffID INTEGER NOT NULL,"
                + "isCompleted INTEGER NOT NULL,"
                + "isAborted INTEGER NOT NULL"
                + ");";
            statement.executeUpdate(projectsTableQuery);

            String constructionProjTasksTableQuery = "CREATE TABLE IF NOT EXISTS ConstructionProjTasks ("
                + "taskID INTEGER PRIMARY KEY,"
                + "taskType TEXT NOT NULL,"
                + "taskName TEXT NOT NULL,"
                + "projectValue REAL NOT NULL,"
                + "dueDate TEXT NOT NULL,"
                + "isCompleted INTEGER NOT NULL,"
                + "isAborted INTEGER NOT NULL,"
                + "projectID INTEGER NOT NULL,"
                + "FOREIGN KEY (projectID) REFERENCES Projects(projectID)"
                + ");";
            statement.executeUpdate(constructionProjTasksTableQuery);
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    //Method to add Staff, and methods to add Projects and related Tasks to database
    public void addStaff(Staff staff) {
         try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(
        		 "INSERT OR IGNORE INTO Staff(staffID, password, staffName, position, department) VALUES (?, ?, ?, ?, ?)")) {
        	 statement.setInt(1, staff.staffID);
        	 statement.setString(2, staff.password);
             statement.setString(3, staff.staffName);
             statement.setString(4, staff.position);
             statement.setString(5, staff.department.toString());

            statement.executeUpdate();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
    
    public void addProject(Project project) {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(
        		"INSERT OR IGNORE INTO Projects (projectID, projectName, department, staffID, isCompleted, isAborted) VALUES (?, ?, ?, ?, ?, ?)")) {
        	statement.setInt(1, project.projectID);
            statement.setString(2, project.projectName);
            statement.setString(3, project.department.toString());
            statement.setInt(4, project.staffID);
            statement.setInt(5, project.isCompleted == true? 1 : 0);
            statement.setInt(6, project.isAborted == true? 1 : 0);

            statement.executeUpdate();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    public void addConstruacitonProjTask(ConstructionProjectTask task) {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(
        		"INSERT OR IGNORE INTO ConstructionProjTasks (taskID, taskType, taskName, projectValue, dueDate, isCompleted, isAborted, projectID) VALUES (?, ?, ?, ?, ?, ?, ?, ?);")) {
            statement.setInt(1, task.taskID);
            statement.setString(2, task.taskType);
            statement.setString(3, task.taskName);
            statement.setDouble(4, task.projectValue);
            statement.setString(5, task.dueDate.toString());
            statement.setInt(6, task.isCompleted == true? 1 : 0);
            statement.setInt(7, task.isAborted == true? 1 : 0);
            statement.setInt(8, task.projectID);

            statement.executeUpdate();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    //Methods to update Projects and Tasks status 
    public void updateProject(Project project) {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(
                "UPDATE Projects SET isCompleted = ?, isAborted =? WHERE projectID = ?")) {
            statement.setInt(1, project.isCompleted == true? 1 : 0);
            statement.setInt(2, project.isAborted == true? 1 : 0);
            statement.setInt(3, project.projectID);

            statement.executeUpdate();
         } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    public void updateConstruacitonProjTask(ConstructionProjectTask task) {
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(
                "UPDATE ConstructionProjTasks SET isCompleted = ?, isAborted = ? WHERE taskID = ?")) {
                statement.setInt(1, task.isCompleted == true? 1 : 0);
                statement.setInt(2, task.isAborted == true? 1 : 0);
                statement.setInt(3, task.taskID);

                statement.executeUpdate();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    //Methods to read from database
    //Method to read Staff from database
    Staff loadStaffByID(int staffID) {
        Staff staff = null;
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(
        		"SELECT * FROM Staff WHERE staffID = ?")) {
                statement.setInt(1, staffID);
                
                try(ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                    	String password = resultSet.getString("password");
                        String staffName = resultSet.getString("staffName");
                        String position = resultSet.getString("position");
                        Department department = Department.fromString(resultSet.getString("department"));

                        switch(position) {
                            case "Employee":
                                staff = new Employee(password, staffName, department);
                                break;	
                            case "Manager":
                                staff = new Manager(password, staffName, department);
                                break;
                            case "General Manager":
                                staff = new GeneralManager(password, staffName);
                                break;
                        }
                        staff.staffID = staffID;
                        //Set the staffCounter by minus one, since this method restores information on an existing Staff from databse instead of "creating" a new Staff
                        Staff.staffCounter--;
                    }
                    return staff;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
    }

    //Methods to read Projects and related Tasks from database that meet various criteria
    //Methods to create querry based on different selection criteria
    String createProjectQueryStatement(Department department) {
        return "SELECT * FROM Projects WHERE department = '" + department.toString() + "'";
    }

    String createProjectQueryStatement(int staffID) {
        return "SELECT * FROM Projects WHERE staffID = " + staffID;
    }

    String createSelectProjectQueryStatement(int projectID) {
        return "SELECT * FROM Projects WHERE projectID = " + projectID;
    }

    String createOngoingProjQueryStatement(Department department) {
        return "SELECT * FROM Projects WHERE isCompleted = 0 AND isAborted = 0 AND department = '" + department.toString() + "'";
    }

    String createOngoingProjQueryStatement(int staffID) {
        return "SELECT * FROM Projects WHERE isCompleted = 0 AND isAborted = 0 AND staffID = " + staffID;
    }
    //Methods to read Projects and related Tasks from database with the query created
    ArrayList<Project> loadSelectedProjects(String query) {
        ArrayList<Project> projects = new ArrayList<>();

        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)){
                try(ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String projectName = resultSet.getString("projectName");
                        Department department = Department.fromString(resultSet.getString("department"));
                        int staffID = resultSet.getInt("staffID");

                        Project project = new Project(projectName, department, staffID);
                        Project.projectCounter--;
                        project.projectID = resultSet.getInt("projectID");
                        project.isCompleted = resultSet.getInt("isCompleted") == 1 ? true : false; 
                        project.isAborted = resultSet.getInt("isAborted") == 1 ? true : false; 
                        
                        ArrayList<ProjectTask> tasks = loadConstructionProjTasks(createTaskQueryStatement(project.projectID));
                        for(ProjectTask task : tasks) { project.addTask(task); }

                        projects.add(project);
                    }
                    return projects;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
    }

    String createTaskQueryStatement(int projectID) {
        return "SELECT * FROM ConstructionProjTasks WHERE projectID = " + projectID;
    }

    String createSelectTaskQueryStatement(int taskID) {
        return "SELECT * FROM ConstructionProjTasks WHERE taskID = " + taskID;
    }

    ArrayList<ProjectTask> loadConstructionProjTasks(String query) {
        ArrayList<ProjectTask> tasks = new ArrayList<ProjectTask>();
       
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(query)){
                try(ResultSet resultSet = statement.executeQuery()) {
                    while(resultSet.next()) {
                        String taskName = resultSet.getString("taskName");
                        Double projectValue = resultSet.getDouble("projectValue");
                        LocalDate dueDate = LocalDate.parse(resultSet.getString("DueDate"), dateFormatter);
                        int projectID = resultSet.getInt("projectID");

                        ConstructionProjectTask task = new ConstructionProjectTask(projectID, taskName, projectValue, dueDate);
                        task.taskID = resultSet.getInt("taskID");
                        ProjectTask.taskCounter--;
                        task.taskType = resultSet.getString("taskType");
                        task.isCompleted = resultSet.getInt("isCompleted") == 1 ? true : false; 
                        task.isAborted = resultSet.getInt("isAborted") == 1 ? true : false; 

                        tasks.add(task);
                    }
                    return tasks;
                }
    	} catch (SQLException exc) {
            exc.printStackTrace();
            return null;
        }
    }

    //Method to get counters so that the counters can be updated when the program is started every time 
    public HashMap<String, Integer> getCounters() {
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery("SELECT MAX(staffID) FROM Staff")) {
                int staffCounter = resultSet1.next() ? resultSet1.getInt(1) : 0;

                try (ResultSet resultSet2 = statement.executeQuery("SELECT MAX(projectID) FROM Projects")) {
                    int projectCounter = resultSet2.next() ? resultSet2.getInt(1) : 0;
                    
                    try (ResultSet resultSet3 = statement.executeQuery("SELECT MAX(taskID) FROM ConstructionProjTasks")) {
                        int taskCounter = resultSet3.next() ? resultSet3.getInt(1) : 0;
                        
                        HashMap<String, Integer> counters = new HashMap<>();
                        counters.put("staffCounter", staffCounter);
                        counters.put("projectCounter", projectCounter);
                        counters.put("taskCounter", taskCounter);
                        
                        return counters;
                    }
                }
        } catch (SQLException exc) {
            exc.printStackTrace();
            return null;
        }
    }
}

 