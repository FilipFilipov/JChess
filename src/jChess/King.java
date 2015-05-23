package jChess;
import javax.swing.ImageIcon;

class King extends Piece {
	private static final ImageIcon ICON_BLACK = new ImageIcon("images/King_b.png");
	private static final ImageIcon ICON_WHITE = new ImageIcon("images/King_w.png");

	public King(boolean pieceIsWhite) {
		this.isPieceWhite = pieceIsWhite;
	}

	@Override
	public ImageIcon getIcon() {
		return isPieceWhite ? ICON_WHITE : ICON_BLACK;
	}

	@Override
	public boolean checkCoordinates() {
//		Regular move to adjacent square.
		if ((Math.abs(targetRow - activeRow) <= 1) &&
				(Math.abs(targetCol - activeCol) <= 1)) {
			return true;
		}
//		Castling - only permitted if a the proper flag is still set to true.
		else if (targetRow == activeRow && Math.abs(targetCol - activeCol) == 2) {
//			Short castling (right).
			if (targetCol - activeCol == 2) {
				if (isPieceWhite && Main.board.canCastleWhShort) {
					return true;
				}
				else if (!isPieceWhite & Main.board.canCastleBlShort) {
					return true;
				}
			}
//			Long castling (left).
			else if (targetCol - activeCol == -2) {
				if (isPieceWhite && Main.board.canCastleWhLong) {
					return true;
				}
				else if (!isPieceWhite & Main.board.canCastleBlLong) {
					return true;
				}			
			}
			System.out.println("Can't castle if you've already moved either the king or rook!");
		}
		return false;
	}

}
