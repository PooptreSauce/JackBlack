public class HumanPlayer extends Player {

	public HumanPlayer(String name, int chips) {
		super(name, chips);
	}

	@Override
	public int placeBet() {
		//todo: replace with REAL input
		//for now (prototype) always bet $10
		return 10;
	}

	@Override
	public boolean decideHit(Hand hand, Card dealerUpCard) {
		//todo: replace with real user inpnut later
		//for now (prototype) simulate hit if handvalue < 17
		return hand.getHandValue() < 17;
	}
}
