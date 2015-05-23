package jChess;
import javax.swing.ImageIcon;

class Bishop extends Piece {
	private static final ImageIcon ICON_BLACK = new ImageIcon("images/Bishop_b.png");
	private static final ImageIcon ICON_WHITE = new ImageIcon("images/Bishop_w.png");

	public Bishop(boolean pieceIsWhite) {
		this.isPieceWhite = pieceIsWhite;
	}

	@Override
	public ImageIcon getIcon() {
		return isPieceWhite ? ICON_WHITE : ICON_BLACK;
	}

//	Only moves diagonally.
	@Override
	public boolean checkCoordinates() {
		return (Math.abs(targetRow - activeRow) == Math.abs(targetCol - activeCol)) ? true : false;
	}

}
