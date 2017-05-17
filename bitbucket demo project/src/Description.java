/**
 * This will pop up the description of 
 * the the icons that are being hover on.
 * @author Norlan Prudente
 *
 */

import java.awt.event.MouseEvent;

import acm.graphics.GImage;
import acm.graphics.GLabel;

public class Description {
	private static final String FONT = "Bernard MT Condensed-";
	
	private String descriptionString;	//string to show inside the box
	private GLabel description;			//make the string to GLabel
	private Effects effects;
	private GImage descriptionBox;		//background image for the box
	private GImage image;				//image to check for hovering or clicking
	private int textSize;				//font size
	
	public Description(GImage image, String description, int textSize){
		this.image 				= image;
		this.descriptionString	= description;
		this.textSize 			= textSize;
		
		setUp();
	}
	
	/**
	 * Set up the description box
	 */
	private void setUp(){
		//what will be display on inside the box
		description = new GLabel(descriptionString);
		description.setFont(FONT + Integer.toString(textSize));
		//Image for description
		descriptionBox	= new GImage("Images/VendorImages/DescriptionBox.png");
		effects 		= new Effects();
	}
	
	/**
	 * make the description box shows
	 * @param program
	 * @param e
	 */
	public void ShowDescription(MainApplication program, MouseEvent e){
		effects.PopUpWindow(image, descriptionBox, description, program, e);
	}
}
