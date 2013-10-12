import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.nxt.SensorPort;

import lejos.nxt.LCD;


public class Operator implements TimerListener {
	private static final int PERIOD = 5;
	
	private static final double TRAVEL_SPEED = 4.0;
	private static final double ROTATE_SPEED = Math.PI / 8;
	
	private Robot robot;
	private Odometer odometer;
	private Position position;
	
	private UltrasonicPoller ultrasonicPoller;
	
	private double angle = Double.NaN;
	private double angleDifference;
	
	private Point point = null;
	private double distance;
	

	public Operator(Robot robot, SensorPort sensorPort) {
		this.robot = robot;
		
		this.odometer = new Odometer(robot);
		this.position = odometer.getPosition();
		
		this.ultrasonicPoller = new UltrasonicPoller(sensorPort);
		
		(new Timer(PERIOD, this)).start();
	}
	
	public void timedOut() {
		this.position = odometer.getPosition();
		
		if (isRotating()) {
			if (rotated()) stopRotating();
			else rotate();
		} else if (isTravelling()) {
			if (travelled()) stopTravelling();
			else travel();
		} else {
			robot.setSpeeds(0.0, 0.0);
		}
		LCD.drawString("up: " + ultrasonicPoller.getDistance(), 0, 3);
	}
	
	public void travelTo(Point point) {
		this.angle = position.angleTo(point);
		this.point = point.clone();
	}
	
	private void travel() {
		double rotateSpeed = 0.0;
		double radians = Angle.difference(position.theta, position.angleTo(point));
		robot.setSpeeds(TRAVEL_SPEED, radians);
	}
	
	private void stopTravelling() {
		robot.setSpeeds(0.0, 0.0);
		this.point = null;
	}
	
	private boolean travelled() {
		double newDistance = position.distanceTo(point);
		
		boolean isClose = newDistance < 2.0;
		boolean worsened = distance <= newDistance;
		
		this.distance = newDistance;
		
		return isClose && worsened;
	}
	
	private boolean isTravelling() {
		return point != null;
	}
	
	public void rotateTo(double angle) {
		this.angle = angle;
	}
	
	private void rotate() {
		robot.setSpeeds(0.0, Angle.direction(position.theta, angle) * ROTATE_SPEED);
	}
	
	private void stopRotating() {
		robot.setSpeeds(0.0, 0.0);
		this.angle = Double.NaN;
	}
	
	private boolean rotated() {
		double difference = Angle.difference(position.theta, angle);
		
		double absDifference = Math.abs(difference);
		
		boolean isClose =  absDifference < Math.PI / 50.0;
		boolean worsening = angleDifference <= absDifference;
		
		this.angleDifference = absDifference;
		
		return isClose && worsening;
	}
	
	private boolean isRotating() {
		return !Double.isNaN(angle);
	}

}