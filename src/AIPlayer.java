public class AIPlayer extends Player {

	public AIPlayer(String name, int chips) {
		super(name, chips);
	}

	public void placeBet(int bet) throws InvalidBetException, InsufficientChipsException {
		if (bet <= 0) throw new InvalidBetException("Bet must be positive");
		if (bet > getChips()) throw new InsufficientChipsException("Not enough chips");

		subtractChips(bet);
		getCurrentHand().setBet(bet);
	}

	public int decideBet(int minBet, int maxBet) {
		//simple strategy for now. always bet min until < x chips
		return Math.min(getChips(), minBet);
	}

	public PlayerAction decideAction(Hand hand) {
		int value = hand.getHandValue();
		//naive for now, hit under 15, stand otherwise
		return value < 15 ? PlayerAction.HIT : PlayerAction.STAND;
	}
}
