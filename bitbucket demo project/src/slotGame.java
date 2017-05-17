//author chiyu cheng
// the class to implement the graphic slot games
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import acm.graphics.*;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

  
public class slotGame extends GraphicsPane{
	// Images
	private GImage top;
	private GImage bottom;
	private GImage changeBetButton;
	private GImage background;
	private GImage lose;// display the screen of win or lose 
	private GImage returnButton;
	private GImage exit;
	private GImage restart;
	private GImage betButton;
	private GImage[] items; // the array of all image
	public GImage slot_image;
	
	// Labels
	private GRect rect;
	private GLabel bet_amount;
	private GLabel money;
	
	// boolean
	private boolean spinning;
	private boolean finish_blink;
	
	// slot
	private slot[] slot; // the slot array to store three slots.
	
	//data
	public static final int PROGRAM_WIDTH = 800;
	public static final int PROGRAM_HEIGHT = 600;
	private int bet_money;//the amount of money play bet
	private int count_blink;
	private int win_lose; // Not playing = -1; lose = 0; win = 1
	
	//others
	private Effects effects;
	private AudioPlayer click;
	private AudioPlayer spin;
	private AudioPlayer happy;
	private AudioPlayer sad;
	private MainApplication program;
	Player player;
	private Color[] colors;
	
	//SlotGame constructor
	public slotGame(MainApplication program, Player player, Data data){
		// initial data
		bet_money = 100;
		win_lose = -1;
		count_blink = 0;
		
		// initial boolean
		finish_blink = false;
		spinning = false;
		
		// store 4 types of color in to colors array
		colors = new Color[4];
		colors[0] = Color.white;
		colors[1] = Color.red;
		colors[2] = Color.blue;
		colors[3] = Color.BLACK;
		
		//initial effects
		effects = new Effects();
		
		// initial musics
		click = new AudioPlayer("Sounds/Clicked.wav");
		spin = new AudioPlayer("Sounds/spin.wav");
		happy = new AudioPlayer("Sounds/haha.wav");
		sad = new AudioPlayer("Sounds/sad.wav");
		
		//initial three slot
		items = new GImage[21];
		initial_slot();
		
		// initial the images used in slot
		top = new GImage("Images/slot/Top.png");
		bottom = new GImage("Images/slot/Bottom.png");
		betButton= new GImage("Images/bet.png",200, 500);
    	betButton.setSize(betButton.getWidth(), betButton.getHeight());
		returnButton = new GImage("Images/returnButton.png",0, 0);
    	returnButton.setSize(returnButton.getWidth(), returnButton.getHeight());
		background = new GImage("Images/slot/background.jpg", 0 , 0);
		background.setSize(800, 600);
		lose = new GImage("Images/slot/lose.jpg");
		
		// initial change button
		changeBetButton = new GImage("Images/ChangeBet.png",400 ,500);
    	changeBetButton.setSize(changeBetButton.getWidth(), changeBetButton.getHeight());
    	
    	// initial money label
    	money = new GLabel ("Money: " + player.getMoney(),660,250);
		money.setColor(Color.white);
		money.setFont("Bernard MT Condensed-22");
		
		// initial bet_amount label
		bet_amount = new GLabel ("Bet: "+ bet_money,660,350);
		bet_amount.setColor(Color.white);
		bet_amount.setFont("Bernard MT Condensed-22");
		
		// initial restart image
		restart = new GImage("Images/slot/reset.jpg");
		restart.setSize(100,100);
		
		//initial exit image
		exit = new GImage("Images/slot/exit.png");
		exit.setSize(100,100);
		
		//initial program and player
		this.program = program;
		this.player = player;
	}
	
	// add images on screen
	@Override 
	public void showContents(){		
		program.add(lose);
		program.add(background);
		drawMachine(100,700,0,600);
    	program.add(returnButton);
    	program.add(betButton);
    	program.add(changeBetButton);
		program.add(money);
		program.add(bet_amount);
		updateMoney();
	}
	
