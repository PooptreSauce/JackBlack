public interface UserInterface {
	void displayMessage(String message);
	void displayGameState(GameState state);
	String getPlayerName();
	int getBetInput(String playerName, int minBet, int maxBet);
	PlayerAction getPlayerAction(String playerName, Hand hand, int handValue);
}
