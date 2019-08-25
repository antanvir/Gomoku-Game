import java.awt.event.MouseListener;
import java.util.ArrayList;


public class BoardMaker {
	
	private BoardGUI gui;
	private int[][] boardMatrix; // 0: Empty,  1: White,  2: Black
	
	
	public BoardMaker(int boardWidth, int totalCell) {
		gui = new BoardGUI(boardWidth, totalCell);
		boardMatrix = new int[totalCell][totalCell];
		
	}
	
	public BoardMaker(BoardMaker board) {
		
		int[][] matrixToCopy = board.getBoardMatrix();		
		boardMatrix = new int[matrixToCopy.length][matrixToCopy.length];
		
		for(int i=0;i<matrixToCopy.length; i++) {
			for(int j=0; j<matrixToCopy.length; j++) {
				boardMatrix[i][j] = matrixToCopy[i][j];
			}
		}
		
	}
	
	
	public void startListening(MouseListener listener) {
		gui.attachListener(listener);
	}
	
	
	public void addStoneNoGUI(int posX, int posY, boolean black) {
		boardMatrix[posY][posX] = black ? 2 : 1;
	}
	
	
	public boolean addStone(int posX, int posY, boolean black) {
		
		if(boardMatrix[posY][posX] != 0) return false;
		
		gui.drawStone(posX, posY, black);
		boardMatrix[posY][posX] = black ? 2 : 1;
		return true;
		
	}
	

	public int getBoardSize() {
		return boardMatrix.length;
	}
	
	
	public int[][] getBoardMatrix() {
		return boardMatrix;
	}
	
	
	public BoardGUI getGUI() {
		return gui;
	}
	
	
	public int getRelativePos(int x) {
		return gui.getRelativePos(x);
	}
	
	
	public void printWinner(int winner, String text) {
		gui.printWinner(winner, text);
	}
	
	

	public ArrayList<int[]> generateMoves() {
		ArrayList<int[]> moveList = new ArrayList<int[]>();
		
		int boardSize = boardMatrix.length;
		

		for(int i=0; i<boardSize; i++) {
			for(int j=0; j<boardSize; j++) {
				
				if(boardMatrix[i][j] > 0) continue;
				
				if(i > 0) {
					if(j > 0) {
						if(boardMatrix[i-1][j-1] > 0 ||
						   boardMatrix[i][j-1] > 0) {
							int[] move = {i,j};
							moveList.add(move);
							continue;
						}
					}
					if(j < boardSize-1) {
						if(boardMatrix[i-1][j+1] > 0 ||
						   boardMatrix[i][j+1] > 0) {
							int[] move = {i,j};
							moveList.add(move);
							continue;
						}
					}
					if(boardMatrix[i-1][j] > 0) {
						int[] move = {i,j};
						moveList.add(move);
						continue;
					}
				}
				if( i < boardSize-1) {
					if(j > 0) {
						if(boardMatrix[i+1][j-1] > 0 ||
						   boardMatrix[i][j-1] > 0) {
							int[] move = {i,j};
							moveList.add(move);
							continue;
						}
					}
					if(j < boardSize-1) {
						if(boardMatrix[i+1][j+1] > 0 ||
						   boardMatrix[i][j+1] > 0) {
							int[] move = {i,j};
							moveList.add(move);
							continue;
						}
					}
					if(boardMatrix[i+1][j] > 0) {
						int[] move = {i,j};
						moveList.add(move);
						continue;
					}
				}
				
			}
		}

		return moveList;
		
	}
	
	
	
}