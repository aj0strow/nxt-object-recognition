/*
*  Grid Controller is in charge of keeping the robot in the grid. It takes the
*  odometer, robot, and maximum point of the grid (max x, max y). 
*  
*  It rotates the robot towards the middle if it's in a corner, and parallel to
*  the wall when the robot is too close to a side. It will only correct every 5cm. 
*/

public class GridController extends RotationController {
	private static final double DEBOUNCE_DISTANCE = 5.0;
	protected static final double DELTA = 18.0;
	protected Point maximum;
	
	private Point correctedAt;
	
	public GridController(Configuration configuration) {
		super(configuration);
		this.maximum = configuration.maximumPoint;
	}
	
	@Override
	protected void setup() {
		super.setup();
		
		if (super.ok()) {
			if (debounced() && outside()) correct();
			else debounce();
		}
	}
	
	private boolean debounced() {
		return correctedAt == null;
	}
	
	private void debounce() {
		if (!debounced() && position.distanceTo(correctedAt) > DEBOUNCE_DISTANCE) {
			this.correctedAt = null;
		}
	}
	
	private void correct() {
		this.correctedAt = position.point();
		rotateTo(angle());
	}
	
	private boolean outside() {
		return xInvalid() || yInvalid();
	}
	
	private boolean xInvalid() {
		return xLow() || xHigh();
	}
	
	private boolean yInvalid() {
		return yLow() || yHigh();
	}
	
	private boolean xHigh() {
		return position.x > (maximum.x - DELTA);
	}
	
	private boolean xLow() {
		return position.x < DELTA;
	}
	
	private boolean yHigh() {
		return position.y > (maximum.y - DELTA);
	}
	
	private boolean yLow() {
		return position.y < DELTA;
	}
	
	
	/*
	*  If the robot is in a corner, it should be pointed straight out of the
	*  corner. Otherwise it should be pointed parallel to the wall in the
	*  direction it was heading. 
	*/
	private double angle() {
		double eighth = Math.PI / 4.0;
		
		// check corner cases (no pun intended)
		if (xLow() && yLow()) return eighth;
		if (xLow() && yHigh()) return 7 * eighth;
		if (xHigh() && yLow()) return 3 * eighth;
		if (xHigh() && yHigh()) return 5 * eighth;
		
		double quarter = 2 * eighth;
		double theta = position.theta;
		
		// set correct parallel angle
		if (yInvalid()) {
			if (theta > quarter && theta < 3 * quarter) return 2 * quarter;
			else return 0;
		} else {
			if (theta > 0 && theta < 2 * quarter) return quarter;
			else return 3 * quarter;		
		}
	}
}