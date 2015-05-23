package jChess;
import javax.swing.ImageIcon;

class Queen extends Piece {
	private static final ImageIcon ICON_BLACK = new ImageIcon("images/Queen_b.png");
	private static final ImageIcon ICON_WHITE = new ImageIcon("images/Queen_w.png");

	public Queen(boolean pieceIsWhite) {
		this.isPieceWhite = pieceIsWhite;
	}

	@Override
	public ImageIcon getIcon() {
		return isPieceWhite ? ICON_WHITE : ICON_BLACK;
	}

//	Combines the movement abilities of the rook and the bishop.
	@Override
	public boolean checkCoordinates() {
		boolean straightMove = activeRow == targetRow || activeCol == targetCol;
		boolean diagonalMove = Math.abs(targetRow - activeRow) == Math.abs(targetCol - activeCol);
		return (straightMove || diagonalMove) ? true : false;
	}

}
