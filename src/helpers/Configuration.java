import java.util.Stack;

public class Configuration {

	public Odometer odometer;
	public ColorPoller colorPoller;
	public UltrasonicPoller ultrasonicPoller;
	public Robot robot;
	public Point maximumPoint;
	public Stack<Point> path;
	
	public Configuration() {
		this.path = new Stack<Point>();
	}

}