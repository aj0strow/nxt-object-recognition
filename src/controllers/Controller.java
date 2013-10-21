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