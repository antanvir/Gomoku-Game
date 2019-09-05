import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class AI_Game {

	private BoardMaker board;
	private boolean isPlayersTurn = true;
	private boolean gameFinished = false;
	private int minimaxDepth = 3;
	private boolean aiStarts = true;
	private MinimaxAlgorithm ai;
	private int winner; // 0: Tie, 1: AI Wins, 2: Human Wins
	
	
	public AI_Game(BoardMaker board) {
		this.board = board;
		ai = new MinimaxAlgorithm(board);
		
		winner = 0;
	}

	public void start() {
		

		if(aiStarts) playMove(board.getBoardSize()/2, board.getBoardSize()/2, false);

		board.startListening(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
				if(isPlayersTurn) {
					isPlayersTurn = false;
					Thread mouseClickThread = new Thread(new MouseClickHandler(arg0));
					mouseClickThread.start();
					
					
				}
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
		
			 
			int posX = board.getRelativePos( e.getX() );
			int posY = board.getRelativePos( e.getY() );
			
			// Place a black stone to that cell.
			if(!playMove(posX, posY, true)) {
				isPlayersTurn = true;
				return;
			}
			
			winner = checkWinner();
			
			if(winner == 2) {
				System.out.println("Player WON!");
				board.printWinner(winner, "YOU WON!");
				gameFinished = true;
				return;
			}

			int[] aiMove = ai.findNextMove(minimaxDepth);
			
			if(aiMove == null) {
				System.out.println("No possible moves left. Game Over.");
				board.printWinner(0, "MATCH TIED!"); 
				gameFinished = true;
				return;
			}
			
			
			// Place a white stone to the found cell.
			playMove(aiMove[1], aiMove[0], false);
			
			System.out.println("Black: " + MinimaxAlgorithm.getScore(board,true,true) + " White: " + MinimaxAlgorithm.getScore(board,false,true));
			
			winner = checkWinner();
			
			if(winner == 1) {
				System.out.println("AI WON!");
				board.printWinner(winner, "COMPUTER WON!");
				gameFinished = true;
				return;
			}
			
			if(board.generateMoves().size() == 0) {
				System.out.println("No possible moves left. Game Over.");
				board.printWinner(0, "MATCH TIED!"); 
				gameFinished = true;
				return;
				
			}
			
			isPlayersTurn = true;
		}
		
	}
	
	private boolean playMove(int posX, int posY, boolean black) {
		return board.addStone(posX, posY, black);
	}
	
	private int checkWinner() {
		if(MinimaxAlgorithm.getScore(board, true, false) >= MinimaxAlgorithm.getWinScore()) return 2; //[DON'T DELETE] Black stone, Human Player 
		if(MinimaxAlgorithm.getScore(board, false, true) >= MinimaxAlgorithm.getWinScore()) return 1;
		return 0;
	}
	
	
	
}
