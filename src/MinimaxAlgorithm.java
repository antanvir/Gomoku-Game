import java.util.ArrayList;


public class MinimaxAlgorithm {
	
	public static int moveCount = 0;
	private BoardMaker board;
	private static final int winScore = 100000000;
	
	
	public MinimaxAlgorithm(BoardMaker board) {
		this.board = board;
	}
	
	public static int getWinScore() {
		return winScore;
	}
	
	public static double evaluateBoardForWhite(BoardMaker board, boolean blacksTurn) {
		moveCount++;
		
		
		double blackScore = getScore(board, true, blacksTurn);
		double whiteScore = getScore(board, false, blacksTurn);
		
		if(blackScore == 0) blackScore = 1.0;
		
		return whiteScore / blackScore;
		
	}
	public static int getScore(BoardMaker board, boolean forBlack, boolean blacksTurn) {
		
		
		int[][] boardMatrix = board.getBoardMatrix();
		return evaluateHorizontal(boardMatrix, forBlack, blacksTurn) +
				evaluateVertical(boardMatrix, forBlack, blacksTurn) +
				evaluateDiagonal(boardMatrix, forBlack, blacksTurn);
	}
	
	public int[] calculateNextMove(int depth) {

		int[] move = new int[2];
		long startTime = System.currentTimeMillis();
		// Check if any available move can finish the game
		Object[] bestMove = searchWinningMove(board); 
		if(bestMove != null ) {
			System.out.println("Finisher!");
			move[0] = (Integer)(bestMove[1]);
			move[1] = (Integer)(bestMove[2]);
			//System.out.println(move[0] + " " + move[1]);
			
		} else {
			// If there is no such move, search the minimax tree with suggested depth.
			bestMove = minimaxSearchAB(depth, board, true, -1.0, getWinScore());
			if(bestMove[1] == null) {
				move = null;
			} else {
				move[0] = (Integer)(bestMove[1]);
				move[1] = (Integer)(bestMove[2]);
			}
		}
		System.out.println("Cases calculated: " + moveCount + " Calculation time: " + (System.currentTimeMillis() - startTime) + " ms");
		
		moveCount=0;
		
		return move;
		
		
	}
	
	
	private static Object[] searchWinningMove(BoardMaker board) {
		ArrayList<int[]> allPossibleMoves = board.generateMoves();
		Object[] winningMove = new Object[3];
		
		// Iterate for all possible moves
		for(int[] move : allPossibleMoves) {
			moveCount++;
			// Create a temporary board that is equivalent to the current board
			BoardMaker dummyBoard = new BoardMaker(board);
			// Play the move to that temporary board without drawing anything
			dummyBoard.addStoneNoGUI(move[1], move[0], false);
			
			// If the white player has a winning score in that temporary board, return the move.
			if(getScore(dummyBoard,false,false) >= winScore) {
				winningMove[1] = move[0];
				winningMove[2] = move[1];
				System.out.println(move[0] + " " + move[1]);
				return winningMove;
			}
		}
		return null;
		
	}
	
	
	/*
	 * alpha : Best AI Move (Max)
	 * beta : Best Player Move (Min)
	 * returns: {score, move[0], move[1]}
	 * */
	private static Object[] minimaxSearchAB(int depth, BoardMaker board, boolean max, double alpha, double beta) {
		if(depth == 0) {
			
			Object[] x = {evaluateBoardForWhite(board, !max), null, null};
			return x;
		}
		
		ArrayList<int[]> allPossibleMoves = board.generateMoves();
		
		if(allPossibleMoves.size() == 0) {
			
			Object[] x = {evaluateBoardForWhite(board, !max), null, null};
			return x;
		}
		
		Object[] bestMove = new Object[3];
		
		
		if(max) {
			bestMove[0] = -1.0;
			// Iterate for all possible moves that can be made.
			for(int[] move : allPossibleMoves) {
				// Create a temporary board that is equivalent to the current board
				BoardMaker dummyBoard = new BoardMaker(board);
				// Play the move to that temporary board without drawing anything
				dummyBoard.addStoneNoGUI(move[1], move[0], false);
				
				// Call the minimax function for the next depth, to look for a minimum score.
				Object[] tempMove = minimaxSearchAB(depth-1, dummyBoard, !max, alpha, beta);
				
				// Updating alpha
				if((Double)(tempMove[0]) > alpha) {
					alpha = (Double)(tempMove[0]);
				}
				// Pruning with beta
				if((Double)(tempMove[0]) >= beta) {
					return tempMove;
				}
				if((Double)tempMove[0] > (Double)bestMove[0]) {
					bestMove = tempMove;
					bestMove[1] = move[0];
					bestMove[2] = move[1];
				}
			}
			
		}
		else {
			bestMove[0] = 100000000.0;
			bestMove[1] = allPossibleMoves.get(0)[0];
			bestMove[2] = allPossibleMoves.get(0)[1];
			for(int[] move : allPossibleMoves) {
				BoardMaker dummyBoard = new BoardMaker(board);
				dummyBoard.addStoneNoGUI(move[1], move[0], true);
				
				Object[] tempMove = minimaxSearchAB(depth-1, dummyBoard, !max, alpha, beta);
				
				// Updating beta
				if(((Double)tempMove[0]) < beta) {
					beta = (Double)(tempMove[0]);
				}
				// Pruning with alpha
				if((Double)(tempMove[0]) <= alpha) {
					return tempMove;
				}
				if((Double)tempMove[0] < (Double)bestMove[0]) {
					bestMove = tempMove;
					bestMove[1] = move[0];
					bestMove[2] = move[1];
				}
			}
		}
		return bestMove;
	}
	
	
	
