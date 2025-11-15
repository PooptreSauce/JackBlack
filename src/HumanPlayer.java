public class HumanPlayer extends Player {

	public HumanPlayer(String name, int chips) {
		super(name, chips);
	}

	@Override
	public int placeBet(int minBet, int maxBet) {
		//todo: prototype bet is $10, replace with REAL input
		int bet = 10;
		subtractChips(bet); //deduct bet from chips
		getCurrentHand().setBet(bet);
		return bet;
	}

	@Override
	public boolean decideHit(Hand hand, Card dealerUpCard) {
		//todo: replace with real user inpnut later
		//for now (prototype) simulate hit if handvalue < 17
		return hand.getHandValue() < 17;
	}
}
