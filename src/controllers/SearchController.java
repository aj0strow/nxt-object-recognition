import java.util.Stack;

public class SearchController extends Controller {
	
	protected ColorPoller colorPoller;
	protected Point maximum;
	Stack<Point> path;
	
	private boolean hadPosession = false;
	private boolean inPosession = false;
	
	public SearchController(Configuration configuration) {
		super(configuration);
		this.colorPoller = configuration.colorPoller;
		this.maximum = configuration.maximumPoint;
		this.path = configuration.path;
	}
	
	@Override
	protected void setup() {
		super.setup();
		this.inPosession = colorPoller.inPosession();
		
		if (inPosession && !hadPosession) {
			path.clear();
			path.push(new Point(maximum.x - 18, maximum.y - 18));
		} else if (path.empty()) {
			wander();
		}
			
		this.hadPosession = inPosession;
	}
	
	private void wander() {
		path.push(new Point(30, 60));
		path.push(new Point(30, 90));
		path.push(new Point(60, 90));
		path.push(new Point(60, 60));
		path.push(new Point(60, 30));
	}

}