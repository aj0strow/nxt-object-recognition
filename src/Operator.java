import lejos.util.Timer;
import lejos.util.TimerListener;

public class Operator implements TimerListener {
	private static final int PERIOD = 10;
	
	private static final double ROTATION_SPEED = Math.PI / 8;
	
	private Robot robot;
	private Odometer odometer;
	
	private double angle = Double.NaN;
	private double angleDifference;
	
	private Position position;

	public Operator(Robot robot) {
		this.robot = robot;
		
		this.odometer = new Odometer(robot);
		this.position = odometer.getPosition();
		
		(new Timer(PERIOD, this)).start();
	}
	
	public void timedOut() {
		this.position = odometer.getPosition();
		
		if (isRotating()) {
			if (rotated()) stopRotating();
			else rotate();
		}
	}
	
	public void rotateTo(double angle) {
		this.angle = angle;
	}
	
	private void rotate() {
		robot.setSpeeds(0.0, Angle.direction(position.theta, angle) * ROTATION_SPEED);
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