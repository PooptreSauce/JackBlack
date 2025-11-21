import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Going to use enums to make handling different suit/rank types easier (each has inherent value)
enum Suit {
	HEARTS,
	DIAMONDS,
	CLUBS,
	SPADES }
enum Rank {
	ACE(11),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	EIGHT(8),
	NINE(9),
	TEN(10),
	JACK(10),
	QUEEN(10),
	KING(10);

	private int value;

	Rank (int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}


public class Card {

	/*
	Each "Card" has:

	- A suit: (ie spade, club, heart, diamond)
	- A rank: (1-9, king etc)
	- An integer value:
		--> (2-10) just numbers
		--> (10) jack
		--> (10 queen
		--> (10) king
		--> (1 OR 11) ACE

	*/

	//ATTRIBUTES ("Suit" and "Rank" access the enum values)
	private final Suit suit;
	private final Rank rank; //i.e "2" "9" "king" "queen" etc

	//CONSTRUCTOR
	public Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
	}

	//GETTERS -----
	public Suit getSuit() {
		return suit;
	}

	public Rank getRank() {
		return rank;
	}

	//Ace count (1 OR 11) handled in HAND
	public int getCardValue() {
		return rank.getValue();
	}

	public int getCardCountValue() {
		//TODO implement card counting values for HI-LO stuff
		return 0;
	}
	//---- END GETTERS

	@Override
	public String toString() {
		return rank + " of " + suit;
	}
}
