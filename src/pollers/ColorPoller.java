import lejos.nxt.ColorSensor;
import lejos.util.Timer;
import lejos.util.TimerListener;
import lejos.nxt.SensorPort;

import lejos.nxt.LCD;

public class ColorPoller implements TimerListener {
	public static final int PERIOD = 50;
	
	private ColorSensor colorSensor;
	private ColorSensor.Color color;
	private Timer timer = null;
	
	public ColorPoller(SensorPort sensorPort) {
		this.colorSensor = new ColorSensor(sensorPort);
		timedOut();		
		start();
	}
	
	public void start() {
		this.timer = new Timer(PERIOD, this);
		timer.start();
	}
	
	public void stop() {
		timer.stop();
		this.timer = null;
	}
	
	public void timedOut() {
		this.color = colorSensor.getColor();
	}
	
	public ColorSensor.Color getColor() {
		return this.color;
	}
	
	public boolean inSight() {
		int red = color.getRed(), blue = color.getBlue();
		return blue > 10 && blue > red;		
	}
	
	public boolean inPosession() {
		return color.getBlue() > 200;
	}
}