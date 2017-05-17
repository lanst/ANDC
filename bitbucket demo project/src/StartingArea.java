import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import acm.graphics.GImage;
import acm.graphics.GLabel;

/**
 * This is where the selection of game happen and where vendor can be access
 * @author Norlan Prudente
 *
 */
public class StartingArea extends GraphicsPane{
	private static final int DESCRIPTION_FONT_SIZE = 24;
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final String FONT = "Bernard MT Condensed-24"; 

	private MainApplication program;
	private Player player;
	private GImage background;
	private GImage poker;
	private GImage slot;
	private GImage roulette;
	private GImage blackJack;
	private GImage vendor;
	private GImage hud;
	private GLabel money;
	private AudioPlayer music;				//background music
	private AudioPlayer click;				//selection sound
	private Effects effects;				//visual effects
	private Description slotDescription;
	private Description pokerDescription;
	private Description blackJackDescription;
	private Description rouletteDescription;
	private Description vendorDescription;
	private ReturnButton returnButton;
	private int countClick;
	private boolean doneConstantCheck;

	public StartingArea(MainApplication program, Player player){
		this.program = program;
		this.player = player;

		effects = new Effects();
		countClick = 0;

		setUpSounds();
		setUpBackground();
		setUpGameIcons();
		setUpHuD();

	}

	@Override
	public void showContents(){
		program.add(background);
		program.add(slot);
		program.add(poker);
		program.add(blackJack);
		program.add(roulette);
		program.add(vendor);
		player.PlaceMan(program, (WIDTH - player.getMan().getWidth()) / 2,
				 5 * (HEIGHT - player.getMan().getHeight()) / 8);
		program.add(hud);
		money.setLabel("Money: $" + player.getMoney());
		program.add(money);
		returnButton.PlaceReturnButton(program);
		
		doneConstantCheck = false;
		
		//play the music
		if (program.currentScreen() == this){
			ConstantCheck();
			music.startLooping();
			music.LoopMusic();
		}
	}

	@Override
	public void hideContents(){
		//stop the music
		music.stopLooping();
		music.Stop();

		//clear all the graphics
		program.removeAll();
		
		player.StopAnimation();
		player.ResetFinish();
		player.removeMan(program);
		doneConstantCheck = true;
	}

	/**
	 * <p>Set up the background image of Main Menu</p>
	 */
	private void setUpBackground(){
		//set up the image to use and its size for the background
		background = new GImage("Images/StartingAreaImages/startingArea.png");
		background.setSize(WIDTH, HEIGHT);
	}

	/**
	 * <p>Set up the icon of the games.</p>
	 */
	private void setUpGameIcons() {
		//slot
		slot = new GImage("Images/StartingAreaImages/slot.png");
		slot.setSize(slot.getWidth() * 1.5, slot.getHeight() * 1.5);
		slot.setLocation(WIDTH - (slot.getWidth() + 20), 20);

		//poker
		poker = new GImage("Images/StartingAreaImages/pokerTable.png");
		poker.setSize(poker.getWidth() * 1.3, poker.getHeight() *1.3);
		poker.setLocation(20,HEIGHT - (poker.getHeight() * 1.5));

		//Black Jack
		blackJack = new GImage("Images/StartingAreaImages/blackJackTable.png");
		blackJack.setSize(blackJack.getWidth() * 1.3, blackJack.getHeight() *1.3);
		blackJack.setLocation(WIDTH - (blackJack.getWidth() + 20), HEIGHT -(blackJack.getHeight() * 1.5));

		//roulette
		roulette = new GImage("Images/StartingAreaImages/roullete table.png");
		roulette.setSize(roulette.getWidth() * 2, roulette.getHeight() * 2);
		roulette.setLocation(20, 20);

		//vendor
		vendor = new GImage("Images/StartingAreaImages/Vendor.png");
		vendor.setSize(vendor.getWidth() * 2, vendor.getHeight() * 2);
		vendor.setLocation((WIDTH - vendor.getWidth())/2, 0);

		//show the names
		slotDescription 		= new Description(slot, "Slots", DESCRIPTION_FONT_SIZE);
		pokerDescription 		= new Description(poker, "Poker", DESCRIPTION_FONT_SIZE);
		blackJackDescription 	= new Description(blackJack, "Black Jack", DESCRIPTION_FONT_SIZE);
		rouletteDescription		= new Description(roulette, "Roulette", DESCRIPTION_FONT_SIZE);
		vendorDescription		= new Description(vendor, "Vendor", DESCRIPTION_FONT_SIZE);
	}

	/**
	 * <p>Set up the sounds</p>
	 */
	private void setUpSounds() {
		music = new AudioPlayer("Sounds/StartingArea.wav");
		click = new AudioPlayer("Sounds/Clicked.wav");
	}

	/**
	 * <p>Set up the HUD of the game</p>
	 */
	private void setUpHuD() {
		//hud
		hud = new GImage("Images/StartingAreaImages/HUD.png");
		hud.setLocation(0, HEIGHT - hud.getHeight());
		money = new GLabel("Money: $" + player.getMoney());
		money.setFont(FONT);
		money.setLocation(WIDTH - (money.getWidth() + 20), HEIGHT + 5 - money.getHeight());

		returnButton = new ReturnButton(0, hud.getY());
	}

	/**
	 * Anything that needs to be check all the time		 
	 */
	public void ConstantCheck(){
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){
			public void run(){
				//to re enable mouse click for moving
				if (player.isFinish()){
					countClick = 0;
					player.ResetFinish();
				}
				
				//to play slot
				if (slot.contains(player.getMan().getX(), player.getMan().getY())) {
					click.Play();
					program.switchToSlot();
				}
				//to play roulette
				else if (roulette.contains(player.getMan().getX(), player.getMan().getY())) {
					click.Play();
					program.switchToRoulette();
				}
				//to play poker
				else if (poker.contains(player.getMan().getX(), player.getMan().getY())) {
					click.Play();
					//program.switchToPoker();
					program.switchToUnderconstruction();
				}
				//to play black jack
				else if (blackJack.contains(player.getMan().getX(), player.getMan().getY())) {
					click.Play();
					program.switchToBlackJack();
					//program.switchToUnderconstruction();
				}
				//to go to the vendor
				else if(vendor.contains(player.getMan().getX(), player.getMan().getY())) {
					click.Play();
					program.switchToVendor();
				}
				
				timer.cancel();
				ConstantCheck();
			}
		};
		
		if (!doneConstantCheck)
			timer.scheduleAtFixedRate(task, 1000, 1);
		else{
			timer.cancel();
			task.cancel();
		}
	}

	/**
	 * <p>When the mouse is pressed</p>
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		//disable mouse click after a single click for walking
		if (countClick == 0){
			player.AnimateMove(e.getX(), e.getY());
			countClick++;
		}
		
		//return to main menu
		returnButton.OnClick("MainMenu", program, e);
	}

	@Override
	public void mouseMoved(MouseEvent e){
		//slot
		effects.PopUp(slot, e);
		slotDescription.ShowDescription(program, e);

		//roulette
		effects.PopUp(roulette, e);
		rouletteDescription.ShowDescription(program, e);

		//black jack
		effects.PopUp(blackJack, e);
		blackJackDescription.ShowDescription(program, e);

		//poker
		effects.PopUp(poker, e);
		pokerDescription.ShowDescription(program, e);

		//vendor
		effects.PopUp(vendor, e);
		vendorDescription.ShowDescription(program, e);

		//return button
		returnButton.OnHover(e);
	}
}
