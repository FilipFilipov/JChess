package jChess;

import java.awt.LayoutManager;
import java.util.ArrayList;

import javax.swing.JPanel;

//The class combines the internal representation of the board - a marix
//of tiles, with the display component - a JPanel with a GridLayout.
public class Board extends JPanel {
	
	protected static Square[][] squares = new Square[8][8];
	protected Square whiteKingSquare;
	protected Square blackKingSquare;
	protected Square active;
	protected Square target;
	protected Square oldActive;
	protected Square oldTarget;
	protected Square enPassantWhiteTarget;
	protected Square enPassantBlackTarget;
	protected boolean isWhitesTurn = true;	
	protected boolean pieceSelected = false;
	protected boolean canCastleWhLong = true;
	protected boolean canCastleWhShort = true;
	protected boolean canCastleBlLong = true;
	protected boolean canCastleBlShort = true;
	protected boolean whiteInCheck = false;
	protected boolean blackInCheck = false;

//	Construct the chess board model.
	public Board(LayoutManager lm) {
//		Make an 8x8 container with a Grid Layout. We'll need to know
//		the color of the tile and the type and color of the occupying piece.
		super(lm);
		boolean isSquareWhite = false;
		boolean isPieceWhite = false;
//		Prepare our own listener to intercept clicks on the board tiles.
		BoardListener bl = new BoardListener();
		
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
//				Generate an empty tile with the required data.
				Square currentSquare = new Square(row, col, isSquareWhite);
//				Add it to our internal matrix.
				squares[row][col] = currentSquare;
//				Fill in everything that's not a pawn.
				if (row == 0 || row == 7) {
					fillRow(col, currentSquare, isPieceWhite);
				}
//				Fill in the pawns.
				else if (row == 1 || row == 6) {
					currentSquare.setPiece(new Pawn(isPieceWhite));
					currentSquare.syncIcon();
//					At this point we're done with the black pieces.
					if (col == 7) {
						isPieceWhite = true;
					}
				}
//				Attach the listener to the new tile, and add it to the swing panel, so it can be drawn.
				currentSquare.addActionListener(bl);
				this.add(currentSquare);
//				To get a chess board pattern don't change the tile color after the end of each row.
				if (col != 7) {
					isSquareWhite = !isSquareWhite;
				}			
			}		
		}
//		Keep track of the kings' position.
		whiteKingSquare = squares[7][4];
		blackKingSquare = squares[0][4];
	}

	private void fillRow(int col, Square currentSquare, boolean isPieceWhite) {
		switch (col) {
		case 0:
		case 7:
			currentSquare.setPiece(new Rook(isPieceWhite)); break;
		case 1:
		case 6:
			currentSquare.setPiece(new Knight(isPieceWhite)); break;
		case 2:
		case 5:
			currentSquare.setPiece(new Bishop(isPieceWhite)); break;
		case 3:
			currentSquare.setPiece(new Queen(isPieceWhite)); break;
		case 4:
			currentSquare.setPiece(new King(isPieceWhite)); break;
		}
		currentSquare.syncIcon();
	}

	void analizeMove() {
//		Find out what piece will be moving.
		Piece currentPiece = active.getPiece();
//		Make backups of the current active and target tiles,
//		in case a move fails, and we need to reverse it.
		oldTarget = new Square(target.getRow(), target.getCol(), target.isWhite());
		oldActive = new Square(active.getRow(), active.getCol(), active.isWhite());
		
//		Check if the start and end position make for a legal move.
		if (currentPiece.isMoveLegal(active, target)) {
//			If yes, then get the indexes of any tiles between them.
			ArrayList<Integer> path = currentPiece.getPath(active, target);
//			Check if there is anything on those tiles.	
			if (isPathClear(path)) {
//				If the king is trying to move by two spaces, the player
//				may be trying to perform a castling, so check if they can.
				if (currentPiece instanceof King &&
						(Math.abs(target.getCol() - active.getCol()) == 2)) {
//					There's two distinct types of castling, a long one(left) and a short one(right).
					int castlingDirection = target.getCol() - active.getCol();
					Square newRookPos = tryCastling(castlingDirection);
//					If castling is successful, make sure it doesn't leave the king in check.
					if (newRookPos != null) {
						if (kingIsSafe()) {
							finishMove(currentPiece);
						}
						else {							
							goBackToSelection();
							reverseCastling(newRookPos);
						}
					}
				}
				else {
//					If the move is possible, make sure it doesn't leave the king in check.
					tryMove(active.getPiece());
					if (kingIsSafe()) {	
						finishMove(currentPiece);
					}
					else {
						goBackToSelection();
					}	
				}
			}
			else {
				System.err.printf("The path is blocked!\n");
			}					
		}
		else {
			System.err.printf("That is not a legal move!\n");
		}
	}

