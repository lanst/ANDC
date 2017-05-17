import acm.graphics.GImage;

/**
 * <p>This class defines the property of an item.</p>
 * @author Norlan Prudente
 *
 */
public class Item {
	//variables
	GImage image;
	String name;
	String description;
	int buyPrice;
	int sellPrice;
	boolean acquired;

	/**
	 * <p>Constructor for creating an item</p>
	 * @param name
	 * @param description
	 * @param buyPrice
	 * @param sellPrice
	 * @param acquired
	 */
	public Item(GImage image, String name, String description, int buyPrice, int sellPrice, boolean acquired) {
		this.image = image;
		this.name = name;
		this.description = description;
		this.buyPrice = buyPrice;
		this.sellPrice = sellPrice;
		this.acquired = acquired;
	}

	/**
	 * <p>set the name of an item</p>
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * <p>set the description of an item</p>
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * <p>set the price for buying an item</
	 * @param buyPricep>
	 */
	public void setBuyPrice(int buyPrice) {
		this.buyPrice = buyPrice;
	}

	/**
	 * <p>set the price for selling an item</p>
	 * @param sellPrice
	 */
	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	/**
	 * <p>set whether the player has the item or not</p>
	 * @param acquired
	 */
	public void setAcquired(boolean acquired) {
		this.acquired = acquired;
	}

	/**
	 * <p>return the name of an item</p>
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * <p>return the description of an item</p>
	 * @return description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * <p>get the price for buying an item</p>
	 * @return buyPrice
	 */
	public int getBuyPrice() {
		return this.buyPrice;
	}

	/**
	 * <p>get the price for selling an item</p>
	 * @return sellPrice
	 */
	public int getSellPrice() {
		return this.sellPrice;
	}

	/**
	 * <p>check if the item is acquired/bought</p>
	 * @return acquired
	 */
	public boolean isAcquired() {
		return this.acquired;
	}
	
	/**
	 * <p>Get the GImage of the item</p>
	 * @return
	 */
	public GImage getImage() {
		return image;
	}

	/**
	 * <p>Set the GImage of the item</p>
	 * @return
	 */
	public void setImage(GImage image) {
		this.image = image;
	}
}
