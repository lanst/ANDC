public class BlackjackGame {

	Player player;
	private int playerMoney;
	int betAmount = 100;
	public BlackjackGame(Player player)
	{
		this.player = player;
		playerMoney = player.getMoney();
	}
	
	public int getMoney()
	{
		return player.getMoney();
	}
	
	public int getBetAmount()
	{
		return betAmount;
	}
	
	public void changeMoney(int newMoney)
	{
		player.setMoney(player.getMoney() + newMoney);
	}

	public static void main(String[] args)
	{
		Player player = new Player();
		BlackjackGame game = new BlackjackGame(player);
	}

	public boolean noMoreMoney()
	{
		if(player.getMoney() <= 0)
		{
			return true;
		}
		return false;
	}
}


