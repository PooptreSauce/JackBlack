import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Going to use enums to make handling different suit/rank types easier (each has inherent value)
enum Suit { HEARTS, DIAMONDS, CLUBS, SPADES }
enum Rank { ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING }


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

	public int getValue() {
		//TODO: implement blackjack value (like 11 for Ace, 10 for face cards etc)
		return 0;
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
