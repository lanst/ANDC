/**
 * @Author Darrel Holt
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import acm.graphics.*;
import acm.util.RandomGenerator;

public class RouletteBoard extends GraphicsPane {

	private static final int DIALOG_HEIGHT = 320;
	private static final int DIALOG_WIDTH = 420;
	private static final int DIALOG_Y = 150;
	private static final int DIALOG_X = 160;
	private static final double DIALOG_LABEL_X = DIALOG_X + DIALOG_WIDTH/4.5;
	private static final double DIALOG_LABEL_Y = DIALOG_Y+DIALOG_HEIGHT/1.8;
	private static final String GAME_FONT = "Bernard MT Condensed-72";
	private static final double SPEED_INCREMENT = 0.05;
	private static final double BASE_BALL_MOVEMENT = 4.3;
	private static final double BALL_ANGLE_INCREMENT = 0.03875;
	private static final double WOOD_SECTION_RADIUS = 2.9;
	private static final double ENDING_SPEED = 95.5;
	private static final double[] BALL_TWEAKS = {3.5, 2.7, 2.1, 1.8, 0.5};
	private static final int BACKGROUND_BLUE = 50;
	private static final int BACKGROUND_GREEN = 123;
	private static final int BACKGROUND_RED = 1;
	private RouletteGame game;											// instantiate the RouletteGame
	private MainApplication program;									// instantiate the MainApplication
	private RouletteImageLibrary imgLib = new RouletteImageLibrary();	// instantiate the ImageLirary
	private AudioPlayer bloop;
	private AudioPlayer click;
	private AudioPlayer music;
	private boolean loop;

	/* Global variables for functions here - They must be global for some functions to work */
	private double theta = 0.0;												// Angle used to calculate trajectory for the RouletteBall to move along
	private boolean stopBall = false;										// must be global to be modified within the TimerTask - used to determine when to stop the ball from moving
	private double speed = 1;												// Same as stopBall (must be global...), used inside the TimerTask - determines how to slow the ball down
	private boolean returnButtonClicked = false;							// used to stop the ball from spinning on the starting area when you leave the Roulette game
	private int betAmount;													// used for the integer value on the label and passing the new money back to the player
	private int totalBetAmount;												// used to keep track of all of the bets the user has made.
	private ArrayList<Integer> relativeBetAmount = new ArrayList<Integer>();// used to relate each betting range/type to it's respective bet amount
	private boolean betButtonPressed = false;								// used to determine if the player has pressed the bet button, if so then it can't be pressed again until the round is over
	private RandomGenerator rgen = RandomGenerator.getInstance();			// used to randomize which number the ball will land on
	private Effects effects = new Effects();								// visual effects
	private boolean openFrame = false;										// determine if we have the betInput open or not
	JFrame betInput = new JFrame("New Bet Value");							// global to allow forcing it to keep focus in different areas of the game
	JComboBox<String> betList = new JComboBox<String>();
	JTextField newBetValue = new JTextField();
	ArrayList<String> betListValues = new ArrayList<String>();
	int selected = 0;
	int winningsOrLosings = 0;

	private ArrayList<GImage> transparentTiles = new ArrayList<GImage>();	// hold all of the tranparent tiles to remove them when we reset the board
	ArrayList<GImage> allTableTiles = new ArrayList<GImage>();				// hold all of the table tiles
	ArrayList<GImage> currentTableTiles = new ArrayList<GImage>();	// hold all of the table tiles that are currently on the board

	/* red and black can't be generated because they're unique */
	private final int[] redRange = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
	private final int[] blackRange = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};

	protected int[] evenRange = new int[18];
	private int[] oddRange = new int[18];
	private int[] lowRange = new int[18];
	private int[] highRange = new int[18];
	private int[] dozen1Range = new int[12];
	private int[] dozen2Range = new int[12];
	protected int[] dozen3Range = new int[12];

	/* This is required to determine the speed of the ball for which number it will land in
	 it is the order of the numbers on the wheel in counter-clockwise order */
	private final int[] orderOfWheel = {0, 26, 3, 35, 12, 28, 7, 29, 18, 22, 9, 31, 14, 20, 1, 33, 16, 24, 5, 10, 23, 8, 30, 11, 36, 13, 27, 6, 34, 17, 25, 2, 21, 4, 19, 15, 32};

	/* ArrayList to hold all integer values that the player has chosen to bet on */
	ArrayList<int[]> bettingRange = new ArrayList<int[]>();					
	/* ArrayList to hold all String values for the ranges the player is betting on */
	ArrayList<String> betTypeRange = new ArrayList<String>();					
	/* link each betType<String> to the bettingRange<Integer>, this data structure is used because it's iterable */
	Entry<ArrayList<String>, ArrayList<int[]>> betAmountRange;			


	/* This data structure will map a single integer (the wheel number) to one double (slow time interval) */
	Map<Integer,Double> numberSpeed = new HashMap<Integer, Double>();

	/*
	 * Create an object that maps a string value (key)
	 * to it's representative GImage which in turn maps to a betting range.
	 * This allows for a beautifully elegant solution to avoiding an if-else if chain for
	 * detecting which table tile for the range bets is chosen
	 */
	Map<String, Entry<GImage, int[]>> allBettingRanges = new HashMap<String, Entry<GImage, int[]>>();

	Integer generatedWheelNumber;

	// Used to reset the background color for other panes
	Color origBg;
	// background color for the board
	Color bg = new Color(BACKGROUND_RED, BACKGROUND_GREEN, BACKGROUND_BLUE);

	private Player player;
	private Data data;

	/**
	 * Constructor
	 * @param app - used to allow swapping between panes
	 * @param player - used to hold the money between games
	 * @param data - used to save dynamically
	 * @param inventory - used to account for the items the player has
	 */
	public RouletteBoard(MainApplication app, Player player, Data data, Inventory inventory){
		this.player = player;
		game = new RouletteGame(player, inventory);
		program = app;								
		origBg = program.getBackground();			//get the background color to return it to how it was before
		calculateNumberSpeed();				
		this.data = data;
		bloop = new AudioPlayer("Sounds/tile_clicked.wav");
		click = new AudioPlayer("Sounds/Clicked.wav");
		music = new AudioPlayer("Sounds/casino_music.wav");
	}
	/**
	 * Loop Music
	 * @Author Norlan Prudente		 
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
			timer.scheduleAtFixedRate(task, 1000, 1);
		else
		{
			timer.cancel();
			task.cancel();
			music.Stop();
		}
	}

	/** generate the numerical ranges for the different types of bets
	 * @author Osvaldo Jimenez
	 * @param arr - array to hold the populated values (the size must have been stated on declaration)
	 * @param low - the number to start on
	 * @param mod - the number to increment by
	 */
	private void populateRange(int[] arr, int low, int mod) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] = low+(i*mod);
		}
	}


	/**
	 * This doesn't include the individual numbers because they aren't ranges
	 * thus, they don't need to be generated or mapified
	 */
	private void mapifyAllBettingRanges(){
		allBettingRanges.put("low", new SimpleEntry<GImage, int[]>(imgLib.tableOutside[0], lowRange));
		allBettingRanges.put("even", new SimpleEntry<GImage, int[]>(imgLib.tableOutside[1], evenRange));
		allBettingRanges.put("red", new SimpleEntry<GImage, int[]>(imgLib.tableOutside[2], redRange));
		allBettingRanges.put("black", new SimpleEntry<GImage, int[]>(imgLib.tableOutside[3], blackRange));
		allBettingRanges.put("odd", new SimpleEntry<GImage, int[]>(imgLib.tableOutside[4], oddRange));
		allBettingRanges.put("high", new SimpleEntry<GImage, int[]>(imgLib.tableOutside[5], highRange));
		allBettingRanges.put("dozen1", new SimpleEntry<GImage, int[]>(imgLib.tableDozens[0], dozen1Range));
		allBettingRanges.put("dozen2", new SimpleEntry<GImage, int[]>(imgLib.tableDozens[1], dozen2Range));
		allBettingRanges.put("dozen3", new SimpleEntry<GImage, int[]>(imgLib.tableDozens[2], dozen3Range));

		/* put all of the current user selections into a SimpleEntry for some beautiful stuff later */
		betAmountRange = new SimpleEntry<ArrayList<String>, ArrayList<int[]>>(betTypeRange, bettingRange);
	}


	/**
	 * Generates ranges that aren't red or black; they are hard coded because their values are sporadic
	 */
	private void generateAllRanges(){
		populateRange(evenRange, 2, 2);
		populateRange(oddRange, 1, 2);
		populateRange(lowRange, 1, 1);
		populateRange(highRange, 19, 1);
		populateRange(dozen1Range, 1, 1);
		populateRange(dozen2Range, 13, 1);
		populateRange(dozen3Range, 25, 1);
	}


	@Override
	public void showContents() {
		loop = true;
		if (program.currentScreen() == this)
			LoopMusic();
		

		betAmount = game.getBetAmount();
		program.add(imgLib.betLabel);
		program.add(imgLib.totalBetLabel);

		/* put all of the betting ranges into a map to be iterated through rather than a handful of if-else-if's */
		mapifyAllBettingRanges();

		/* generate the ranges for tiles that encompass a range of numbers */
		generateAllRanges();		

		/* make the background color match the board */
		program.setBackground(bg);

		/* Add the buttons */
		program.add(imgLib.returnButton);
		program.add(imgLib.betButton);
		program.add(imgLib.changeBetButton);

		/* Add the Roulette Wheel */
		program.add(imgLib.wheel);

		/* acts to set and reset the table tiles */
		resetBoard();

		/* Update the players money label before displaying it */
		updateMoneyLabel();

		/* Display the players money */
		program.add(imgLib.currentMoney);
	}


	/**
	 * reset all of the tiles on the board
	 */
	private void resetBoard(){
		/* put every table tile into the ArrayList */
		for(GImage tile : imgLib.tableNumbers)
			allTableTiles.add(tile);
		for(GImage tile : imgLib.tableDozens)
			allTableTiles.add(tile);
		for(GImage tile : imgLib.tableOutside)
			allTableTiles.add(tile);

		int programElements = program.getElementCount();
		/* determine the tiles currently on the board */
		for(int i = 0; i < allTableTiles.size(); i++)
			for(int j = 0; j < programElements; j++)
				if(allTableTiles.get(i).equals(program.getElement(j)))
					currentTableTiles.add(allTableTiles.get(i));

		/* if there are any tiles currently on the board, remove them */
		if(currentTableTiles.size() != 0)
			for(GImage tile : currentTableTiles)
				program.remove(tile);

		/* remove the transparent tiles as well */
		if(transparentTiles.size() != 0)
			for(GImage transparentTile : transparentTiles)
				program.remove(transparentTile);


		/*
		 * The following 3 loops will place new tiles on the table
		 */
		for(int i = 0; i < imgLib.tableOutside.length; i++)		// place the outside bets on the board
			program.add(imgLib.tableOutside[i]);
		for(int i = 0; i < imgLib.tableNumbers.length; i++)		// Put the number tiles on the board (inside bets)
			program.add(imgLib.tableNumbers[i]);
		for(int i = 0; i < imgLib.tableDozens.length; i++)		// Put the dozen tiles on the board (inner-outside bets)
			program.add(imgLib.tableDozens[i]);


	}


	@Override
	public void hideContents() {
		/* return the background color to what it was before */
		program.setBackground(origBg);
		program.removeAll();
		loop = false;
	}


	/**
	 * update the label to reflect the player's current money
	 */
	private void updateMoneyLabel(){
		imgLib.currentMoney.setLabel("Money: " + player.getMoney());
	}


	/**
	 * update the label to reflect the player's most recently made and next default bet value
	 */
	private void updateBetLabel(){
		imgLib.betLabel.setLabel("Bet: " + betAmount);
	}

	private int getTotalBetAmount(){
		totalBetAmount = 0;
		if(relativeBetAmount.isEmpty())
			return 0;

		for(int bet : relativeBetAmount)
			totalBetAmount += bet;
		return totalBetAmount;
	}

	private void updateTotalBetLabel(){
		imgLib.totalBetLabel.setLabel("Total Bets: " + getTotalBetAmount());
	}


	/**
	 * use trig to make the ball go around the wheel
	 */
	private void rotateTheBall(){
		/* used to determine if the player is trying to leave the game early */
		if(!returnButtonClicked)
			program.add(imgLib.ball);

		double wheelCenterX = imgLib.wheel.getX() + imgLib.wheel.getWidth()/2;
		double wheelCenterY = imgLib.wheel.getY() + imgLib.wheel.getHeight()/2;
		double wheelRadius = imgLib.wheel.getWidth()/WOOD_SECTION_RADIUS;

		/* Ensure theta doesn't grow too large and make sure it does a full circle each time */
		if(theta > (2*Math.PI))									
			theta = 0.0;

		/* the smaller the angle (theta) is, the smaller the gap is between subsequent ball locations */
		theta += BALL_ANGLE_INCREMENT;		

		/* new ball location depending on where we are on the circle relative to the center of the wheel */
		double x = wheelCenterX + wheelRadius*Math.sin(theta);
		double y = wheelCenterY + wheelRadius*Math.cos(theta);

		/* imagine we're placing the balls center, not it's last added vertex (top of the ball)
		 * this allows for a circular rotation rather than elliptical */
		imgLib.ball.setLocation(x,y - imgLib.ball.getHeight()/2);
	}


	@Override
	public void mouseMoved(MouseEvent e){
		/* effects for the betting and return buttons */
		effects.ChangeImage(imgLib.returnButton, "invertedReturnButton.png", "returnButton.png", e);
		effects.ChangeImage(imgLib.betButton, "invertedBet.png", "Bet.png", e);
		effects.ChangeImage(imgLib.changeBetButton, "invertedChangeBet.png", "ChangeBet.png", e);
	}


	/**
	 * clear all types (String) and ranges (int[]) that the player bet on
	 */
	public void clearPlayerBets(){
		betAmountRange.getValue().clear();
		betAmountRange.getKey().clear();
	}

	/**
	 * Only reset the board and clear the players bet values if you are still in the game
	 * @param returnButtonStatus - status for if you've pressed the return button
	 */
	public void doReset(boolean returnButtonStatus){
		relativeBetAmount.clear();
		clearPlayerBets();
		if(returnButtonStatus == false)
			resetBoard();
	}

	/**
	 * Spins the wheel and resets the properties associated with the spin for each spin
	 */
	private void SpinWheel(){
		for(int i = 0; i < program.getElementCount(); i++){			// Determine if the ball is on the GCanvas (remove it each time this is called to avoid duplicates)
			if(program.getElement(i) == imgLib.ball){
				program.remove(imgLib.ball);
			}else{												
				program.add(imgLib.ball);						// There is no ball, put the ball on the GCanvas
			}
		}
		returnButtonClicked = false;							// Reset the return button status if the player left and came back
		theta = 0.0;											// Resets the balls starting position for the spin
		stopBall = false;										// Reset the ball status so it can spin again
		speed = 0;												// Reset the speed so the ball starts off fast
		startSpin();											// Begin the spin
	}

	/**
	 * Player returning to StartingArea:
	 * update the money for the player and clear all bets made
	 */
	public void returning(){
		returnButtonClicked = true;							// allows exiting the game if the ball is still spinning
		player.setMoney(game.getMoney());
		program.switchToStartingArea();
		clearPlayerBets();
	}

	/**
	 * player makes their bet
	 */
	public void betting(){
		generatedWheelNumber = rgen.nextInt(0, 36);
		if(betButtonPressed == false){
			SpinWheel();
			betButtonPressed = true;						// can't bet again until done spinning
		}
	}

	/**
	 * As long as the player has chosen at least one tile and the ball isn't currently spinning
	 * then they are allowed to change current bet value(s)
	 */
	public void changeBetIfPossible(){
		if (betButtonPressed == false && transparentTiles.size() != 0 && betTypeRange.size() != 0) {
			if(!openFrame) {
				openBetModifier();								// can't change bet unless round is over
			}else{
				betInput.toFront();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e){
		/* force the betInput to have top-level focus when it's open */
		betInput.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				betInput.requestFocus();
				newBetValue.requestFocusInWindow();
				newBetValue.selectAll();
			}
			@Override
			public void windowGainedFocus(WindowEvent e) {}
		});

		GObject obj = program.getElementAt(e.getX(),e.getY());									// get the object we're pressing the mouse on

		if (imgLib.returnButton.contains(e.getX(), e.getY())) { 								// return to the starting area
			click.Play();
			returning();

		}else if (imgLib.betButton.contains(e.getX(), e.getY()) && betTypeRange.size() != 0) {	// spin the wheel if there are any bets placed
			if(betInput.isVisible()){
				betInput.dispose();
			}
			betting();

		}else if(imgLib.changeBetButton.contains(e.getX(), e.getY())){							//open a JFrame to change the bet value
			changeBetIfPossible();

		}else if (allTableTiles.contains(obj)) 													//we don't modify the transparency of any GImage other than betting tiles
		{		
			/* only add the bet if the player isn't trying to/hasn't bet all of their money */
			if(!bettingMoreThanMax(betAmount)){ 
				relativeBetAmount.add(betAmount);
				updateTotalBetLabel();
				betChosen(e);																// make the chosen tiles transparent
			}else{
				relativeBetAmount.add(0);
				updateTotalBetLabel();
				betChosen(e);																// make the chosen tiles transparent

			}
			bloop.Play();
		}
	}


	public String determineWinOrLose(){
		String winOrLose = "";
		if(game.getWonOrLost() == true && winningsOrLosings > 0){ 
			winOrLose = "You Won!";
		}else{
			winOrLose = "You Lost!";
		}
		return winOrLose;
	}

	/**
	 * Display a temporary dialog (3 seconds) for whether the player won or lost
	 */
	public void displayWinOrLose(){
		GRoundRect dialogBorder = new GRoundRect(DIALOG_X, DIALOG_Y, DIALOG_WIDTH, DIALOG_HEIGHT);
		dialogBorder.setFillColor(Color.black);
		dialogBorder.setFilled(true);
		GRoundRect dialogBorderEdge = new GRoundRect(DIALOG_X, DIALOG_Y, DIALOG_WIDTH, DIALOG_HEIGHT);
		dialogBorderEdge.setColor(Color.red);
		GLabel outcomeLabel = new GLabel(determineWinOrLose(), DIALOG_LABEL_X, DIALOG_LABEL_Y);
		outcomeLabel.setFont(GAME_FONT);
		outcomeLabel.setColor(Color.yellow);
		try {
			program.add(dialogBorder);
			program.add(dialogBorderEdge);
			program.add(outcomeLabel);
			Thread.sleep(2800);
			program.remove(dialogBorder);
			program.remove(dialogBorderEdge);
			program.remove(outcomeLabel);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Determine if the player tries to maximize their bet values and then
	 *  add more tiles to bet more than they have
	 * @param betAmount - the amount the player is trying to bet on the tile they're choosing
	 * @return true if they're trying to bet more than they have
	 */
	public boolean bettingMoreThanMax(int betAmount){
		int total = 0;
		int totalMinusSelected = 0;
		for(int cumulative : relativeBetAmount){ 
			totalMinusSelected += cumulative;
			total += cumulative;
		}
		if(relativeBetAmount.size() != 0)
			totalMinusSelected -= relativeBetAmount.get(selected);
		
		if(betAmount + total > game.getMoney()){ 
			this.betAmount = 0;
			updateBetLabel();
			updateTotalBetLabel();
		}
		if(totalMinusSelected + betAmount > game.getMoney())
			return true;
		return false;
	}

	/**
	 * Make an ArrayList with the details to go into the JComboBox
	 * @param betTypes - ArrayList of Strings to hold the bet types the user has selected thus far
	 * @param betNumbers - ArrayList of integer arrays to hold the bet numbers associated with the bet type
	 * @return the new ArrayList containing the relative details
	 */
	public ArrayList<String> initializeComboBox(ArrayList<String> betTypes, ArrayList<int[]> betNumbers){
		// create shallow copy of the bet type
		ArrayList<String> types = new ArrayList<String>(betTypes);
		for(int i = 0; i < types.size(); i++){
			if(types.get(i).equals("number")){
				types.set(i, types.get(i) + ": " + betNumbers.get(i)[0] + "  \u2192 $" + relativeBetAmount.get(i));
			}else{
				types.set(i, types.get(i) + "  \u2192 $" + relativeBetAmount.get(i));
			}
		}
		return types;
	}


	/**
	 * show the bet type, bet number (if applicable), and the bet amount associated with each bet type in the JComboBox
	 * @param betTypes - ArrayList of Strings to hold the bet types the user has selected thus far
	 * @param betNumbers - ArrayList of integer arrays to hold the bet numbers associated with the bet type
	 */
	public void createBetList(ArrayList<String> betTypes, ArrayList<int[]> betNumbers){
		ArrayList<String> types = initializeComboBox(betTypes, betNumbers);

		// Create a model so things work smoother and don't end up with suppression warnings
		List<String> tmp = types;
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(tmp.toArray(new String[tmp.size()]));
		betList.setModel(model);
	}

	/**
	 * dynamically update the bet value in the JComboBox
	 * @param betTypes - ArrayList of Strings to hold the bet types the user has selected thus far
	 * @param betNumbers - ArrayList of integer arrays to hold the bet numbers associated with the bet type
	 * @param newBetValue - String representation of the JTextField used to get the text from for dynamic updating
	 * @param indexToChange - the index of the JComboBox that we want to update the text for
	 */
	public void updateSelectedBet(ArrayList<String> betTypes, ArrayList<int[]> betNumbers, String newBetValue, int indexToChange){
		ArrayList<String> types = initializeComboBox(betTypes, betNumbers);

		// This does the modifications on only our selected JComboBox item
		if(types.get(indexToChange).contains("number")){
			types.set(indexToChange, betTypes.get(indexToChange) + ": " + betNumbers.get(indexToChange)[0] + "  \u2192 $" + newBetValue);
		}else{
			types.set(indexToChange, betTypes.get(indexToChange) + "  \u2192 $" + newBetValue);
		}

		if(parseIfNumericInput(newBetValue))
			relativeBetAmount.set(indexToChange, betAmount);
		/* Create a model so things work smoother and don't end up with suppression warnings
		 * DefaultComboBoxModel is used for dynamic modification of it's values
		 */
		List<String> tmp = types;
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(tmp.toArray(new String[tmp.size()]));
		betList.setModel(model);
		betList.setSelectedIndex(indexToChange);
	}


	/**
	 * open a JFrame with a JTextFrame and a JButton to take the new bet value
	 */
	public void openBetModifier(){
		newBetValue = new JTextField(Integer.toString(relativeBetAmount.get(0)));
		JButton confirmButton = new JButton("Confirm");

		// initialize the betList JComboBox with chosen bets
		createBetList(betAmountRange.getKey(), betAmountRange.getValue());

		betInput.setLayout(new BorderLayout());
		betInput.setSize(180, 120);
		betInput.setLocation((int)MouseInfo.getPointerInfo().getLocation().getX()-betInput.getWidth()/2, (int)MouseInfo.getPointerInfo().getLocation().getY()-betInput.getHeight()/2);
		JPanel pane = new JPanel(new BorderLayout());
		betInput.setContentPane(pane);
		betInput.add(betList, BorderLayout.NORTH);
		betInput.add(newBetValue, BorderLayout.CENTER);
		betInput.add(confirmButton,BorderLayout.SOUTH);
		betInput.setVisible(true);
		betInput.setAlwaysOnTop(true);

		newBetValue.requestFocusInWindow();
		newBetValue.selectAll();

		// get the index of the betList item we're on to do our subsequent manipultions
		selected = betList.getSelectedIndex();

		// allow the enter key to press the confirm button
		betInput.getRootPane().setDefaultButton(confirmButton);

		// the following window listener is tied into the JFrame being opened/disposed, not closed
		betInput.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// prevent the user from opening multiple bet modification windows
		betInput.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				openFrame = true;
			}

			@Override
			public void windowClosed(WindowEvent e) {
				openFrame = false;
			}
		});

		/*
		 * used to force the selection not to go back to the first index when making a modification in the document listener
		 * also sets the text field to be the current selections bet value
		 * Additionally, ONLY if the betList item is switched then update the textbox with the bet value of the item we're switching to
		 */
		betList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selected = betList.getSelectedIndex();
				if(!e.getSource().toString().contains("invalid")){
					newBetValue.setText(Integer.toString(relativeBetAmount.get(selected)));
					newBetValue.requestFocusInWindow();
					newBetValue.selectAll();
				}
			}
		});

		// used to dynamically update the betList values, which means it also dynamically updates the actual betting values as well
		// thus, the confirm button is only necessary for background actions
		newBetValue.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateSelectedBet(betAmountRange.getKey(), betAmountRange.getValue(), newBetValue.getText(), selected);
				betList.setSelectedIndex(selected);
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateSelectedBet(betAmountRange.getKey(), betAmountRange.getValue(), newBetValue.getText(), selected);
				betList.setSelectedIndex(selected);
			}
			@Override// Plain text components (JTextFrame for example) don't fire this event
			public void changedUpdate(DocumentEvent e) {}
		});

		newBetValue.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(newBetValue.getText().matches("[a-zA-Z]+") || bettingMoreThanMax(Integer.parseInt(newBetValue.getText().trim()))){ 
					newBetValue.setText(Integer.toString(relativeBetAmount.get(selected)));
					betInput.dispose(); 
					openBetModifier();
				}else{
					newBetValue.setText(Integer.toString(relativeBetAmount.get(selected)));
					betInput.dispose();
				}
				updateBetLabel();
				updateTotalBetLabel();
			}
		});
		newBetValue.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				newBetValue.selectAll();
			}
		});




		// action listener to set the bet label when the button is clicked
		// also, only close the bet modifier if the player has given valid input (single and cumulative bets < total money)
		confirmButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean close = parseIfNumericInput(newBetValue.getText());			
				updateBetLabel();
				updateTotalBetLabel();
				if(close){
					betInput.dispose();	
					betInput.dispatchEvent(new WindowEvent(betInput,WindowEvent.WINDOW_CLOSING));
				}

			}
		});

	}



	/**
	 * determine if we can parse the text box input to an integer
	 * @param betValue to be parsed
	 * @return true if valid
	 * @exception NumberFormatException if cannot parse or value given causes total bets to exceed player money
	 */
	private boolean parseIfNumericInput(String betValue){
		int betTotal = 0;
		for(int value : relativeBetAmount){
			betTotal += value;
		}
		try
		{
			/* make sure you cannot exceed your total money with one bet, or cumulative bets */
			if(bettingMoreThanMax(Integer.parseInt(betValue.trim())) || Integer.parseInt(betValue) > game.getMoney() || betTotal > game.getMoney()){
				NumberFormatException nfe = new NumberFormatException();
				throw nfe;
			}else{
				betAmount = Integer.parseInt(betValue.trim());
			}
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}


	/**
	 * determine the relative distance from 0 for the ball to get to subsequent numbers on the wheel
	 * The ball needs a little distance tweak every so often
	 * @param wheelNumber - number for the ball to go to
	 * @return distance for the ball to travel
	 */
	private double relativeDistance(double wheelNumber){
		if (wheelNumber > 29){
			return wheelNumber * BASE_BALL_MOVEMENT + BALL_TWEAKS[0];				//for the final numbers we have to go a further than most others
		}else if (wheelNumber > 25){
			return wheelNumber * BASE_BALL_MOVEMENT + BALL_TWEAKS[1];				//for later numbers we have to go a little further
		}else if (wheelNumber > 22){
			return wheelNumber * BASE_BALL_MOVEMENT + BALL_TWEAKS[2];				//a little more than a nudge
		}else if (wheelNumber > 17){
			return wheelNumber * BASE_BALL_MOVEMENT + BALL_TWEAKS[3];				//for the midpoint numbers we have to nudge their distances a bit			
		}else if (wheelNumber != 0 && (int)wheelNumber%4 == 0 || wheelNumber >= 6){
			return wheelNumber * BASE_BALL_MOVEMENT + BALL_TWEAKS[4];				//for number 4 and other numbers >=6 but less than 18
		}else if(wheelNumber != 0 && wheelNumber <= 5){
			return wheelNumber * BASE_BALL_MOVEMENT;						//for numbers 1-5 that haven't been dealt with above
		}
		return 0;													//default case, I think it only applies to zero
	}

	/**
	 * Use a timer to give the illusion of the ball moving
	 * Also uses random number generation to make it so the balls ending value is random
	 */
	private void startSpin() {
		Timer timer = new Timer();																	// Timer for how long the ball should go around the wheel

		TimerTask task = new TimerTask() {															// Task to deal with the balls movement duration
			public void run() {
				rotateTheBall();																	// moving the ball based on the time interval
				if(speed > ENDING_SPEED + relativeDistance(generatedWheelNumber)){					// Stop the balls movement
					stopBall = true;
					winningsOrLosings = new Integer(player.getMoney());
					game.playGame(orderOfWheel[generatedWheelNumber], betAmountRange, relativeBetAmount);
					winningsOrLosings = player.getMoney() - winningsOrLosings;
					displayWinOrLose();
					updateMoneyLabel();
					data.Save();
					doReset(returnButtonClicked);
					betButtonPressed = false;
					updateTotalBetLabel();
					if(game.outOfMoney())
						program.switchToGameOver();
				}

				if(speed < numberSpeed.get(orderOfWheel[(int) Math.floor(generatedWheelNumber)])){	// still going fast
					speed += SPEED_INCREMENT;		
				}else
					speed += 1;  							// slowing down the ball
				timer.cancel(); 							//end the timer so we can get a dynamic ball speed
				startSpin();   								//refresh the timer with the new ball speed
			}
		};
		if(!stopBall){										// As long as we shouldn't stop the ball update the balls speed
			timer.scheduleAtFixedRate(task, (long)speed,1); // This is the actual timer that governs the balls speed
		}
	}
	/** Make the given object transparent (GImage) by turning it into a 2D array of pixels 
	 * then we deconstruct each pixel into its RGBA components, modify the alpha, rebuilt the pixel
	 * then finally reconstruct and return the image with the new alpha value
	 * @author Darrel Holt
	 * @param image in which to modify the transparency
	 * @return a copy of the given image with transparency
	 */
	private GImage changeTransparency(GImage image, int newAlpha) {
		int[][] oldPixels = image.getPixelArray();								
		int width = oldPixels[0].length;										
		int height = oldPixels.length;											
		int[][] newPixels = new int[height][width];									
		for (int i = 0; i < height; i++) {											
			for (int j = 0; j < width; j++) {										
				int origAlpha = (oldPixels[i][j] >> 24 & 0xFF000000) >> 24;				
				/* new transparency - (0 = invisible and 255 = solid) */
				origAlpha += newAlpha;
				int red   = (oldPixels[i][j] & 0x00FF0000) >> 16;					
				int green = (oldPixels[i][j] & 0x0000FF00) >> 8;
				int blue  =  oldPixels[i][j] & 0x000000FF;				
				newPixels[i][j] = GImage.createRGBPixel(red, green, blue, origAlpha);
			}
		}
		GImage transparent = new GImage(newPixels);					
		transparent.setSize(image.getWidth(), image.getHeight());	
		return transparent;											
	}

	/**
	 * This will create the time value associated with each number on the wheel
	 * in decreasing intervals from 0 going clockwise
	 * each interval of 26 is one number
	 */
	private void calculateNumberSpeed(){
		for(int i = 1; i <= 37; i++){
			numberSpeed.put(orderOfWheel[i-1], 7.74);
		}
	}

	/**
	 * Add the chosen number to the range of betting values
	 * @param num - number to be added to the range that will be used to calculate if the player won or lost
	 */
	private void addNumberToBettingRange(int num){
		int[] singleNumber = {num};
		bettingRange.add(singleNumber);
	}

	/**
	 * add the chosen betting range to the range of all betting values
	 * @param range - the subRange to be added to the range to calculate if the player won or lost
	 */
	private void addSubRangeToBettingRange(int[] range){
		bettingRange.add(range);
	}

	/** Retrieve a key from a Map given a value
	 * @author http://www.java2s.com/Code/Java/Collections-Data-Structure/GetakeyfromvaluewithanHashMap.htm
	 * @param hashMap - HashMap to do the search on
	 * @param value - value assigned to the key we're looking for
	 * @return first instance of a key that matches the given value
	 */
	private Object getKeyFromValue(Map<?,?> hashMap, Object value) {
		for (Object o : hashMap.keySet()) {
			if (hashMap.get(o).equals(value)) {
				return o;
			}
		}
		return null;
	}

	/**
	 * This performs two tasks:
	 * One, it takes the bettingRange value(s) of the tile for computation later
	 * Two, it changes the tile so you can both see what you've already chosen and makes it unclickable again to prevent duplicating a betting range
	 * @param e - MouseEvent to determine if a tile was clicked on
	 */
	private void betChosen(MouseEvent e){
		GObject obj = program.getElementAt(e.getX(), e.getY());			// used to determine the betting tile being clicked on
		if(obj instanceof GImage){
			GPoint loc = obj.getLocation();								// we need the location of the betting tile for after changing it's trransparency
			program.remove(obj);										// remove the betting tile before re-adding it with transparency
			GImage betNumber = changeTransparency((GImage)obj, 100);	// create the new tile with transparency
			betNumber.setLocation(loc);									// put the transparent tile at the old tiles location
			program.add(betNumber);										// put the new transparent tile on the board


			transparentTiles.add(betNumber);
		}


		/* Determine which betting tile we're choosing so we can use it's associated values with the game */
		int index = Arrays.asList(imgLib.tableNumbers).indexOf(obj);


		/* add individual numbers to the pool */
		if(Arrays.asList(imgLib.tableNumbers).contains(obj)){
			addNumberToBettingRange(index);
			betTypeRange.add("number");
		}else{
			/* add the chosen range tiles to the pool */
			for (Entry<GImage, int[]> val : allBettingRanges.values()) {
				if (val.getKey() == obj) {
					addSubRangeToBettingRange( (int[]) val.getValue() );
					betTypeRange.add( (String) getKeyFromValue(allBettingRanges, val) );
				}
			}
		} 
	}
}