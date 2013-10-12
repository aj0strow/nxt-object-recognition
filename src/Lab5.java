import lejos.nxt.*;

public class Lab5 {

	public static void main(String[] args) {
		Button.ESCAPE.addButtonListener(new ExitListener());
		
		Button.waitForAnyPress();
		
		// lab code here
		
		Button.waitForAnyPress();
	}
	
}
