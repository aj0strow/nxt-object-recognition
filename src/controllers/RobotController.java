abstract class RobotController extends OdometerController {
	// forward speed: 6 cm/s
	protected static final double FORWARD_SPEED = 6.0;
	
	// rotate speed: 30 deg/s
	protected static final double ROTATE_SPEED = Math.toRadians(30);
	
	protected Robot robot;
	
	protected RobotController(Configuration configuration) {
		super(configuration);
		this.robot = configuration.robot;
	}

}