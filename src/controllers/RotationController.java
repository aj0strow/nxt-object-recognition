abstract class RotationController extends RobotController {
	
	// target angle
	private double angle = Double.NaN;
	private double angleDifference = Double.NaN;
	
	public RotationController(Configuration configuration) {
		super(configuration);
	}
	
	@Override
	protected void setup() {
		super.setup();
		if (isRotating()) rotate();
	}
	
	@Override
	protected boolean ok() {
		return !isRotating();
	}
	
	protected void rotateTo(double angle) {
		this.angle = angle;
	}
		
	private void rotate() {
		if (rotated()) {
			robot.setSpeeds(0.0, 0.0);
			this.angle = Double.NaN;
			this.angleDifference = Double.NaN;
		} else {
			robot.setSpeeds(0.0, Angle.direction(position.theta, angle) * ROTATE_SPEED);
		}
	}
	
	protected boolean rotated() {
		double difference = Angle.difference(position.theta, angle);
		
		double absDifference = Math.abs(difference);
		
		boolean isClose =  absDifference < Math.PI / 50.0;
		boolean worsening = angleDifference <= absDifference;
		
		this.angleDifference = absDifference;
		
		return isClose && worsening;
	}
	
	protected boolean isRotating() {
		return !Double.isNaN(angle);
	}

}