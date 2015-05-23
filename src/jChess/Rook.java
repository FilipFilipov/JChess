package jChess;
import javax.swing.ImageIcon;

class Rook extends Piece {
	private static final ImageIcon ICON_BLACK = new ImageIcon("images/Rook_b.png");
	private static final ImageIcon ICON_WHITE = new ImageIcon("images/Rook_w.png");

	public Rook(boolean pieceIsWhite) {
		this.isPieceWhite = pieceIsWhite;
	}

	@Override
	public ImageIcon getIcon() {
		return isPieceWhite ? ICON_WHITE : ICON_BLACK;
	}

//	Only moves on the same row/column.
	@Override
	public boolean checkCoordinates() {
		return (activeRow == targetRow || activeCol == targetCol) ? true : false;
	}

}
