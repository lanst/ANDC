/**
 * <p>This class will be responsible for start menu and main menu.
 * 
 * @author Norlan Prudente
 *
 */

import java.awt.event.MouseEvent;
import java.io.File;

import acm.graphics.GImage;

public class MainMenu extends GraphicsPane {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	//Variables
	private MainApplication program;	//to control switches of screen
	private GImage background;			//background image of Game
	private GImage clickToPlay;			//Image that say Click to Play
	private GImage newGame;				//Image that for New Game
	private GImage load;				//Image that for Load Game
	private GImage exit;				//Image that for Exit
	private GImage credits;				//Image for credits
	private boolean isMainMenu;			//to confirm start menu is gone
	private Data data;					//For loading
	private AudioPlayer music;			//background music
	private AudioPlayer click;			//when player clickToPlay and choose a selection from menu
	private Effects effects;			//visual effects
	private Player player;
	private Inventory inventory;
	
	//constructor
	public MainMenu(MainApplication app, Player player, Data data, Inventory inventory){
		this.program = app;
		this.data = data;
		this.player = player;
		this.inventory = inventory;
		
		//whether we are on start menu or main menu
		isMainMenu = false;
		
		effects = new Effects();

		setUpBackground();
		setUpButtons();
		setUpSounds();
	}

	@Override
	public void showContents(){
		//show the background
		program.add(background);

		//play the music
		if (program.currentScreen() == this) {
			music.startLooping();
			music.LoopMusic();
		}
		
		if (!isMainMenu)
			program.add(clickToPlay);
		else {
			//remove the click to play
			program.remove(clickToPlay);

			//change the background image
			background.setImage("Images/MainMenuImages/MainMenu.jpg");
			background.setSize(WIDTH, HEIGHT);

			//add the 3 choices to select from
			program.add(newGame);
			program.add(load);
			program.add(exit);
			program.add(credits);
		}
	}

	@Override
	public void hideContents(){
		//stop the music
		music.stopLooping();
		music.Stop();

		//remove all that is currently on the screen
		program.removeAll();
	}

	/**
	 * <p>Set up the background image of Main Menu</p>
	 */
	private void setUpBackground(){

		//set up the image to use and its size for the background
		background = new GImage("Images/MainMenuImages/logo.jpg");
		background.setSize(WIDTH, HEIGHT);

		//set up the image to use and its size for the clickToPlay
		clickToPlay = new GImage("Images/MainMenuImages/ClickToPlay.png");
		clickToPlay.setLocation((WIDTH - clickToPlay.getWidth())/2, 7*HEIGHT/8);
	}

	/**
	 * <p>Set up the selection of button</p>
	 */
	private void setUpButtons(){
		//set up the image to use and its size for the new game
		newGame = new GImage("Images/MainMenuImages/NewGame.png");
		newGame.setLocation((WIDTH - newGame.getWidth())/2, 6*HEIGHT/9);

		//set up the image to use and its size for the load
		load = new GImage("Images/MainMenuImages/Load.png");
		load.setLocation((WIDTH - load.getWidth())/2, (6*HEIGHT/9) + newGame.getHeight());

		//set up the image to use and its size for the exit
		exit = new GImage("Images/MainMenuImages/Exit.png");
		exit.setLocation((WIDTH - exit.getWidth())/2, (6*HEIGHT/9) + (2*newGame.getHeight()));
		
		//set up the image to use and its size for the load
		credits = new GImage("Images/MainMenuImages/CreditButton.png");
		credits.setSize(credits.getWidth() * 1.3, credits.getHeight() * 1.3);
		credits.setLocation((WIDTH - credits.getWidth())/2, (6*HEIGHT/9) + (3*newGame.getHeight()));
	}
	
	/**
	 * Set up all the sounds necessary for main menu
	 */
	public void setUpSounds(){
		music = new AudioPlayer("Sounds/MaiMenu.wav");
		click = new AudioPlayer("Sounds/Clicked.wav");
	}
	
	/**
	 * <p>When the mouse is pressed</p>
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		//if we are in the main menu(selection screen)
		if(!isMainMenu) {
			click.Play();
			//set it to true that we are already in the main menu
			isMainMenu = true;
			showContents();

		}
		//when selecting
		else {
			//new game
			if (newGame.contains(e.getX(), e.getY())) {
				//play sound effect
				click.Play();
				
				//reset money
				player.ResetMoney();
				
				//reset inventory
				inventory.ResetInventory();
				
				//save
				data.Save();
				
				//switch to Starting Area
				program.switchToStartingArea();
			}
			//load game
			else if (load.contains(e.getX(), e.getY())) {
				//check if file exist
				File f = new File("Save.data");

				//if it does load the game
				if(f.exists() && !f.isDirectory()) { 
					//read the file
					data.Load();
					
					//play sound
					click.Play();
					
					//switch to roulette game
					program.switchToStartingArea();
				}
			}
			//exit the program
			else if (exit.contains(e.getX(), e.getY())) {
				System.exit(14);
			}
			else if (credits.contains(e.getX(), e.getY())){
				click.Play();
				program.switchToCredit();
			}
		}

	}
	/**
	 * The object will follow the mouse location
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		//visual effects on selection menu
		effects.PopUp(newGame, e);
		effects.PopUp(load, e);
		effects.PopUp(exit, e);
		effects.PopUp(credits, e);
	}
}
