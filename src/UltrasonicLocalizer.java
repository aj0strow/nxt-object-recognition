import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.nxt.Sound;
import lejos.nxt.LCD;

public class UltrasonicLocalizer implements TimerListener {
	private static int PERIOD = 10;
	private Timer timer;

	private Robot robot;
	private UltrasonicPoller ultrasonicPoller;
	private Odometer odometer;
	
	private static int OUTER_DISTANCE = 35;
	private static int INNER_DISTANCE = 25;
	
	// edge angles
	private double outerAngle, innerAngle;
	
	// actual localization angles
	private double positive, negative;
	
	private boolean ready = false;
	private int distance;
	private double dtheta;

	public UltrasonicLocalizer(Robot robot, Odometer odometer, UltrasonicPoller ultrasonicPoller) {
		this.robot = robot;
		this.odometer = odometer;
		this.ultrasonicPoller = ultrasonicPoller;
		this.timer = null;
	}
	
	public void localize() {		
		this.positive = Double.NaN;
		this.negative = Double.NaN;
		resetEdgeAngles();
		
		this.timer = new Timer(PERIOD, this);
		timer.start();
	}
	
	private void resetEdgeAngles() {
		this.outerAngle = Double.NaN;
		this.innerAngle = Double.NaN;
	}
	
	public void timedOut() {
		this.distance = ultrasonicPoller.getDistance();
		setSpeeds();
		
		if (ready) {
			LCD.drawString("READY    ", 0, 0);
			
			if (edgeAnglesFound()) {
				checkForOffset();
			} else {
				checkForEdge();
			}
			
			
		} else {
			LCD.drawString("NOT READY", 0, 0);
			this.ready = distance >= 50;
		}
	}
	
	private void setSpeeds() {
		if (Double.isNaN(negative)) {
			robot.setSpeeds(0.0, -Math.PI / 8);
			LCD.drawString("---", 0, 1);
		} else if (Double.isNaN(positive)) {
			robot.setSpeeds(0.0, Math.PI / 8);
			LCD.drawString("+++", 0, 1);
		} else {
			robot.setSpeeds(0.0, 0.0);
			LCD.drawString("done", 0, 1);
		}
	}
	
	private void checkForOffset() {
		if (Double.isNaN(negative)) {
			this.negative = Angle.between(outerAngle, innerAngle);
			robot.setSpeeds(0.0, 0.0);
			this.ready = false;
			resetEdgeAngles();
			Sound.beep();
		} else if (Double.isNaN(positive)) {
			this.positive = Angle.between(outerAngle, innerAngle);
			robot.setSpeeds(0.0, 0.0);
				
			double dtheta = 5 * Math.PI / 4 - Angle.between(positive, negative);
				
			LCD.drawString("" + Math.toDegrees(negative), 0, 2);
			LCD.drawString("" + Math.toDegrees(positive), 0, 3);
			LCD.drawString("" + Math.toDegrees(dtheta), 0, 4);
				
			odometer.incrPosition(0.0, dtheta);
				
			LCD.drawString("theta: " + Math.toDegrees(odometer.getTheta()), 0, 5);
				
			stopLocalizing();
			Sound.beep();
		}
	}
	
	private boolean edgeAnglesFound() {
		return !(Double.isNaN(outerAngle) || Double.isNaN(innerAngle));
	}
	
	private void checkForEdge() {
		if (Double.isNaN(outerAngle)) {
			if (distance < OUTER_DISTANCE) {
				this.outerAngle = odometer.getTheta();
			}
		} else if (Double.isNaN(innerAngle)) {
			if (distance < INNER_DISTANCE) {
				this.innerAngle = odometer.getTheta();
			}
		}
	}

	private void stopLocalizing() {
		timer.stop();
		this.timer = null;
	}
	
	public boolean isLocalizing() {
		return timer != null;
	}

}