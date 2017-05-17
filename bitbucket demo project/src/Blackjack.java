public class Blackjack extends BlackJackCards
{
	BlackJackCards blackjackCards = new BlackJackCards();
    public static String HIT = "hit";
    public static String STAND = "stand";
    public static String COMPUTER = "computer";
    public static String HUMAN = "human";
    public static String TIE = "tie";
	
    public Blackjack()
    {
        this.makeShuffledDeck();
    }
    
    public final void makeShuffledDeck()
    {
		   shuffleDeck(makeDeck());
	}

	public void addDiscardedAndShuffle()
	{
	    deck.addAll(discardedCards);
	    shuffleDeck(deck);
	}

	public void dealPlayersCards()
	{
		playerCards.addElement(deck.firstElement());
	    this.deck.remove(deck.firstElement());
	}

	public void dealComputersCards()
	{
		computerCards.addElement(deck.firstElement());
	        this.deck.remove(deck.firstElement());
	}

	public int getPlayerScore()
	{
	   return (getScore(playerCards));
	}

	public int getComputerScore()
	{
	    int score = 0;
	    for(Card card : computerCards){
	            score += card.pointValue;
	    }
	   return (score); 
	}

	public int clearTable()
	{
	   int currentDiscardedCardsSize = discardedCards.size();
	   moveCards(playerCards,discardedCards);
	   moveCards(computerCards,discardedCards);
	   return (discardedCards.size() - currentDiscardedCardsSize);
	}
	
	public int cardsInDeckCount()
	{
	    return(deck.size());
	}
}
