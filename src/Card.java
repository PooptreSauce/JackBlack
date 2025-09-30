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

	//ATTRIBUTES
	public String suit;
	public String rank; //i.e "2" "9" "king" "queen" etc
	public int value; //actual number value of card

	//CONSTRUCTOR
	public Card(String suit, String rank, int value) {
		this.suit = suit;
		this.rank = rank;
		this.value = value;
	}

	//GETTERS -----
	public String getSuit() {
		return suit;
	}

	public String getRank() {
		return rank;
	}

	public int getValue() {
		return value;
	}
	//---- END GETTERS


}
