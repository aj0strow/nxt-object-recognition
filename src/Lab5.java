import lejos.nxt.*;
import lejos.nxt.ColorSensor;
import lejos.util.*;

public class Lab5 {

	public static void main(String[] args) {
		Button.ESCAPE.addButtonListener(new ExitListener());
		
		Button.waitForAnyPress();
		
		SensorPort s3 = SensorPort.S3;
		final ColorSensor colorSensor = new ColorSensor(s3);
		
		/*
		*  The color sensor gets random readings under 10 when there is
		*  is no object within a reasonable distance. However when over
		*  10 for RGB, if R > B then it is a wood block, if B > R it is
		*  a styrofoam block. 
		*/
				
		Timer timer = new Timer(300, new TimerListener() {
			public void timedOut() {
				LCD.clear();				
				String descriptor = detection(colorSensor.getColor());
				if (descriptor.length() > 0) {
					LCD.drawString("Object Detected", 0, 0);
					LCD.drawString(descriptor, 0, 1);
				}
			}
					
			public String detection(ColorSensor.Color color) {
				int red = color.getRed(), blue = color.getBlue();
				String descriptor = "";
				if (red > 10 && blue > 10) {
					descriptor = red > blue ? "wood" : "styrofoam";
				}
				return descriptor;
			}
		});
		
		timer.start();
		
		// lab code here
		
		Button.waitForAnyPress();
	}
	
}
