import java.util.*;

public class BlackjackGame {
	private RoundManager roundManager;
	private final UserInterface ui;
	private final GameConfig config;

	private final Deck deck;
	private final List<Player> players;
	private final Dealer dealer;
	// todo: private CardCounter counter;

	public BlackjackGame(GameConfig config, UserInterface ui) {
		this.config = config;
		this.ui = ui;
		this.deck = new Deck(config.getNumDecks());
		this.players = new ArrayList<>();
		this.dealer = new Dealer();
	}

	public void addPlayer(Player player) {
		players.add(player);
	}

	public void startGame() {
		deck.reset(config.getNumDecks());
		deck.shuffle();
		roundManager = new RoundManager(deck, players, dealer, config, ui);
		//todo: counter.reset();
	}

	public void playRound() {

		//1---collect bets
		roundManager.collectBets();
		ui.displayGameState(this, "Bets have been Placed...");

		//2---Deal first 2 cards to each player (alternating -> one per round)
		roundManager.dealInitialCards();
		ui.displayGameState(this, "Initial Deal");

		//3---Handle player turns sequentially
		roundManager.executePlayerTurns();

		//4---Do Dealers turn
		roundManager.executeDealerTurn();
		ui.displayGameState(this, "Dealer's Turn Finished");

		//5----Find winners --> give payouts
		roundManager.processPayout();
		ui.displayGameState(this, "Round Complete");
	}

	public void resetRound() {
		//clear the hands of all the players
		for (Player player : players) {
			player.clearHands();
		}
		dealer.clearHand();
	}

	//GETTERS
	public List<Player> getPlayers() {
		return players;
	}
	public Dealer getDealer() {
		return dealer;
	}
	public GameConfig getConfig() {
		return config;
	}
}
