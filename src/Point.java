/*
*  Point is the cartesian coordinates of a place on
*  the board (x, y).
*/

public class Point {
	public double x;
	public double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point clone() {
		return new Point(x, y);
	}
}