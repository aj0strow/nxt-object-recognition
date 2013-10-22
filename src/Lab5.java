import lejos.nxt.*;
import lejos.nxt.SensorPort;
import lejos.nxt.ColorSensor;
import lejos.util.*;

public class Lab5 {

	public static void main(String[] args) {
		Button.ESCAPE.addButtonListener(new ExitListener());
		
		Button.waitForAnyPress();
		
		// configure
		
		Configuration config = new Configuration();
				
		config.robot = new Robot(Motor.A, Motor.B);
		config.robot.setAcceleration(10000);
				
		config.odometer = new Odometer(config.robot);
				
		config.ultrasonicPoller = new UltrasonicPoller(SensorPort.S2);
				
		config.colorPoller = new ColorPoller(SensorPort.S4);
						
		config.maximumPoint = new Point(4 * 30, 8 * 30);
		
		Controller[] controllers = new Controller[]{
			new GridController(config),
			new CollisionController(config),
			new SearchController(config),
			new PathController(config)
		};

		Operator operator = new Operator(controllers);
		operator.start();
				
		
		// localize
				
		/*
		UltrasonicLocalizer ultrasonicLocalizer = new UltrasonicLocalizer(config.robot, 
				config.odometer, config.ultrasonicPoller);
		LightSensor lightSensor = new LightSensor(SensorPort.S1);
		LineLocalizer lineLocalizer = new LineLocalizer(config.robot, config.odometer, lightSensor);
		
		ultrasonicLocalizer.localize();
		while (ultrasonicLocalizer.isLocalizing()) halt();
		
		lineLocalizer.localize();
		while (lineLocalizer.isLocalizing()) halt();
		
		Sound.twoBeeps();
		
		path.push(new Point(0.0, 0.0));
		operator.start();

		
		
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
