public class GameConfig {
	private final int numDecks;
	private final int minBet;
	private final int maxBet;
	private final int startingChips;

	public GameConfig(int numDecks, int minBet, int maxBet, int startingChips) {
		this.numDecks = numDecks;
		this.minBet = minBet;
		this.maxBet = maxBet;
		this.startingChips = startingChips;
	}

	//Im doing a builder pattern so everything is easier to config
	public static class Builder {
		private int numDecks = 1;
		private int minBet = GameConstants.DEFAULT_MIN_BET;
		private int maxBet = GameConstants.DEFAULT_STARTING_CHIPS;
		private int startingChips = GameConstants.DEFAULT_STARTING_CHIPS;

		public Builder numDecks(int numDecks) {
			this.numDecks = numDecks;
			return this;
		}

		public Builder minBet(int minBet) {
			this.minBet = minBet;
			return this;
		}

		public Builder maxBet(int maxBet) {
			this.maxBet = maxBet;
			return this;
		}

		public Builder startingChips(int startingChips) {
			this.startingChips = startingChips;
			return this;
		}

		public GameConfig build() {
			return new GameConfig(numDecks, minBet, maxBet, startingChips);
		}
	}

	//GETTERS
	public int getNumDecks() { return numDecks; }
	public int getMinBet() { return minBet; }
	public int getMaxBet() { return maxBet; }
	public int getStartingChips() { return startingChips; }

}
