import lejos.nxt.LightSensor;
import lejos.util.TimerListener;
import lejos.util.Timer;
import lejos.nxt.Sound;
import lejos.nxt.LCD;

public class LineLocalizer implements TimerListener {
	public static final int PERIOD = 10;
	private static final int LIGHT_THRESHOLD = 400;
	private static final double ROTATE_SPEED = Math.PI / 6;
	
	private static final double SENSOR_DISPLACEMENT = 12.4;
	
	Robot robot;
	Odometer odometer;
	LightSensor lightSensor;
	Timer timer;
	
	Position position;
	
	boolean started = false;
	double[] angles;
	
	boolean wasLine = false;

	public LineLocalizer(Robot robot, Odometer odometer, LightSensor lightSensor) {
		this.robot = robot;
		this.odometer = odometer;
		this.lightSensor = lightSensor;
		
		this.angles = new double[]{ Double.NaN, Double.NaN, Double.NaN, Double.NaN };
		
		this.timer = null;
		
	}
	
	public void localize() {
		this.started = false;
		this.wasLine = false;
		this.timer = new Timer(PERIOD, this);
		timer.start();
	}
	
	public void timedOut() {
		this.position = odometer.getPosition();
		if (started) logic(); else initial();
	}
	
	private void logic() {
		robot.setSpeeds(0.0, -ROTATE_SPEED);
				
		if (angleIndex() == angles.length) stop();
		else detectLine();
	}
	
	private void detectLine() {
		boolean isLine = lightSensor.readNormalizedValue() < LIGHT_THRESHOLD;
		if (isLine && !wasLine) {
			Sound.beep();
			angles[angleIndex()] = position.theta;
		}
		this.wasLine = isLine;
	}
	
	private Position realPosition() {
		double x = SENSOR_DISPLACEMENT * Math.cos((angles[3] - angles[1]) / 2);
		double y = SENSOR_DISPLACEMENT * Math.cos((angles[2] - angles[0]) / 2);
		return new Position(-x, -y, position.theta);
	}
	
	private void stop() {
		robot.setSpeeds(0.0, 0.0);
		this.timer.stop();
		this.timer = null;
		odometer.setPosition(realPosition());
	}
	
	private int angleIndex() {
		for (int i=0; i < angles.length; i++) {
			if (Double.isNaN(angles[i])) return i;
		}
		return angles.length;
	}
	
	private void initial() {
		double forty5deg =  Math.toRadians(45.0);
		if (Math.abs(position.theta - forty5deg) > Math.PI / 100) {
			double direction = Angle.direction(position.theta, forty5deg);
			robot.setSpeeds(0.0, direction * ROTATE_SPEED);
		} else if (position.x < 10.0) {
			robot.setSpeeds(4.0, 0.0);
		} else {
			robot.setSpeeds(0.0, 0.0);
			this.started = true;
		}
	}
	
	public boolean isLocalizing() {
		return this.timer != null;
	}

}