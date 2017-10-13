package test;

import static org.junit.Assert.*;
import org.junit.Test;

import assignment.*;

import java.awt.Point;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class IntegrationTest {

	// Base pieces for use
	public static final TetrisPiece I = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  3 0");
	public static final TetrisPiece J = (TetrisPiece) TetrisPiece.getPiece("0 0  0 1  1 0  2 0");
	public static final TetrisPiece L = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  2 1");
	public static final TetrisPiece T = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  1 1");
	public static final TetrisPiece SQUARE = (TetrisPiece) TetrisPiece.getPiece("0 0  0 1  1 0  1 1");
	public static final TetrisPiece DUCKRIGHT = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  1 1  2 1");
	public static final TetrisPiece DUCKLEFT = (TetrisPiece) TetrisPiece.getPiece("0 1  1 1  1 0  2 0");

    @Test
    public void testBoard() {
        // We should be able to create "non-standard" board sizes.
        Board board = new TetrisBoard(JTetris.WIDTH, 3 * JTetris.HEIGHT);

        assertEquals(board, board);

        // We should be able to get the width/height appropriately.
        assertEquals(board.getWidth(), JTetris.WIDTH);
        assertEquals(board.getHeight(), 3 * JTetris.HEIGHT);

        // Trying to move results in NO_PIECE initially, and should result in
        // no change of board state.
        assertEquals(Board.Result.NO_PIECE, board.move(Board.Action.DOWN));
        assertEquals(Board.Result.NO_PIECE, board.move(Board.Action.CLOCKWISE));

        // We should be able to gauge the drop height of a piece on an empty board.
        Piece piece = I;
        assertEquals(0, board.dropHeight(piece, JTetris.WIDTH / 2));

        // We should be able to give the board a piece.
        board.nextPiece(piece);

        // We should be able to TEST a move, where we create a new board
        // which is non-null and reflects the results of trying to take the
        // given action.
        Board board2 = board.testMove(Board.Action.LEFT);
        assertNotNull(board2);
        assertNotEquals(board, board2);

        // The new board should have the resulting action, but the old
        // board should not (as it is completely separate).
        assertEquals(Board.Result.SUCCESS, board2.getLastResult());
        assertEquals(Board.Action.LEFT, board2.getLastAction());
        assertEquals(Board.Action.CLOCKWISE, board.getLastAction());

        // We should be able to use the standard movements on the board.
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.DOWN));
        // Make sure lastAction and lastResult update correctly
        assertEquals(Board.Action.DOWN, board.getLastAction());
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.NOTHING));
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.DOWN));
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.LEFT));
        // Make sure lastAction and lastResult update correctly
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.CLOCKWISE));
        assertEquals(Board.Action.CLOCKWISE, board.getLastAction());
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.COUNTERCLOCKWISE));

        // The last action/last result should change appropriately.
        assertEquals(Board.Result.SUCCESS, board.getLastResult());
        assertEquals(Board.Action.COUNTERCLOCKWISE, board.getLastAction());
        // Board2 should not have been affected
        assertEquals(Board.Action.LEFT, board2.getLastAction());

        // We should be able to drop the piece and place it; due to variance
        // in implementation, the actual PLACE could occur either after the
        // drop or after a down after the drop.
        Board.Result dropResult = board.move(Board.Action.DROP);
        Board.Result downResult = board.move(Board.Action.DOWN);
        assertTrue(dropResult == Board.Result.PLACE || downResult == Board.Result.PLACE);
        
        // The row width of the bottom piece should be 4 exactly.
        assertEquals(4, board.getRowWidth(0));

        // The max row height should now be 1.
        assertEquals(1, board.getMaxHeight());

        // The last action/last result should change appropriately.
        assertEquals(Board.Action.DOWN, board.getLastAction());
        assertEquals(downResult, board.getLastResult());

        // We shouldn't have a piece anymore again.
        assertEquals(Board.Result.NO_PIECE, board.move(Board.Action.DOWN));
        
        // Let's clear some rows
        // Should be able to clear two rows at once
        // First, there should be no rows cleared yet
        assertEquals(0, board.getRowsCleared());
        board.nextPiece(I);
        board.move(Board.Action.LEFT);
        board.move(Board.Action.DROP);
        // The row width of the bottom should still be 4 exactly.
        assertEquals(4, board.getRowWidth(0));
        // Row should now also be 4
        assertEquals(4, board.getRowWidth(1));

        // The max row height should now be 2
        assertEquals(2, board.getMaxHeight());
        
        // Add a square to far left side
        board.nextPiece(SQUARE);
        board.move(Board.Action.LEFT);
        board.move(Board.Action.LEFT);
        board.move(Board.Action.LEFT);
        board.move(Board.Action.LEFT);
        board.move(Board.Action.DROP);
    	// The row width of the bottom should still be 6 exactly.
        assertEquals(6, board.getRowWidth(0));
        // Row should now also be 6
        assertEquals(6, board.getRowWidth(1));
        
        // Add a square to far right side
        board.nextPiece(SQUARE);
        board.move(Board.Action.RIGHT);
        board.move(Board.Action.RIGHT);
        board.move(Board.Action.RIGHT);
        board.move(Board.Action.RIGHT);
        board.move(Board.Action.DROP);
        
        board.nextPiece(SQUARE);
        board.move(Board.Action.RIGHT);
        board.move(Board.Action.RIGHT);
        board.move(Board.Action.DROP);
        
        // Board should clear two rows here
        assertEquals(2, board.getRowsCleared());
        // Height and width for everything should be zero
        for (int x = 0; x < board.getWidth(); x++) {
        	assertEquals(0, board.getColumnHeight(x));
        }
        for (int y = 0; y < board.getHeight(); y++) {
        	assertEquals(0, board.getRowWidth(y));
        }
        
        // Let's test some wall kicks
        board.nextPiece(J.nextRotation()); // we want long side against wall
        // Move J all the way against right wall
        board.move(Board.Action.RIGHT);
        board.move(Board.Action.RIGHT);
        board.move(Board.Action.RIGHT);
        board.move(Board.Action.RIGHT);
        board.move(Board.Action.DOWN);
        board.move(Board.Action.DOWN);
        
        // Trigger wall kick by trying to rotate into wall
        board.move(Board.Action.COUNTERCLOCKWISE);
        
        // Check rotated piece's position against expected calculated position
        TetrisPiece wallKickedPiece = ((TetrisBoard) board).getPiece();
		Pivot expectedLocation = new Pivot(8, 56);
		assertEquals(expectedLocation.getX(), wallKickedPiece.getLocation().getX(), 0);
	    assertEquals(expectedLocation.getY(), wallKickedPiece.getLocation().getY(), 0);
    }
}
