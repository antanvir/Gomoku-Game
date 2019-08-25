import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class BoardGUI extends JPanel {
	
	
	private Graphics2D g2D;
	private BufferedImage image;
	
	private static final long serialVersionUID = 1L;
	
	private int boardWidth; 
	private int totalCell; 
	private final int cellLength;
	
	
	public BoardGUI(int boardWidth, int totalCell) {
		this.boardWidth = boardWidth;
		this.totalCell = totalCell;
		this.cellLength  = boardWidth / totalCell;
		
		
		image = new BufferedImage(boardWidth, boardWidth, BufferedImage.TYPE_INT_ARGB);
		
		g2D = (Graphics2D)image.getGraphics();
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                			 RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2D.setColor(Color.LIGHT_GRAY);
		g2D.fillRect(0,0,boardWidth, boardWidth);
		
		g2D.setColor(Color.black);
		
		for(int i=1; i<=totalCell; i++) {
			g2D.drawLine(i*cellLength, 0, i*cellLength, boardWidth);
		}
		
		
		for(int i=1; i<=totalCell; i++) {
			g2D.drawLine(0, i*cellLength, boardWidth, i*cellLength);
		}
		
		
	}
	
	
	public int getRelativePos(int x) {
		if(x >= boardWidth) x = boardWidth-1;
		
		return (int) ( x * totalCell / boardWidth );
	}
	
	
	public Dimension getPreferredSize() {
		return new Dimension(boardWidth, boardWidth);
	}
	
	
	public void printWinner(int winner, String text) {
		FontMetrics metrics = g2D.getFontMetrics(g2D.getFont());		
		
		g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
   			 				 RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2D.setFont(new Font(g2D.getFont().getName(), Font.PLAIN, 48));
		
		g2D.setColor(Color.black);
		int x = (boardWidth/2 - metrics.stringWidth(text)*2);
		int y = boardWidth/2;
		

		
		g2D.setColor(winner == 2 ? Color.green : (winner == 1 ? Color.red : Color.blue));
		
		g2D.drawString(text,x,y);
		
		repaint();
		
	}
	
	
	public void drawStone(int posX, int posY, boolean black) {
		
		if(posX >= totalCell || posY >= totalCell) return;
		
		
		
		g2D.setColor(black ? Color.black : Color.white);
		g2D.fillOval((int)(cellLength*(posX+0.1)), 
					 (int)(cellLength*(posY+0.1)), 
					 (int)(cellLength*0.8), 
					 (int)(cellLength*0.8));
		
		g2D.setColor(Color.blue);
		g2D.setStroke(new BasicStroke((float)1.5));
		g2D.drawOval((int)(cellLength*(posX+0.1)), 
					 (int)(cellLength*(posY+0.1)), 
					 (int)(cellLength*0.8), 
					 (int)(cellLength*0.8));
		
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		g2D.setColor(Color.blue);
		g2D.setStroke(new BasicStroke(2));
		g2D.drawOval((int)(cellLength*(posX+0.1)), 
					 (int)(cellLength*(posY+0.1)), 
					 (int)(cellLength*0.8), 
					 (int)(cellLength*0.8));
		
		repaint();
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2D = (Graphics2D) g.create();
		
		// Draw the board
		g2D.drawImage(image, 0, 0, boardWidth, boardWidth, null);
		
		
		// Drawing border
		g2D.setColor(Color.black);
        g2D.drawRect(0, 0, boardWidth, boardWidth);
	}
	
	
	public void attachListener(MouseListener listener) {
		addMouseListener(listener);
	}
	
	
}