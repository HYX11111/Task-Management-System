package finalProject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import finalProject.enums.RiskCode;

//The class that assess the RiskCode for each Task	
public class RiskCodeAssessor {
    static private RiskCodeAssessor instance;

    public static RiskCodeAssessor getInstance() {
        if (instance == null) { instance = new RiskCodeAssessor(); }
        return instance;
    }

    public RiskCode getRiskCode(ProjectTask task) {
    	if(task.isCompleted == true || task.isAborted == true) {
    		return RiskCode.GREEN; 
    	} else {
			//The RiskCode is determined bases on the project value and overdue time
	        long delayDays = ChronoUnit.DAYS.between(task.getDueDate(), LocalDate.now());
	        double projectValue = ((ConstructionProjectTask)task).getProjectValue();
	       
			if(projectValue >= 5000000) {
		        if(delayDays >= 60) return RiskCode.RED;
		        else if(delayDays >= 30) return RiskCode.ORANGE;
		        else if(delayDays >= 0) return RiskCode.YELLOW;
		        else return RiskCode.GREEN;
		    } else {
		        if(delayDays >= 90) return RiskCode.RED;
		        else if(delayDays >= 60) return RiskCode.ORANGE;
		        else if(delayDays >= 0) return RiskCode.YELLOW;
		        else return RiskCode.GREEN;
		    }
    	}
    }
}
