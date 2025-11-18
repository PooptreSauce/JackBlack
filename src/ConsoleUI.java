import java.util.Scanner;

public class ConsoleUI implements UserInterface {
	private final Scanner scanner = new Scanner(System.in);

	@Override
	public void displayMessage(String message) {
		System.out.println(message);
	}

	@Override
	public void displayGameState(BlackjackGame game, String context) {
		System.out.println(" = = BLACKJACK - " + context + " = =");
		System.out.println("Dealer Upcard: " + game.getDealer().getUpCard());

		for (Player player : game.getPlayers()) {
			Hand hand = player.getCurrentHand();

			//show if player is sitting out this round
			if (hand.getBet() == 0) {
				System.out.println(player.getName() + " - Sitting out this round...");
				continue;
			}

			if (hand.getCards().isEmpty()) continue; //skips if empty

			System.out.printf("%s (Chips: %d, Bet: %d)%n",
			player.getName(), player.getChips(), hand.getBet());

			System.out.printf(" Hand: %s (Value: %d)%n",
			hand.getCards(), hand.getHandValue());

			if (hand.isBust()) {
				System.out.println(" BUST!!!");
			} else if (hand.isBlackjack()) {
				System.out.println(" BLACKJACK!!!");
			}
		}
		System.out.println("-----------------------------------");
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

				if (bet == 0) {//allow sitting out
					return 0;
				}

				if (bet >= minBet && bet <= maxBet) {
					return bet;
				}
				System.out.printf("Incorrect bet: must be between %d and %d.%n",
				minBet, maxBet);
			} catch (NumberFormatException e) {
				System.out.println("Enter valid number");
			}
		}
	}

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
}
