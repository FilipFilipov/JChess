package jChess;

import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

//This just gives the game's outcome as an image overlaid on the board and exits on click anywhere.
public class GlassPane extends JComponent implements MouseListener {

	private static BufferedImage image;

	public GlassPane(int opCode) {
		addMouseListener(this);
		String filepath = "";
		switch (opCode) {
		case -1:
			filepath = "images/white_wins.png"; break;
		case 0:
			filepath = "images/draw.png"; break;
		case 1:
			filepath = "images/black_wins.png"; break;
		}
		try {
			image = ImageIO.read(new File(filepath));
		}
		catch (IOException error) {
			error.printStackTrace();
		}
	}

//	Paint the image in the middle of the board.
	protected void paintComponent(Graphics g) {
		g.drawImage(image, (Main.board.getWidth() - image.getWidth()) / 2, 
				(Main.board.getHeight() - image.getHeight()) / 2, image.getWidth(), image.getHeight(), null);
	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
		Main.close();
	}

	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
		// TODO Auto-generated method stub
	}

}
