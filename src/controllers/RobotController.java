abstract class RobotController extends OdometerController {
	protected Robot robot;
	
	protected RobotController(Configuration configuration) {
		super(configuration);
		this.robot = configuration.robot;
	}

}