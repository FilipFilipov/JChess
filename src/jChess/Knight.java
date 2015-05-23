package jChess;
import java.util.ArrayList;

import javax.swing.ImageIcon;

class Knight extends Piece {
	private static final ImageIcon ICON_BLACK = new ImageIcon("images/Knight_b.png");
	private static final ImageIcon ICON_WHITE = new ImageIcon("images/Knight_w.png");
	
	public Knight(boolean pieceIsWhite) {
		this.isPieceWhite = pieceIsWhite;
	}
	
	@Override
	public ImageIcon getIcon() {
		return isPieceWhite ? ICON_WHITE : ICON_BLACK;
	}
	
//	Knights move in one of 8 L-shaped path, all of witch can be
//	generalized to one tile horizontally, two tiles vertically or vice-versa.
	@Override
	public boolean checkCoordinates() {
		boolean oneTwoMove = Math.abs(targetRow - activeRow) == 1 && Math.abs(targetCol - activeCol) == 2;
		boolean twoOneMove = Math.abs(targetRow - activeRow) == 2 && Math.abs(targetCol - activeCol) == 1;
		return (oneTwoMove || twoOneMove) ? true : false;
	}
	
//	Because a knight's path can't be blocked, mapping and checking it
//	is pointless, so this method is overridden to return an empty list.
	@Override
	public ArrayList<Integer> getPath(Square active, Square target) {
		return new ArrayList<Integer>();
	}
}
