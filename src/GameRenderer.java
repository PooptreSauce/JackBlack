public class GameRenderer {
	public void renderGameState(BlackjackGame game, String context) {
		System.out.println(" = = BLACKJACK - " + context + " = =");
		System.out.println("Dealer Upcard: " + game.getDealer().getUpCard());

		for (Player player : game.getPlayers()) {
			Hand hand = player.getCurrentHand();

			if (hand.getCards().isEmpty()) continue; //skips if empty

			System.out.println
			(
					player.getName() +
					" (Chips: " + player.getChips() +
					", Bet: " + hand.getBet() + ")"
			);
			System.out.println
			(
					" Hand: " +
					hand.getCards() +
					" (Value: " +
					hand.getHandValue() + ")"
			);
			if (hand.isBust()) {
				System.out.println(" BUST!!!");
			} else if (hand.isBlackjack()) {
				System.out.println(" BLACKJACK!!!");
			}
		}
		System.out.println("-----------------------------------");
	}
}
