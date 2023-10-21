package finalProject.enums;

public enum Department {
    ROAD_PROJECT, BUILDING_PROJECT, FINANCIAL_DEPARTMENT, OTHER;

    public static Department fromString(String str) {
        return Department.valueOf(str);
    }
}