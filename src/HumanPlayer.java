import java.util.Scanner;

public class HumanPlayer extends Player {
	private final UserInterface ui;

	public HumanPlayer(String name, int chips, UserInterface ui) {
		super(name, chips);
		this.ui = ui;
	}

	@Override
	public int placeBet(int minBet, int maxBet) throws InvalidBetException, InsufficientChipsException {
		//check if player has enough chips
		if (getChips() < minBet) {
			throw new InsufficientChipsException(
				"Not enough chips... you have " + getChips() + ", minimum bet is " + minBet
			);
		}

		int actualMaxBet = Math.min(maxBet, getChips());
		int bet = ui.getBetInput(getName(), minBet, actualMaxBet);

		subtractChips(bet); //deduct bet from chips
		getCurrentHand().setBet(bet);
		return bet;
	}

	@Override
	public boolean decideHit(Hand hand, Card dealerUpCard) {
		PlayerAction action = ui.getPlayerAction(getName(), hand, hand.getHandValue());
		if (action == PlayerAction.HIT) {
			return true;
		} else {
			return false;
		}
	}
}
