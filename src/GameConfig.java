public class GameConfig {
	private final int numDecks;
	private final int minBet;
	private final int maxBet;
	private final int startingChips;
	private final boolean animatedMode; //automatic game pacing
	private final boolean manualStepMode; // "press enter to continue"
	private final long uiDelayMs;


	//todo: refactor to be compact + editable
	public GameConfig(int numDecks, int minBet, int maxBet, int startingChips, boolean animatedMode, boolean manualStepMode, long uiDelayMs) {
		this.numDecks = numDecks;
		this.minBet = minBet;
		this.maxBet = maxBet;
		this.startingChips = startingChips;
		this.animatedMode = animatedMode;
		this.manualStepMode = manualStepMode;
		this.uiDelayMs = uiDelayMs;
	}

	//Im doing a builder pattern so everything is easier to config
	public static class Builder {
		private int numDecks = 1;
		private int minBet = GameConstants.DEFAULT_MIN_BET;
		private int maxBet = GameConstants.DEFAULT_STARTING_CHIPS;
		private int startingChips = GameConstants.DEFAULT_STARTING_CHIPS;
		private boolean	animatedMode = true;
		private boolean manualStepMode = false;
		private long uiDelayMs = 300;

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

		public Builder animated(boolean value) {
			this.animatedMode = value;
			return this;
		}

		public Builder manualStep(boolean value) {
			this.manualStepMode = value;
			return this;
		}

		public Builder uiDelay(long value) {
			this.uiDelayMs = value;
			return this;
		}

		public GameConfig build() {
			return new GameConfig(numDecks, minBet, maxBet, startingChips, animatedMode, manualStepMode, uiDelayMs);
		}
	}

	//GETTERS
	public int getNumDecks() { return numDecks; }
	public int getMinBet() { return minBet; }
	public int getMaxBet() { return maxBet; }
	public int getStartingChips() { return startingChips; }
	public boolean isAnimated() { return animatedMode;}
	public boolean isManualStep() { return manualStepMode;}
	public long getUiDelayMs() { return uiDelayMs;}
}
