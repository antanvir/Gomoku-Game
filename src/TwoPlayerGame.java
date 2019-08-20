import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class TwoPlayerGame {

//	private Minimax ai;
	private Board board;
	private boolean isFirstPlayersTurn = true;
	private boolean gameFinished = false;
	private int minimaxDepth = 3;
	private boolean aiStarts = true; 

	private int winner; // 0: There is no winner yet, 1: Second Player Wins, 2: First Player Wins
	
	
	public TwoPlayerGame(Board board) {
		this.board = board;
//		ai = new Minimax(board);
		
		winner = 0;
	}
	/*
	 * 	Loads the cache and starts the game, enabling human player interactions.
	 */
	public void start() {
		
		
		// Make the board start listening for mouse clicks.
		board.startListening(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {

				Thread mouseClickThread = new Thread(new MouseClickHandler(arg0));
				mouseClickThread.start();
				System.out.println("First Player Turn: " + isFirstPlayersTurn);
			}

			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	/*
	 * 	Sets the depth of the minimax tree. (i.e. how many moves ahead should the AI calculate.)
	 */
	public void setAIDepth(int depth) {
		this.minimaxDepth = depth;
		
	}
	
	
	public void setAIStarts(boolean aiStarts) {
		this.aiStarts = aiStarts;
	}
	
	
	public class MouseClickHandler implements Runnable{
		MouseEvent e;
		public MouseClickHandler(MouseEvent e) {
			this.e = e;
		}
		
		public void run() {
			if(gameFinished) return;
			boolean isWinner = false;
			
			// Find out which cell of the board do the clicked coordinates belong to.
			if(isFirstPlayersTurn) {
				System.out.println("Here BLACK STONE");
				int posX = board.getRelativePos( e.getX() );
				int posY = board.getRelativePos( e.getY() );
				
				// Place a black stone to that cell.
				if(!playMove(posX, posY, true)) {
					return;
				}
				
				// Check if the last move ends the game.
				isWinner = checkWinner(2);
				
				if(isWinner) {
					System.out.println("1st PLAYER WON!");
					board.printWinner(winner, "1ST PLAYER WON!");
					gameFinished = true;
					return;
				}
			}
			else {
				System.out.println("Here WHITE STONE");
				int posX = board.getRelativePos( e.getX() );
				int posY = board.getRelativePos( e.getY() );
				
				// Place a white stone to that cell.
				if(!playMove(posX, posY, false)) {
					return;
				}
				
				// Check if the last move ends the game.
				isWinner = checkWinner(1);
				
				if(isWinner) {
					System.out.println("2nd Player WON!");
					board.printWinner(winner, "2ND PLAYER WON!");
					gameFinished = true;
					return;
				}
			}
			
			if(board.generateMoves().size() == 0) {
				System.out.println("No possible moves left. Game Over.");
				board.printWinner(0, "MATCH TIED!"); // Prints "TIED!"
				gameFinished = true;
				return;
				
			}
			setPlayersTurn(!isFirstPlayersTurn);
			
			
		}
		
	}
	
	private void setPlayersTurn(boolean PlayersTurn) {
		this.isFirstPlayersTurn = PlayersTurn;
		System.out.println("Let's see! " + isFirstPlayersTurn);
	}
	
	
	private boolean checkWinner(int playerID) {
		int[][] boardMatrix = board.getBoardMatrix();
		
		// Horizontal lookup
		for(int i=0; i<boardMatrix.length; i++) {
			int consecutive = 0;
			for(int j=0; j<boardMatrix[0].length; j++) {
				
				if(consecutive >= 5) {
					return true;
				}
				if(boardMatrix[i][j] == playerID) {
					consecutive++;
					
				}
				else if(consecutive > 0 && boardMatrix[i][j] != playerID) {
					consecutive = 0;
				}
			}
			
		}
		
		//Vertical Lookup
		for(int j=0; j<boardMatrix.length; j++) {
			int consecutive = 0;
			for(int i=0; i<boardMatrix[0].length; i++) {
				
				if(consecutive >= 5) {
					return true;
				}
				if(boardMatrix[i][j] == playerID) {
					consecutive++;
				}
				else if(consecutive > 0 && boardMatrix[i][j] != playerID) {
					consecutive = 0;
				}
			}

		}
		
		// From bottom-left to top-right diagonally
		for (int k = 0; k <= 2 * (boardMatrix.length - 1); k++) {
			int iStart = Math.max(0, k - boardMatrix.length + 1);
			int iEnd = Math.min(boardMatrix.length - 1, k);
			
			int consecutive = 0;
			for (int i = iStart; i <= iEnd; ++i) {
				int j = k - i;
				if(consecutive >= 5) {
					return true;
				}
				if(boardMatrix[i][j] == playerID) {
					consecutive++;
				}
				else if(consecutive > 0 && boardMatrix[i][j] != playerID) {
					consecutive = 0;
				}
			}
		}
		
		// From top-left to bottom-right diagonally
		for (int k = 1-boardMatrix.length; k < boardMatrix.length; k++) {
			int iStart = Math.max(0, k);
			int iEnd = Math.min(boardMatrix.length + k - 1, boardMatrix.length-1);
			
			int consecutive = 0;
			for (int i = iStart; i <= iEnd; ++i) {
				int j = i - k;
				if(consecutive >= 5) {
					return true;
				}
				if(boardMatrix[i][j] == playerID) {
					consecutive++;
				}
				else if(consecutive > 0 && boardMatrix[i][j] != playerID) {
					consecutive = 0;
				}
			}
		}
		
		return false;
	}
	
//	private int checkWinner() {
//		if(Minimax.getScore(board, true, false) >= Minimax.getWinScore()) return 2; // Black stone, Human Player
//		if(Minimax.getScore(board, false, true) >= Minimax.getWinScore()) return 1;
//		return 0;
//	}
	
	
	private boolean playMove(int posX, int posY, boolean black) {
		return board.addStone(posX, posY, black);
	}
	
}