	public static int evaluateHorizontal(int[][] boardMatrix, boolean forBlack, boolean playersTurn ) {
		
		int countConsecutive = 0;
		int openEnds = 0;
		int score = 0;
		
		for(int i=0; i<boardMatrix.length; i++) {
			for(int j=0; j<boardMatrix[0].length; j++) {
				
				if(boardMatrix[i][j] == (forBlack ? 2 : 1)) {
					countConsecutive++;
				}
				
				else if(boardMatrix[i][j] == 0) {
					if(countConsecutive > 0) {
						openEnds++;
						score += getGameScore(countConsecutive, openEnds, forBlack == playersTurn);
						countConsecutive = 0;
						openEnds = 1;
					}
					else {
						openEnds = 1;
					}
				}
				
				else if(countConsecutive > 0) {
					score += getGameScore(countConsecutive, openEnds, forBlack == playersTurn);
					countConsecutive = 0;
					openEnds = 0;
				}
				
				else {
					openEnds = 0;
				}
			
			}
		
			if(countConsecutive > 0) {
				score += getGameScore(countConsecutive, openEnds, forBlack == playersTurn);
				
			}
			countConsecutive = 0;
			openEnds = 0;
			
		}
		return score;
	}
	
	public static  int evaluateVertical(int[][] boardMatrix, boolean forBlack, boolean playersTurn ) {
		
		int countConsecutive = 0;
		int openEnds = 0;
		int score = 0;
		
		for(int j=0; j<boardMatrix[0].length; j++) {
			for(int i=0; i<boardMatrix.length; i++) {
					if(boardMatrix[i][j] == (forBlack ? 2 : 1)) {
						countConsecutive++;
					}
					
					else if(boardMatrix[i][j] == 0) {
						if(countConsecutive > 0) {
							openEnds++;
							score += getGameScore(countConsecutive, openEnds, forBlack == playersTurn);
							countConsecutive = 0;
							openEnds = 1;
						}
						else {
							openEnds = 1;
						}
					}
					
					else if(countConsecutive > 0) {
						score += getGameScore(countConsecutive, openEnds, forBlack == playersTurn);
						countConsecutive = 0;
						openEnds = 0;
					}
					
					else {
						openEnds = 0;
					}
				
			}
			
			if(countConsecutive > 0) {
				score += getGameScore(countConsecutive, openEnds, forBlack == playersTurn);
				
			}
			countConsecutive = 0;
			openEnds = 0;
			
		}
		return score;
	}
	
