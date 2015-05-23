package jChess;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

class Square extends JButton {
	
	private static final LineBorder ACTIVE_BORDER = new LineBorder(Color.GREEN, 2);
	private static final Dimension SIZE = new Dimension(50, 50);
	private static final Color LIGHT = new Color(0xffce9e);
	private static final Color DARK = new Color(0xd18b47);
	
	private int row;
	private int col;
	private boolean isSquareWhite;
	private Piece piece = null;
	private Piece oldPiece = null;
	private ImageIcon icon = null;
	
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
	public boolean isWhite() {
		return isSquareWhite;
	}
	public Piece getPiece() {
		return piece;
	}
	public void setPiece(Piece piece) {
		this.oldPiece = this.piece;
		this.piece = piece;
	}	
	public Piece getOldPiece() {
		return oldPiece;
	}
	
	@Override
	public ImageIcon getIcon() {
		return icon;
	}
	
	public Square(int row, int col, boolean isSquareWhite) {
		super();
		this.row = row;
		this.col = col;
		this.isSquareWhite = isSquareWhite;		
		this.setBackground(isSquareWhite ? LIGHT : DARK);
		this.setPreferredSize(SIZE);
		this.setBorder(null);
		this.setFocusPainted(false);
	}
	
	public boolean isFull(){
		return (piece != null) ? true : false;
	}
	
	public void syncIcon() {
		this.icon = (piece != null) ? piece.getIcon() : null;
	}
	
	public void addBorder() {
		this.setBorder(ACTIVE_BORDER);
	}
	
	public void removeBorder() {
		this.setBorder(null);
	}
	
	public void rollBackPiece() {
		this.piece = this.oldPiece;
	}
	
	public void clearSquare() {
		this.oldPiece = this.piece;
		this.piece = null;
	}
	
}