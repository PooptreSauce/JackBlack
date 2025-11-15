public class Dealer {
	private final Hand hand;

	public Dealer() {
		this.hand = new Hand(0);
	}

	//Using STANDARD rules
	// handValue >= 17 --> stand
	// handValue <= 16 --> hit
	public void playTurn(Deck deck) {
		while (hand.getHandValue() < 17) {
			Card card = deck.dealCard();
			if (card != null) {
				hand.addCard(card);
			}
		}
	}

	//returns dealers face-up card (first dealt card)
	public Card getUpCard() {
		//todo: make direct isEmpty access
		if (hand.getCards().isEmpty()) {
			return null;
		}
		//upcard
		return hand.getCards().get(0);
	}

	//get the dealers full hand (!AFTER! player turns)
	public Hand getHand() {
		return hand;
	}

	//clear hand for the NEXt round
	public void clearHand() {
		hand.clear();
	}

	@Override
	public String toString() {
		return "Dealer: " + hand;
	}
}
