import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class MainClass {
	
	public static void main(String[] args) {
		
		final int width = 600;

		final GUI_Frame gui = new GUI_Frame(width, width, "GoMoku Game (Five-in-a-row)");
		
		BoardMaker board = new BoardMaker(width, 10);
				
		final TwoPlayerGame humanGame = new TwoPlayerGame(board);
		final AI_Game game = new AI_Game(board);
			

		
		
		
		
		
		
	}
}
