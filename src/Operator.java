import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.LCD;
import lejos.nxt.ColorSensor;

public class Operator implements TimerListener {
	private static final int PERIOD = 5;
	private Timer timer;
	
	private static final double TRAVEL_SPEED = 6.0;
	private static final double ROTATE_SPEED = Math.PI / 6;
	
	private static final int PROXIMITY = 25;
	
	private Robot robot;
	private Odometer odometer;
	private Position position;
	private ColorSensor.Color color;
	
	private UltrasonicPoller ultrasonicPoller;
	private ColorPoller colorPoller;
	private int colorSensorState = 0;
	
	private double angle = Double.NaN;
	private double closeAngle = Double.NaN;
	private int direction = -1;
	private int wanderDistance = 60;
	private int turnCount = 0;
	
	private Point startPoint;
	
	private double angleDifference;
	
	private Point point = null;
	boolean navigating = false;
	private int randomDirection = 0;
	
	private double distance;
	

	public Operator(Robot robot, Odometer odometer, UltrasonicPoller ultrasonicPoller, ColorPoller colorPoller) {
		this.robot = robot;
		
		this.odometer = odometer;
		this.position = odometer.getPosition();
		this.startPoint = position.point();
		
		this.ultrasonicPoller = ultrasonicPoller;
		
		this.colorPoller = colorPoller;		
	}
	
	public void start() {
		this.timer = new Timer(PERIOD, this);
		this.timer.start();
	}
	
	public void stop() {
		this.timer.stop();
		this.timer = null;
	}
	
	public void timedOut() {
		this.position = odometer.getPosition();
		
		LCD.clear();
		LCD.drawString("d: " + ultrasonicPoller.getDistance(), 0, 0);
				
		if (colorPoller.inPosession()) deposit();
		else search();
		
		/*
		this.position = odometer.getPosition();
		
		
		if (searching()) search();
		else deposit();
		
		if (isRotating()) {
			if (rotated()) stopRotating();
			else rotate();
		} else if (isTravelling()) {
			if (travelled()) stopTravelling();
			else travel();
		} else if (isNavigating()) {
			navigate();
		} else {
			robot.setSpeeds(0.0, 0.0);
		}
		*/
		// LCD.drawString("up: " + ultrasonicPoller.getDistance(), 0, 3);
	}
	
	private void deposit() {
		
	}
	
	private void search() {
		if (Double.isNaN(closeAngle)) {
			wander();
		} else {
			double radians = Angle.difference(closeAngle, position.theta);
			if (Math.abs(radians) > Math.PI / 2) {
				this.closeAngle = Double.NaN;
				this.startPoint = position.point();
			}
		}
	}
	
	public void wander() {
		if (tooClose() || turnCount == 2) {
			this.turnCount ++;
			this.closeAngle = position.theta;
			robot.setSpeeds(0.0, ROTATE_SPEED * direction);
		} else if (position.distanceTo(startPoint) > wanderDistance) {
			this.turnCount ++;
			this.closeAngle = position.theta;
			if (wanderDistance == 60) {
				this.direction = -direction;
				this.wanderDistance = 30;
			} else {
				this.wanderDistance = 60;
			}
			robot.setSpeeds(0.0, ROTATE_SPEED * direction);
		} else {
			if (position.distanceTo(startPoint) > 10) {
				this.turnCount = 0;
			}
			robot.setSpeeds(TRAVEL_SPEED, 0.0);
		}
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
	
	public boolean isNavigating() {
		return isTravelling() || isRotating() || navigating;
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
	
	private boolean tooClose() {
		return ultrasonicPoller.getDistance() < PROXIMITY;
	}
}