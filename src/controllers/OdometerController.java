abstract class OdometerController extends Controller {

	protected Odometer odometer;
	protected Position position;
	
	protected OdometerController(Configuration configuration) {
		super(configuration);
		this.odometer = configuration.odometer;
		this.position = odometer.getPosition();
	}
	
	@Override
	protected void setup() {
		super.setup();
		this.position = odometer.getPosition();
	}

}