public class HumanPlayer extends Player {

	public HumanPlayer(String name, int chips) {
		super(name, chips);
	}

	@Override
	public void placeBet(int bet) throws InvalidBetException, InsufficientChipsException {
		if (bet < 0) {
			throw new InvalidBetException("Bet Amount CANT BE NEGATIVE.");
		}

		if (bet > getChips()) {
			throw new InsufficientChipsException("Not enough chips to place that bet.");
		}

		//Deduct and record the bet
		subtractChips(bet);
		getCurrentHand().setBet(bet);
	}
}
