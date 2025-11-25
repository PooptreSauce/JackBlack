public class Dealer {
	private final Hand hand;

	public Dealer() {
		this.hand = new Hand(0);
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

	public void addCard(Card card) {
		hand.addCard(card);
	}

	public Hand getHand() {
		return hand;
	}

	public void clearHand() {
		hand.clear();
	}

	@Override
	public String toString() {
		return "Dealer: " + hand;
	}
}