	@Override
	public void hideContents() {
		// TODO Auto-generated method stub
	}
	
	
	// initial all the slots
	public void initial_slot(){
		//reset the position three slot
		slot = new slot[3];
		for(int g = 0; g < 3; g++){
			slot[g] = new slot();
			slot[g].setRandCode();
			check_rand_code(g); // check if it is a valid random code
			for(int i = 0; i< 7; i++){
				int w = i - slot[g].getRandCode();
				if(w < 0){
					w = w + 7;
				}
				String filename = "Images/slot/"+Integer.toString(w)+".png";
				items[i+7*g] = new GImage(filename);
			}
		}
	}
	
	// The function to make sure all the three rand_code are different
	public void check_rand_code(int slotNum){
		int previous_code = 0; // the rand code of previous slot
		if(slotNum >= 1){
			for (int i = 0; i < slotNum; i++){
				boolean valid_rand_code = false; 
				while(!valid_rand_code){
					previous_code = slot[i].getRandCode();
					if(previous_code == slot[slotNum].getRandCode()){ // if same then re-generate a new one
						slot[slotNum].setRandCode();
					}
					else{
						valid_rand_code = true; // other wise, valid_rand_code = true;
					}
				}
			}
		}
	}
	
	// the function to rest the slot machine
	public void reset_slot(){
		//remove the useless image
		program.remove(lose);
		program.remove(bet_amount);
		program.remove(exit);
		program.remove(restart);
		
		//reset the data
		count_blink = 0;
		bet_money = 100;
		win_lose = -1;
		bet_money = 100;
		finish_blink = false;
		spinning = false;
		
		//reset each slot
		initial_slot();
	}
	
	// the function to update the money label and bet label
	public void updateMoney(){
		//remove the old label first
		program.remove(money);
		program.remove(bet_amount);
		
		//reset a new money label with current money
		money = new GLabel ("Money: " + player.getMoney(),660,250);
		money.setColor(Color.white);
		money.setFont("Bernard MT Condensed-22");
		program.add(money);
		
		//reset a new betAmount bale with bet amount
		bet_amount = new GLabel ("Bet: "+ bet_money,660,350);
		bet_amount.setColor(Color.white);
		bet_amount.setFont("Bernard MT Condensed-22");
		program.add(bet_amount);
	}
	
	/** the function to check if the all slot are same
	 return true if all the slot are finished*/
	public boolean Spinning(){
		if(slot[0].getFinish()&&slot[1].getFinish()&&slot[2].getFinish()){
			return false;
		}
		return true;
	}
	
