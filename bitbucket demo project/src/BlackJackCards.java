import java.util.Collections;
import java.util.Vector;

public class BlackJackCards {

	protected Vector<Card>deck = new Vector<Card>();
    protected Vector<Card>discardedCards = new Vector<Card>();
    protected Vector<Card>playerCards = new Vector<Card>();
    protected Vector<Card>computerCards = new Vector<Card>();
    
	public BlackJackCards() 
	{
	    Collections.shuffle(deck);
	    discardedCards.removeAllElements();
	    playerCards.removeAllElements();	    
	    computerCards.removeAllElements();
	}
	
	public Vector<Card> getDeck()
	{
		return deck;
	}
	
	public Vector<Card> getDiscardedCards()
	{
		return discardedCards;
	}
	
	public Vector<Card> getPlayerCards()
	{
		return playerCards;
	}
	
	public Vector<Card> getComputerCards()
	{
		return computerCards;
	}
	
	public boolean hasAnAce(Vector<Card>cards)
	{
	    for(int i=0;i < cards.size();i++)
	    {
	        if(cards.get(i).getRank().equals(Card.ACE))
	        {
	        	return true;
	        }
	    }
	return false;
	}
	
	public int getPointSum(Vector<Card>cards)
	{
		  int sum = 0;
		  for(int i = 0;i < cards.size();i++)
		  {
			  sum += cards.get(i).getPointValue();
		  }
		  return sum;
	}

	public int getScore(Vector<Card> cards)
	{
		int score = getPointSum(cards);
	    if (hasAnAce(cards))
	    {
	    	if (score + 10 <= 21)
	    	{
	    		score = score + 10;
	        }
	    }
	    return(score);
	}
	
	Vector<Card>makeDeck()
	{
		int indexCards = 0;
		for (String suitFileName: Card.SUITS)
		{
			for (String rankFileName : Card.RANKS)
			{
				Card currentCard = new Card(0,0,suitFileName, rankFileName, true);
				this.deck.add(indexCards,currentCard);
				indexCards++; 
			}
		}
		Collections.shuffle(this.deck);
		return this.deck;
	}
	
	public void clearAllDecks()
	{
		deck.removeAllElements();
		discardedCards.removeAllElements();
	    playerCards.removeAllElements();	    
	    computerCards.removeAllElements();		
	}
	
	int cardCount(Vector<Card>cards)
	{
	    return(cards.size());
	}

	/**
	 * Shuffle the cards provided
	 * @param cards vector of cards to be shuffled
	 * @return the shuffled cards
	 */
	Vector<Card> shuffleDeck(Vector<Card> cards)
	{
	    Collections.shuffle(cards);
	    return cards;
	}
	
	void moveCards(Vector<Card>source, Vector<Card>target) throws RuntimeException
	{
	    if(source.isEmpty())throw new RuntimeException ("No elements in the Vector");
	    target.addAll(source);
	    source.removeAllElements();
	}
	
	public String computersAction(Vector<Card>computerCards) {
		  int score = getScore(computerCards);
		  if(score < 17)
		  {
			  return("Hit");
		  }
		  
		  else
		  {
			  return ("Stand");
		  }
		}
	
	public String winner(Vector<Card>cardsHeldByComputerPlayer, Vector<Card>cardsHeldByHumanPlayer){
	    
	    int scoreForComputer = getScore(this.computerCards);
	    int scoreForPlayer = getScore(this.playerCards);
	    
	    if(scoreForPlayer > 21 && scoreForComputer <= 21) 
	    {
	    	return("You Lose!");
	    }
	    if(scoreForComputer > 21 && scoreForPlayer <= 21)
	    {
	    	return ("You Win!");
	    }
	    if(scoreForComputer == scoreForPlayer)
	    { 
	    	return("It's a Tie!");
	    }
	    if(scoreForComputer > scoreForPlayer)
	    {
	    	return("You Lose!");
	    }
	    return("Player");
	}
	
	public Card drawFromDeck()
	{
		if(deck.size() == 0)
		{
			moveCards(discardedCards, deck);
			Collections.shuffle(deck);
		}
		return deck.remove(0);
	}
	
}