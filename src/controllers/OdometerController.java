abstract class OdometerController extends Controller {

	protected Odometer odometer;
	protected Position position;
	
	protected OdometerController(Odometer odometer) {
		super();
		this.odometer = odometer;
		this.position = odometer.getPosition();
	}
	
	@Override
	protected void setup() {
		super.setup();
		this.position = odometer.getPosition();
	}

}