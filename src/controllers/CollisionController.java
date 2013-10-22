import java.util.Stack;
import lejos.nxt.Sound;

public class CollisionController extends RotationController {
	// cannot be closer than 25 cm to anything
	public static final int PROXIMITY = 25;
	
	protected Stack<Point> path;
	protected UltrasonicPoller ultrasonicPoller;
	
	private int ultrasonicDistance;
	private int avoidanceCount;
	private int sign = -1;	
	
	// 0 -> ok
	// 1 -> spin
	// 2 -> forward
	// 3 -> spin
	// 4 -> forward
	private int state = 0;
	
	
	public CollisionController(Configuration configuration) {
		super(configuration);
		this.ultrasonicPoller = configuration.ultrasonicPoller;
		this.path = configuration.path;
	}
	
	@Override
	protected void setup() {
		super.setup();
		this.ultrasonicDistance = ultrasonicPoller.getDistance();
		
		if (super.ok()) {
			if (state > 0) avoid();
			else if (tooClose()) avoidCollision();
		}
	}
	
	@Override
	protected boolean ok() {
		return state == 0;		
	}
	
	private boolean tooClose() {
		return ultrasonicDistance < PROXIMITY;
	}

	private void avoidCollision() {
		this.state = 1;
		
		double distance = position.distanceTo(path.peek());
		if (Math.abs(distance - ultrasonicDistance) < 8) {
			path.pop(); Sound.beep();
		} 
		avoid();
	}
	
	private void avoid() {
		if (state == 1 || state == 3) {
			spin();
		} else if (state == 2 || state == 4) {
			if (avoidanceCount > 0) {
				avoidanceCount --;
				robot.setSpeeds(FORWARD_SPEED, 0.0);
			}
			else state ++;
		} else {
			this.state = 0;
		}
	}
	
	private void setCount() {
		this.avoidanceCount = 3000 / Operator.PERIOD;
	}
	
	private void spin() {
		double rightAngle = Angle.normalize(position.theta + sign * Math.toRadians(80));
		this.sign = -sign;
		rotateTo(rightAngle);
		setCount();
		state ++;
	}
}