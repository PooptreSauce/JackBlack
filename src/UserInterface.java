public interface UserInterface {
	void displayMessage(String message);
	void displayGameState(BlackjackGame game, String context);
	String getPlayerName();
	int getBetInput(String playerName, int minBet, int maxBet);
	PlayerAction getPlayerAction(String playerName, Hand hand, int handValue);
}
