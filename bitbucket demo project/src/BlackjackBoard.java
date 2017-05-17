import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import acm.graphics.GImage;
import acm.graphics.GLabel;

public class BlackjackBoard extends GraphicsPane 
{
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	private GImage background;
	private GImage returnButton;
	private GImage hitButton;
	private GImage standButton;
	private AudioPlayer click;
	private MainApplication program;
	private Effects effects;
	private int betAmount;
	private BlackjackGame game;
	private GImage changeBetButton;
	private Data playerData;
	protected final int MONEY_X = 10;
	protected final int MONEY_Y = 490;
	protected final int DECK_X = 720;
	protected final int DECK_Y = 10;
	protected final int HIT_X = 700;
	protected final int HIT_Y = 500;
	protected final int PLAYERHAND_X = 290;
	protected final int PLAYERHAND_Y = 420;
	protected final int PLAYERSCORELABEL_X = 290;
	protected final int PLAYERSCORELABEL_Y = 535;
	protected final int CONDITIONLABEL_X = 320;
	protected final int CONDITIONLABEL_Y = 60;
	protected final int COMPUTERSCORELABEL_X = 290;
	protected final int COMPUTERSCORELABEL_Y = 235;
	protected final int COMPUTERHAND_X = 290;
	protected final int COMPUTERHAND_Y = 120;
	protected final int offset = 12;
	private int playerCardIndex;
	private int computerCardIndex;
	public GLabel playerMoney;
	public GLabel betLabel;
	public GLabel playerScoreLabel;
	public GLabel computerScoreLabel;
	public GLabel playerLoseLabel;
	public GLabel playerWinLabel;
	public GLabel playerTieLabel;
	private Player player;
	private Blackjack allCards;
	private boolean stopDrawingCards;
	private boolean computerTurn;
	
	public BlackjackBoard(MainApplication program, Player player) 
	{
		this.program = program;
		this.player = player;
		effects = new Effects();
		game = new BlackjackGame(this.player);
		allCards = new Blackjack();
		betAmount = game.betAmount;
		setLayout();
	}

	private void drawPlayerCard(Card playerHand)
	{
		playerHand.setLocation(PLAYERHAND_X + (offset*playerCardIndex++), PLAYERHAND_Y);
		program.add(playerHand);
	}
	
	private void drawComputerCard(Card computerHand)
	{
		computerHand.setLocation(COMPUTERHAND_X + (offset*computerCardIndex++), COMPUTERHAND_Y);
		program.add(computerHand);
	}
	
	private void drawStartingCard()
	{
		updateComputerScore();
		updatePlayerScore();
		stopDrawingCards = false;
		playerCardIndex = 0;
		allCards.getPlayerCards().add(allCards.drawFromDeck());
		allCards.getPlayerCards().add(allCards.drawFromDeck());
		
		for(int i = 0; i  < allCards.getPlayerCards().size(); i++)
		{
			drawPlayerCard(allCards.getPlayerCards().get(i));
		}
		
		computerCardIndex = 0;
		Card temp = allCards.drawFromDeck();
		temp.hideCard();
		allCards.getComputerCards().add(temp);
		allCards.getComputerCards().add(allCards.drawFromDeck());
		
		for(int i = 0; i  < allCards.getComputerCards().size(); i++)
		{
			drawComputerCard(allCards.getComputerCards().get(i));
		}
	}
	
	private void computerTurn()
	{
		computerTurn = true;
		allCards.getComputerCards().get(0).showCard();
		while (allCards.getComputerScore() < 17)
		{
			Card tempCard = allCards.drawFromDeck();
			allCards.getComputerCards().add(tempCard);
			drawComputerCard(tempCard);
		}
		updateComputerScore();
		winner();
		updateMoneyLabel();
	}
	
