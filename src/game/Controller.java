package game;
import java.io.*;

/**
 * The way the user interacts with the game.
 */
public class Controller {
	public View view = null;
	public Game game = null;
	
	public Controller(View view) {
		this.view = view;
	}
	
	/**
	 * Handles user input and keeps track of what the user is input towards
	 */
	public void play() {
		
		while (true) {
			
			if (game == null) {
				//start new game
				this.game = new Game();
			
			}
			
			if (game.rules == null) {
				
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String input = null;
				while (input == null) {
					view.DisplayMenu();
					try {
						 input = br.readLine();
					} catch (IOException e) {
						input = null;
						System.out.println("Input Error: Unable to read input.");
					}
					
					if (input != null) {
						try {
							if(input.equalsIgnoreCase("exit")) {
								view.DisplayExitMessage();
								System.exit(0);
							}
							
							int variation = Integer.parseInt(input);
							
							if (variation == 1) { game.rules = new ClassicChess(); }
							else if (variation == 2) { game.rules = new PeasantsRevoltChess(); }
							else if (variation == 3) { game.rules = new TakeAllChess(); }
							else if (variation == 4) { 
								new TestingSuite(new View()).start(); 
								input = null;
								}
							else {
								input = null;
								System.out.println("Input Error: Not a valid variation index.");
							}
						}
						catch (Exception e)
						{
							input = null;
							System.out.println("Error loading variation.");
						}
					}
					
				}
				game.board = game.rules.SetStartingPositions(this.game.white, this.game.black);
			}
			
			if (game != null) {
				view.DisplayBoard(game.board);
				game.NextTurn();
				
				
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String input = null;
				while (input == null) {
					if(game.rules.checkForStalemate(game.activePlayer, game.board)) {
						view.DisplayStalemateMessage(game.rules.getStalemateMessage(game.activePlayer));
						game = null;
						break;
					}
					
					view.DisplayTurnNotification(game.turnNumber, game.activePlayer.color);
					try {
						 input = br.readLine();
					} catch (IOException e) {
						input = null;
						View.setErrorMessage("Input Error: Unable to read input.");
						view.DisplayErrorMessage();
					}
					
					if (input != null) {
						try {
							
							if(input.equalsIgnoreCase("q") || input.equalsIgnoreCase("quit")) {
								view.DisplaySurrenderMessage(game.activePlayer.color);
								game = null;
								break;
							}
							
							if(input.equalsIgnoreCase("exit")) {
								view.DisplayExitMessage();
								System.exit(0);
							}
							
							
							
							Move move = new Move(game.activePlayer, input, game.board);
					
							if (move.IsValid() && game.rules.ValidateMove(move, game.board) == 0) {
								MoveCompleteResult moveResult = game.CompleteMove(move);
								// check for game ending condition
								if(moveResult.gameOver) {
									view.DisplayWinMessage(moveResult.winner);
									game = null;
									break;
								}
							}
							else {
								input = null;
								view.DisplayErrorMessage();
							}
						}
						catch (Exception e)
						{
							input = null;
							System.out.println("Input Error: Unable to parse input.");
							e.printStackTrace();
						}
					}
					
				}
			}

		}
			
		
	}
	
}
