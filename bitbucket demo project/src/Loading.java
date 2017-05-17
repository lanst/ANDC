import acm.graphics.GImage;

/**
 * <p>Loading screen</p>
 * @author Norlan Prudente
 *
 */
public class Loading extends GraphicsPane {
	private static final int MAX_PERCENT = 100;
	private static final int BAR_ALLIGN = 20;
	private static final int HEIGHT = 600;
	private static final int WIDTH = 800;
	
	private GImage background;
	private GImage loadBar;
	private MainApplication program;
	private double originalBarWidth;
	
	public Loading(MainApplication program){
		this.program = program;
		
		setUp();
		originalBarWidth = loadBar.getSize().getWidth();
	}
	
	/**
	 * <p>Set up the loading screen.</p>
	 */
	private void setUp(){
		//background
		background = new GImage("Images/Loading.png");
		
		//loading bar
		loadBar = new GImage("Images/LoadingBar.png");
		loadBar.setLocation(WIDTH - (loadBar.getWidth() + loadBar.getWidth()/6),
				HEIGHT/2 + (loadBar.getHeight() + BAR_ALLIGN));
	}
	
	/**
	 * <p>Set the size of the loading bar.</p>
	 */
	public void setPercentage(double percentage){
		//to get the percent 1 == 100%
		percentage /= MAX_PERCENT;
		
		//get the actual size of the bar to match the percentage
		percentage *= originalBarWidth;
		
		//set the size of bar and show it to the screen
		loadBar.setSize(percentage, loadBar.getHeight());
		program.add(loadBar);
		
	}
	
	@Override
	public void showContents() {
		program.add(background);
		program.add(loadBar);
	}

	@Override
	public void hideContents() {
		program.removeAll();

	}

}
