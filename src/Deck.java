import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Deck {

	private List<Card> cards = new ArrayList<>();

	public Deck(int numDecks) {
		//TODO: initialize deck with numDecks * 52 cards
		reset(numDecks);
	}

	//clear and rebuild the deck with numDecks * 52 card (accessing
	//all the suit and rank enums values)
	public void reset (int numDecks) {
		cards.clear();
		for (int i = 0; i < numDecks; i++) {
			for (Suit suit : Suit.values()) {
				for (Rank rank : Rank.values()) {
					cards.add(new Card(suit, rank));
				}
			}
		}
		shuffle();
	}

	public void shuffle() {
		Collections.shuffle(cards);
	}

	public Card dealCard() {
		//check if deck empty?, then remove from deck
		if (!cards.isEmpty()) {
			return cards.remove(0);
		} else {
			return null;
		}
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}
}
