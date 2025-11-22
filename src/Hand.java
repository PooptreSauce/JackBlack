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
		cards.add(card);
	}

	//calculate total numerical hand value (also need to handle aces)
	public int getHandValue() {
		int total = 0;
		int aces = 0;

		for (Card card : cards) {
			int value = card.getCardValue();
			if (card.getRank() == Rank.ACE) {
				aces += 1;
			}
			total += value;
		}

		//todo: change up this logic to be more "accurate" to what's happening
		//if BUST --> change ace value from 11 to 1
		while (total > 21 && aces > 0) {
			total -= 10;
			aces -= 1;
		}

		return total;
	}

	//check if hand has EXACTLY 2 cards which are also the same rank
	public boolean canSplit() {
		if (cards.size() == 2) {
			if (cards.get(0).getRank() == cards.get(1).getRank()) {
				return true;
			}
		}
		//can't split
		return false;
	}

	public List<Card> getCards() {
		return new ArrayList<>(cards);
	}

	public int getBet() {
		return bet;
	}

	public void setBet(int bet) {
		this.bet = bet;
	}

	//if hand value is > 21, hand is BUST
	public boolean isBust() {
		return getHandValue() > 21;
	}

	public boolean isBlackjack() {
        if (getHandValue() == 21 && cards.size() == 2) {
            return true;
        }
		return false;
	}

	public void clear() {
		cards.clear();
		bet = 0;
	}

	@Override
	public String toString() {
		return cards.toString();
	}
}
