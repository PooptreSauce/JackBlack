import java.util.*;

public class Main {
	public static void main(String[] args) {

		//first, create a game configuration
		GameConfig config = new GameConfig.Builder()
				.numDecks(1)
				.minBet(10)
				.maxBet(1000)
				.startingChips(1000)
				.build();

		//create UI
		UserInterface ui = new ConsoleUI();

		BlackjackGame game = new BlackjackGame(config, ui);

		//("if" for safety)
		if (ui instanceof ConsoleUI consoleUI) {
			consoleUI.setGame(game); //let the consoleUI have access to game object
		}

		game.startGame();

		//get player name and add player with starting chips
		String playerName = ui.getPlayerName();
		game.addPlayer(new HumanPlayer(playerName, config.getStartingChips()));
		game.addPlayer(new HumanPlayer(playerName, config.getStartingChips()));


		//MAIN GAME LOOP-----
		boolean playing = true;
		while (playing) {
			game.playRound();
			game.resetRound();

			//see if player wants to continue
			ui.displayMessage("Play again? (y/n)");

			Scanner scanner = new Scanner(System.in);
			String response = scanner.nextLine().trim().toLowerCase();
			if (!response.equals("y")) {
				playing = false;
			}

		}

	}
}
