package finalProject;

import finalProject.enums.Department;

public class Administrator {
    static final int STAFF_ID = 0;
    static final String PASSSWORD = "Admin";
    static StaffAndProjectManagementService staffAndProjectManagementService = StaffAndProjectManagementService.getInstance();
    
    public static void createAndStoreStaff() {
        Employee employee1 = new Employee("11", "Alice", Department.ROAD_PROJECT);
        staffAndProjectManagementService.addStaff(employee1); //staffID is 1
        Employee employee2 = new Employee("12", "Bob", Department.ROAD_PROJECT);
        staffAndProjectManagementService.addStaff(employee2); //staffID is 2
        Employee employee3 = new Employee("13", "Charlie", Department.BUILDING_PROJECT);
        staffAndProjectManagementService.addStaff(employee3); //staffID is 3
        Employee employee4 = new Employee("14", "Diana", Department.FINANCIAL_DEPARTMENT);
        staffAndProjectManagementService.addStaff(employee4); //staffID is 4

        Manager manager1 = new Manager("21", "Ellie", Department.ROAD_PROJECT);
        staffAndProjectManagementService.addStaff(manager1); //staffID is 5
        Manager manager2 = new Manager("22", "Fred", Department.BUILDING_PROJECT);
        staffAndProjectManagementService.addStaff(manager2); //staffID is 6
        Manager manager3 = new Manager("23", "George", Department.FINANCIAL_DEPARTMENT);
        staffAndProjectManagementService.addStaff(manager3); //staffID is 7

        GeneralManager generalManager = new GeneralManager("31", "Helen");
        staffAndProjectManagementService.addStaff(generalManager); //staffID is 8
    }
}
