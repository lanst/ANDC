/**
 * @Author Darrel Holt
 */

import java.awt.Color;

import acm.graphics.GDimension;
import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.graphics.GPolygon;

public class RouletteImageLibrary {

	
	private static final String GAME_FONT = "Bernard MT Condensed-22";
	protected final int MOVE_ALL_Y = 62;
	protected final int MOVE_OUTSIDE_BETS_X = 100;
	protected final int MOVE_ZERO_X = 49;
	protected final int MOVE_DOZENS_X = 195;
	protected final int MONEY_Y = 20;
	protected final int MONEY_X = 650;
	protected final int WINDOW_HEIGHT = 600;
	protected final int WINDOW_WIDTH = 800;
	protected final double WHEEL_HEIGHT = 397 * .7;
	protected final double WHEEL_WIDTH = 396 * .7;
	protected final int DOZEN_WIDTH = 243 * 4/5 + 4;
	protected final int ZERO_HEIGHT = 216*4/5;
	protected final int ZERO_WIDTH = 61*4/5;
	protected final int NUMBER_HEIGHT = 78*4/5;
	protected final int NUMBER_WIDTH = 66*4/5;
	protected final int OUTSIDE_BET_HEIGHT = 77*4/5;
	protected final int OUTSIDE_BET_WIDTH = 125*4/5;

	/*
	 * sizes to shrink the images to fit other things on the screen
	 */
	GDimension numberSize = new GDimension(NUMBER_WIDTH, NUMBER_HEIGHT);
	GDimension zeroSize = new GDimension(ZERO_WIDTH, ZERO_HEIGHT);
	GDimension evenOddRedBlackHighLowSize = new GDimension(OUTSIDE_BET_WIDTH, OUTSIDE_BET_HEIGHT);
	GDimension dozenSize = new GDimension(DOZEN_WIDTH, NUMBER_HEIGHT);
	GDimension wheelSize = new GDimension(WHEEL_WIDTH, WHEEL_HEIGHT);

	/* button to return to the starting area */
	GImage returnButton = new GImage("Images/returnButton.png");

	/* button to place out bet */
	GImage betButton = new GImage("Images/Bet.png");
	
	/* button to change the bet value */
	GImage changeBetButton = new GImage("Images/ChangeBet.png");

	/* Players money */
	public GLabel currentMoney;
	
	public GLabel betLabel;
	public GLabel totalBetLabel;
	

	/**
	 * Arrays to hold images
	 */
	public GImage[] tableNumbers = new GImage[37];
	public GImage[] tableDozens = new GImage[3];
	public GImage[] tableOutside = new GImage[6];
	
	private String[] outside = {"low", "even", "red", "black", "odd", "high"};

	// Only one wheel so no need for a data structure
	public GImage wheel =  new GImage("Images/rouletteImages/rouletteWheel.png");

	// Only one ball so no need for a data structure
	public GPolygon ball;

	/**
	 * 
	 * Used for populating the locations of the numbered table tiles
	 * @param arr - manipulate the locations of it's elements
	 * @param start - where to begin manipulation
	 * @param newX - new X location for the tile
	 * @param newY - new Y location for the tile
	 * @param mod - how many tiles to skip based on the row they are in
	 * @param dx - how much to change it's X location by
	 * @param dy - how much to change it's Y location by
	 */
	private void populateTableNumberLocations(GImage[] arr, int start, int newX, int newY,int dx, int dy, int mod) {
		for(int i = 0; i < arr.length/3; i++) {
			arr[start+(i*mod)].setLocation(newX * (i+1) + dx, newY + dy);
		}
	}

	/**
	 * Used for populating the locations of the dozen table tiles
	 * @param arr - manipulate the locations of it's elements
	 * @param newX - new X location for the tile
	 * @param newY - new Y location for the tile
	 * @param dy - how much to change it's Y location by
	 */
	private void populateDozenLocations(GImage[] arr, int newX, int newY, int dy) {
		for(int i = 0; i < arr.length; i++) {
			if(i == 0){
				arr[i].setLocation(newX - 1 , newY + dy);
			}else{
				arr[i].setLocation(newX + MOVE_DOZENS_X*i + 2 , newY + dy);
			}
		}
	}	

