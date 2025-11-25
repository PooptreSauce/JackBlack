import java.util.Scanner;
import java.util.List;

public class ConsoleUI implements UserInterface, GameEventListener {
	private final Scanner scanner = new Scanner(System.in); //DONT CLOSE (TIED TO SYSTEM.IN)

	//todo: remove and switch to using DTO for multiplayer
	//ui currently holds game reference to display table
	private BlackjackGame game;

	public void setGame(BlackjackGame game) {
		this.game = game;
	}

	//------------UI PACING HELPERS
	private void uiPause() {
		if (game == null) return;

		GameConfig config = game.getConfig(); //to grab pacing values from config

		if (config.isManualStep()) {
			waitForPlayer(""); //("prompt")
		}
		else if (config.isAnimated()) {
			try {
				Thread.sleep(config.getUiDelayMs());
			} catch (InterruptedException ignored) {}
		}
	}

	private void waitForPlayer(String prompt) {
		System.out.print(prompt + " (Press Enter) ");

		try {
			System.in.read();
			//clear extra newlines maybe still in input buffer
			while (System.in.available() > 0) System.in.read();
		} catch (Exception ignored) {}
		System.out.println();
	}
	//--------------EVENT HANDLING
	@Override
	public void onGameEvent(GameEvent event) {
		GameEventType type = event.getType();
		GameState state = event.getState();

		//what type of event? (!!not state!!)
		switch (type) {
			case STATE_CHANGED -> {
				if (state != GameState.COLLECTING_BETS) {
					refreshScreen(state);
				}
				else {//DO NOT show full table during bet phase
					TerminalUtils.clearScreen();
					displayGameState(state);
					printDivider();
				}
			}
			case DEALER_HIT -> {
				Card card = (Card) event.getPayload();

				displayMessage("Dealer draws: " + card);

				uiPause();
				refreshScreen(event.getState());
			}
			case PLAYER_HIT -> {
				Object[] payload = (Object[]) event.getPayload();
				Player player = (Player) payload[0];
				Card card = (Card) payload[1];

				displayMessage(player.getName() + " HITS: " + card);

				uiPause();  //<----delay for dealing
				refreshScreen(event.getState());
			}
			case PLAYER_BET -> {
				PlayerResult playerResult = (PlayerResult) event.getPayload();
				if (playerResult.payout() > 0) {
					displayMessage(playerResult.player().getName() + " places bet: " + playerResult.payout());
				}
			}
			case ROUND_END -> {
				@SuppressWarnings("unchecked") //it's fine to cast as this list type
				List<PlayerResult> results = (List<PlayerResult>) event.getPayload();
				refreshScreen(event.getState()); //show final table ONE time
				printRoundSummary(results);
				uiPause();
			}
			case ERROR_MESSAGE -> {
				String msg = (String) event.getPayload();
				displayMessage("ERROR! " + msg);
			}
		}
	}

	@Override
	public void displayGameState(GameState state) {
		System.out.println("\n = = " + state + " = =");

		//this is technically redundant
		/*
		switch (state) {
			case COLLECTING_BETS -> System.out.println("Collecting Bets...");
			case DEALING_INITIAL_CARDS -> System.out.println("Dealing initial cards...");
			case PLAYER_TURNS -> System.out.println("Players are taking turns..."); //todo: individualize for each player
			case DEALER_TURN -> System.out.println("Dealer revealing and drawing cards...");
			case PAYOUT -> System.out.println("Paying out winnings!!!");
			case ROUND_COMPLETE -> System.out.println("Round finished!!!");
			default -> {} //not really sure how to test this
		}
		*/

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

		boolean hideHole = (state == GameState.DEALING_INITIAL_CARDS || state == GameState.PLAYER_TURNS);

		//DEALER display
		if (!dealerHand.getCards().isEmpty()) {
			AsciiCardRenderer.printLabeledHand("Dealer", dealerHand, hideHole);
		}
		else {
			System.out.println("Dealer has not been dealt cards yet...");
		}

		printBorder2();

		//PLAYER(S) display
		for (Player player : game.getPlayers()) {
			printBorder();
			Hand hand = player.getCurrentHand();
			int bet = hand.getBet();

			if (state == GameState.COLLECTING_BETS) {
				System.out.println(player.getName() + " waiting to bet...");
				continue;
			}

			PlayerRoundState prs = player.getRoundState();

			switch (prs) {
				case WAITING_FOR_BET -> System.out.println(player.getName() + " waiting to bet...");
				case SITTING_OUT -> System.out.println(player.getName() + " is sitting out this round...");
				case PLAYING, FINISHED -> {
					System.out.printf("%s (Chips: %d, Bet: %d)\n", player.getName(), player.getChips(), hand.getBet());
					System.out.printf("   Hand (Value: %d):\n", hand.getHandValue());
					AsciiCardRenderer.printHand(hand.getCards());
					if (hand.isBust()) System.out.println("  --> BUST!!!");
					else if (hand.isBlackjack()) System.out.println("  --> BLACKJACK!!!");
				}
			}
		}

		printDivider();

	}

	//FORMAT/DISPLAY HELPERS
	private void printDivider() {
		System.out.println("===============================================");
	}

	private void printBorder() {
		System.out.println("-----------------------------------------------");
	}

	private void printBorder2() {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

	private void printHeader(String text) {
		System.out.printf("%n==== %s ====%n", text);
	}

	private void refreshScreen(GameState state) {
		if (game == null) return;

		TerminalUtils.clearScreen();
		displayGameState(state);
		printDivider();
		showTable(game, state);
	}

	//iterate through all players' results, print their name, HandOutcome and what they won/lost
	private void printRoundSummary (List<PlayerResult> results) {
		printHeader("RESULTS");

		for (PlayerResult pr : results) {
			Player p = pr.player();
			HandOutcome outcome = pr.outcome();
			int change = pr.payout();

			String outcomeText = switch (outcome) {
				case PLAYER_BLACKJACK -> "BLACKJACK! +" + change;
				case PLAYER_WIN -> "WIN +" + change;
				case PUSH -> "PUSH (no change)";
				case PLAYER_LOSE -> "LOSE -" + Math.abs(change);
				case PLAYER_BUST -> "BUST -" + Math.abs(change);
			};

			System.out.printf("%-10s %-15s (Chips: %d)\n", p.getName(), outcomeText, p.getChips());
		}

		printDivider();
	}
}
