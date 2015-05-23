package jChess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardListener implements ActionListener {
// 	Detects when a board tile is clicked and reacts accordingly.
	@Override
	public void actionPerformed(ActionEvent e) {
		Main.board.target = (Square)e.getSource();
// 		If there was no tile/piece previously selected...
		if (Main.board.active == null && !Main.board.pieceSelected) {
// 			Check if the selected tile is occupied.
			if (Main.board.target.isFull()) {
// 				Check if the occupying piece is the right for the current player,
// 				and if so select it for moving and indicate it with a gree border.
				if (Main.board.target.getPiece().isWhite() == Main.board.isWhitesTurn) {
					Main.board.active = Main.board.target;
					Main.board.active.addBorder();
				}
				else {
					System.err.printf("It's %s turn now!\n",
							Main.board.isWhitesTurn ? "white's" : "black's");
				}
			}
			else {
				System.err.printf("Select a piece first!\n");
			}
		}
//		If a piece was previously selected, and the player is
//		clicking somewhere else, check what they are trying to do.
		else if (Main.board.active != Main.board.target) {
//			If they selected another one of their own pieces, make that 
//			the active piece, remove the old border and draw a new one.
			if (Main.board.target.isFull() &&
					Main.board.active.getPiece().isWhite() == Main.board.target.getPiece().isWhite()) {
				Main.board.active.removeBorder();
				Main.board.target.addBorder();
				Main.board.active = Main.board.target;
			}
//			If they selected an enemy piece or an empty tile, try to execute the move.
			else {		
				Main.board.analizeMove();
			}
		}     
	}
}
