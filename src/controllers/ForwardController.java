// this is a stub, just so the default action is forward motion

public class ForwardController extends RobotController {

	protected Robot robot;

	public ForwardController(Configuration configuration) {
		super(configuration);
		this.robot = configuration.robot;
	}
	
	@Override
	protected boolean ok() {
		robot.setSpeeds(4.0, 0.0);
		return true;
	}

}