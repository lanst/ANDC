//author chiyu  cheng
//this class presents each slot
import java.awt.*;
import java.util.*;
import acm.graphics.*;
import acm.program.*;

public class slot{
	// index 0: cherry
	// index 1: apple
	// index 2: lemon
	// index 3: grape 
	// index 4: watermelon
	// index 5: diamond
	// index 6: 7 
	private int target_index; // the stop position on the screen of slot
	private boolean finish; // check if this slot stop or not
	private double delay_initial; // the initial delay of this slot
	private double count;  // how many times this slot moves.
	private int result; // the middle item of slot
	private int rand_code;// The code to random the slot image
	//construct
	public slot(){
		target_index = -1;
		finish = false;
		delay_initial = 2;
		count = 0;
		rand_code = 0;
		result = -1;
	}
	
	public void setResult(int r){
		result = r;
	}
	
	public int getResult(){
		return result;
	}
	
	// randomly generate the random code in order to load the item on screen randomly
	public void setRandCode(){
		Random rand = new Random();
		int num = rand.nextInt(7);
		rand_code = num;
	}
	
	public int getRandCode(){
		return rand_code;
	}
	
	public int getIndex(){
		return target_index;	
	}
	public void setIndex(int i){
		target_index = i;	
	}
	public boolean getFinish(){
		return finish;
	}
	public void setFinish(boolean f){
		finish = f;
	}
	public double getDelay(){
		return delay_initial;
	}
	public void setDelay(double s){
		delay_initial = s;
	}
	public void setCount(double c){
		count = c;
	}
	public double getCount(){
		return count;
	}
	public void print(){
		System.out.print("The speed is "+ delay_initial + " the count is " + count + " Finish is "+ finish + " The randcode is " + rand_code+ "\n");
	}
	
	public void countPlus(){
		count = count + 1;
	}
	
	// randomly generate the stop position on the screen
	public void rand_num(){
		Random rand = new Random();
		int num = rand.nextInt(7);
		this.setIndex(num);
	}
}


