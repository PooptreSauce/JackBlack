import java.util.*;

abstract class Player {

    /*
      Each "Player" has:
      - A name
      - One or more hands (ArrayList stack of cards)
      - A chip balance
      - Methods to update state (hit, place bet, add/subtract chips)
    */

	protected String name;
	protected List<Hand> hands;
	protected int chips;

	public Player(String name, int chips) {
		this.name = name;
		this.hands = new ArrayList<>();
		this.hands.add(new Hand(0));
		this.chips = chips;
	}

	//-------ABSTRACT methods
	// The controller decides the bet amount and passes it here for validation (MVC)
	public abstract void placeBet(int bet) throws InvalidBetException, InsufficientChipsException;

	//----GAMEPLAY Actions
	public void hit(Hand hand, Card card) {
		hand.addCard(card);
	}

	//----Getters / Helpers

	public List<Hand> getHands() {
		return hands;
	}

	public Hand getCurrentHand() {
		//prototype -> single hand only (splitting later)
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