	public static  int evaluateDiagonal(int[][] boardMatrix, boolean forBlack, boolean playersTurn ) {
		
		int countConsecutive = 0;
		int openEnds = 0;
		int score = 0;
		
		
		// Bottom-left to top-right diagonally, PLEASE DON'T DELETE
		for (int k = 0; k <= 2 * (boardMatrix.length - 1); k++) {
		    int iStart = Math.max(0, k - boardMatrix.length + 1);
		    int iEnd = Math.min(boardMatrix.length - 1, k);
		    
		    for (int i = iStart; i <= iEnd; ++i) {
		        int j = k - i;
		        
		        if(boardMatrix[i][j] == (forBlack ? 2 : 1)) {
					countConsecutive++;
				}
				
				else if(boardMatrix[i][j] == 0) {
					if(countConsecutive > 0) {
						openEnds++;
						score += getGameScore(countConsecutive, openEnds, forBlack == playersTurn);
						countConsecutive = 0;
						openEnds = 1;
					}
					else {
						openEnds = 1;
					}
				}
				
				else if(countConsecutive > 0) {
					score += getGameScore(countConsecutive, openEnds, forBlack == playersTurn);
					countConsecutive = 0;
					openEnds = 0;
				}
				
				else {
					openEnds = 0;
				}
			
			}
			
			if(countConsecutive > 0) {
				score += getGameScore(countConsecutive, openEnds, forBlack == playersTurn);
				
			}
			countConsecutive = 0;
			openEnds = 0;
		
		}
		
		
		// Top-left to bottom-right diagonally, PLEASE DON'T DELETE
		for (int k = 1-boardMatrix.length; k < boardMatrix.length; k++) {
		    int iStart = Math.max(0, k);
		    int iEnd = Math.min(boardMatrix.length + k - 1, boardMatrix.length-1);
		    
		    for (int i = iStart; i <= iEnd; ++i) {
		        int j = i - k;
		        
		        if(boardMatrix[i][j] == (forBlack ? 2 : 1)) {
					countConsecutive++;
				}
				
				else if(boardMatrix[i][j] == 0) {
					if(countConsecutive > 0) {
						openEnds++;
						score += getGameScore(countConsecutive, openEnds, forBlack == playersTurn);
						countConsecutive = 0;
						openEnds = 1;
					}
					else {
						openEnds = 1;
					}
				}
				
				else if(countConsecutive > 0) {
					score += getGameScore(countConsecutive, openEnds, forBlack == playersTurn);
					countConsecutive = 0;
					openEnds = 0;
				}
				
				else {
					openEnds = 0;
				}
			
		    }
		
			if(countConsecutive > 0) {
				score += getGameScore(countConsecutive, openEnds, forBlack == playersTurn);
				
			}
			countConsecutive = 0;
			openEnds = 0;
			
		}
		return score;
	}
	
	
	public static  int getGameScore(int consecutive, int openEnds, boolean currentTurn) {	
		int winGuaranty = 1000000;
		if (openEnds == 0 && consecutive < 5)
			return 0;
		
		switch (consecutive) {
			case 5: 
				return winScore;
				
			case 4:
				if(currentTurn) return 1000000;
				else {
					if(openEnds == 2) return 250000;
					else return 200;
				}

				
			case 3:
				if(openEnds == 2) {
					if(currentTurn) return 50000;
					else return 200;
				}
				else {
					if(currentTurn) return 10;
					else return 5;
				}
				
			case 2:
				if(openEnds == 2) {
					if(currentTurn) return 7;
					else return 5;
				}
				else {
					return 3;
				}
				
			case 1:				
					return 1;

			default:
				return 2*winScore;
		}
		
	}
}