	private void winner()
	{
		if (allCards.getPlayerScore() > allCards.getComputerScore() || allCards.getComputerScore() > 21)
		{
			program.add(playerWinLabel);
			game.player.setMoney(game.player.getMoney() + betAmount);
		}
		
		else if((allCards.getPlayerScore() < allCards.getComputerScore()))
		{
			program.add(playerLoseLabel);
			game.player.setMoney(game.player.getMoney() - betAmount);
		}
		
		else if((allCards.getPlayerScore() == allCards.getComputerScore()))
		{
			program.add(playerTieLabel);
			game.player.setMoney(game.player.getMoney() + 0);
		}
	}
	
	
	private void restartGame()
	{
		for(int i = 0; i < allCards.getPlayerCards().size(); i++)
		{
			program.remove(allCards.getPlayerCards().get(i));
		}
		
		for(int i = 0; i < allCards.getComputerCards().size(); i++)
		{
			program.remove(allCards.getComputerCards().get(i));
			allCards.getComputerCards().get(i).showCard();
		}
		
		allCards.moveCards(allCards.getPlayerCards(), allCards.getDiscardedCards());
		allCards.moveCards(allCards.getComputerCards(), allCards.getDiscardedCards());

		drawStartingCard();
		updatePlayerScore();
		program.remove(playerWinLabel);
		program.remove(playerLoseLabel);
		program.remove(playerTieLabel);

	}
	
	private void setLayout() 
	{
		background = new GImage("Images/Images.Blackjack/Blackjack_Table.png");
		background.setSize(WIDTH,HEIGHT);
		returnButton = new GImage("Images/returnButton.png");
		click = new AudioPlayer("Sounds/Clicked.wav");
		
		playerMoney = new GLabel("Money: ", MONEY_X, MONEY_Y);
		playerMoney.setFont("Bernard MT Condensed-22");
		
		betLabel = new GLabel("Bet: " + betAmount, playerMoney.getX(), playerMoney.getY() + playerMoney.getHeight());
		betLabel.setFont("Bernard MT Condensed-22");
		
		changeBetButton = new GImage("Images/ChangeBet.png");
		changeBetButton.setLocation(betLabel.getX(), betLabel.getY() + betLabel.getHeight());
		changeBetButton.setSize(changeBetButton.getWidth(), changeBetButton.getHeight());
		
		hitButton = new GImage("Images/Images.Blackjack/Hit.png", HIT_X, HIT_Y);
		standButton = new GImage("Images/Images.Blackjack/Stand.png");
		standButton.setLocation(hitButton.getX(), hitButton.getY() + hitButton.getHeight());
		
		playerScoreLabel = new GLabel("Your Score: " + allCards.getPlayerScore(), PLAYERSCORELABEL_X, PLAYERSCORELABEL_Y);
		playerScoreLabel.setFont("Bernard MT Condensed-18");
		
		computerScoreLabel = new GLabel("Computer Score: " + allCards.getComputerScore(), COMPUTERSCORELABEL_X, COMPUTERSCORELABEL_Y);
		computerScoreLabel.setFont("Bernard MT Condensed-18");
		
		playerLoseLabel = new GLabel("YOU LOSE!!!", CONDITIONLABEL_X, CONDITIONLABEL_Y);
		playerLoseLabel.setFont("Bernard MT Condensed-56");
		playerLoseLabel.setColor(Color.red);
		
		playerWinLabel = new GLabel("YOU WIN!!!", CONDITIONLABEL_X, CONDITIONLABEL_Y);
		playerWinLabel.setFont("Bernard MT Condensed-56");
		playerWinLabel.setColor(Color.green);
		
		playerTieLabel = new GLabel("TIE!!!", CONDITIONLABEL_X, CONDITIONLABEL_Y);
		playerTieLabel.setFont("Bernard MT Condensed-56");

	}
	
	@Override
	public void showContents() 
	{
		playerCardIndex = 0;
		program.add(background);
		program.add(returnButton);
		program.add(changeBetButton);
		program.add(playerMoney);
		program.add(hitButton);
		program.add(standButton);
		program.add(betLabel);
		program.add(playerScoreLabel);
		program.add(computerScoreLabel);
		allCards.makeDeck();
		drawStartingCard();
		updateMoneyLabel();
		updateBetLabel();
		updatePlayerScore();
	}
	
