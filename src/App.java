import javax.swing.JOptionPane;

public class App {
		static Display display;
    public static void main(String[] args) throws Exception {
			int cont;
			String[] assignments = {"times", "pali"};
			//init display
			display = new Display(assignments, "ITDEV140");

			do{
				String choice = display.runMenu();	//run menu, switch on value
				switch (choice){
					case "times":
						showTimes();
						break;
					case "pali":
						showPalindrome();
						break;
				}
				//ask to continue
				cont = display.showYN("Done?");
			} while (cont == JOptionPane.NO_OPTION);
			
			//close display, ending program
			display.finish();
    }

		public static int times(int control, int base){
			//check for negative control value, swap second value sign
			if (control < 0) {
				control = Math.abs(control);
				base = -base;
			}
		
			//check for 0 value (base is also checked because multiplying by 0 is 0)
			if (control == 0 || base == 0)
				return 0;
			//do the math
			else
				return base + times(control - 1, base);

		}

		public static void showTimes(){
			int control;
			int valOne;
			int valTwo;
			int product;
			String input;
			String[] splitInput;
			String message;
			String format = "%d times %d is %d.%n%nGo again?";

			do {
				//get input
				input = display.showInput("Enter 2 numbers separated by a space. Please don't enter an invalid input it will break.");
				//turn input usable
				splitInput = input.split(" ");
				valOne = Integer.parseInt(splitInput[0]);
				valTwo = Integer.parseInt(splitInput[1]);

				//math
				product = times(valOne, valTwo);

				//display result and ask to repeat
				message = String.format(format, valOne, valTwo, product);
				control = display.showYN(message);

			} while (control == JOptionPane.YES_OPTION);

		}
		
		public static boolean isPalindrome(String check){
			//most palindromes are odd lettered and even palindromes will end up making check empty
			if (check.length() == 1 || check.length() == 0)
				return true;
			else { 
				//check first and last values
				if (check.charAt(0) == check.charAt(check.length() - 1))
					return isPalindrome(check.substring(1, check.length() - 1));	//trim the first and last chars and forward
				else
					return false;
			}
		}

		public static void showPalindrome(){
			String input;
			int control;
			StringBuilder sb;
			boolean palindrome;
			String formatSuccess = "Congratulations, %s is a palindrome!%n%nGo again?";
			String formatFail = "Unfortunately, %s is not a palindrome!%n%nGo again?";

			do{
				//get input
				input = display.showInput("Enter a potential palindrome");

				//get rid of non-alphabet characters, and unify case
				sb = new StringBuilder(input.trim().toUpperCase());
				int i = 0;
				while (i < sb.length()){
					if (sb.charAt(i) < 65 || sb.charAt(i) > 90){
						sb.deleteCharAt(i);
						sb.trimToSize();
					} else {
						i++;
					}
				}
				//call method
				palindrome = isPalindrome(sb.toString());

				//display result, get continue or not
				if (palindrome)
					control = display.showYN(String.format(formatSuccess, input));
				else 
					control = display.showYN(String.format(formatFail, input));
				
			}	while (control == JOptionPane.YES_OPTION);

		}
	}
