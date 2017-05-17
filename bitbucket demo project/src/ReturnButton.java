/**
 * A button to return to the previous screen.
 * @author Norlan Prudente
 *
 */

import java.awt.event.MouseEvent;

import acm.graphics.GDimension;
import acm.graphics.GImage;

public class ReturnButton {
	private GImage image;			//image of the return button
	private double x;				// x coordinate
	private double y;				//y coordinate
	private AudioPlayer click;		//selection sound
	private Effects effects;		//visual effect
	
	public ReturnButton(double x, double y){
		this.x = x;
		this.y = y;
		setUp();
	}
	
	/**
	 * return the size of the image.
	 * @return
	 */
	public GDimension getSize(){
		return image.getSize();
	}
	
	/**
	 * set the location of the button
	 * @param newX
	 * @param newY
	 */
	public void setLocation(double newX, double newY){
		image.setLocation(newX, newY);
	}
	
	/**
	 * set the size of the image.
	 * @param width
	 * @param height
	 */
	public void setSize(double width, double height){
		image.setSize(width, height);
	}
	
	/**
	 * set the size of the image.
	 * @param dimension
	 */
	public void setSize(GDimension dimension){
		image.setSize(dimension);
	}
	
	/**
	 * set up the button.
	 */
	private void setUp(){
		image = new GImage("Images/returnButton.png");
		image.setLocation(x,y);
		
		click = new AudioPlayer("Sounds/Clicked.wav");
		
		effects = new Effects();
	}
	
	/**
	 * Place the image on the screen
	 * @param program
	 */
	public void PlaceReturnButton(MainApplication program){
		program.add(image);
	}
	
	/**
	 * <p>Give the place to go as string "Main Menu"
	 * or "StartingArea".</p>
	 * <p>Place this on mouseClicked</p>
	 * @param screen
	 * @param program
	 * @param e
	 */
	public void OnClick(String screen, MainApplication program, MouseEvent e){
		if (image.contains(e.getX(), e.getY())) {
			click.Play();
			if (screen == "MainMenu")
				program.switchToMainMenu();
			else  if (screen == "StartingArea")
				program.switchToStartingArea();
		}
	}
	
	/**
	 * <p>Place this on mouseMoved.</p>
	 * @param e
	 */
	public void OnHover(MouseEvent e){
		effects.ChangeImage(image, "invertedReturnButton.png", "returnButton.png", e);
	}

}
