import java.util.*;

abstract class Player {

	/*
	Each "Player" has:

	- A name
	- A current HAND (ArrayList stack of cards)

		--> SUM of CARDS in HAND will be assigned to player for their CURRENT "SCORE"

	- Balance: i.e $100.00 (This is SUM of HAND balances) (for splitting)

	- Current bet amount

	- Actions
		--> Hit
			---> Hit(Double)
		--> Stand
		--> Split (duplicate) HANDS

	*/

	//ATTRIBUTES
	protected String name;
	protected List<Hand> hands = new ArrayList<>();
	protected int chips;

	public Player(String name, int chips) {
		this.name = name;
		this.hands = new ArrayList<>();
		this.hands.add(new Hand(0));
		this.chips = chips;
	}

	public abstract int placeBet();

	public abstract boolean decideHit(Hand hand, Card dealerUpCard);

	public void hit(Hand hand, Card card) {
		hand.addCard(card);
	}

	public List<Hand> getHands() {
		return hands;
	}

	public Hand getCurrentHand() {
		//todo: prototype only hands single hand for now (>1 for split feature)
		return hands.get(0);
	}

	public int getChips() {
		return chips;
	}

	public void addChips(int amount) {
		this.chips += amount;
	}

	public void subtractChips(int amount) {
		this.chips -= amount;
	}

	public String getName() {
		return name;
	}

	public void clearHands() {
		hands.clear();
		hands.add(new Hand(0));
	}

	@Override
	public String toString() {
		return name + " ($" + chips + ")";
	}
}
