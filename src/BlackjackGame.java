import java.util.*;

public class BlackjackGame {
	private RoundManager roundManager;
	private final UserInterface ui;
	private final GameConfig config;

	private final Deck deck;
	private final List<Player> players;
	private final Dealer dealer;
	//todo: private CardCounter counter;

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

		//register UI to the event listeners, "if" is for safety
		if (ui instanceof GameEventListener listener) {
			roundManager.addListener(listener);
		}
	}

	public void playRound() {
		roundManager.collectBets(); //1---collect bets
		roundManager.dealInitialCards(); //2----Deal first 2 cards to each player (alternating -> one per round)
		roundManager.executePlayerTurns(); //3---Handle player turns sequentially
		roundManager.executeDealerTurn(); //4---Do Dealers turn
		roundManager.processPayout(); //5----Find winners ---> give payouts
	}

	//clear the hands of all the players
	public void resetRound() {
		for (Player player : players) {
			player.clearHands();
		player.setRoundState(PlayerRoundState.WAITING_FOR_BET);
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
