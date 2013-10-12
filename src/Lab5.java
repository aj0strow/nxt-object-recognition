import lejos.nxt.*;
import lejos.nxt.ColorSensor;
import lejos.util.*;

public class Lab5 {

	public static void main(String[] args) {
		Button.ESCAPE.addButtonListener(new ExitListener());
		
		Button.waitForAnyPress();
		
		Robot robot = new Robot(Motor.A, Motor.B);
		Operator operator = new Operator(robot);
		
		robot.setAcceleration(10000);
		
		operator.travelTo(new Point(25, 25));
		try { Thread.sleep(12000); } catch(InterruptedException e) {}
		
		Button.waitForAnyPress();
	}
	
}
