import java.util.Scanner;

public class ConsoleUI implements UserInterface, GameEventListener {
	private final Scanner scanner = new Scanner(System.in); //DONT CLOSE (TIED TO SYSTEM.IN)

	//todo: remove and switch to using DTO for multiplayer
	//ui currently holds game reference to display table
	private BlackjackGame game;

	public void setGame(BlackjackGame game) {
		this.game = game;
	}

	//--------------EVENT HANDLING
	@Override
	public void onGameEvent(GameEvent event) {
		GameEventType type = event.getType();
		GameState state = event.getState();

		//what type of event? (!!not state!!)
		switch (type) {
			case STATE_CHANGED -> {
				displayGameState(state);
				if (game != null) {
					showTable(game, state); //showTable will handle display logic
				}
			}
			case PLAYER_HIT -> {
				Object[] payload = (Object[]) event.getPayload();
				Player player = (Player) payload[0];
				Card card = (Card) payload[1];
				displayMessage(player.getName() + " HITS: " + card);
			}
			case PLAYER_BET -> {
				PlayerResult playerResult = (PlayerResult) event.getPayload();
				displayMessage(playerResult.player().getName() + " bets " + playerResult.payout());
			}
			case ROUND_END -> printHeader("ROUND COMPLETE");
			case ERROR_MESSAGE -> {
				String msg = (String) event.getPayload();
				displayMessage("ERROR! " + msg);
			}
		}
	}

	@Override
	public void displayGameState(GameState state) {
		System.out.println("\n = = " + state + " = =");

		switch (state) {
			case COLLECTING_BETS -> System.out.println("Collecting Bets...");
			case DEALING_INITIAL_CARDS -> System.out.println("Dealing initial cards...");
			case PLAYER_TURNS -> System.out.println("Players are taking turns..."); //todo: individualize for each player
			case DEALER_TURN -> System.out.println("Dealer revealing and drawing cards...");
			case PAYOUT -> System.out.println("Paying out winnings!!!");
			case ROUND_COMPLETE -> System.out.println("Round finished!!!");
			default -> {} //not really sure how to test this
		}

		//later we can do summary or something
	}

	//--------CORE MESSAGE + UI INPUTS
	@Override
	public void displayMessage(String message) {
		System.out.println(message);
	}

	@Override
	public String getPlayerName() {
		System.out.print("Enter your name: ");
		String name = scanner.nextLine().trim();
		if (name.isEmpty()) {
			return GameConstants.DEFAULT_PLAYER_NAME;
		} else {
			return name;
		}
	}

	@Override
	public int getBetInput(String playerName, int minBet, int maxBet) {
		while (true) {
			System.out.printf("%s - enter your bet (min: %d, max: %d, or 0 to sit out): ",
			playerName, minBet, maxBet);

			try {
				int bet = Integer.parseInt(scanner.nextLine().trim());
				if (bet == 0 || (bet >= minBet && bet <= maxBet)) {
					return bet;
				}

			} catch (NumberFormatException e) {
				System.out.println("Enter valid number");
			}
		}
	}

	//todo: add other actions (maybe abort y/n type deal)
	@Override
	public PlayerAction getPlayerAction(String playerName, Hand hand, int handValue) {
		while (true) {
			System.out.printf("%s - hand: %s (value: %d) hit? (y/n) ",
			playerName, hand.getCards(), handValue);

			String input = scanner.nextLine().trim().toLowerCase();

			if (input.equals("y")) {
				return PlayerAction.HIT;
			} else if (input.equals("n")) {
				return PlayerAction.STAND;
			}
			System.out.println("Yo, Enter 'y' or 'n'");
		}
	}

	public boolean askPlayAgain() {
		System.out.print("Play Again? (y/n): ");
		return scanner.nextLine().trim().equalsIgnoreCase("y");
	}

	//--------SHOW TABLE
	//we can add more helper methods to split this up a bit
	public void showTable(BlackjackGame game, GameState state) {
		//title
		printHeader("TABLE");

		//dealer --> has hand
		Dealer dealer = game.getDealer();
		Hand dealerHand = dealer.getHand();

		boolean dealerRevealed = (state != GameState.DEALING_INITIAL_CARDS && state != GameState.PLAYER_TURNS);

		//dealer display
		if (dealerRevealed) {
			System.out.println("Dealer hand: " + dealerHand.getCards() + " (Value: " + dealerHand.getHandValue() + ")");
		}
		else {
			Card upCard = dealer.getUpCard();
			System.out.println("Dealer shows: " + (upCard != null ? upCard.toString() : "NULL?") + " and [hidden]");
		}

		//all players hands AND chips (or sitting out)
		for (Player player : game.getPlayers()) {

			Hand hand = player.getCurrentHand();
			if (hand.getBet() == 0) { //todo: get rid of magic number (player states?)
				System.out.println(player.getName() + " is sitting out this round...");
				continue;
			}

			System.out.printf("%s (Chips: %d, Bet: %d)\n", player.getName(), player.getChips(), hand.getBet());
			System.out.println("   Hand: " + hand.getCards() + " (Value: " + hand.getHandValue() + ")");
			if (hand.isBust()) System.out.println("  --> BUST!!!");
			else if (hand.isBlackjack()) System.out.println("  --> BLACKJACK!!!");
		}

		printDivider();

	}

	//FORMAT/DISPLAY HELPERS
	private void printDivider() {
		System.out.println("-------------------------------------------");
	}

	private void printHeader(String text) {
		System.out.printf("%n==== %s ====%n", text);
	}
}
