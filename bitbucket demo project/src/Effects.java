import java.awt.event.MouseEvent;

import acm.graphics.GImage;
import acm.graphics.GLabel;

/**
 * class for various visual effects
 * @author Norlan Prudente
 *
 */
public class Effects {

	private static final int HEIGHT = 600;
	private static final int WIDTH = 800;
	private static final double POPUP_WINDOW_ADJUSTER = 1.5;
	private static final double POPUP_SIZE_ADJUSTER = 1.2;
	private boolean isIn;			//check if the mouse is inside the image
	private GImage activeButton;	//store the last image that was hover in

	public Effects(){
		isIn = false;
	}

	/**
	 * <p>Gives the effect of making the 
	 * image bigger when hovering over them</p>
	 * 
	 * @param image
	 * @param e
	 */
	public void PopUp(GImage image, MouseEvent e) {
		//when hovering increase the size
		if (image.contains(e.getX(), e.getY()) && !isIn) {
			image.setSize(image.getWidth() * POPUP_SIZE_ADJUSTER, image.getHeight() * POPUP_SIZE_ADJUSTER);
			isIn 			= true;
			activeButton 	= image;
		}
		//when not decrease them
		else if (!image.contains(e.getX(), e.getY()) && isIn && activeButton.equals(image)) {
			image.setSize(image.getWidth() / POPUP_SIZE_ADJUSTER, image.getHeight() / POPUP_SIZE_ADJUSTER);
			isIn = false;
		}
	}

	/**
	 * <p>Change the Image
	 * @param image
	 * @param newImage
	 * @param e
	 */
	public void ChangeImage(GImage image, String newImage, String oldImage, MouseEvent e) {
		//when hovering change the image
		if (image.contains(e.getX(), e.getY()) && !isIn) {
			image.setImage("Images/" + newImage);
			isIn 			= true;
			activeButton 	= image;
		}
		//when not change it back
		else if (!image.contains(e.getX(), e.getY()) && isIn && activeButton.equals(image)) {
			image.setImage("Images/" + oldImage);
			isIn = false;
		}
	}

	/**
	 * <p>Window will appear and disappear on mouse hover.</p>
	 * @param image
	 * @param label
	 * @param program
	 * @param e
	 */
	public void PopUpWindow(GImage image, GImage box, GLabel label, MainApplication program, MouseEvent e) {
		//when hovering show the window
		if (image.contains(e.getX(), e.getY()) && !isIn) {
			box.setSize(label.getWidth() * POPUP_WINDOW_ADJUSTER, label.getHeight() * POPUP_WINDOW_ADJUSTER);
			program.add(box);
			program.add(label);

			isIn 			= true;
			activeButton 	= image;
		}
		//when not hide the window
		else if (!image.contains(e.getX(), e.getY()) && isIn && activeButton.equals(image)) {
			program.remove(box);
			program.remove(label);
			isIn = false;
		}
		//normal location
		box.setLocation(e.getX(), e.getY());
		label.setLocation(e.getX() + (box.getWidth() - label.getWidth())/2,
				e.getY() + (box.getHeight() + label.getHeight())/2.2);

		//if off the screen
		if ((box.getLocation().getX() + box.getWidth() > WIDTH) ||
				(box.getLocation().getY() + box.getHeight() > HEIGHT)){
			box.setLocation(e.getX() - box.getWidth(), e.getY() - box.getHeight());
			label.setLocation((e.getX() + (box.getWidth() - label.getWidth())/2) - box.getWidth(),
					(e.getY() + (box.getHeight() + label.getHeight())/2.2) - box.getHeight());
		}
	}
}