	// if player doesn't have enough money 
	public boolean kicked_out(){
		if (player.getMoney()<=0){
			return true;
		}
		return false;
	}
	
	
	
	
/************************************************************************************************************
 * Mouse listener functions
 */
	@Override
	public void mouseMoved(MouseEvent e) 
	{
		effects.ChangeImage(returnButton, "invertedReturnButton.png", "returnButton.png", e);
		effects.ChangeImage(changeBetButton, "InvertedChangeBet.png", "ChangeBet.png", e);
		effects.ChangeImage(betButton, "invertedBet.png", "bet.png", e);
		effects.PopUp(exit, e);
		effects.PopUp(restart, e);
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		click.Play();// play the clicked music
		GObject obj = program.getElementAt(e.getX(),e.getY()); // initial obj
		
		if(kicked_out()){
			try {
			    Thread.sleep(1000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			reset_slot();
			program.switchToGameOver();
		}
		else{
		// if slot not spinning and game not started, then go back to starting area
		if(obj == returnButton && !spinning && win_lose == -1){
			reset_slot();
			program.switchToStartingArea();
		}
		
		// if slot not spinning and game not started, then allow player to bet
		if (obj == betButton && !spinning && win_lose == -1){
				// set the current money
				int update_money = player.getMoney() - bet_money;
				player.setMoney(update_money);
				//play each slot
				for(int i = 0; i<3 ;i ++){
					slot[i].rand_num();
					move(i,8+4*i);
				}
		}
		
		// exit
		if(obj == exit){
			reset_slot();
			program.switchToStartingArea();
		}
		
		// restart
		if(obj == restart){
			reset_slot();
			program.switchToSlot();
		}
		
		// if not spinning and game not start, then allow player to change the bet
		if(obj == changeBetButton && !spinning && win_lose == -1){
			openBetModifier();		
		}
	}
}

	
/********************************************************************************************************
 * The functions to move the slot.
 * move function allows user to move each slot individually with the wanted speed
 * @param slotNum: the index of each slot.
 * @param max_delay : the maxim delay that this slot can reach. It decide how long this slot can run.
 * The slot will move slowly if the delay increase
 */
	public void move(int slotNum, int max_delay){
		Timer time = new Timer();
		TimerTask task = new TimerTask(){
			public void run(){
				slot_run(slotNum, 600);
				time.cancel();
				// recursive all move until it finished one round
				move(slotNum, max_delay);  
			}
		};
		if (!slot[slotNum].getFinish())
			// the speed of this slot is depends on the delay of each move
			time.scheduleAtFixedRate(task, (long)slot[slotNum].getDelay()/2, (long)slot[slotNum].getDelay()/2);
		else {
			// now slot finished one round with a specific delay
			// reset
			time.cancel();
			task.cancel();
			slot[slotNum].setFinish(false);
			slot[slotNum].setCount(0);
			
			// this is the current delay the slot have
			double delay_initial = slot[slotNum].getDelay();
			
			// The delay will increase depends on its value
			// This function will make the speed of slot different in different round.
			// The slot will become slower while the delay increase
			setDelay(slotNum, delay_initial);
			
			// compare the current speed with maxim speed
			// if not reach the maxim speed, then call move again.
			if(slot[slotNum].getDelay() <= max_delay){
				move(slotNum,max_delay);
			}
			// if reach the maxim delay, then reset delay and move to stop function
			else{
				slot[slotNum].setDelay(2);
				stop(slotNum,caculateCount(slot[slotNum].getIndex()));
			}
		}
	}
	
/**
 * stop function can help each slot stop in the targeted position
 * @param slotNum: the index of each slot.
 * @param count: how many times this function should run
 * This function is necessary because move function only can move a entire round
 */
	public void stop(int slotNum,double count){
		Timer time = new Timer();
		TimerTask task = new TimerTask(){
			public void run(){
				slot_run(slotNum, count);
				time.cancel();
				stop(slotNum, count);  
			}
		};
		if (!slot[slotNum].getFinish()){
			time.scheduleAtFixedRate(task, 10, 10);
		}
		else {
			// This slot now stops in the position we want
			// reset 
			time.cancel();
			task.cancel();
			slot[slotNum].setFinish(false);
			slot[slotNum].setCount(0);
			if(Spinning()){
				spinning = false;
			}
			
			// set the final result of this slot
			slot[slotNum].setResult(caculateResult(slotNum));
			if(slotNum == 2){ // if this is the last slot, then it means the spinning is ended.
				win_lose = 1; // initial win_lose to win
				if(!win(slot[0].getResult(),slot[1].getResult(),slot[2].getResult())){ // if not win, set it to lose
					win_lose = 0;
				}
				//update the current money of player
				int win_money = player.getMoney()+caculate_reward(bet_money,slot[0].getResult(),slot[1].getResult(),slot[2].getResult());
				player.setMoney(win_money);
				updateMoney();
				//call blink_box to blink
				blink_box(50,win_lose + 1,win_lose);
			}
		}
	}
	
	// this function can move a specific slot down 1 pixel.
	public void slot_run(int slotNum, double count){
		int item_index = slotNum*7;
		spinning = true; // begin spinning
		for(int w = 0; w < 7;w++){
			// if 
			if(items[w + item_index].getY() >= 600){
				items[w + item_index].setLocation(items[w+item_index].getX(), items[w+item_index].getY()-600);
			}
			else{
				items[w + item_index].setLocation(items[w+item_index].getX(), items[w+item_index].getY()+1);
			}
		}
		if(slot[slotNum].getCount()%(600/7) == 0){
			spin.Play();
		}
		if(slot[slotNum].getCount() >= count){
			slot[slotNum].setFinish(true);
		}
		slot[slotNum].countPlus();
	}
	
	// increase the delay time depend on the value initial delay.
	// this function can make slot move slower and slower
	public void setDelay(int slotNum, double delay_initial){
		if(delay_initial <= 4){
			slot[slotNum].setDelay(delay_initial + 0.5);
	    }
		else if(delay_initial <= 6){
			slot[slotNum].setDelay(delay_initial + 1);
		}
		else{
			slot[slotNum].setDelay(delay_initial + 2);
		}
		
	}
	
	// Calculate how many money player win
	public int caculate_reward(int betMoney,int s1,int s2, int s3){
		if(win(s1,s2,s3)){
			return betMoney*(s1+1);
		}
		return 0; 
	}
	
	// check the result to see if player win or not
	public boolean win(int s1, int s2, int s3){
		if(s1 == s2 && s2 == s3 && s3 != -1){
			return true;
		}
		return false;
	}
	
	//Calculate how many time the stop functions should run in order to stop in the targeted position
	public double caculateCount(int targetIndex){
		return (600/7)*targetIndex;
	}
	
	//calculate the result of each slot after spinning based on the rand_code of each slot
	public int caculateResult(int slotNum){
		int item4 = 3 - slot[slotNum].getRandCode();
		if(item4 < 0){
			item4 = item4 + 7;
		}
		int result = item4 - slot[slotNum].getIndex();
		if(result < 0){
			result = result + 7;
		}
		return result;
	}
	
	
	

/*************************************************************************************************** 
 * The function to implement the blinked box in the end of the game
 * @param max_count: how many times user want the box to blink
 * @param color_code: The code to determine the wanted color. 1 is red ; 2 is green.
 * @param w_l: the value of win_lose. 0 means lose, 1 means win.
 * If lose, the edge of the blinked box will be red
 * If win, the edge of the blinked box will be blue
 * After finished blink, the program will be transfer to win() or lose() function depends on w_l
 */
	public void blink_box(int max_count, int color_code, int w_l){
		Timer time = new Timer();
		TimerTask task = new TimerTask(){
			public void run(){
			    // if the color of rect is white, then set it to the c[color_code]
				if(rect.getColor()==colors[0]){
					rect.setColor(colors[color_code]);
				}
				else{// if not white, set the color to white
					rect.setColor(colors[0]);
				}
				count_blink = count_blink + 1;
				if(count_blink >= max_count){
					finish_blink = true;
				}
				time.cancel();
				blink_box(max_count, color_code,w_l);
			}
		};
		if(!finish_blink){
			time.scheduleAtFixedRate(task, 100, 100);// the blinked speed
		}
		else{
			time.cancel();
			task.cancel();
			finish_blink = false;
			count_blink = 0;
			if(w_l == 0){ //if lose, go to lose function
				end_lose();
			}
			if(w_l == 1){ //if win, go to win function
				end_win();
			}
		}
	}
	
	/**
	 * The function to do the lose ending.
	 * It will set the win screen
	 */
	public void end_win(){
		rect.setColor(Color.green);
		win_lose = -1;
		lose = new GImage("Images/slot/win.png", 0,0);
		lose.setSize(800, 600);
		program.add(lose);
		happy.Play();
		exit_restart();
	}
	
	/**
	 * The function to do the win ending
	 * It will set the lose screen.
	 */
	public void end_lose(){
		rect.setColor(Color.red);
		lose = new GImage("Images/slot/lose.jpg", 0,0);
		win_lose = -1;
		lose.setSize(800, 600);
		program.add(lose);
		sad.Play();
		exit_restart();
	}
	
	/**
	 * The function to put exit button and restart 
	 * button after slot game ends.
	 */
	public void exit_restart(){
		exit.setLocation(0, 0);
		program.add(exit);
		restart.setLocation(650,0);
		program.add(restart);
	}
	
	
/***************************************************************************************************
 * The function to draw the whole slot machine
 * @param c_start: the start point of column
 * @param c_end: the end point of column
 * @param r_start: the start point of row
 * @param r_end: the end point of column
 */
	public void drawMachine(int c_start, int c_end, int r_start, int r_end){
    	double h_space = (c_end - c_start)/7;
    	double v_space = (r_end - r_start)/7;
    	for(int i = 0; i < 3; i ++){
    		int w = i + 1;
    		drawSlot(w,2*w*h_space,((2*w)+1)*h_space,0,600);
    	}
		rect = new GRect(2*h_space+2,3*h_space,5*v_space + 37,h_space); 
    	program.add(rect);
		top = new GImage("Images/slot/Top.png", 100, 0);
		top.setSize(600, 2*v_space);
		program.add(top);
		bottom = new GImage("Images/slot/Bottom.png",100, 5*v_space);
		bottom.setSize(600, 2*v_space+50);
		program.add(bottom);
    }
	
/** The function to draw the each slot
 * @param slotNum: the slot index which will tell the function to draw which slot.
 * @param c_start: the start point of column
 * @param c_end: the end point of column
 * @param r_start: the start point of row
 * @param r_end: the end point of column
 */
	public void drawSlot(int slotNum,double c_start, double c_end, double r_start, double r_end){
	    	slot_image  = new GImage("Images/slot/slot.png",c_start-5, r_start-25);
	    	slot_image.setSize(c_end - c_start + 50, r_end - r_start+50);
	    	program.add(slot_image);
	    	double space = (r_end - r_start)/7;
	    	for(int i = 0; i < 7;i++){
	    		int w = (slotNum-1)*7 + i;
	    		items[w].setLocation(c_start+20, (i)*space);
	    		items[w].setSize(space, space);
	    		program.add(items[w]);
	    	}
	    }
	
	
/*******************************************************************************************************
* Input functions:	
* author: Darrel
* I revise his function in order to get the input from user.
*/
	 
	public void openBetModifier(){
		JFrame betInput = new JFrame("New Bet Value");
		JTextField newBetValue = new JTextField("Please enter your bet");
		JButton confirmButton = new JButton("ok");

		betInput.setLayout(new BorderLayout());
		betInput.setSize(100, 85);
		betInput.setLocation(500, 370);
		betInput.add(newBetValue, BorderLayout.NORTH);
		betInput.add(confirmButton,BorderLayout.SOUTH);
		betInput.setVisible(true);
		//set confirm button
		betInput.getRootPane().setDefaultButton(confirmButton);

		newBetValue.addMouseListener(new MouseListener(){
			@Override
			public void mousePressed(MouseEvent e) {
				newBetValue.setText("");
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});

		newBetValue.addKeyListener(new KeyListener(){
			boolean newField = true;
			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(newField){ 
					newBetValue.setText(Character.toString(e.getKeyChar()));
					newField = false;
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

		});

		// action listener to set the bet label when the button is clicked
		confirmButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				parseIfNumericInput(newBetValue.getText());
				updateMoney();
				betInput.dispose();
			}
		});	
	}
	
	private boolean parseIfNumericInput(String betValue){
		try
		{
			if(Integer.parseInt(betValue) > player.getMoney()){
				NumberFormatException nfe = new NumberFormatException();
				throw nfe;
			}
			else{
				bet_money = Integer.parseInt(betValue.trim());
			}
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}
	
}

