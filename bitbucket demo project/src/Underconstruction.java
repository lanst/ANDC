/**
 * This will be shown for games that are not finish.
 * 
 * @author Norlan Prudente
 */
import java.awt.event.MouseEvent;
import acm.graphics.GImage;

public class Underconstruction extends GraphicsPane {
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	private GImage background;
	private ReturnButton returnButton;
	private MainApplication program;
	
	public Underconstruction(MainApplication program) {
		this.program = program;
		
		returnButton = new ReturnButton(0, 0);
		returnButton.setLocation(WIDTH - returnButton.getSize().getWidth(),
				HEIGHT - returnButton.getSize().getHeight());
		
		setUp();
	}
	
	/**
	 * <p>Set up the image and sound of the game.
	 */
	private void setUp(){
		background = new GImage("Images/underconstruction.jpg");
	}
	@Override
	public void showContents() {
		// TODO Auto-generated method stub
		program.add(background);
		returnButton.PlaceReturnButton(program);
	}

	@Override
	public void hideContents() {
		// TODO Auto-generated method stub
		program.removeAll();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		returnButton.OnClick("StartingArea", program, e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		//effect will be shown when hovering over the return button.
		returnButton.OnHover(e);
	}
}
