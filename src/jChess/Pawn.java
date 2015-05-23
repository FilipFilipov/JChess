package jChess;
import javax.swing.ImageIcon;

class Pawn extends Piece {
//	Pawns are special as their allowed movement directions depends on the color,
//	they can jump two spaces only from their starting position,
//	and they have a promotional ability if they reach the opposite end of the board.
	private float directionFwd;
	private int startRow;
	private int promotionRow;
	private static final ImageIcon ICON_BLACK = new ImageIcon("images/Pawn_b.png");
	private static final ImageIcon ICON_WHITE = new ImageIcon("images/Pawn_w.png");
	
	public int getPromotionRow() {
		return promotionRow;
	}
	
	public Pawn(boolean isPieceWhite) {
		this.isPieceWhite = isPieceWhite;
		directionFwd = isPieceWhite ? -1 : 1;
		startRow = isPieceWhite ? 6 : 1;
		promotionRow = isPieceWhite ? 0 : 7;
	}		

	@Override
	public ImageIcon getIcon() {
		return isPieceWhite ? ICON_WHITE : ICON_BLACK;		
	}
	
//	Because the various checks here require more than just the coordinates,
//	this method has been effectively merged with checkCoordinates.
	@Override
	public boolean isMoveLegal(Square active, Square target) {
//		Make sure the pawn only goes forward, never back.
		activeRow = active.getRow();
		targetRow = target.getRow();
		if (Math.signum(targetRow - activeRow) != directionFwd) {
			return false;
		}
		activeCol = active.getCol();
		targetCol = target.getCol();
		boolean enPassant = isPieceWhite ? Main.board.enPassantWhiteTarget == target :
			Main.board.enPassantBlackTarget == target;
//		Conditions for the three kinds of pawn moves - forward, forward diagonally and two tiles forward.
		boolean captureCheck = (activeCol == targetCol + 1 || activeCol == targetCol - 1) &&
				activeRow + directionFwd == targetRow;
		boolean regMoveCheck = activeCol == targetCol && activeRow + directionFwd == targetRow;
		boolean jumpMoveCheck = activeCol == targetCol && activeRow + 2 * directionFwd == targetRow;
		
		if (activeRow == startRow && jumpMoveCheck && !target.isFull()) {
			return true;
		}
		else if ((captureCheck && target.isFull()) || (enPassant && captureCheck) ||
				(regMoveCheck && !target.isFull())) {
			return true;
		}
		return false;
	}
	
//	This is only here because the parent class mandates it, it's never called for this class.
	@Override
	public boolean checkCoordinates() {
		return false;
	}
	
}