//	Castling has a lot of prerequisites, make sure they are all met. Because the move involves
//	two figures, this method also returns the tile where the rook should go to, or null if the move fails.
	private Square tryCastling(int direction) {
		boolean castleLong = false;
		boolean castleShort = false;
//		Make sure the player isn't trying to castle out of a check.
		if ((isWhitesTurn && whiteInCheck) || (!isWhitesTurn && blackInCheck)) {
			System.out.println("You can't castle out of check!");
		}
		else if (direction == -2) {
//		If the attempted castling is long, make sure the rook's path is also clear.
			if (squares[target.getRow()][target.getCol() - 1].isFull() || target.isFull()) {
				System.out.println("Path between king and rook must be clear!");
			}
//			Then make sure the king's path is also clear.
			else {
//				Perform the move in two steps, by changing the active and target tiles manually.
				target = squares[target.getRow()][target.getCol() + 1];
				if (checkCastlingPath()) {
					active = target;
					target = squares[target.getRow()][target.getCol() - 1];
//					We know the second part of the move will never fail, because we already
//					checked that the target tile is clear, which is why the result here is not saved.
					tryMove(active.getPiece());
					castleLong = true;
				}
			}
		}
//		Short castling - same deal as above except the moves are in the other direction
//		and you don't need to check the rook's path, as the king passes through the same tiles.
		else if (direction == 2) {
			if (target.isFull()) {
				System.out.println("Path between king and rook must be clear!");
			}
			else {
				target = squares[target.getRow()][target.getCol() - 1];
				if (checkCastlingPath()) {
					active = target;
					target = squares[target.getRow()][target.getCol() + 1];
					tryMove(active.getPiece());
					castleShort = true;
				}			
			}
		}
//		Finally, find out where the rook should go.
//		This will return null is the king's move failed, as conveyed by the flags.
		return getRookPosition(castleLong, castleShort);
	}

//	The king cannot move through a check position during castling,
//	so perform the first half of his move and make sure he's not in check.
//	We do not update the visible part of the board yet.
	private boolean checkCastlingPath() {
		tryMove(active.getPiece());
		if (!kingIsSafe()) {
//			If he is in check reverse the move.
			reverseMove(active, target);
			System.err.println("You can't castle through a check position!");
			return false;
		}
		return true;
	}
	
	private Square getRookPosition(boolean castleLong, boolean castleShort) {
		Square oldRookPos = null;
		Square newRookPos = null;
//		The relative movement is identical for the black and white figures.
		if (target.getCol() == 6 && castleShort) {
			oldRookPos = squares[target.getRow()][target.getCol() + 1];
			newRookPos = squares[target.getRow()][target.getCol() - 1];
		}
		else if (target.getCol() == 2 && castleLong){
			oldRookPos = squares[target.getRow()][target.getCol() - 2];
			newRookPos = squares[target.getRow()][target.getCol() + 1];
		}
//		Only one of the two flags can be true, and only
//		if the king's part of the castling was successful.
		if (castleLong || castleShort) {
			newRookPos.setPiece(oldRookPos.getPiece());
			oldRookPos.clearSquare();		
		}
		return newRookPos;
	}
	
