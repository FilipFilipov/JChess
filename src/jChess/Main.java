package jChess;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

public class Main implements ActionListener{
	protected static Board board;
	protected static GlassPane glassPane;
	private static JPanel contentPane;
	private static JButton resetButton;
	private static JFrame frame;
		
	public static void main(String[] args) {
// 		Create top level swing container.
		Main jChess = new Main();
		frame = new JFrame("JChess");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);	
// 		Create a wrapper panel and put it in the window container.
		contentPane = new JPanel(new BorderLayout());
		contentPane.setOpaque(true);
		frame.setContentPane(contentPane);
// 		Create the chess board using a 8x8 GridLayout and put it in the center.
		board = new Board(new GridLayout(8, 8));
		contentPane.add(board, BorderLayout.CENTER);
// 		Create a reset button and put it below the board.
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		contentPane.add(panel, BorderLayout.SOUTH);		
		
		resetButton = new JButton("Reset Board");
		resetButton.addActionListener(jChess);
		panel.add(resetButton);
// 		Draw everything in the middle of the screen.		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);				
	}
	
// 	When there is a checkmate or stalemate overlay an image on the board.
	static void endGame(int opCode) {
		glassPane = new GlassPane(opCode);
		frame.setGlassPane(glassPane);
		glassPane.setVisible(true);
	}
	
// 	This removes the current game board and starts over with a new one.
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == resetButton) {
			contentPane.remove(board);
			board = new Board(new GridLayout(8, 8));
			contentPane.add(board, BorderLayout.CENTER);						
			board.revalidate();
			board.repaint();
		}		
	}
	
	static void close() {
		frame.dispose();
	}
}