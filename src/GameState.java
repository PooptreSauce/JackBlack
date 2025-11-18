//Represent state of the game. use these to compartmentalize
//game flow and prevent "out of bounds" actions
public enum GameState {
	WAITING_FOR_PLAYERS,
	COLLECTING_BETS,
	DEALING_INITIAL_CARDS,
	PLAYER_TURNS,
	DEALER_TURN,
	PAYOUT,
	ROUND_COMPLETE
}