//	Check all the path indexes in the board matrix to make sure the tiles are clear.
	private boolean isPathClear(ArrayList<Integer> path) {
		for (int i = 0; i < path.size(); i += 2) {
			if (squares[path.get(i)][path.get(i + 1)].isFull()) {
				return false;
			}
		}
		return true;
	}

//	Perform a move and handle some special cases. We already know the move is possible.
	private void tryMove(Piece currentPiece) {
		target.setPiece(currentPiece);
		active.clearSquare();
//		If moving a pawn...
		if (currentPiece instanceof Pawn) {
//			Check if it moved in a diagonal. 
			boolean pawnCaptureMove = active.getCol() == target.getCol() + 1 ||
					active.getCol() == target.getCol() - 1;
//			Check if it's making a two-tile jump, and set the tile
//			that will be opened to an en passant capture on the next move.
			if (Math.abs(target.getRow() - active.getRow()) == 2) {
				if (isWhitesTurn) {
					enPassantBlackTarget = squares[target.getRow() + 1][target.getCol()];
				}
				else {
					enPassantWhiteTarget = squares[target.getRow() - 1][target.getCol()];
				}
			}
//			If a capture move was performed on an empty tile, we have an
//			en passant capture, and need to manually remove the captured pawn.
			else if (pawnCaptureMove && target.getOldPiece() == null) {
				squares[active.getRow()][target.getCol()].clearSquare();
			}
		}

//		Update the kings position when it moves.
		if (currentPiece instanceof King) {
			if (isWhitesTurn) {
				whiteKingSquare = target;
			}
			else {
				blackKingSquare = target;
			}
		}
	}

	private boolean kingIsSafe() {
//		First find where the current player's king is.
		Square kingSquare = isWhitesTurn ? whiteKingSquare : blackKingSquare;
		int kingRow = kingSquare.getRow();
		int kingCol = kingSquare.getCol();
//		We perform two consecutive searches around the king's position.
//		The first is for knights only, because they can threaten him from unusual positions.
		boolean doWideSearch = false;
		for (int i = 0; i < 2; i++) {
//			The first search goes through every tile two spaces away,
//			since knights can only move two squares in any one direction,
//			the second search starts from every tile one space away from the king.
			int searchStart = doWideSearch ? -1 : -2;
			int searchEnd = doWideSearch ? 1 : 2;
			for (int rowDiff = searchStart; rowDiff <= searchEnd; rowDiff++) {
				for (int colDiff = searchStart; colDiff <= searchEnd; colDiff++) {
					int testRow = kingRow + rowDiff;
					int testCol = kingCol + colDiff;
//					From each of the searched tiles, start looking in a straight
//					line in that direction until you reach the end of the board.
					while (isInBoard(testRow) && isInBoard(testCol)) {
						Square testSquare = squares[testRow][testCol];
//						For each tile on the line, check if it's occupied.
						if (testSquare.isFull()) {
							Piece testPiece = testSquare.getPiece();
//							Check if the piece is an enemy and
//							it can legally move on the king's position.
							if ((testPiece.isWhite() != isWhitesTurn) &&
									testPiece.isMoveLegal(testSquare, kingSquare)) {
//								If we're searching for knights, we don't need to have
//								a clear path, so we've proven the king is in check.
								if (doWideSearch) {
									return false;
								}
//								If we're not searching for knights, we should
//								also make sure the path to the king is not blocked.
								if (!doWideSearch) {
									ArrayList<Integer> path = testPiece.getPath(testSquare, kingSquare);
									if (isPathClear(path)) {
										return false;
									}
								}																					
							}
//							If we' found any figure on the line, the path
//							after it is blocked, so we stop extending the line.
							break;
						}
//						If we're looking for knights, we don't extend
//						the line, as knights never move in a straight line.
						else if (!doWideSearch) {
							break;
						}
//						Otherwise we extend the line.
						else {
							testRow += rowDiff;
							testCol += colDiff;
						}						
					}
				}
			}
//			Once we've done the knight search, move on to the general search.
			doWideSearch = true;
		}	
		return true;
	}

	private boolean isInBoard(int i) {
		return (i >= 0 && i <= 7) ? true : false;
	}

