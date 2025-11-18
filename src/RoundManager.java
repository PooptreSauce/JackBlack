import java.util.List;

public class RoundManager {
	private GameState currentState; //ie dealing (WAITING_FOR_PLAYERS, COLLECTING BETS) etc
	private final Deck deck;
	private List<Player> players;
	private final Dealer dealer;
	private final GameConfig config;
	private final UserInterface ui;

	public RoundManager(Deck deck, List<Player> players, Dealer dealer, GameConfig config, UserInterface ui) {
		this.deck = deck;
		this.players = players;
		this.dealer = dealer;
		this.config = config;
		this.currentState = GameState.WAITING_FOR_PLAYERS; //<----- round's game state always starts here
		this.ui = ui;
	}

	//1-----COLLECTING BETS
	public void collectBets() {
		currentState = GameState.COLLECTING_BETS;

		//get bets from each player
		for (Player player : players) {
			try {
				//maxBet -> player can't bet more than their chips OR maxBet value
				player.placeBet(config.getMinBet(), Math.min(config.getMaxBet(), player.getChips()));

			} catch (InvalidBetException | InsufficientChipsException e) {
				//todo: add some kind of balance reload feature to deal with this
				//Log the error and skip this player for the round

				System.err.println("Betting ERROR for " + player.getName() + ": " + e.getMessage());

				//IMPORTANT-> their bet needs to be 0 to not be included in round
				player.getCurrentHand().setBet(0);
				continue;
			}
		}
	}

	//2---Deal first 2 cards to each player (alternating -> one per deal)
	public void dealInitialCards() {
		currentState = GameState.DEALING_INITIAL_CARDS;

		for (int i = 0; i < 2; i++) {
			for (Player player : players) {

				//skip players who didn't place a bet
				if (player.getCurrentHand().getBet() == 0) {
					continue;
				}

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
	}

	//3---Handle player turns sequentially
	public void executePlayerTurns() {
		currentState = GameState.PLAYER_TURNS;
		for (Player player : players) {
			//skip players who didn't place bet
			if (player.getCurrentHand().getBet() == 0) {
				continue;
			}
			executeSinglePlayerTurn(player);
		}
	}

	public void executeSinglePlayerTurn(Player player) {
		Hand hand = player.getCurrentHand();

		//check for blackjack - should auto stand
		if (hand.isBlackjack()) {
			ui.displayMessage(player.getName() + "has BLACKJACK! Automatically stands");
		}
		while (!hand.isBust() && player.decideHit(hand, dealer.getUpCard())) {
			Card card = deck.dealCard();
			if (card != null) {
				player.hit(hand, card);
				ui.displayMessage(player.getName() + " hits: " + card);
			}
		}
	}

	//4---Do Dealers turn
	public void executeDealerTurn() {
		currentState = GameState.DEALER_TURN;
		dealer.playTurn(deck);
	}

	//5----Find winners --> give payouts
	public void processPayout() {
		currentState = GameState.PAYOUT;
		Hand dealerHand = dealer.getHand();

		//go through all players, compare their hand to dealer, payout accordingly
		for (Player player : players) {
			Hand playerHand = player.getCurrentHand(); //todo: deal with multiple hands for split
			int bet = playerHand.getBet();

			//skip players who didn't place bet
			if (bet == 0 ) {
				continue;
			}

			HandOutcome outcome = GameLogic.determineOutcome(playerHand, dealerHand);
			int payout = GameLogic.calculatePayout(outcome, bet);

			if (payout > 0) {
				player.addChips(bet + payout);
			}
			else if (payout == 0) {
				player.addChips(bet); //push --> returns original bet
			}
			//otherwise player lost bet (already deducted)

		}
	}

	public GameState getCurrentState() {
		return currentState;
	}

}
