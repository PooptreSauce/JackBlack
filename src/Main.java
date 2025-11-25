public class Main {
	public static void main(String[] args) {

		//first, create a game configuration
		GameConfig config = new GameConfig.Builder()
				.numDecks(1)
				.minBet(10)
				.maxBet(1000)
				.startingChips(1000)
				.animated(true)
				.manualStep(false)
				.uiDelay(800)
				.build();

		//create UI
		UserInterface ui = new ConsoleUI();

		BlackjackGame game = new BlackjackGame(config, ui);

		//("if" for safety)
		if (ui instanceof ConsoleUI consoleUI) {
			consoleUI.setGame(game); //let the consoleUI have access to game object
		}

		String player1Name = ui.getPlayerName();

		//get player name and add player with starting chips
		game.addPlayer(new AIPlayer("Robot guy", config.getStartingChips()));
		game.addPlayer(new HumanPlayer(player1Name, config.getStartingChips()));


		game.startGame();

		//MAIN GAME LOOP-----
		boolean playing = true;
		while (playing) {
			game.playRound();
			game.resetRound();
			playing = ui.askPlayAgain();
		}
	}
}