	private void updatePlayerScore()
	{
		playerScoreLabel.setLabel("Your Score: " + allCards.getPlayerScore());
	}
	
	private void updateComputerScore()
	{
		computerScoreLabel.setLabel("Computer Score: " + allCards.getComputerScore());
	}
	
	private void updateMoneyLabel()
	{
		playerMoney.setLabel("Money: " + game.player.getMoney());
		if(game.noMoreMoney())
			program.switchToGameOver();
	}
	
	private void updateBetLabel()
	{
		betLabel.setLabel("Bet: " + betAmount);
	}

	@Override
	public void hideContents() 
	{
		program.removeAll();
		allCards.clearAllDecks();
	}

	public void mousePressed(MouseEvent e) 
	{
		if (returnButton.contains(e.getX(), e.getY())) 
		{
			click.Play();
			program.switchToStartingArea();
		}
		
		else if(changeBetButton.contains(e.getX(), e.getY()))
		{
			openBetModifier();
		}
		
		else if(!stopDrawingCards && hitButton.contains(e.getX(), e.getY()))
		{
			Card tempCard = allCards.drawFromDeck();
			allCards.getPlayerCards().add(tempCard);
			if(allCards.getPlayerScore() > 21)
			{
				program.add(playerLoseLabel);
				game.player.setMoney(game.player.getMoney() - betAmount);
				updateMoneyLabel();
				stopDrawingCards = true;
			}
			drawPlayerCard(tempCard);
			updatePlayerScore();
		}
		
		else if(stopDrawingCards && hitButton.contains(e.getX(), e.getY()))
		{
			restartGame();
		}
				
		else if(!stopDrawingCards && standButton.contains(e.getX(), e.getY()))
		{
			stopDrawingCards = true;
			computerTurn();
		}
	}		
	
	@Override
	public void mouseMoved(MouseEvent e) 
	{
		effects.ChangeImage(returnButton, "invertedReturnButton.png", "returnButton.png", e);
		effects.ChangeImage(changeBetButton, "InvertedChangeBet.png", "ChangeBet.png", e);
		effects.ChangeImage(hitButton, "Images.Blackjack/InvertedHit.png", "Images.Blackjack/Hit.png", e);
		effects.ChangeImage(standButton, "Images.Blackjack/InvertedStand.png", "Images.Blackjack/Stand.png", e);
	}
	
	public void openBetModifier()
	{
		JFrame betInput = new JFrame("Change Bet");
		JTextField newBetValue = new JTextField("");
		JButton confirmButton = new JButton("Enter");

		betInput.setLayout(new BorderLayout());
		betInput.setSize(100, 85);
		betInput.setLocation(500, 370);
		betInput.add(newBetValue, BorderLayout.NORTH);
		betInput.add(confirmButton,BorderLayout.SOUTH);
		betInput.setVisible(true);
		betInput.getRootPane().setDefaultButton(confirmButton);
		newBetValue.addMouseListener(new MouseListener()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				newBetValue.setText("");
			}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				
			}
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				
			}
		});

		newBetValue.addKeyListener(new KeyListener()
		{
			boolean newField = true;
			@Override
			public void keyPressed(KeyEvent e)
			{
				
			}

			@Override
			public void keyReleased(KeyEvent e)
			{
				if(newField)
				{ 
					newBetValue.setText(Character.toString(e.getKeyChar()));
					newField = false;
				}
			}

			@Override
			public void keyTyped(KeyEvent e) 
			{
				
			}

		});

		confirmButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				parseIfNumericInput(newBetValue.getText());
				updateBetLabel();
				betInput.dispose();
			}
		});	
	}
	
	private boolean parseIfNumericInput(String betValue)
	{
		try
		{

			if(Integer.parseInt(betValue) > game.getMoney())
			{
				NumberFormatException nfe = new NumberFormatException();
				throw nfe;
			}
			
			else
			{
				betAmount = Integer.parseInt(betValue.trim());
			}
		}
		
		catch (NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}

}

