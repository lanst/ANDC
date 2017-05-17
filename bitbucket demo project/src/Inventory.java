import java.util.HashMap;
import java.util.Map.Entry;

import acm.graphics.GImage;

/**
 * <p>Inventory to be use by vendor and player.</p>
 * 
 * @author Norlan Prudente
 *
 */
public class Inventory {
	private static final int HEIGHT_MARGIN = 6;
	private static final int WIDTH_MARGIN = 10;
	private static final int HEART_BUY_PRICE = 500;
	private static final int HEART_SELL_PRICE = 500;
	private static final int DIAMOND_BUY_PRICE = 700;
	private static final int DIAMOND_SELL_PRICE = 700;
	private static final double BUY_INITIAL_X = 32;
	private static final double SELL_INITIAL_X = 465;
	private static final int HEIGHT = 600;
	private HashMap<String, Item> map;
	private GImage heartIcon;
	private GImage diamondIcon;

	public Inventory() {
		map = new HashMap<String, Item>();
		SetUpImages();
		FillInventory();
	}
	
	/**
	 * <p>Return the hash map.</p>
	 * @return
	 */
	public HashMap<String, Item> getInventory(){
		return map;
	}
	
	/**
	 * <p>Add item to the inventory.</p>
	 * @param item
	 */
	public void AddItem(Item item) {
		//check if you don't have the item
		if (!item.isAcquired()) {
			//adding item to map
			map.put(item.getName(), item);
		}
	}
	
	/**
	 * setup the images needed for item.
	 */
	private void SetUpImages() {
		//heart item
		heartIcon = new GImage("Images/VendorImages/Heart.png");
		diamondIcon = new GImage("Images/VendorImages/Diamond.png");
	}
	
	/**
	 * fill the inventory with available items
	 */
	public void FillInventory(){
		//adding it to hashmap
		AddItem(new Item(heartIcon, "Heart", "Win 22% more. Price: $500", HEART_BUY_PRICE, HEART_SELL_PRICE, false));
		AddItem(new Item(diamondIcon, "Diamond", "Lose only 50%. Price: $700", DIAMOND_BUY_PRICE, DIAMOND_SELL_PRICE, false));
	}
	
	/**
	 * Get the item by providing its name
	 * @param itemName
	 * @return
	 */
	public Item GetItem(String itemName){
		return map.get(itemName);
	}
	
	/**
	 * <p>Display all the item on the screen</p>
	 * @param program
	 */
	public void showInventory(MainApplication program){
		int i = 0;
		
		//assign the right position for each item
		for(Entry<String, Item> entry : map.entrySet()){
			//if player hasn't bought it yet
			if (!entry.getValue().acquired){
				entry.getValue().getImage().setLocation(BUY_INITIAL_X + ((entry.getValue().getImage().getWidth() + WIDTH_MARGIN) * i),
						(HEIGHT - entry.getValue().getImage().getHeight())/5 + HEIGHT_MARGIN);
			}
			//if player bought it already
			else if (entry.getValue().acquired){
				entry.getValue().getImage().setLocation(SELL_INITIAL_X + ((entry.getValue().getImage().getWidth() + WIDTH_MARGIN) * i),
						(HEIGHT - entry.getValue().getImage().getHeight())/5 + HEIGHT_MARGIN);
			}
			//refresh the screen
		    program.add(entry.getValue().getImage());
		    i++;
		}
	}
	
	/**
	 * <p>Reset the inventory</p>
	 */
	public void ResetInventory(){
		for(Entry<String, Item> entry : map.entrySet()){
			entry.getValue().setAcquired(false);
		}
	}
	
	/**
	 * <p>See if player has any item.</p>
	 * @return
	 */
	public boolean HasItems(){
		for(Entry<String, Item> entry : map.entrySet()){
			if (entry.getValue().isAcquired())
				return true;
		}
		
		return false;
	}
}

