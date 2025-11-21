public class GameEvent {
	private final GameEventType type;
	private final GameState state;
	private final Object payload; //ie Player, Card etc etc

	public GameEvent(GameEventType type, GameState state, Object payload) {
		this.type = type;
		this.state = state;
		this.payload = payload;
	}

	//getters
	public GameEventType getType() {
		return type;
	}

	public GameState getState() {
		return state;
	}

	public Object getPayload() {
		return payload;
	}
}
