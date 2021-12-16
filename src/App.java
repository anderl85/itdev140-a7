import java.sql.SQLDataException;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.JOptionPane;

public class App {
	public static Display display;
	public static void main(String[] args) throws Exception {
		///*
		String[] menu = {"Existing Employee", "New Employee", "New Employee, Hired Today"};
		int control;
		String choice;
	
		display = new Display(menu, "ITDEV140");
		do {

			choice = display.runMenu();
			switch (choice){
				case "Existing Employee":
					existing();
					break;
				case "New Employee":
					newEmployee();
					break;
				case "New Employee, Hired Today":
					hiredToday();
					break;
			}

			control = display.showYN("Done?");
		} while (control == JOptionPane.NO_OPTION);

		display.finish();
		// */
		
	}

	public static void employeeMenu(Employee employee){
		String[] options = {"Finish", "Change name", "Change hire date", 
												"Change id number", "Remove employee"};
		String format = "Employee Info: %s | %s | %s%n%n%s";
		String menuPrompt = "What would you like to do?";
		String commitPrompt = "Would you like to submit this record to the database?";

		int control = 1;
		int deleted = 0;

		String message;
		String choice;
		int commit;

		//loop menu until user says finish or deletes employee from db
		do {
			message = String.format(format, employee.getName(), employee.getIdNumber(), 
															employee.getHireDateFormatted(), menuPrompt);

			//get user choice 
			choice = display.showInputLimited(message, options);

			switch (choice) {
				case "Finish":
					control = 0;
					break;

				case "Change name":
					updateName(employee);
					break;

				case "Change hire date":
					invalidOption();
					break;
					
				case "Change id number":
					invalidOption();
					break;

				case "Remove employee":
					if (removeEmployee(employee) == 1) {
						deleted = 1;
						control = 0;
					}
					break;
			}

		} while (control == 1);

		//if record was not deleted ask if user wants to commit to db
		if (deleted != 1){
			message = String.format(format, employee.getName(), employee.getIdNumber(), 
															employee.getHireDateFormatted(), commitPrompt);
	    
			//get user decision
			commit = display.showYN(message);
			
			//if user answers yes, attempts to commit to db
			if (commit == JOptionPane.YES_OPTION)
				commitToDatabase(employee);
			//failure to commit is not fixable
		}

	}

	//menu options
	public static void invalidOption(){
		display.showError("That cannot be done, please contact your system admin.");
	}

	public static int removeEmployee(Employee employee){
		int retval = 0;	

		//verify if certain on action
		int choice = display.showYN("Are you sure you wish to remove this employee?");

		if (choice == JOptionPane.YES_OPTION){
			int result = 0;

			//attempt to remove employee
			try {

				result = employee.removeEmployee();

			} catch (SQLException e) {

				display.showError("Something went wrong.\n\n" + e.getMessage());

			}

			//check int result from method
			if (result != 1){
				//displays if attempting to delete a non-existent record
				display.showError("Something has gone wrong. " + result + " entries affected.");
			} else {
				retval = 1;
			}

		}

		//return 1 if everything went right
		return retval;
	}

	public static void updateName(Employee employee){
		String input = display.showInputOpen("Enter new name.");
		employee.setName(input);
	}

	public static void commitToDatabase(Employee employee){
		int result = 0;

		//attempt to send to database
		//there is no fixing failure, just an error message

		try {

			result = employee.sendToDatabase();

		} catch (SQLDataException e){

			display.showError("Cannot submit an employee to the database without a name.");

		} catch (SQLException ex) {

			display.showError("Something went wrong.\n\n" + ex.getMessage());

		}

		//check int result from method just in case something really wonky happened
		//should also report for less wonky errors
		if (result != 1){
			display.showError("Something has gone wrong. " + result + " entries affected.");
		}

	}

	//existing employee
	public static void existing(){
		Employee employee = findEmployee();
		 
		//move to employee menu
		if (employee != null)
			employeeMenu(employee);
	}

	public static Employee findEmployee(){
		String input;
		Employee employee = null;
		int control;

		//get id from user, attempt to locate, loop until user quit
		do{ 
			input = display.showInputOpen("Enter employee number:");

			control = 999;
			try {

				employee = new Employee(input);

			} catch (InvalidEmployeeNumber e){

				display.showError("Input is not a valid id number.");
				control = display.showYN("Try again?");

			} catch (SQLException ex) {

				display.showError("Employee does not exist.");
				control = display.showYN("Try again?");

			}

		} while (control == JOptionPane.YES_OPTION);

		return employee;
	}

	//new employee
	public static void newEmployee(){
		String name;
		String date;
		String id;

		int control;
		int controlTwo;

		Employee employee = null;
		LocalDate properDate = null;

		//get name
		name = display.showInputOpen("Enter employee's name:");

		//get date, loop until success
		do {
			date = display.showInputOpen("Enter employee's hire date. YYYY-MM-DD");
			date = date.trim();

			//replace spaces with dashes
			if (date.charAt(4) == ' '){
				String fixedDate = date.substring(0, 4);
				fixedDate += "-" + date.substring(5, 7);
				fixedDate += "-" + date.substring(8);
				date = fixedDate;
			}

			control = 0;

			//attempt to parse date
			try {
				properDate = LocalDate.parse(date);
			} catch (Exception e) {
				display.showError("Not a valid date.");
				control = 1;
			}

		} while (control == 1);

		//get id, try to make employee, loop until success
		do{
			id = display.showInputOpen("Enter employee number. ###-L");

			controlTwo = 0;

			try{
				employee = new Employee(name, id, properDate);
			} catch (InvalidEmployeeNumber e){
				display.showError("Not a valid employee number.");
				controlTwo = 1;
			}

		} while (controlTwo == 1);

		//move to employee menu
		if (employee != null)
			employeeMenu(employee);
	
	}

	//new employee, default constructor
	public static void hiredToday(){
		//create employee
		Employee employee = new Employee();
		//go to employee menu
		employeeMenu(employee);
	}

}
