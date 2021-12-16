public class InvalidEmployeeNumber extends Exception {
	
	/**
	@exception InvalidEmployeeNumber 
	when idNumber is not in scheme ddd-c 
	or c is outside of valid range A-M
	*/
	public InvalidEmployeeNumber() {
		super("Error: invalid employee number");
	}

	public InvalidEmployeeNumber(String numb) {
		super("Error: invalid employee number: "  + numb);
	}
}
