abstract class RobotController extends OdometerController {
	protected Robot robot;
	
	protected RobotController(Odometer odometer, Robot robot) {
		super(odometer);
		this.robot = robot;
	}

}