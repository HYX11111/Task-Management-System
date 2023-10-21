package finalProject;

import javax.swing.SwingUtilities;

public class Main {
	private static DataStoringService dataStoringService = DataStoringService.getInstance();
	private static StaffAndProjectManagementService staffAndProjectManagementService = StaffAndProjectManagementService.getInstance();
	
	public static void main(String[] args) {
		//Launch the main user interface
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UI0LoginPage loginPage = new UI0LoginPage();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		//To initialize the tables for the database
		dataStoringService.createTable();
		//To create Staff and store Staff information in the database
		Administrator.createAndStoreStaff();
		//To update counters when the program is started everytime
		staffAndProjectManagementService.updateCounters();

			
		//To create and load Projects for testing, uncomment the following two lines 
		/* 
		TestProjects testPrjoects = new TestPrjoects();
		testProjects.createTestProjects();
		*/
	}
}
