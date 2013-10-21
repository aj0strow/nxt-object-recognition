import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.nxt.LCD;

/*
* Odometer defines cooridinate system as such...
* 
*               π/2 rad:pos y-axis
*                      |
*                      |
*                      |
*                      |
* π rad:neg x-axis------------ 0 rad:pos x-axis
*                      |
*                      |
*                      |
*                      |
*              3π/2 rad:neg y-axis
* 
* The odometer is initalized to (0, 0, 0), so at the origin facing up 
* the positive x-axis
*/

public class Odometer implements TimerListener {
	private static final int PERIOD = 10;
	private Timer timer;
	
	private Position position;
	private Object lock;
	
	private Robot robot;
	
	double displacement = 0.0, heading = 0.0;

	public Odometer(Robot robot) {
		this.position = new Position(30.0, 30.0, 0.0);
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
	
	// increase the position by polar displacement (cm), and angle (rad)
	public void incrPosition(double centimeters, double radians) {
		double theta = getTheta();
		
		double dx = centimeters * Math.cos(theta);
		double dy = centimeters * Math.sin(theta);
		
		incrPosition(dx, dy, radians);
	}
	
	// increase the position by dx (cm), dy (cm), dtheta (rad)
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
	
	public void setTheta(double theta) {
		synchronized (lock) { position.theta = theta; }
	}
	
	public void displayPosition() {
		Position p = getPosition();
		LCD.clear();
		LCD.drawString("x: " + p.x, 0, 0);
		LCD.drawString("y: " + p.y, 0, 1);
		LCD.drawString("t: " + Math.toDegrees(p.theta), 0, 2);
	}

}