import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import acm.graphics.GImage;

/**
 * <p>Class responsible for holding the money and item(s).</p>
 * 
 * @author Darrel Holt
 * <p>Modified by Norlan Prudente</p>
 * Modification: Images, Animation and clean up the code.
 *
 */
public class Player {
	private static final int UP = -1;
	private static final int DOWN = 1;
	private static final int RIGHT = 1;
	private static final int LEFT = -1;
	private static final int STARTING_MONEY = 1000;
	
	private int money;									
	private HashMap<String, String> playerImages;		//holds the GImage of the player
	private GImage man;									//starting image of the player
	private boolean done;								//to stop the timer and task
	private int count;									//for changing image for animation
	private boolean isMovingUp;							//if the player is moving up
	private boolean isMovingDown;						//if the player is moving down
	private boolean isMovingLeft;						//if the player is moving left
	private boolean isMovingRight;						//if the player is moving right
	private boolean finish;								//if the player is finished walking
	
	/**
	 * Default constructor for the Player
	 * @param direction for player to face
	 * @param money for player to have
	 * @param image to represent the player
	 */
	public Player(){
		setMoney(STARTING_MONEY);									//initialize money
		done = false;
		count = 0;
		isMovingUp = false;
		isMovingDown = false;
		isMovingLeft = false;
		isMovingRight = false;
		finish = false;
		
		PutImagesToMap();
		man = new GImage(playerImages.get("man1"));
	}
	
	/**
	 * <p>Reset the money of the player.
	 */
	public void ResetMoney(){
		setMoney(STARTING_MONEY);
	}
	
	/**
	 * <p>store all the GImage of the player into a hashmap</p>
	 */
	public void PutImagesToMap(){
		playerImages = new HashMap<String, String>();
		
		//store all the images of the player for animation
		for (int i = 0; i < 12; i++){
			String name = "man" + Integer.toString(i);
			String imagePath = "Images/Man/man" + Integer.toString(i) + ".png";
			
			playerImages.put(name, imagePath); 
		}
	}
	
	/**
	 * <p>for accessing the GImages of the player</p>
	 * @return
	 */
	public GImage getMan(){
		return man;
	}
	
	/**
	 * <p>Give the starting position and place the player on the screen</p>
	 * @param program
	 * @param x
	 * @param y
	 */
	public void PlaceMan(MainApplication program,double x, double y){
		man.setLocation(x, y);
		
		program.add(man);
	}
	
	/**
	 * <p>Remove the man</p>
	 * @param program
	 */
	public void removeMan(MainApplication program){
		program.remove(man);
	}
	
	//For stopping the walking animation
	public void StopAnimation(){
		done = true;
		count = 0;
	}
	
	/**
	 * <p>return the x coordinate of the player.</p>
	 * @return
	 */
	public double getX(){
		return man.getX();
	}
	
	/**
	 * <p>return the y coordinate of the player.</p>
	 * @return
	 */
	public double getY(){
		return man.getY();
	}
	
	/**
	 * checking status of finish variable.
	 * @return
	 */
	public boolean isFinish(){
		return finish;
	}
	
	/**
	 * reset the finish
	 */
	public void ResetFinish(){
		finish = false;
	}
	
	/**
	 * animation for the player walk.
	 * @param destinationX
	 * @param destinationY
	 */
	public void AnimateMove(double destinationX, double destinationY){
		Timer time = new Timer();
		
		TimerTask task = new TimerTask(){
			public void run(){
				//moving up
				if (destinationY < man.getY() && !isMovingDown){
					isMovingUp = true;
					MoveUp(destinationY);
				}
				//moving down
				else if (destinationY > man.getY() && !isMovingUp){
					isMovingDown = true;
					MoveDown(destinationY);
				}
				
				//moving right
				if (destinationX > man.getX() && !isMovingLeft){
					isMovingRight = true;
					MoveRight(destinationX);
				}
				//moving left
				else if (destinationX < man.getX() && !isMovingRight){
					isMovingLeft = true;
					MoveLeft(destinationX);
				}
				time.cancel();
				AnimateMove(destinationX, destinationY);
			}
		};
		
		if (!done)
			time.scheduleAtFixedRate(task, 12, 1);
		else {
			//stop everything that is connected to the walking animation
			time.cancel();
			task.cancel();
			finish = true;
			//so I can reanimate when finished
			done = false;
			isMovingUp = false;
			isMovingDown = false;
			isMovingLeft = false;
			isMovingRight = false;
			
		}
	}
	
