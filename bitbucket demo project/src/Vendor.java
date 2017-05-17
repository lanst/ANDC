/**
 * <p>Buy or sell item here.</p>
 * @author Norlan Prudente
 *
 */

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import acm.graphics.GImage;
import acm.graphics.GLabel;

public class Vendor extends GraphicsPane {
	private static final int LONG_PERIOD = 1;
	private static final int DELAY = 1000;
	private static int WIDTH = 800;
	private static final String FONT = "Bernard MT Condensed-24"; 
	
	private MainApplication program;
	private Inventory inventory;
	private Player player;
	private GImage background;
	private ReturnButton returnButton;
	private GLabel totalMoney;
	private Effects effects;
	private Description heartDescription;
	private Description diamondDescription;
	private AudioPlayer music;
	private AudioPlayer click;
	private boolean loop;
	private Data data;
	
	public Vendor(MainApplication program, Player player, Data data, Inventory inventory){
		this.program = program;
		this.player = player;
		this.inventory = inventory;
		this.data = data;
		
		setUp();
		
	}
	
	@Override
	public void showContents() {
		loop = true;
		program.add(background);
		inventory.showInventory(program);
		totalMoney.setLabel("Total Money: $" + player.getMoney());
		program.add(totalMoney);
		returnButton.PlaceReturnButton(program);
		
		if (program.currentScreen() == this){
			LoopMusic();
		}
	}

	@Override
	public void hideContents() {
		loop = false;
		program.removeAll();

	}
	
	/**
	 *<p>setup the Images.</p>
	 */
	private void setUp() {
		//Sounds
		music = new AudioPlayer("Sounds/Vendor.wav");
		click = new AudioPlayer("Sounds/Clicked.wav");
		
		//background
		background = new GImage("Images/VendorImages/Vendor.png");
		
		//player's total money
		totalMoney = new GLabel("Total Money: $" + player.getMoney());
		totalMoney.setFont(FONT);
		totalMoney.setColor(Color.YELLOW);
		totalMoney.setLocation(WIDTH - totalMoney.getWidth() - 20, totalMoney.getHeight());
		
		//return button
		returnButton = new ReturnButton(0,0);
		returnButton.setLocation(WIDTH - returnButton.getSize().getWidth() - 20, 
				totalMoney.getLocation().getY() + 5);
		
		//effects
		effects = new Effects();
				
		//description of item
		heartDescription = new Description(inventory.GetItem("Heart").getImage(), inventory.GetItem("Heart").getDescription(), 16);
		diamondDescription = new Description(inventory.GetItem("Diamond").getImage(), inventory.GetItem("Diamond").getDescription(), 16);
	}
	
	/**
	 * Loop Music		 
	 */
	public void LoopMusic(){
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){
			public void run(){
				if (!music.isRunning())
					music.Play();
				
				timer.cancel();
				LoopMusic();
			}
		};
		
		if (loop)
			timer.scheduleAtFixedRate(task, DELAY, LONG_PERIOD);
		else
		{
			timer.cancel();
			task.cancel();
			music.Stop();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		//heart
		if (inventory.GetItem("Heart").getImage().contains(e.getX(), e.getY())){
			if (!inventory.GetItem("Heart").isAcquired() && player.getMoney() >= inventory.GetItem("Heart").getBuyPrice()){
				if (inventory.GetItem("Heart").getImage().contains(e.getX(), e.getY())){
					inventory.GetItem("Heart").setAcquired(true);
					player.setMoney(player.getMoney() - inventory.GetItem("Heart").getBuyPrice());
					totalMoney.setLabel("Total Money: $" + player.getMoney());
					inventory.showInventory(program);
					data.Save();
					click.Play();
				}
			}
			else if (inventory.GetItem("Heart").isAcquired()){
				if (inventory.GetItem("Heart").getImage().contains(e.getX(), e.getY())){
					inventory.GetItem("Heart").setAcquired(false);
					player.setMoney(player.getMoney() + inventory.GetItem("Heart").getBuyPrice());
					totalMoney.setLabel("Total Money: $" + player.getMoney());
					inventory.showInventory(program);
					data.Save();
					click.Play();
				}
			}
		}
		else if (inventory.GetItem("Diamond").getImage().contains(e.getX(), e.getY())){
			if (!inventory.GetItem("Diamond").isAcquired() && player.getMoney() >= inventory.GetItem("Diamond").getBuyPrice()){
				if (inventory.GetItem("Diamond").getImage().contains(e.getX(), e.getY())){
					inventory.GetItem("Diamond").setAcquired(true);
					player.setMoney(player.getMoney() - inventory.GetItem("Diamond").getBuyPrice());
					totalMoney.setLabel("Total Money: $" + player.getMoney());
					inventory.showInventory(program);
					data.Save();
					click.Play();
				}
			}
			else if (inventory.GetItem("Diamond").isAcquired()){
				if (inventory.GetItem("Diamond").getImage().contains(e.getX(), e.getY())){
					inventory.GetItem("Diamond").setAcquired(false);
					player.setMoney(player.getMoney() + inventory.GetItem("Diamond").getBuyPrice());
					totalMoney.setLabel("Total Money: $" + player.getMoney());
					inventory.showInventory(program);
					data.Save();
					click.Play();
				}
			}
		}
		
		returnButton.OnClick("StartingArea", program, e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		//item
		heartDescription.ShowDescription(program, e);
		diamondDescription.ShowDescription(program, e);
		effects.ChangeImage(inventory.GetItem("Heart").getImage(), "VendorImages/OnHeart.png", "VendorImages/Heart.png", e);
		effects.ChangeImage(inventory.GetItem("Diamond").getImage(), "VendorImages/OnDiamond.png", "VendorImages/Diamond.png", e);
		
		//return button
		returnButton.OnHover(e);
	}

}

