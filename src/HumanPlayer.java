import java.util.Scanner;

public class HumanPlayer extends Player {

	public static final Scanner scanner = new Scanner(System.in); //A shared scanner for input

	public HumanPlayer(String name, int chips) {
		super(name, chips);
	}

	@Override
	public int placeBet(int minBet, int maxBet) {
		int bet = 0;

		//get and handle BET input from user
		while (true) {
			System.out.print(getName() + " - enter your bet (min: " + minBet + ", max: " + maxBet + "): ");
			try
			{
				bet = Integer.parseInt(scanner.nextLine().trim());
				if (bet >= minBet && bet <= maxBet && bet <= getChips()) {
					break;
				} else {
					System.out.println("Incorrect bet - must be between (" + minBet + " <--> " + maxBet + " (and <= your chips: " + getChips() + ")");
				}
			} catch(NumberFormatException e) {
				System.out.println("Please enter a number");
			}
		}

		subtractChips(bet); //deduct bet from chips
		getCurrentHand().setBet(bet);
		return bet;
	}

	@Override
	public boolean decideHit(Hand hand, Card dealerUpCard) {

		//get and handle HIT/STAND input from user (y/n for now)
		while (true) {
			System.out.print(getName() + " - your hand: " + hand.getCards() + " (value: " + hand.getHandValue() + ") Hit? (y/n)");
			String input = scanner.nextLine().trim().toLowerCase();
			if (input.equals("y")) {
				return true;
			}
			else if (input.equals("n")) {
				return false;
			}
			else {
				System.out.println("Please enter 'y' or 'n' ");
			}
		}
	}
}
