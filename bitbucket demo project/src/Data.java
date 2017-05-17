import java.io.*;
import java.util.ArrayList;
import java.util.Map.Entry;

/**
 * <p>This class is responsible for saving<br>
 *    and reading the saved file.</p>
 * @author Norlan Prudente
 *
 */
public class Data {
	//private variables
	private Player player;				//to save the money
	private Inventory inventory;		//to save the item
	private String fileName;			//file name for saving the data

	//constructor
	public Data(Player player, Inventory inventory){
		this.player	= player;
		this.inventory = inventory;
		fileName 	= "Save.data";
	}

	/**
	 * <p>Save money and item to the file</p>
	 */
	public void Save() {
		try {
			// Assume default encoding.
			FileWriter fileWriter =
					new FileWriter(fileName);

			//Wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter =
					new BufferedWriter(fileWriter);

			//write the string representation of the player's money
			bufferedWriter.write(new Integer(player.getMoney()).toString());
			
			//write the item whether it is acquired or not
			for(Entry<String, Item> entry : inventory.getInventory().entrySet()){
				bufferedWriter.write(" " + entry.getValue().getName() + " " +
				Boolean.toString(entry.getValue().isAcquired()));
			}

			//closing file.
			bufferedWriter.close();
		}
		catch(IOException ex) {
			System.out.println(
					"Error writing to file '"
							+ fileName + "'");
		}
	}

	/**
	 * <p>Read the saved file.</p>
	 */
	public void Load() {
		// This will reference one line at a time
		String line = null;
		ArrayList<String> words = new ArrayList<String>();

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = 
					new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = 
					new BufferedReader(fileReader);

			//read until the end of file
			while((line = bufferedReader.readLine()) != null) {
				//split the line to separate the words 
				String[] w = line.split(" ");
				
				//add the words to array list
				for (int i = 0; i < w.length; i++){
					words.add(w[i]);
				}
			}
			
			//set the total money of the payer
			player.setMoney(Integer.parseInt(words.get(0)));
			
			//set the items
			for (int i = 1; i < words.size(); i++){
				if (i%2 == 0){
					
				}
				else{
					inventory.GetItem(words.get(i)).setAcquired(Boolean.parseBoolean(words.get(i+1)));
				}
			}

			// Always close files.
			bufferedReader.close();         
		}
		catch(FileNotFoundException ex) {
			System.out.println(
					"Unable to open file '" + 
							fileName + "'");                
		}
		catch(IOException ex) {
			System.out.println(
					"Error reading file '" 
							+ fileName + "'");                  
			// Or we could just do this: 
			// ex.printStackTrace();
		}
	}
}
