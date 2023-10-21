package finalProject;

import java.time.LocalDate;
import finalProject.enums.Department;

//Class to create and store test Projects and Tasks
public class TestProjects {
    StaffAndProjectManagementService staffAndProjectManagementService = StaffAndProjectManagementService.getInstance();
    
    public void createTestProjects() {
        //Department.ROAD_PROJECT
        Project project1 = new Project("Road Project 1", Department.ROAD_PROJECT, 1); //ProjectID: 1
        ProjectTask task1 = new ConstructionProjectTask(1, "Road Part I", 1000000, LocalDate.of(2023, 1, 1)); //TaskID: 1, RiskCode: RED
        ProjectTask task2 = new ConstructionProjectTask(1, "Road Part II", 700000, LocalDate.of(2023, 7, 1));  //TaskID: 2, RickCode: GREEN (Completed)
        project1.addTask(task1);
        project1.addTask(task2);
        staffAndProjectManagementService.addProjectAndTasks(project1);

        task2.markAsCompleted();
        staffAndProjectManagementService.updateTask(task2);


        Project project2 = new Project("Road Project 2", Department.ROAD_PROJECT, 1); //ProjectID: 2
        ProjectTask task3 = new ConstructionProjectTask(2, "Road Maintainance", 1500000.99, LocalDate.of(2022,1,1)); //TaskID: 3, RiskCode: GREEN (Project Aborted)
        project2.addTask(task3);
        staffAndProjectManagementService.addProjectAndTasks(project2);
        
        project2.markAsAborted();
        staffAndProjectManagementService.updateProject(project2);


        Project project3 = new Project("Road Project 3", Department.ROAD_PROJECT, 2); //ProjectID: 3
        ProjectTask task4 = new ConstructionProjectTask(3, "New Road Part I", 211111111, LocalDate.of(2022,5,1)); //TaskID: 4, RiskCode: GREEN (Completed & Project Aborted)
        ProjectTask task5 = new ConstructionProjectTask(3, "New Road Part II", 500000, LocalDate.of(2022,7,1)); //TaskID: 5, RiskCode: GREEN (Project Aborted)
        project3.addTask(task4);
        project3.addTask(task5);
        staffAndProjectManagementService.addProjectAndTasks(project3);

        project3.markAsAborted();
        staffAndProjectManagementService.updateProject(project3);


        //Department.BUILDING_PROJECT
        Project project4 = new Project("Building Project 1", Department.BUILDING_PROJECT, 3); //ProjectID: 4
        ProjectTask task6 = new ConstructionProjectTask(4, "Building No.1", 777777, LocalDate.of(2021, 10, 22)); //TaskID: 6, RiskCode: GREEN (Task Aborted)
        ProjectTask task7 = new ConstructionProjectTask(4, "Building No.2", 666666, LocalDate.of(2023,6,1)); //TaskID: 7, RiskCode: ORANGE
        ProjectTask task8 = new ConstructionProjectTask(4, "Building No.3", 1600000.11, LocalDate.of(2023,7,1)); //TaskID: 8, RiskCode: YELLOW
        ProjectTask task9 = new ConstructionProjectTask(4, "Building No.4", 5555555, LocalDate.of(2023,8,1)); //TaskID: 9, RiskCode: YELLOW
        ProjectTask task10 = new ConstructionProjectTask(4, "Building No.5", 6000000, LocalDate.of(2023,11,15)); //TaskID: 10, RiskCode: GREEN (Not Due)
        project4.addTask(task6);
        project4.addTask(task7);
        project4.addTask(task8);
        project4.addTask(task9);
        project4.addTask(task10);
        staffAndProjectManagementService.addProjectAndTasks(project4);

        task6.markAsAborted();
        staffAndProjectManagementService.updateTask(task6);


        //Department.FINANCIAL_DEPARTMENT
        Project project5 = new Project("Payment 1", Department.FINANCIAL_DEPARTMENT, 4); //ProjectID: 5
        ProjectTask task11 = new ConstructionProjectTask(5, "Payment to A", 7654321, LocalDate.of(2024,1,1)); //TaskID: 11, RiskCode: GREEN (Not due)
        project5.addTask(task11);
        staffAndProjectManagementService.addProjectAndTasks(project5);


        Project project6 = new Project("Payment 2", Department.FINANCIAL_DEPARTMENT, 4); //ProjectID: 6
        ProjectTask task12 = new ConstructionProjectTask(6, "Payment to B1", 5500000, LocalDate.of(2023, 7, 1)); //TaskID: 12, RiskCode: ORANGE
        ProjectTask task13 = new ConstructionProjectTask(6, "Payment to B2", 1900000, LocalDate.of(2023,7,15)); //TaskID: 13, RiskCode: YELLOW
        project6.addTask(task12);
        project6.addTask(task13);
        staffAndProjectManagementService.addProjectAndTasks(project6);


        Project project7 = new Project("Payment 3", Department.FINANCIAL_DEPARTMENT, 4); //ProjectID: 7
        ProjectTask task14 = new ConstructionProjectTask(7, "Payment to C1", 600000.19, LocalDate.of(2023, 8, 1)); //TaskID: 14, RiskCode: YELLOW
        ProjectTask task15 = new ConstructionProjectTask(7, "Payment to C2", 150000, LocalDate.of(2023,8,1)); //TaskID: 15, RiskCode: GREEN (Completed)
        ProjectTask task16 = new ConstructionProjectTask(7, "Payment to C3", 7000000, LocalDate.of(2023,8,8)); //TaskID: 16, RiskCode: YELLOW
        project7.addTask(task14);
        project7.addTask(task15);
        project7.addTask(task16);
        staffAndProjectManagementService.addProjectAndTasks(project7);

        task15.markAsCompleted();
        staffAndProjectManagementService.updateTask(task15);


        Project project8 = new Project("Payment 4", Department.FINANCIAL_DEPARTMENT, 4); //ProjectID: 8
        ProjectTask task17 = new ConstructionProjectTask(8, "Payment to D", 1700000, LocalDate.of(2023, 7, 10)); //TaskID: 17, RiskCode: GREEN (Completed)
        project8.addTask(task17);
        staffAndProjectManagementService.addProjectAndTasks(project8);

        task17.markAsCompleted();
        project8.markAsCompleted();
        staffAndProjectManagementService.updateProject(project8);
    }
}
