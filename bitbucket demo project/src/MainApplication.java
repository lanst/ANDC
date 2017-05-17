
@SuppressWarnings("serial")
public class MainApplication extends GraphicsApplication {
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	
	private RouletteBoard roulette;
	private MainMenu mainMenu;
	private StartingArea startingArea;
	private Underconstruction underconstruction;
	private Vendor vendor;
	private slotGame slot;
	private BlackjackBoard blackJack;
	private Loading loading;
	private GameOver gameOver;
	private Credit credit;
	
	private Player player = new Player();
	private Inventory inventory = new Inventory();
	private Data data = new Data(player, inventory);
	
	public void init() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	}
	
	public void run() {
		//Declaration of screens
		loading = new Loading(this);
		loading.setPercentage(0);
		switchToLoading();
		
		roulette = new RouletteBoard(this, player, data, inventory);
		loading.setPercentage(10);
		
		mainMenu = new MainMenu(this, player, data, inventory);
		loading.setPercentage(20);
		
		startingArea = new StartingArea(this, player);
		loading.setPercentage(30);
		
		underconstruction = new Underconstruction(this);
		loading.setPercentage(40);
		
		vendor = new Vendor(this, player, data, inventory);
		loading.setPercentage(50);
		
		slot = new slotGame(this, player, data);
		loading.setPercentage(60);
		
		blackJack = new BlackjackBoard(this, player);
		loading.setPercentage(70);
		
		gameOver = new GameOver(this);
		loading.setPercentage(80);
		
		credit = new Credit(this);
		loading.setPercentage(90);
		
		setupInteractions();
		loading.setPercentage(100);
		
		//switchToGameOver();
		switchToMainMenu();
	}
	
	/* Method: setupInteractions
	 * -------------------------
	 * must be called before switching to another
	 * pane to make sure that interactivity
	 * is setup and ready to go.
	 */
	private void setupInteractions() {
		requestFocus();
		addKeyListeners();
		addMouseListeners();
	}
	
	public void switchToLoading(){
		switchToScreen(loading);
	}
	
	public void switchToMainMenu() {
		switchToScreen(mainMenu);
	}
	
	public void switchToRoulette() {
		switchToScreen(roulette);
	}
	
	public void switchToStartingArea() {
		switchToScreen(startingArea);
	}
	
	public void switchToUnderconstruction() {
		switchToScreen(underconstruction);
	}
	
	public void switchToVendor() {
		switchToScreen(vendor);
	}
	
	public void switchToSlot() {
		switchToScreen(slot);
	}

	public void switchToBlackJack(){
		switchToScreen(blackJack);
	}
	
	public void switchToGameOver(){
		switchToScreen(gameOver);
	}
	
	public void switchToCredit(){
		switchToScreen(credit);
	}
}
