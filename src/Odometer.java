import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.nxt.LCD;

public class Odometer implements TimerListener {
	private static final int PERIOD = 10;
	private Timer timer;
	
	private Position position;
	private Object lock;
	
	private Robot robot;
	
	double displacement = 0.0, heading = 0.0;

	public Odometer(Robot robot) {
		this.position = new Position(0.0, 0.0, 0.0);
		this.lock = new Object();
		
		this.robot = robot;
		
		(new Timer(PERIOD, this)).start();
	}
	
	public void timedOut() {
		double newDisplacement = robot.getDisplacement();
		double newHeading = robot.getHeading();
		
		incrPosition(newDisplacement - displacement, heading - newHeading);
		
		this.displacement = newDisplacement;
		this.heading = newHeading;
		
		displayPosition();
	}
	
	public Position getPosition() {
		Position position = null;
		synchronized (lock) {
			position = this.position.clone();
		}
		return position;
	}
	
	public void setPosition(Position position) {
		synchronized (lock) {
			this.position = position.clone();
		}
	}
	
	public void incrPosition(double centimeters, double radians) {
		double theta = getTheta();
		
		double dx = centimeters * Math.cos(theta);
		double dy = centimeters * Math.sin(theta);
		
		incrPosition(dx, dy, radians);
	}
	
	public void incrPosition(double dx, double dy, double dtheta) {
		synchronized (lock) {
			this.position.incr(dx, dy, dtheta);
		}
	}
	
	// 0 <= theta < 2π
	public double getTheta() {
		double result;
		synchronized (lock) { result = position.theta; }
		return result;
	}
	
	public void displayPosition() {
		Position p = getPosition();
		LCD.clear();
		LCD.drawString("x: " + p.x, 0, 0);
		LCD.drawString("y: " + p.y, 0, 1);
		LCD.drawString("t: " + Math.toDegrees(p.theta), 0, 2);
	}

}