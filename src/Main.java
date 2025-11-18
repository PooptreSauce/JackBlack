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

		//create and start GAME
		BlackjackGame game = new BlackjackGame(config, ui);
		game.startGame();

		//get player name and add player with starting chips
		String playerName = ui.getPlayerName();
		game.addPlayer(new HumanPlayer(playerName, config.getStartingChips(), ui));

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
