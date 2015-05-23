package jChess;

import java.util.ArrayList;

import javax.swing.ImageIcon;
//Parent class for all figures, holding some common logic.
abstract class Piece {
	protected boolean isPieceWhite;
	protected int activeRow;
	protected int activeCol;
	protected int targetRow;
	protected int targetCol;
	protected float directionRow;
	protected float directionCol;
	
	public boolean isWhite() {
		return isPieceWhite;
	}
	
//	This should always be called before getPath.
	public boolean isMoveLegal(Square active, Square target) {
		activeRow = active.getRow();
		activeCol = active.getCol();
		targetRow = target.getRow();
		targetCol = target.getCol();
		return checkCoordinates();
	}

	public ArrayList<Integer> getPath(Square active, Square target) {
		ArrayList<Integer> path = new ArrayList<>();
		directionRow = Math.signum(targetRow - activeRow);
		directionCol = Math.signum(targetCol - activeCol);
		
		activeRow += directionRow;
		activeCol += directionCol;		
		while (activeRow != targetRow || activeCol != targetCol) {
			path.add(activeRow);
			path.add(activeCol);
			activeRow += directionRow;
			activeCol += directionCol;
		}
		return path;
	}
	
//	To be implemented by the child classes.
	abstract public boolean checkCoordinates();
	abstract public ImageIcon getIcon();
	
}
