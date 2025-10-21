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
}
