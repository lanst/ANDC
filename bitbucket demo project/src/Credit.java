import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GPoint;

/**
 * <p>Show the developers.</p>
 * @author Norlan Prudente
 *
 */
public class Credit extends GraphicsPane {
	private static final int _LONG_PERIOD = 1;
	private static final int _DELAY = 30;
	private static final int CREDIT_STOPPER = 20;
	private static final int _PIXEL_Y_ADJUSTMENT = 1;
	private static final int TOTAL_NAMES = 9;
	private static final String FONT = "Bernard MT Condensed-24";
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final Color fontColor = new Color(0, 162, 232);
	private static final String fileName = "credits.txt";
	private MainApplication program;
	private GImage background;
	private GImage overhead;
	private ArrayList<GLabel> names;
	private ArrayList<GPoint> originalLocation;
	private boolean stopAnimation;
	private AudioPlayer music;
	private AudioPlayer click;
	
	public Credit(MainApplication program){
		this.program = program;
		stopAnimation = false;
		names = new ArrayList<GLabel>();
		originalLocation = new ArrayList<GPoint>();
		
		ReadFile();
		setUp();
	}
	
	@Override
	public void showContents() {
		program.add(background);
		
		for (int i = 0; i < TOTAL_NAMES; i++){
			program.add(names.get(i));
		}
		
		program.add(overhead);
		
		if(program.currentScreen() == this){
			music.startLooping();
			music.LoopMusic();
			Animate();
		}

	}

	@Override
	public void hideContents() {
		music.stopLooping();
		music.Stop();
		
		stopAnimation = false;
		resetLocation();
		
		program.removeAll();
	}
	
	/**
	 * <p>reset the location</p>
	 */
	public void resetLocation(){
		for (int i = 0; i < TOTAL_NAMES; i++){
			names.get(i).setLocation(originalLocation.get(i));
		}
	}
	
	/**
	 * <p>set up all the necessary elements.</p>
	 */
	private void setUp(){
		click = new AudioPlayer("Sounds/Clicked.wav");
		background = new GImage("Images/CreditBackground.png");
		overhead = new GImage("Images/CreditOverhead.png");

		//set the location,font style and font color of the labels
		for (int i = 0; i < TOTAL_NAMES; i++){
			names.get(i).setFont(FONT);
			names.get(i).setColor(fontColor);
			names.get(i).setLocation((WIDTH - names.get(i).getWidth())/2, 
					HEIGHT + (names.get(i).getHeight()*i));
			originalLocation.add(names.get(i).getLocation());
			if(names.get(i).getLabel().equals("Programmers") || names.get(i).getLabel().equals("Testers")){	// Only modify the Title labels
				Font underlinedFont = names.get(0).getFont();										// Capture the initial attributes to add underlining to
				@SuppressWarnings("unchecked")
				Map<TextAttribute, Integer> attributes = (Map<TextAttribute, Integer>) underlinedFont.getAttributes();									// Mapify the attributes we captured
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);				// add the underlining attributes to the font
				names.get(i).setFont(underlinedFont.deriveFont(attributes));						// underline the labels specified in the if-statement
			}
		}
		
		music = new AudioPlayer("Sounds/Credit.wav");
	}
	
	/**
	 * <p>Scroll the name up.</p>
	 */
	public void Animate(){
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){
			public void run(){
				//move the names up
				for (int i = 0; i < TOTAL_NAMES; i++){
					names.get(i).setLocation(names.get(i).getLocation().getX(), names.get(i).getLocation().getY() - _PIXEL_Y_ADJUSTMENT);
				}
				
				if (names.get(0).getY() < overhead.getHeight() + CREDIT_STOPPER){
					stopAnimation = true;
				}
				
				timer.cancel();
				Animate();
			}
		};
		
		if (!stopAnimation)
			timer.scheduleAtFixedRate(task, _DELAY, _LONG_PERIOD);
		else
		{
			timer.cancel();
			task.cancel();
			stopAnimation = true;
		}
	}
	
	/**
	 * <p>Read the credit text</p>
	 */
	public void ReadFile(){
		String line;
		
		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = 
					new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = 
					new BufferedReader(fileReader);

			//read until the end of file
			while((line = bufferedReader.readLine()) != null) {
				names.add(new GLabel(line));
			}
			
			// Always close files.
			bufferedReader.close();         
		}
		catch(FileNotFoundException ex) {
			System.out.println(
					"Unable to open file '" + 
							fileName + "'");                
		}
		catch(IOException ex) {
			System.out.println(
					"Error reading file '" 
							+ fileName + "'");                  
			// Or we could just do this: 
			// ex.printStackTrace();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		click.Play();
		
		//go to credit
		program.switchToMainMenu();
	}

}
