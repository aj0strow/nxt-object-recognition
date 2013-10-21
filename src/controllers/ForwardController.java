// this is a stub, just so the default action is forward motion

public class ForwardController extends Controller {

	protected Robot robot;

	public ForwardController(Robot robot) {
		super();
		this.robot = robot;
	}
	
	@Override
	protected boolean ok() {
		robot.setSpeeds(4.0, 0.0);
		return true;
	}

}