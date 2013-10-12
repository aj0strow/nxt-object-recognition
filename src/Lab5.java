import lejos.nxt.*;
import lejos.nxt.ColorSensor;
import lejos.util.*;

public class Lab5 {

	public static void main(String[] args) {
		Button.ESCAPE.addButtonListener(new ExitListener());
		
		Button.waitForAnyPress();
		
		Robot robot = new Robot(Motor.A, Motor.B);
		robot.setAcceleration(10000);
		
		Odometer odometer = new Odometer(robot);
		UltrasonicPoller ultrasonicPoller = new UltrasonicPoller(SensorPort.S2);
		
		UltrasonicLocalizer ultrasonicLocalizer = new UltrasonicLocalizer(robot, odometer, ultrasonicPoller);
		Operator operator = new Operator(robot, odometer, ultrasonicPoller);
		
		// localize
		
		ultrasonicLocalizer.localize();
		
		while (ultrasonicLocalizer.isLocalizing()) {
			try { Thread.sleep(1000); } catch(InterruptedException e) {}
		}
				
		Button.waitForAnyPress();
	}
	
}
