import java.util.*;

public class BlackjackGame {
	private final Deck deck;
	private List<Player> players;
	private Dealer dealer;
	// todo: private CardCounter counter;
	private GameRenderer renderer;
	private int numDecks;

	public BlackjackGame(int numDecks) {
		this.deck = new Deck(numDecks);
		this.players = new ArrayList<>();
		this.dealer = new Dealer();
		//todo: this.counter = new CardCounter(numDecks);
		this.renderer = new GameRenderer();
		this.numDecks = numDecks;
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public void startGame() {
		deck.reset(numDecks);
		deck.shuffle();
		//todo: counter.reset();
	}

	public void playRound() {
		//TODO: implement round logic

		//1---collect bets
		for (Player player : players) {
			player.placeBet(1, player.getChips()); //todo make minBet configurable somewhere
		}
		renderer.renderGameState(this, "Bets Have Been Placed...");

		//2---Deal first 2 cards to each player (alternating -> one per round)
		//going to get rid of null checks later...
		for (int i = 0; i < 2; i++) { //-> 2 passes for 2 cards

			for (Player player : players) {
				Card card = deck.dealCard();
				if (card != null) {
					player.hit(player.getCurrentHand(), card);
				}
			}

			Card dealerCard = deck.dealCard();
			if (dealerCard != null) {
				dealer.getHand().addCard(dealerCard);
			}
		}

		//todo: update card counter
		renderer.renderGameState(this, "Initial Deal");

		//3---Handle player turns sequentially
		for (Player player : players) {
			Hand hand = player.getCurrentHand();

			while (!hand.isBust() && player.decideHit(hand, dealer.getUpCard())) {
				Card card = deck.dealCard();
				if (card != null) {
					player.hit(hand, card);
					renderer.renderGameState(this, player.getName() + " Hits");
				}
			}

			if (!hand.isBust()) {
				renderer.renderGameState(this, player.getName() + " Stands");
			}
		}

		//4---Do Dealers turn
		dealer.playTurn(deck);
		renderer.renderGameState(this, "Dealer's Turn COMPLETE (Full Hand is... " + dealer.getHand().getCards() + ", VALUE: " + dealer.getHand().getHandValue() + ")");

		//5----Find winners --> give payouts
		int dealerValue = dealer.getHand().getHandValue();
		boolean dealerBust = dealer.getHand().isBust();
		boolean dealerBlackjack = dealer.getHand().isBlackjack();

		//check each player and their hand(s)
		for (Player player : players) {
			Hand hand = player.getCurrentHand();
			int bet = hand.getBet();
			int playerValue = hand.getHandValue();
			boolean playerBust = hand.isBust();
			boolean playerBlackjack = hand.isBlackjack();

			if (playerBust == true) {
				//isBust already deducts chips (above), so just print
				System.out.println(player.getName() + "busts... they lose: " + bet + " - - - (T_T)");
			}
			else if (playerBlackjack && !dealerBlackjack) {//player wins (3:2 payout)
				int payout = (int) (bet * 1.5);
				player.addChips(bet + payout);
				System.out.println(player.getName() + " WINS WITH BLACKJACK YAYYY!!!! - Payout: " + payout);
			}
			else if (dealerBust || playerValue > dealerValue) {//player wins (1:1 payout)
				player.addChips(bet * 2);
				System.out.println(player.getName() + " wins! Payout: " + bet);
			}
			else if (playerValue == dealerValue) {// "push" --> return bet to player
				player.addChips(bet);
				System.out.println(player.getName() + " ties with dealer (push)");
			}
			else {//otherwise player lost bet (already deducted)
				System.out.println(player.getName() + " loses " + bet);
			}
		}
		renderer.renderGameState(this, "- - -Round is Complete- - -");
	}

	//get List of players
	public List<Player> getPlayers() {
		return players;
	}

	public Dealer getDealer() {
		return dealer;
	}

	public void resetRound() {
		//clear the hands of all the players
		for (Player player : players) {
			player.clearHands();
		}

		dealer.clearHand();
	}

	//main
	public static void main(String[] args) {

		BlackjackGame game = new BlackjackGame(1); //BlackjackGame(numDecks) <----
		game.startGame();

		game.addPlayer(new HumanPlayer("PLAYER 1", 1000)); //todo: allow user to choose name
		for (Player player: game.players) {
			System.out.println(player.toString());
		}
		System.out.println(game.dealer.toString());
		game.playRound();
		game.resetRound();
	}



}
