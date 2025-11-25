import java.util.List;

public final class AsciiCardRenderer {

    private AsciiCardRenderer() {} //no instantiation

    //render cards as a fully assembled HAND side by side
    public static void printHand(List<Card> cards) {
        //check if hand empty
        if (cards == null || cards.isEmpty()) {
            System.out.println("[EMPTY HAND]");
            return;
        }

        //convert each card to 7 line ascii "block" (not using 2d array for simplicity)
        List<String[]> cardLines = cards.stream()
                .map(AsciiCardRenderer::renderCard)
                .toList();

        //print line by line (to keep cards aligned horizontally)
        for (int line = 0; line < cardLines.get(0).length; line++) {
            for (String[] card : cardLines) {
                System.out.print(card[line] + " ");
            }

            System.out.println();
        }
    }

    public static void printLabeledHand(String label, Hand hand, boolean hideHoleCard) {
        //empty hand
        if (hand == null || hand.getCards().isEmpty()) {
            System.out.println(label + "  (no cards yet)");
            return;
        }

        int displayValue;
        if (hideHoleCard) {
            //show only visible (up-card) value
            Card up = hand.getCards().get(0);
            displayValue = up.getCardValue();
        } else {
            displayValue = hand.getHandValue();
        }

        System.out.printf("%s  (Value: %d)%n", label, displayValue);

        //render cards
        if (hideHoleCard && hand.getCards().size() >= 2) {
            //visible + hidden card
            Card visible = hand.getCards().get(0);
            printHandWithHiddenCard(List.of(visible));
            return;
        }

        printHand(hand.getCards());
    }

    private static void printHandWithHiddenCard(List<Card> visibleCards) {
        List<String[]> cardLines = visibleCards.stream()
                .map(AsciiCardRenderer::renderCard)
                .toList();

        String[] hiddenCard = {
                "┏━━━━━━━━━┓",
                "┃░░░░░░░░░┃",
                "┃░░░░░░░░░┃",
                "┃░░░░░░░░░┃",
                "┃░░░░░░░░░┃",
                "┃░░░░░░░░░┃",
                "┗━━━━━━━━━┛"
        };

        //append hidden card lines
        for (int line = 0; line < hiddenCard.length; line++) {
            for (String[] card : cardLines) {
                System.out.print(card[line] + " ");
            }
            System.out.print(hiddenCard[line]);
            System.out.println();
        }
    }

    //render a single card into a 7 line ascii format (11x7) for each card
    private static String[] renderCard(Card card) {
        String rank = getRankSymbol(card.getRank());
        String suit = getSuitSymbol(card.getSuit());

        return new String[] {
                              "┏━━━━━━━━━┓",
                String.format("┃%-2s       ┃", rank),
                              "┃         ┃",
                String.format("┃    %s    ┃", suit),
                              "┃         ┃",
                String.format("┃       %-2s┃", rank),
                              "┗━━━━━━━━━┛"
        };
    }

    //mapping of enum SUIT values ----> "Miscellaneous Symbols" Unicode symbols
    private static String getSuitSymbol(Suit suit) {
        return switch (suit) {
            case HEARTS -> "♥";
            case DIAMONDS -> "♦";
            case CLUBS -> "♣";
            case SPADES -> "♠";
        };
    }

    //mapping of enum RANK values to shortened labels
    private static String getRankSymbol(Rank rank) {
        return switch (rank) {
            case ACE -> "A";
            case TWO -> "2";
            case THREE -> "3";
            case FOUR -> "4";
            case FIVE -> "5";
            case SIX -> "6";
            case SEVEN -> "7";
            case EIGHT -> "8";
            case NINE -> "9";
            case TEN -> "10";
            case JACK -> "J";
            case QUEEN -> "Q";
            case KING -> "K";
        };
    }
}