	/**
	 * check if all the movement is done.
	 * @return
	 */
	public boolean FinishMoving(){
		if (isMovingUp == false &&
			isMovingDown == false &&
			isMovingRight == false &&
			isMovingLeft == false)
			return true;
		return false;
	}
	
	/**
	 * <p>Animation and movement for moving up</p>
	 * @param destinationY
	 */
	public void MoveUp(double destinationY){
		if (destinationY >= man.getY() - 7) {
			isMovingUp = false;
			if (FinishMoving())
				done = true;
		}
		man.setLocation(man.getX(), man.getY() + UP);
		count++;
		if (count == 10)
			man.setImage(playerImages.get("man0"));
		else if (count == 20)
			man.setImage(playerImages.get("man1"));
		else if (count == 30){
			man.setImage(playerImages.get("man2"));
		}
		else if (count == 40){
			man.setImage(playerImages.get("man1"));
			count = 0;
		}
	}
	
	/**
	 * <p>Animation and movement for moving down</p>
	 * @param destinationY
	 */
	public void MoveDown(double destinationY){
		if (destinationY <= man.getY() + 5) {
			isMovingDown = false;
			if (FinishMoving())
				done = true;
		}
		man.setLocation(man.getX(), man.getY() + DOWN);
		count++;
		if (count == 10)
			man.setImage(playerImages.get("man9"));
		else if (count == 20)
			man.setImage(playerImages.get("man10"));
		else if (count == 30){
			man.setImage(playerImages.get("man11"));
		}
		else if (count == 40){
			man.setImage(playerImages.get("man10"));
			count = 0;
		}
	}
	
	/**
	 * <p>Animation and movement for moving right</p>
	 * @param destinationX
	 */
	public void MoveRight(double destinationX){
		if (destinationX <= man.getX() + 5) {
			isMovingRight = false;
			if (FinishMoving())
				done = true;
		}
		man.setLocation(man.getX() + RIGHT, man.getY());
		count++;
		if (count == 10)
			man.setImage(playerImages.get("man3"));
		else if (count == 20)
			man.setImage(playerImages.get("man4"));
		else if (count == 30){
			man.setImage(playerImages.get("man5"));
		}
		else if (count == 40){
			man.setImage(playerImages.get("man4"));
			count = 0;
		}
	}
	
	/**
	 * <p>Animation and movement for moving left</p>
	 * @param destinationX
	 */
	public void MoveLeft(double destinationX){
		if (destinationX >= man.getX() - 7) {
			isMovingLeft = false;
			if (FinishMoving())
				done = true;
		}
		man.setLocation(man.getX() + LEFT, man.getY());
		count++;
		if (count == 10)
			man.setImage(playerImages.get("man6"));
		else if (count == 20)
			man.setImage(playerImages.get("man7"));
		else if (count == 30){
			man.setImage(playerImages.get("man8"));
		}
		else if (count == 40){
			man.setImage(playerImages.get("man7"));
			count = 0;
		}
	}
	
	/**
	 * <p>Set the money</p>
	 * @param money
	 */
	public void setMoney(int money){
		this.money = money;
	}
	
	/**
	 * <p>Return the total money of the player</p>
	 * @return
	 */
	public int getMoney(){
		return this.money;
	}
}
