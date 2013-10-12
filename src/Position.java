/*
*  Position is a Point with an angle (theta).
*/

public class Position extends Point {
	
	// 0 <= theta < 2π
	public double theta;
	
	public Position(double x, double y, double theta) {
		super(x, y);
		this.theta = Angle.normalize(theta);
	}
	
	public Position(Point point, double theta) {
		this(point.x, point.y, theta);
	}
	
	public Position clone() {
		return new Position(x, y, theta);
	}
	
	public void incr(double dx, double dy, double dtheta) {
		this.x += dx;
		this.y += dy;
		this.theta = Angle.normalize(theta + dtheta);
	}
}