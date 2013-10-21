import lejos.nxt.*;
import lejos.nxt.ColorSensor;
import lejos.util.*;

public class Lab5 {

	public static void main(String[] args) {
		Button.ESCAPE.addButtonListener(new ExitListener());
		
		Button.waitForAnyPress();
		
		Configuration config = new Configuration();
		config.robot = new Robot(Motor.A, Motor.B);
		config.robot.setAcceleration(10000);
		
		config.odometer = new Odometer(config.robot);
		config.ultrasonicPoller = new UltrasonicPoller(SensorPort.S2);
		config.colorPoller = new ColorPoller(SensorPort.S3);
		
		config.maximumPoint = new Point(2 * 30, 2 * 30);	
		
		Controller[] controllers = new Controller[]{ 
			new GridController(config), 
			new ForwardController(config),
			new SearchController(config)
		};

		Operator operator = new Operator(controllers);
		operator.start();
		
		// localize
		
		/*
		LightSensor lightSensor = new LightSensor(SensorPort.S1);
		// UltrasonicLocalizer ultrasonicLocalizer = new UltrasonicLocalizer(robot, odometer, ultrasonicPoller);
		// LineLocalizer lineLocalizer = new LineLocalizer(robot, odometer, lightSensor);
		
		ultrasonicLocalizer.localize();
		while (ultrasonicLocalizer.isLocalizing()) halt();
		
		lineLocalizer.localize();
		while (lineLocalizer.isLocalizing()) halt();
				
		operator.start();
		operator.travelTo(new Point(0.0, 0.0));
		while (operator.isNavigating()) halt();
		
		Sound.twoBeeps();
		
		operator.rotateTo(0.0);
		while (operator.isNavigating()) halt();
		
		while (lightSensor.readNormalizedValue() > 400) {
			robot.setSpeeds(0.0, -Math.PI / 4);
			halt(5);
		}
		robot.setSpeeds(0.0, 0.0);
		odometer.setTheta(0.0);
		*/
		
		// find blocks
		
		// operator.start();
		// operator.wander();
		
		Button.waitForAnyPress();
	}
	
	private static void halt() {
		halt(1000);
	}
	
	private static void halt(int ms) {
		try { Thread.sleep(ms); } catch(InterruptedException e) {}
	}
}