//	Update the game state after a successful move.
	private void finishMove(Piece currentPiece) {
		flagReset(currentPiece);
//		If a pawn has reached the opposite end of the board, promote it to queen.
		if (currentPiece instanceof Pawn &&
				((Pawn)currentPiece).getPromotionRow() == target.getRow()) {
			target.setPiece(new Queen(isWhitesTurn));
		}
//		Make the new piece positions visible.
		redrawBoard();
//		Try to initiate a new move.
		isWhitesTurn = !isWhitesTurn;
//		If the player starts the move in check, declare it.
		if (!kingIsSafe()) {
			declareCheck();
		}
//		If the player has no available moves, end the game.
		if (!usefulMovesLeft()) {
//			If there is no check currently declared, the game ends in a draw.
			if (!blackInCheck && !whiteInCheck) {
				System.out.println("Stalemate!");
				Main.endGame(0);
			}
			else if (isWhitesTurn) {
				System.out.println("Checkmate for white!");
				Main.endGame(1);
			}
			else {
				System.out.println("Checkmate for black!");
				Main.endGame(-1);
			}			
		}
		active = null;
	}

	private void flagReset(Piece currentPiece) {
//		Whoever was in check, they can no longer be in check after a successful move.
		whiteInCheck = false;
		blackInCheck = false;
		pieceSelected = false;
//		En passant captures are only allowed for one move,
//		so if the other player missed the chance, clear the flags.
		if (enPassantBlackTarget != null && !isWhitesTurn) {
			enPassantBlackTarget = null;
		}
		if (enPassantWhiteTarget != null && isWhitesTurn) {
			enPassantWhiteTarget = null;
		}	
//		If the king has been moved (including castling), then that player can no longer castle.
		if (currentPiece instanceof King) {
			if (isWhitesTurn) {
				canCastleWhLong = false;
				canCastleWhShort = false;
			}
			else {
				canCastleBlLong = false;
				canCastleBlShort = false;
			}
		}
//		If a rook has been moved (without castling), the player can no longer castle in that rook's direction.
		else if (currentPiece instanceof Rook) {
			if (isWhitesTurn) {
				if (active.getCol() == 0) {
					canCastleWhLong = false;
				}
				else {
					canCastleWhShort = false;
				}
			}
			else {
				if (active.getCol() == 0) {
					canCastleBlLong = false;
				}
				else {
					canCastleBlShort = false;
				}
			}
		}
	}	

//	Iterate through the board matrix and sync the corresponding tiles in the swing panel.
	private void redrawBoard() {
		for (int row = 0; row < squares.length; row++) {
			for (int col = 0; col < squares[row].length; col++) {
				Square current = squares[row][col];
				current.syncIcon();
				current.removeBorder();
				current.repaint();
			}
		}		
	}

	private void declareCheck() {
		if (isWhitesTurn) {
			whiteInCheck = true;
		}
		else {
			blackInCheck = true;
		}
		System.out.printf("%s king is in check!\n", isWhitesTurn ? "White's" : "Black's");
	}
	
