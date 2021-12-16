import java.sql.SQLDataException;
import java.sql.SQLException;
import java.time.LocalDate;

public class Employee {
	private String name;
	private String idNumber;
	private LocalDate hireDate;
	private boolean existing;

	//constructors
	//create obj with inputs
	public Employee (String aName, String id, LocalDate date) throws InvalidEmployeeNumber{
		id = id.trim().toUpperCase();		//ensure basic problem doesn't throw format error
		
		//check value, throw error if needed, else populate
		if (!isValidId(id)) 
			throw new InvalidEmployeeNumber(id);
		else
			idNumber = id;

		//populate other fields
		name = aName;
		hireDate = date;
		existing = false;
	}

	//create obj from database
	public Employee(String id) throws InvalidEmployeeNumber, SQLException{
		id = id.trim().toUpperCase();
		if (!isValidId(id))
			throw new InvalidEmployeeNumber(id);

		Database db = new Database();
		String[] results = db.queryById(id);

		if (results.length != 2){
			//tbh i dont think this is reachable but in case it is
			System.out.println("The employee with id " + id + " does not exist.");
		} else {
			name = results[0];
			hireDate = LocalDate.parse(results[1]);
			idNumber = id;
			existing = true;
		}
	}

	//default
	public Employee() {
		Database db = new Database();
		String lastId = db.getLastId();
		String id;

		//increment based on largest id
		if (lastId.charAt(4) + 1 < 78){
			id = lastId.substring(0, 4);
			char letter = lastId.charAt(4);
			letter++;	//cries if this is done in one line
			id += letter;
		} else {
			int numb = Integer.parseInt(lastId.substring(0, 3));
			numb++;
			id = numb + "-A";
		}

		//set fields
		name = "";
		idNumber = id;
		hireDate = LocalDate.now();
		existing = false;
	}

	//getters
	public String getName() {
		return name;
	}

	public String getIdNumber(){
		return idNumber;
	}

	public LocalDate getHireDate(){
		return hireDate;
	}

	public String getHireDateFormatted(){
		return String.format("%te %tB, %tY", hireDate, hireDate, hireDate);
	}

	//setters
	public void setName(String name){
		this.name = name;
	}
	
	//methods
	private boolean isValidId(String id){
		boolean retVal;

		//check matches format first by character count
		//then checking individual character values by index
		if (id.length() != 5)
			retVal = false;
		else if (id.charAt(3) != '-')
			retVal = false;
		else if (id.charAt(0) < 48 || id.charAt(0) > 57)	//number
			retVal = false;
		else if (id.charAt(1) < 48 || id.charAt(1) > 57)	//number
			retVal = false;
		else if (id.charAt(2) < 48 || id.charAt(2) > 57)	//number
			retVal = false;
		else if (id.charAt(4) < 65 || id.charAt(4) > 77)	//letter, capitals a thru m
			retVal = false;
		else 
			retVal = true;

		return retVal; 
	}

	public int sendToDatabase() throws SQLException{
		Database db = new Database();
		int success = 0;

		//checks existing value to determine if update or insert
		if (existing){
			success = db.updateById(idNumber, name, hireDate.toString());
		} else {
			//stop empty name from being sent to database
			//if someone updating the record deletes the name they can take a nondescript error
			if (name != "") {
				success = db.insert(idNumber, name, hireDate.toString());
			} else {
				throw new SQLDataException();
			}
		}
		
		return success;	//number of records affected
	}

	public int removeEmployee() throws SQLException{
		int success;

		//does not attempt if the employee is not in the database
		if (!existing){
			success = 0;
		} else {
			Database db = new Database();
			success = db.deleteById(idNumber);
		}

		return success;		//number of records affected
	}
}
