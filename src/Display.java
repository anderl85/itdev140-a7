import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Display {
	private JFrame frame;
	private JOptionPane menu;
	private JDialog menuDialog;
	private String title;

	//constructors, no default yet
	public Display(String[] assignments, String t){
		JFrame.setDefaultLookAndFeelDecorated(true);
		title = t;
		//create frame, set location & close operation
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		//top left display 2: -1920, 4
		//may in future do this right with some graphicsConfiguration things
		frame.setLocation(250,800);

		//create the menu
		createMenu(assignments);
	}

	private void createMenu(String[] assignments){
		//create menu joptionpane with array passed to constructor
		menu = new JOptionPane("Challenges:", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, assignments);
	
		frame.setVisible(true);	//as parent, best choice for less breaking chance

		//create jdialog and give joptionpane
		menuDialog = new JDialog(frame, title, true);
		menuDialog.setContentPane(menu);

		//grabbed from docs, not entirely sure whats going on
		menu.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				String prop = event.getPropertyName();

				if (menuDialog.isVisible() && event.getSource() == menu && prop.equals(JOptionPane.VALUE_PROPERTY)){
					menuDialog.setVisible(false);
				}
			}
		});
		
		menuDialog.setLocationRelativeTo(frame);	//necessary for some reason
		menuDialog.pack();

		 frame.setVisible(false);	//this is ugly rn, current setup is to always hide when possible
		frame.pack();	//this might help the ugly?
	}

	public String runMenu(){
		frame.setVisible(true);
		menuDialog.setVisible(true);
		frame.setVisible(false);
		return menu.getValue().toString();
	}

	public JFrame getFrame(){
		//currently unused, wasnt sure if i needed it, instead added more specific methods to keep frame internal
		frame.setVisible(true);
		return frame;
	}

	public void showMessage(String message){
		frame.setVisible(true);
		JOptionPane.showMessageDialog(frame, message, title, JOptionPane.PLAIN_MESSAGE);
		//since joptionpane is modal by default i should be able to do this:
		frame.setVisible(false);

		/*not entirely sure on hiding the frame like this but right now 
		*	things are pretty sketch so i dont want it visibly sketch.
		* lack of frame.pack may be a problem but idk
		*/
	}

	public int showYN(String message){
		int retVal;
		// frame.pack();
		frame.setVisible(true);
		retVal = JOptionPane.showConfirmDialog(frame, message, title, JOptionPane.YES_NO_OPTION);
		frame.setVisible(false);
		return retVal;
	}
	
	public void showError(String message){
		frame.setVisible(true);
		// frame.pack();
		JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
		frame.setVisible(false);
	}

	public String showInput(String message){
		return JOptionPane.showInputDialog(frame, message, title, JOptionPane.PLAIN_MESSAGE);
	}
	//dispose of frame (and therefore everything else here)
	//could in future make re-displayable via .pack(), but currently controls exit behavior
	public void finish(){
		frame.dispose();
	}
}