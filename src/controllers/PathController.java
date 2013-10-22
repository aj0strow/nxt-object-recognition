import java.util.Stack;
import lejos.nxt.LCD;

public class PathController extends RobotController {

	private Stack<Point> path;
	private double distance;

	public PathController(Configuration configuration) {
		super(configuration);
		this.path = configuration.path;
	}
	
	@Override
	protected void setup() {
		LCD.drawString(path.peek().toString(), 0, 5);
		super.setup();
		if (super.ok() && !path.empty()) {
			if (arrived()) path.pop();
			else navigate();
		}
	}
	
	@Override
	protected boolean ok() {
		return super.ok() && path.empty();
	}
	
	private void navigate() {		
		double angle = position.angleTo(path.peek());
		double difference = Angle.difference(position.theta, angle);
		double direction = Math.abs(difference) / difference;

		if (Math.abs(difference) > Math.toRadians(5)) {
			robot.setSpeeds(0.0, direction * ROTATE_SPEED);
		} else {
			robot.setSpeeds(FORWARD_SPEED, difference * 4);
		}
	}
	
	private boolean arrived() {
		double newDistance = position.distanceTo(path.peek());
		
		boolean isClose = newDistance < 2.0;
		boolean worsened = distance <= newDistance;
		
		this.distance = newDistance;
		
		return isClose && worsened;
	}
}