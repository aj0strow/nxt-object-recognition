import java.util.Stack;

public class PathController extends RobotController {

	private Stack<Point> path;

	public PathController(Configuration configuration) {
		super(configuration);
		this.path = configuration.path;
	}
	
	@Override
	protected void setup() {
		super.setup();
		if (super.ok()) navigate();
	}
	
	@Override
	protected boolean ok() {
		return super.ok() && path.empty();
	}
	
	private void navigate() {
		Point point = path.peek();
		
		double angle = position.angleTo(point);
		double difference = Angle.difference(position.theta, angle);
		double direction = Math.abs(difference) / difference;

		if (Math.abs(difference) > Math.toRadians(5)) {
			robot.setSpeeds(0.0, direction * ROTATE_SPEED);
		} else {
			robot.setSpeeds(FORWARD_SPEED, difference * 4);
		}
	}
}