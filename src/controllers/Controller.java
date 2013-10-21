/*
*  Controllers are to be used in series. Call next() and it
*  will return whether the next controller should run. This
*  allows for basic needs (like not hitting the wall) to
*  supersede the need to capture blocks, etc. 
*/

abstract class Controller {
	
	public Controller() {}
	
	// override these methods
	protected void setup() {}
	protected boolean ok() { return true; }
	
	// don't override this method
	public boolean next() {
		setup(); return ok();
	}
}