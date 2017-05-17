/**
 * @Author Darrel Holt
 */

import java.util.ArrayList;
import java.util.Map.Entry;

import acm.util.RandomGenerator;

public class RouletteGame{

	private static RandomGenerator rgen = RandomGenerator.getInstance();

	private Player player;					//player to go between games
	private int money;						//players money
	private int betAmount = 100;			//initial bet amount
	private double itemMultiplier = 1;		//account for the attribute of the Heart
	private double lossReducer = 1;			//account for the attribute of the Diamond
	private  boolean wonOrLost;

	Inventory inventory;

	public RouletteGame(Player player, Inventory inventory){
		this.player = player;
		money = player.getMoney();
		this.inventory = inventory;
		determineItemAttribute();
	}

	public int getBetAmount(){
		return betAmount;
	}

	public boolean getWonOrLost(){
		return wonOrLost;
	}


	private  boolean winOrLose(int betValue, int spinValue){
		return betValue == spinValue;
	}

	//will use value as specified by the labels on RouletteBoard for parameter inputs

	/**
	 * Spin the Roulette wheel and determine if you won or lost, then change money by the respective amount
	 * @param spinValue - the number that the ball lands on
	 * @param betAmountRange - link the betType to the respective bettingRange
	 * @param betAmount - amount of money player is betting
	 */
	protected void playGame(int spinValue,Entry<ArrayList<String>, ArrayList<int[]>> betAmountRange, ArrayList<Integer> relativeBetAmount){
		int rangeIndex = 0;
		wonOrLost = false;
		for(String type : betAmountRange.getKey()){
			changeMoney(winLoseAmount(type, relativeBetAmount.get(rangeIndex), spinValue, betAmountRange.getValue().get(rangeIndex)));
			rangeIndex++;
		}
	}

	public boolean outOfMoney(){
		if(player.getMoney() <= 0 && !(inventory.HasItems())){
			return true;
		}
		return false;
	}

	private void determineItemAttribute(){
		if(inventory.GetItem("Heart").isAcquired() == true)
			itemMultiplier = 1.22;
		if(inventory.GetItem("Diamond").isAcquired() == true)
			lossReducer = 0.5;
	}

	public double getItemMultiplier(){
		determineItemAttribute();
		return itemMultiplier;
	}

	private double getLossReducer(){
		determineItemAttribute();
		return lossReducer;
	}
	
	private  int winLoseAmount(String betType, int betAmount, int spinValue, int[] betRange){
		boolean found = false;						//determine if bet range matches roulette spin outcome
		for(int betValue : betRange){				//go through the bet range
			if(winOrLose(betValue, spinValue)){		//compare the values of the spin to the bet number
				found = true;						//if we have a match (player won) break the loop and continue
				break;
			}
		}
		if(found)
			wonOrLost = found;							//used to determine if we won or list on the RouletteBoard

		if(found){ //win -> increase money by bet amount as specified by multiplier
			switch (betType) { //winning cases
			case "number":
				return (int)(betAmount * 35 * getItemMultiplier());
			case "red":
				return (int)(betAmount * 2 * getItemMultiplier());
			case "black":
				return (int)(betAmount * 2 * getItemMultiplier());
			case "low":
				return (int)(betAmount * 2 * getItemMultiplier());
			case "high":
				return (int)(betAmount * 2 * getItemMultiplier());
			case "even":
				return (int)(betAmount * 2 * getItemMultiplier());
			case "odd":
				return (int)(betAmount * 2 * getItemMultiplier());
			case "dozen1":
				return (int)(betAmount * 3 * getItemMultiplier());
			case "dozen2":
				return (int)(betAmount * 3 * getItemMultiplier());
			case "dozen3":
				return (int)(betAmount * 3 * getItemMultiplier());
			default: //default - zero always loses
				return betAmount * 0;
			}
		} else { //lost -> decrease money by bet amount
			return (int)(-betAmount * getLossReducer());
		}
	}

	/**
	 * change the money
	 * @param newMoney 
	 */
	public void changeMoney(int newMoney){
		player.setMoney(player.getMoney() + newMoney);
	}

	public int getMoney(){
		return player.getMoney();
	}
}
