import java.util.List;
import java.util.ArrayList;

public class RoundManager {
	private GameState currentState;
	private final Deck deck;
	private final List<Player> players;
	private final Dealer dealer;
	private final GameConfig config;
	private final UserInterface ui;

	//NOT in the constructor
	private final List<GameEventListener> listeners = new ArrayList<>();

	public RoundManager(Deck deck, List<Player> players, Dealer dealer, GameConfig config, UserInterface ui) {
		this.deck = deck;
		this.dealer = dealer;
		this.players = players;
		this.config = config;
		this.ui = ui;
		this.currentState = GameState.WAITING_FOR_PLAYERS;
	}

	public void addListener(GameEventListener listener) {
		listeners.add(listener);
	}

	private void fireEvent(GameEventType type, Object payload) {
		GameEvent event = new GameEvent(type, currentState, payload);
		for (GameEventListener listener : listeners) {
			listener.onGameEvent(event);
		}
	}

	//-----------------GAME HANDLING METHODS
	//------COLLECTING BETS
	public void collectBets() {
		currentState = GameState.COLLECTING_BETS;
		fireEvent(GameEventType.STATE_CHANGED, null);

		for (Player player : players) {
			try {
				int actualMaxBet = Math.min(config.getMaxBet(), player.getChips());
				int bet;

				//is bet coming from human or AI?
				if (player instanceof AIPlayer ai) {
					bet = ai.decideBet(config.getMinBet(), actualMaxBet);
					fireEvent(GameEventType.PLAYER_BET, new PlayerResult(ai, null, bet));
				}
				else {
					bet = ui.getBetInput(
							player.getName(),
							config.getMinBet(),
							actualMaxBet
					);
				}

				//let player sit out if they want to
				if (bet == 0) {
					player.setRoundState(PlayerRoundState.SITTING_OUT);
					player.getCurrentHand().setBet(0);
					continue;
				}

				//validate then apply via the player model
				player.placeBet(bet);
				player.setRoundState(PlayerRoundState.PLAYING);

			} catch (InvalidBetException | InsufficientChipsException e) {
				fireEvent(GameEventType.ERROR_MESSAGE, player.getName() + ": " + e.getMessage());
				player.getCurrentHand().setBet(0);
			}
		}
	}

	//-----DEAL FIRST TWO CARDS
	public void dealInitialCards() {
		currentState = GameState.DEALING_INITIAL_CARDS;
		fireEvent(GameEventType.STATE_CHANGED, null);

		for (int i = 0; i < 2; i++) {
			for (Player player : players) {

				//todo: (get rid of magic number later) (player states?)
				//sitting out
				if (player.getCurrentHand().getBet() == 0) continue;

				Card card = deck.dealCard();
				if (card != null) player.hit(player.getCurrentHand(), card);
			}
			Card dealerCard = deck.dealCard();
			if (dealerCard != null) dealer.getHand().addCard(dealerCard);
		}

		fireEvent(GameEventType.STATE_CHANGED, null);
	}

	//------EXECUTE ALL PLAYER TURNS IN ORDER
	public void executePlayerTurns() {
		currentState = GameState.PLAYER_TURNS;
		fireEvent(GameEventType.STATE_CHANGED, null);

		for (Player player : players) {
			if (player.getCurrentHand().getBet() == 0) continue;
			executeSinglePlayerTurn(player);
		}
	}

	private void executeSinglePlayerTurn(Player player) {
		Hand hand = player.getCurrentHand();

		//check for blackjack --> auto-stand
		if (hand.isBlackjack()) return;

		while (!hand.isBust()) {
			PlayerAction action;

			//is action from human or AI?
			if (player instanceof AIPlayer ai) {
				action = ai.decideAction(hand);
			}
			else {
				action = ui.getPlayerAction(player.getName(), hand, hand.getHandValue());
			}

			if (action == PlayerAction.HIT) {
				Card card = deck.dealCard();
				if (card != null) {
					player.hit(hand, card);
					fireEvent(GameEventType.PLAYER_HIT, new Object[]{ player, card});
				}
			} else {
				break; //STAND or (other choice [unimplemented])
			}
		}
	}

	//-----DEALER TURN
	public void executeDealerTurn() {
		currentState = GameState.DEALER_TURN;
		fireEvent(GameEventType.STATE_CHANGED, null);
		dealer.playTurn(deck);
	}

	//----PROCESSING PAYOUTS
	public void processPayout() {
		currentState = GameState.PAYOUT;
		fireEvent(GameEventType.STATE_CHANGED, null);

		Hand dealerHand = dealer.getHand();

		for (Player player : players) {
			Hand playerHand = player.getCurrentHand();
			int bet = playerHand.getBet();

			//skip if sitting out (maybe change method so that sitting-out players are "pre-considered")
			if (bet == 0) continue;

			HandOutcome outcome = GameLogic.determineOutcome(playerHand, dealerHand);
			int payout = GameLogic.calculatePayout(outcome, bet);

			if (payout > 0) {
				player.addChips(bet + payout);
			} else if (payout == 0) {
				player.addChips(bet); //return the original bet on push
			}
			//if payout < 0 they already lost their bet (chips deducted earlier)
			playerHand.setBet(0);
			player.setRoundState(PlayerRoundState.FINISHED);

			fireEvent(GameEventType.PLAYER_BET, new PlayerResult(player, outcome, payout)); //let UI handle ALL players
		}

		currentState = GameState.ROUND_COMPLETE;
		fireEvent(GameEventType.STATE_CHANGED, null);
		fireEvent(GameEventType.ROUND_END, null);

	}
	//------------------

	public GameState getCurrentState() {
		return currentState;
	}
}






