import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class TwoPlayerGame {


	private BoardMaker board;
	private boolean isFirstPlayersTurn = true;
	private boolean gameFinished = false;
	private int minimaxDepth = 3;
	private boolean aiStarts = true; 

	private int winner; // 0: There is no winner yet, 1: Second Player Wins, 2: First Player Wins
	
	
	public TwoPlayerGame(BoardMaker board) {
		this.board = board;		
		winner = 0;
	}
	
	
	public void start() {
		
		
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
	
	
	
	public class MouseClickHandler implements Runnable{
		MouseEvent e;
		public MouseClickHandler(MouseEvent e) {
			this.e = e;
		}
		
		public void run() {
			
			if(gameFinished) return;
			boolean isWinner = false;

			if(isFirstPlayersTurn) {
				
				System.out.println("BLACK STONE");
				int posX = board.getRelativePos( e.getX() );
				int posY = board.getRelativePos( e.getY() );
				
				// Place black stone to that cell.
				if(!playMove(posX, posY, true)) {
					return;
				}
				
				isWinner = checkWinner(2);
				
				if(isWinner) {
					System.out.println("1st PLAYER WON!");
					board.printWinner(winner, "1ST PLAYER WON!");
					gameFinished = true;
					return;
				}
			}
			else {
				System.out.println("WHITE STONE");
				int posX = board.getRelativePos( e.getX() );
				int posY = board.getRelativePos( e.getY() );
				
				// Place a white stone to that cell.
				if(!playMove(posX, posY, false)) {
					return;
				}
				
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
				board.printWinner(0, "MATCH TIED!"); 
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
	
	

	private boolean playMove(int posX, int posY, boolean black) {
		return board.addStone(posX, posY, black);
	}

	
	private boolean checkWinner(int playerID) {
		int[][] boardMatrix = board.getBoardMatrix();
		

		if(horizontalLookup(playerID,boardMatrix)==true)
			return true;
		if(verticalLookup(playerID,boardMatrix)==true)
			return true;
		if(bottomLeftToRightLookup(playerID,boardMatrix)==true)
			return true;
		if(topLeftToBottomRight(playerID,boardMatrix)==true)
			return true;
		
		return false;
	}
	
	
	
	private  boolean horizontalLookup(int playerID, int [][] boardMatrix){

		boardMatrix = board.getBoardMatrix();

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
		return false;

	}

	private  boolean verticalLookup(int playerID, int [][] boardMatrix){

		boardMatrix = board.getBoardMatrix();

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
		return false;

	}

	private  boolean bottomLeftToRightLookup(int playerID, int [][] boardMatrix){

		boardMatrix = board.getBoardMatrix();

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
		return false;

	}

	private  boolean topLeftToBottomRight(int playerID, int [][] boardMatrix){

		boardMatrix = board.getBoardMatrix();

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

}