	/**
	 * 
	 * @param arr - fill the array for the numbered tiles with the respective GImages
	 */
	private void populateTableNumbers(GImage[] arr) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] = new GImage("Images/rouletteImages/" + Integer.toString(i) + ".jpg");
		}
	}

	/**
	 * 
	 * @param arr - fill the array for the dozen tiles with the respective GImages
	 */
	private void populateTableDozens(GImage[] arr) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] = new GImage("Images/rouletteImages/dozen" + Integer.toString(i+1) + ".jpg");
		}
	}

	/**
	 * 
	 * @param arr - fill the array for the dozen tiles with the respective GImages
	 */
	private void populateTableOutside(GImage[] arr) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] = new GImage("Images/rouletteImages/" + outside[i] + ".jpg");
		}
	}

	/**
	 * Used for populating the locations of the dozen table tiles
	 * @param arr - manipulate the locations of it's elements
	 * @param newX - new X location for the tile
	 * @param newY - new Y location for the tile
	 * @param dy - how much to change it's Y location by
	 */
	private void populateOutsideLocations(GImage[] arr) {
		for(int i = 0; i < arr.length; i++) {
			if(i == 0){
				arr[i].setLocation(MOVE_ZERO_X - 1 , MOVE_ALL_Y*4 - 3);
			}else{
				arr[i].setLocation(MOVE_ZERO_X + MOVE_OUTSIDE_BETS_X*i - 2*i , MOVE_ALL_Y*4 - 3);
			}
		}
	}	

	/**
	 * Container constructor for the images used on the RouletteBoard
	 */
	public RouletteImageLibrary(){	

		setupTable();
		
		// put the wheel where it belongs
		wheel.setLocation(MOVE_ZERO_X, MOVE_ALL_Y*5 + 5);

		// create the ball, make it a ball, set its color to white
		ball = new GPolygon(0, 0);
		ball.addArc(15, 15, 90, 360);
		ball.setFillColor(Color.white);
		ball.setFilled(true);
	}

	public void setupTable(){
		// position the return button
		returnButton.setLocation(WINDOW_WIDTH - returnButton.getWidth(), WINDOW_HEIGHT - returnButton.getHeight());
		
		// position the bet button
		betButton.setSize(betButton.getWidth(), betButton.getHeight());

		// position the money and set it's label
		currentMoney = new GLabel("Money: ", MONEY_X, MONEY_Y);
		currentMoney.setFont(GAME_FONT);
		
		// position the bet button relative to the money
		betButton.setLocation(currentMoney.getX(), currentMoney.getY() + currentMoney.getHeight());
		
		// position the betting value and set it's label
		betLabel = new GLabel("Bet: 100", betButton.getX(), betButton.getY() + 2*betButton.getHeight());
		betLabel.setFont(GAME_FONT);
		
		// position the changeBetButton under the bet label
		changeBetButton.setLocation(betLabel.getX(), betLabel.getY() + betLabel.getHeight());

		// populate all of the table tiles
		populateTableTiles();

		// The size of zero is unique
		tableNumbers[0].setSize(zeroSize);

		// set the size for the table numbers
		for(int i = 1; i < tableNumbers.length; i++){
			tableNumbers[i].setSize(numberSize);
		}
		
		// set the size for the outside bets
		for(int i = 0; i < tableOutside.length; i++){
			tableOutside[i].setSize(evenOddRedBlackHighLowSize);
		}
		
		// set the size of the dozen bet tiles
		for(int i = 0; i < tableDozens.length; i++){
			tableDozens[i].setSize(dozenSize);
		}
		
		// resize the wheel
		wheel.setSize(wheelSize);
		
		//Zero stays because it's a unique tile	
		tableNumbers[0].setLocation(0, 5);										

		// set the locations of the number tiles beginning with the top row
		populateTableNumberLocations(tableNumbers, 3, MOVE_ZERO_X, 0, -1, 0, 3);			  		
		populateTableNumberLocations(tableNumbers, 2, MOVE_ZERO_X, MOVE_ALL_Y, -1, -3, 3);			
		populateTableNumberLocations(tableNumbers, 1, MOVE_ZERO_X, MOVE_ALL_Y*2, -1, -3, 3); 	
		
		// set the locations of the dozen tiles	
		populateDozenLocations(tableDozens, MOVE_ZERO_X, MOVE_ALL_Y*3, -3);		
		
		// set the locations of the outside tiles
		populateOutsideLocations(tableOutside);
		
		// position the cumulative bets
		totalBetLabel = new GLabel("Total Bets: 0", tableOutside[5].getX(), tableOutside[5].getY() + 1.5*tableOutside[5].getHeight());
		totalBetLabel.setFont(GAME_FONT);
	}
	
	public void populateTableTiles(){
		// Create the table's Even, Odd, etc. (outside bets)
		populateTableOutside(tableOutside);

		// Create the table numbers (inside bets)
		populateTableNumbers(tableNumbers);

		// Create the table dozen (inner-outside bets)
		populateTableDozens(tableDozens);
	}
}