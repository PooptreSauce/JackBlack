public class GameLogic {

	//compare player hand against dealer hand and pick correct HandOutcome
	//from enum (ie win, lose, blackjack, tie etc)
	public static HandOutcome determineOutcome(Hand playerHand, Hand dealerHand) {

		boolean dealerBust = dealerHand.isBust();
		boolean dealerBlackjack = dealerHand.isBlackjack();
		boolean playerBust = playerHand.isBust();
		boolean playerBlackjack = playerHand.isBlackjack();

		int playerValue = playerHand.getHandValue();
		int dealerValue = dealerHand.getHandValue();

		//run checks for correct outcome
		if (playerBust) {
			return HandOutcome.PLAYER_BUST;
		}
		else if (playerBlackjack && !dealerBlackjack) {
			return HandOutcome.PLAYER_BLACKJACK;
		}
		else if (dealerBust || playerValue > dealerValue) {
			return HandOutcome.PLAYER_WIN;  // === "DEALER_BUST"
		}
		else if (playerValue == dealerValue) {
			return HandOutcome.PUSH; //tie
		}
		else {
			return HandOutcome.PLAYER_LOSE;
		}
	}


	//get the correct payout using player's original bet ==> (blackjack, tie etc)
	public static int calculatePayout(HandOutcome outcome, int bet) {
		switch(outcome) {
			case PLAYER_BLACKJACK:
				return (int) (bet * GameConstants.BLACKJACK_PAYOUT_RATIO);
			case PLAYER_WIN:
				return bet;
			case PUSH:
				return 0; //return original bet
			case PLAYER_LOSE:
			case PLAYER_BUST:
				return -bet;  //player "loses" the bet
			default:
				return 0;

		}
	}
}