//	Walks through the board matrix and tries to move every piece of the current player
//	until it finds a move that doesn't end with them in check or it runs out of moves.
	private boolean usefulMovesLeft() {
		for (Square[] squareRow : squares) {
			for (Square currentSquare : squareRow) {
				if (currentSquare.isFull() && currentSquare.getPiece().isWhite() == isWhitesTurn) {
					active = currentSquare;
					Piece currentPiece = active.getPiece();
					int activeRow = active.getRow();
					int activeCol = active.getCol();
//					The algorithm here is the same as the check test, except we start with
//					the initial figure's position known, and the target tile unknown.
//					Again, the knights move in a very different way so they are handled separately.
					boolean KnightSearch = currentPiece instanceof Knight;
						int searchStart =  KnightSearch ? -2 : -1;
						int searchEnd =  KnightSearch ? 2 : 1;
						for (int rowDiff = searchStart; rowDiff <= searchEnd; rowDiff++) {
							for (int colDiff = searchStart; colDiff <= searchEnd; colDiff++) {
							int testRow = activeRow + rowDiff;
							int testCol = activeCol + colDiff;
							while (isInBoard(testRow) && isInBoard(testCol)) {
								target = squares[testRow][testCol];
								if (!target.isFull() || target.getPiece().isPieceWhite != isWhitesTurn) {
									if (currentPiece.isMoveLegal(active, target)) {
//										For every possible move, we perform it silently, test if it results in 
//										check and reverse it if it does. We don't need to check if the path
//										is clear, because we expand the search area one square at a time.
										tryMove(currentPiece);
//										If the player can avoid a check, end the search.
										if (kingIsSafe()) {
											reverseMove(active, target);
											return true;
										}
										else {
											reverseMove(active, target);
										}
									}
								}
								if (target.isFull() || KnightSearch) {
									break;
								}						
								testRow += rowDiff;
								testCol += colDiff;		
							}
						}
					}			
				}
			}
		}
		return false;
	}
	
	private void reverseCastling(Square newRookPos) {
//		Calculate the old rook position from the new one and roll back both squares.
		if (isWhitesTurn) {
			if (newRookPos.getCol() == 3) {
				squares[7][0].rollBackPiece();
			}
			else {
				squares[7][7].rollBackPiece();
			}
		}
		else {
			if (newRookPos.getCol() == 3) {
				squares[0][0].rollBackPiece();
			}
			else {
				squares[0][7].rollBackPiece();
			}		
		}
		newRookPos.rollBackPiece();
	}

	private void goBackToSelection() {
		reverseMove(oldActive, oldTarget);
		pieceSelected = true;
		System.err.printf("That move would leave your king in check!\n");
	}

	private void reverseMove(Square oldActive, Square oldTarget) {
//		Because the old active and target field values changed when we attempted the move
//		use the backup tile indexes to find those tiles again from the board matrix and roll
//		them back to their previous state. Finally assign the old active tile to the field.
		Square oldActiveBoard = squares[oldActive.getRow()][oldActive.getCol()];
		oldActiveBoard.rollBackPiece();
		active = oldActiveBoard;
		Square oldActiveTarget = squares[oldTarget.getRow()][oldTarget.getCol()];
		oldActiveTarget.rollBackPiece();
//		Some pieces may require further actions to reverse the effect of their moves.
		Piece currentPiece = active.getPiece();
		if (currentPiece instanceof Pawn) {
//			If we reverse a pawn move that would have opened it to an en passant, reset those flags as well.
			if (isWhitesTurn && enPassantBlackTarget != null) {
				enPassantBlackTarget = null;
			}
			else if (!isWhitesTurn && enPassantWhiteTarget != null) {
				enPassantWhiteTarget = null;
			}
//			Because an en passant capture changes 3 tiles at once,
//			if we're reversing it, we also need to reverse an extra tile.
			boolean pawnCaptureMove = active.getCol() == target.getCol() + 1 ||
					active.getCol() == target.getCol() - 1;
			if (pawnCaptureMove && !target.isFull()) {
				squares[active.getRow()][target.getCol()].rollBackPiece();				
			}			
		}
//		If we reverse a king's move, restore its field in the board as well.
		if (currentPiece instanceof King) {
			if (isWhitesTurn) {
				whiteKingSquare = active;
			}
			else {
				blackKingSquare = active;
			}
		}
	}

}
