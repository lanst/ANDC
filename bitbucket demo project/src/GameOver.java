import java.awt.event.MouseEvent;
import acm.graphics.GImage;

/**
 * <p>Shows when the player lose all their money and items.</p>
 * @author Norlan Prudente
 *
 */
public class GameOver extends GraphicsPane {
	private MainApplication program;
	private GImage background;
	private AudioPlayer click;
	
	public GameOver(MainApplication program){
		this.program = program;
		background = new GImage("Images/GameOver.png");
		click = new AudioPlayer("Sounds/Clicked.wav");
	}
	
	@Override
	public void showContents() {
		program.add(background);
	}

	@Override
	public void hideContents() {
		program.removeAll();
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
		click.Play();
		
		//go to credit
		program.switchToCredit();
	}

}
