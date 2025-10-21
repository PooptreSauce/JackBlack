import java.util.ArrayList;
import java.util.List;


public class Hand {
	/*
	-  HAS (ArrayList stack of cards)

		--> Number of cards in ArrayList (Stack)
		--> SUM of CARDS in HAND will be assigned to player for their CURRENT "SCORE"
		--> Balance
	*/

	private List<Card> cards = new ArrayList<>();
	private int bet;

	public Hand(int bet) {
		this.bet = bet;
	}

	public void addCard(Card card) {
		//todo: add card to hand
	}

	public int getValue() {
		//Todo: calculate hand value (also need to handle aces)
		return 0;
	}

	public boolean canSplit() {
		//todo: check if hand has EXACTLY 2 cards of same rank for split
		return false;
	}

	public List<Card> getCards() {
		//todo: return COPY of cards list
		return new ArrayList<>(cards);
	}

	public int getBet() {
		return bet;
	}

	public void setBet(int bet) {
		this.bet = bet;
	}

	public boolean isBust() {
		//todo check if hand value > 21
		return false;
	}

	public boolean isBlackjack() {
		//todo check if natural blackjack
		return false;
	}


	public void clear() {
		//todo clear cards and reset the bet
		cards.clear();
		bet = 0;
	}

	@Override
	public String toString() {
		return cards.toString();
	}
}
