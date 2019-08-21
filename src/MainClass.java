import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class MainClass {
	
	public static void main(String[] args) {
		
		final int width = 600;

		final MainGUI gui = new MainGUI(width, width, "GoMoku Game (Five-in-a-row)");
		
		Board board = new Board(width, 10);
		

		
		final TwoPlayerGame humanGame = new TwoPlayerGame(board);
		
		

		gui.listenGameStartButton(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				
				

				boolean computerStarts = false;
				int depth = 4;
				boolean playingWithComputer = gui.playWithComputer();
				System.out.println(playingWithComputer);
				
				System.out.println("Depth: " + depth + " AI Makes the first move: " + computerStarts );
				
				gui.attachBoard(board.getGUI());
				gui.pack();
				gui.setVisible(true);
				
				gui.showBoard();
				
				if(playingWithComputer) {

//					game.setAIDepth(depth);
//					game.setAIStarts(computerStarts);
//					
//					game.start();
					humanGame.start();
				}
				else {									

					humanGame.start();
				}
				
			}
			
		});
		
		
		
		
		
		
	}
}
