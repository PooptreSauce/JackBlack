import java.util.*;

public class BlackjackGame {
	private Deck deck;
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
		renderer.renderGameState(this); //render this blackjackGame object
	}

	//get players
	/*
	public void getPlayers() {
		for (players)
	}
	*/


	//get dealer

	//reset round
	//main



}
