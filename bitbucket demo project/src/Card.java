import acm.graphics.*;

public class Card extends GImage{
		
	protected String suit = null;
	protected String rank = null;
    protected int pointValue = 0;
    public static final String ACE = "1";
    public static final String KING = "k";
    public static final String QUEEN = "q";
    public static final String JACK = "j";
    public static final String TEN = "10";
    public static final String NINE = "9";
    public static final String EIGHT = "8";
    public static final String SEVEN = "7";
    public static final String SIX = "6";
    public static final String FIVE = "5";
    public static final String FOUR = "4";
    public static final String THREE = "3";
    public static final String TWO = "2";
    public static final String CLUB = "c";
    public static final String DIAMOND = "d";
    public static final String HEART = "h";
    public static final String SPADE = "s";
    public boolean visible  = true;
    private boolean faceUp;

    public static final String backOfCard = "Images/Images.Cards/Cards/b2fv.gif";
    public static final String[] SUITS = {Card.CLUB,Card.DIAMOND,Card.HEART,Card.SPADE};
    public static final String[] RANKS = {Card.KING,Card.QUEEN,Card.JACK,Card.TEN,Card.NINE,Card.EIGHT,Card.SEVEN,Card.SIX,Card.FIVE,Card.FOUR,Card.THREE,Card.TWO,Card.ACE};
	public static GImage makeCard(String suit, String rank)
	{
		String filename = null;
		filename = getFileName(suit, rank);
		new GImage(filename).getImage();   
	    return new GImage("Images/Images.Cards/Cards" + filename);
	}
	
	public static String getFileName(String suit, String rank)
	{
		return String.format("%s%s.gif",suit,rank);

	}
	
	public Card(int x, int y, String suit, String rank, boolean faceUp)
	{
		super("Images/Images.Cards/Cards/" + getFileName(suit, rank), x, y);
		this.suit = suit;
	    this.rank = rank;
	    this.faceUp = faceUp;
	    if(!faceUp)
	    {
	    	super.setImage(backOfCard);
	    }
	    
	    if(this.rank.equals(Card.KING) || rank.equals(Card.QUEEN) || rank.equals(Card.JACK))
	    {
            pointValue += 10;
        }
	    
	    else if(this.rank.equals(Card.ACE))
	    {
	    	pointValue += 1;
        }
	    
	    else
	    {
	    	pointValue += Integer.parseInt(rank);    
	    }
	}
	
	public void showCard()
	{
		faceUp = true;
		super.setImage("Images/Images.Cards/Cards/" + getFileName(suit, rank));
	}
	
	public void hideCard()
	{
		faceUp = false;
		super.setImage(backOfCard);
	}
	
	public String getSuit()
	{
	    return(this.suit);
	}
	
	public String getRank()
	{
	    return(this.rank);
	}
	
	public int getPointValue()
	{
	    return(this.pointValue);
	}
	
	public boolean isAce()
	{
	      if(this.rank.equals(Card.ACE))
	      {
	          return true;
	      }
	      
	      else
	      {
	    	  return false;
	      } 
	}
	
}
