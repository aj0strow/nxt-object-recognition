import lejos.util.Timer;
import lejos.util.TimerListener;

public class Operator implements TimerListener {
	public static final int PERIOD = 5;
	private Timer timer;
	
	private Controller[] controllers;
	
	
	public Operator(Controller[] controllers) {
		this.controllers = controllers;
	}
	
	public void start() {
		this.timer = new Timer(PERIOD, this);
		this.timer.start();
	}
	
	public void stop() {
		this.timer.stop();
		this.timer = null;
	}
	
	public void timedOut() {
		for (Controller controller : controllers) {
			if (!controller.next()) return;
		}
	}
	
}