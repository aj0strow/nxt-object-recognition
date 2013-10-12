import lejos.nxt.NXTRegulatedMotor;

public class Robot {
	
	// wheel radius (cm), separation (cm)
	public static final double WHEEL_RADIUS = 2.75;
	public static final double WHEEL_SEPARATION = 15.8;
	
	// max angular speed for each motor (deg / s)
	private static final int MAX_SPEED = 400;
	
	public NXTRegulatedMotor leftMotor, rightMotor;
	
	private double forwardSpeed = 0.0, rotationSpeed = 0.0;
	
	public Robot(NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
	}
	
	// acceleration (deg / s^2)
	public void setAcceleration(int acceleration) {
		leftMotor.setAcceleration(acceleration);
		rightMotor.setAcceleration(acceleration);
	}
	
	// forward speed (cm / s), left rotation speed (rad / s)
	public void setSpeeds(double forwardSpeed, double rotationSpeed) {
		this.forwardSpeed = forwardSpeed;
		this.rotationSpeed = rotationSpeed;
		setMotorSpeeds();
	}
	
	private void setMotorSpeeds() {
		double turnSpeed = rotationSpeed * WHEEL_SEPARATION / 2.0;
		setMotorSpeed(leftMotor, forwardSpeed - turnSpeed);
		setMotorSpeed(rightMotor, forwardSpeed + turnSpeed);
	}
	
	// velocity (cm / s)
	private static void setMotorSpeed(NXTRegulatedMotor motor, double velocity) {
		int speed = (int) Math.abs(Math.toDegrees(velocity / WHEEL_RADIUS));
		if (velocity >= 0) {
			motor.forward();
		} else {
			motor.backward();
		}
		motor.setSpeed(Math.min(MAX_SPEED, speed));
	}
	
	// total displacement since start (cm)
	public double getDisplacement() {
		return (distance(leftMotor) + distance(rightMotor)) / 2.0;
	}
	
	// total angle since start (rad)
	public double getHeading() {
		return (distance(leftMotor) - distance(rightMotor)) / WHEEL_SEPARATION;
	}
	
	private static double distance(NXTRegulatedMotor motor) {
		return arcDistance(motor.getTachoCount());
	}
	
	private static double arcDistance(int tachoCount) {
		return Math.toRadians(tachoCount) * WHEEL_RADIUS;
	}

}