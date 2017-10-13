package test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import assignment.Board;
import assignment.JTetris;
import assignment.Piece;
import assignment.Pivot;
import assignment.TetrisBoard;
import assignment.TetrisPiece;

public class TetrisBoardTest {
	private static TetrisBoard testBoard;
	public static final TetrisPiece I = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  3 0");
	public static final TetrisPiece J = (TetrisPiece) TetrisPiece.getPiece("0 0  0 1  1 0  2 0");
	public static final TetrisPiece L = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  2 1");
	public static final TetrisPiece T = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  1 1");
	public static final TetrisPiece SQUARE = (TetrisPiece) TetrisPiece.getPiece("0 0  0 1  1 0  1 1");
	public static final TetrisPiece DUCKRIGHT = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  1 1  2 1");
	public static final TetrisPiece DUCKLEFT = (TetrisPiece) TetrisPiece.getPiece("0 1  1 1  1 0  2 0");
	
	@BeforeClass
	public static void initBoard() {
		// Base board used for testing
		testBoard = new TetrisBoard(10,20);
		testBoard.nextPiece(I);
	}
	
	@Test
	public void validTest() {
		// Out of bounds locations should fail test
		assertFalse(testBoard.valid(-10, 20));
		assertFalse(testBoard.valid(20, 10));
		assertFalse(testBoard.valid(5, -5));
		
		// In bounds locations should pass test
		assertTrue(testBoard.valid(0, 0));
		assertTrue(testBoard.valid(9, 19));
		assertTrue(testBoard.valid(5, 5));
	}
	
	@Test
	public void equalsTest() {
		// Board should equal itself
		assertTrue(testBoard.equals(testBoard));
		assertEquals(testBoard, testBoard);
		
		// Same size board with just one I piece added should pass
		TetrisBoard otherBoard = new TetrisBoard(10,20);
		otherBoard.nextPiece(I);
		assertEquals(testBoard, otherBoard);
		
		// Different size board with no actions applied should fail
		otherBoard = new TetrisBoard(15,10);
		assertNotEquals(testBoard, otherBoard);
	}
	
	@Test
	public void testTestMove() {
		// Test move should produce a distinct and independent representation of the moved board.
		Board movedBoard = testBoard.testMove(Board.Action.RIGHT);
		assertNotNull(movedBoard);
		assertNotEquals(testBoard, movedBoard);
		
		// Moving original board to match moved board should make them equal again
		testBoard.move(Board.Action.RIGHT);
		assertEquals(testBoard, movedBoard);
		
		// Reset testBoard
		testBoard.move(Board.Action.LEFT);
		assertNotEquals(testBoard, movedBoard);
	}
	
	@Test
	public void getWidthAndHeightTest() {
		// Make sure getWidth returns correctly
		assertEquals(10, testBoard.getWidth());
		
		// Make sure getHeight returns correctly
		assertEquals(20, testBoard.getHeight());
	}
	
	@Test
	public void dropHeightTest() {
		// If we drop a STICK, L, or J directly from the top onto empty board, it should be at zero
		assertEquals(0, testBoard.dropHeight(I, JTetris.WIDTH / 2));
		assertEquals(0, testBoard.dropHeight(J, JTetris.WIDTH / 2));
		assertEquals(0, testBoard.dropHeight(L, JTetris.WIDTH / 2));
		
		// Should increase once we drop some more pieces
		Board droppingBoard = testBoard.testMove(Board.Action.DROP);
		// Should be 1 after dropping first I piece
		assertEquals(1, droppingBoard.dropHeight(I, JTetris.WIDTH / 2));
		// Should be 4 after dropping I onto already dropped rotated vertical J piece (3 units tall)
		droppingBoard.nextPiece(J.nextRotation());
		droppingBoard.move(Board.Action.DROP);
		assertEquals(4, droppingBoard.dropHeight(I, JTetris.WIDTH / 2));
		
	}

	@Test
	public void moveTest() {
		// Board should be able to do every action
		Board copy = testBoard.testMove(Board.Action.NOTHING);
        assertEquals(Board.Result.SUCCESS, copy.move(Board.Action.DOWN));
        assertEquals(Board.Result.SUCCESS, copy.move(Board.Action.NOTHING));
        assertEquals(Board.Result.SUCCESS, copy.move(Board.Action.DOWN));
        
        // Should be able to getLastAction() and getLastResult()
        assertEquals(Board.Result.SUCCESS, copy.getLastResult());
        assertEquals(Board.Action.DOWN, copy.getLastAction());
        
        assertEquals(Board.Result.SUCCESS, copy.move(Board.Action.LEFT));
        assertEquals(Board.Result.SUCCESS, copy.move(Board.Action.CLOCKWISE));
        assertEquals(Board.Result.SUCCESS, copy.move(Board.Action.COUNTERCLOCKWISE));
	}
	
	@Test
	public void getMaxHeightTest() {
		// Board height should be zero if we haven't done anything to it
		Board copy = testBoard.testMove(Board.Action.NOTHING);
        assertEquals(0, copy.getMaxHeight());
        
        // Board height should be 1 after dropping a horizontal STICK
        copy.move(Board.Action.DROP);
        assertEquals(1, copy.getMaxHeight());
        
        // Board height should be 5 after dropping a vertical STICK on top of horizontal STICK
        copy.nextPiece(I.nextRotation());
        copy.move(Board.Action.DROP);
        assertEquals(5, copy.getMaxHeight());
        
        // Drop a vertical STICK in the left-most column shouldn't affect max height of board
        copy.nextPiece(I.nextRotation());
        copy.move(Board.Action.LEFT);
        copy.move(Board.Action.LEFT);
        copy.move(Board.Action.LEFT);
        copy.move(Board.Action.LEFT);
        copy.move(Board.Action.DROP);
        assertEquals(5, copy.getMaxHeight());
	}
	
	@Test
	public void getRowsClearedTest() {
		// Board height should be zero if we haven't done anything to it
		Board copy = testBoard.testMove(Board.Action.NOTHING);
        assertEquals(0, copy.getRowsCleared());
        
        // Rows cleared should still be 0 after dropping a horizontal STICK in the leftmost column
        copy.move(Board.Action.LEFT);
        copy.move(Board.Action.LEFT);
        copy.move(Board.Action.LEFT);
        copy.move(Board.Action.DROP);
        assertEquals(0, copy.getRowsCleared());
        
        // Rows cleared should still be 0 after dropping a horizontal STICK adjacent to first one
        copy.nextPiece(I);
        copy.move(Board.Action.RIGHT);
        copy.move(Board.Action.DROP);
        assertEquals(0, copy.getRowsCleared());
        // Max height should still be 1 at this point
        assertEquals(1, copy.getMaxHeight());
        
        // Fill in row with a square and check rowsCleared
        copy.nextPiece(SQUARE);
        copy.move(Board.Action.RIGHT);
        copy.move(Board.Action.RIGHT);
        copy.move(Board.Action.RIGHT);
        copy.move(Board.Action.RIGHT);
        copy.move(Board.Action.DROP);
        assertEquals(1, copy.getRowsCleared());
        // Max height should still be 1 at this point
        assertEquals(1, copy.getMaxHeight());
	}
	
	@Test
	public void getColumnHeightTest() {
		Board copy = testBoard.testMove(Board.Action.NOTHING);
		// Every column should be empty
		for (int i = 0; i < copy.getWidth(); i++) {
			assertEquals(0, copy.getColumnHeight(i));
		}
		
		// Drop a STICK on leftmost edge
		copy.move(Board.Action.LEFT);
        copy.move(Board.Action.LEFT);
        copy.move(Board.Action.LEFT);
        copy.move(Board.Action.DROP);
        // First 4 columns should have height 1, rest should have height zero
        for (int i = 0; i < 4; i++) {
        	assertEquals(1, copy.getColumnHeight(i));
        }
        for (int i = 4; i < copy.getWidth(); i++) {
        	assertEquals(0, copy.getColumnHeight(i));
        }
        
        // Doing it again should result in 1 higher in the stack of sticks, zero in everything else
        copy.nextPiece(I);
        copy.move(Board.Action.LEFT);
        copy.move(Board.Action.LEFT);
        copy.move(Board.Action.LEFT);
        copy.move(Board.Action.DROP);
        // First 4 columns should have height 1, rest should have height zero
        for (int i = 0; i < 4; i++) {
        	assertEquals(2, copy.getColumnHeight(i));
        }
        for (int i = 4; i < copy.getWidth(); i++) {
        	assertEquals(0, copy.getColumnHeight(i));
        }
	}
	
	@Test
	public void getRowWidthTest() {
		Board copy = testBoard.testMove(Board.Action.NOTHING);
		// Every row should be empty
		for (int i = 0; i < copy.getHeight(); i++) {
			assertEquals(0, copy.getRowWidth(i));
		}
		
		// Drop a STICK on leftmost edge, row 0 should end up with width 4
		copy.move(Board.Action.LEFT);
        copy.move(Board.Action.LEFT);
        copy.move(Board.Action.LEFT);
        copy.move(Board.Action.DROP);
        assertEquals(4, copy.getRowWidth(0));
        
        // Rows cleared should still be 0 after dropping a horizontal STICK adjacent to first one
        copy.nextPiece(I);
        copy.move(Board.Action.RIGHT);
        copy.move(Board.Action.DROP);
        assertEquals(0, copy.getRowsCleared());
        // Max height should still be 1 at this point
        assertEquals(1, copy.getMaxHeight());
        
        // Fill in row with a square to clear row
        copy.nextPiece(SQUARE);
        copy.move(Board.Action.RIGHT);
        copy.move(Board.Action.RIGHT);
        copy.move(Board.Action.RIGHT);
        copy.move(Board.Action.RIGHT);
        copy.move(Board.Action.DROP);
        assertEquals(1, copy.getRowsCleared());
        // Row 0 should have only width 2 now, since sticks cleared
        assertEquals(2, copy.getRowWidth(0));
        // Max height should still be 1 at this point
        assertEquals(1, copy.getMaxHeight());
	}
	
	@Test
	public void getGridTest() {
		Board copy = testBoard.testMove(Board.Action.NOTHING);
		copy.nextPiece(null);
		// Every cell
		for (int y = 0; y < copy.getHeight(); y++) {
			for (int x = 0; x < copy.getWidth(); x++) {
				assertFalse(copy.getGrid(x, y));
			}
		}
		
		// Drop a STICK on leftmost edge, 4 cells in bottom left corner should be true
		copy.nextPiece(I);
		copy.move(Board.Action.LEFT);
	    copy.move(Board.Action.LEFT);
	    copy.move(Board.Action.LEFT);
	    copy.move(Board.Action.DROP);
	    for (int x = 0; x < 4; x++) {
	    	assertTrue(copy.getGrid(x, 0));
	    }
	    // Rest of row should be empty
	    for (int x = 4; x < copy.getWidth(); x++) {
	    	assertFalse(copy.getGrid(x, 0));
	    }
	}
	
	@Test
	public void cloneTest() {
		// Clone should have same state as original immediately after cloning
		Board original = testBoard.clone();
		Board clone = testBoard.clone();
		assertEquals(clone, original);
		
		// If some pieces are placed in clone, it should not be equal to original anymore
		clone.nextPiece(SQUARE);
		clone.move(Board.Action.DROP);
		assertNotEquals(clone, original);
		
		// If same action is done in original, states should be equal again
		original.nextPiece(SQUARE);
		original.move(Board.Action.DROP);
		assertEquals(clone, original);
		
		// Neither original nor clone should be equal to testBoard anymore
		assertNotEquals(clone, testBoard);
		assertNotEquals(original, testBoard);
	}
	
	@Test
	public void getPieceAndNextPieceTest() {
		// Setting a new curPiece should be reflected in getPiece
		Board clone = testBoard.clone();
		clone.nextPiece(DUCKLEFT);
		assertNotNull(((TetrisBoard) clone).getPiece());
		assertEquals(DUCKLEFT, ((TetrisBoard) clone).getPiece());
		clone.nextPiece(L);
		assertNotEquals(DUCKLEFT, ((TetrisBoard) clone).getPiece());
		assertEquals(L, ((TetrisBoard) clone).getPiece());
		
		// Should work for null
		clone.nextPiece(null);
		assertNull(((TetrisBoard) clone).getPiece());
	}
	
	@Test
	public void wallKicksTest() {
		// Move a vertical STICK to left side of board
		Board clone = testBoard.clone();
		clone.nextPiece(I);
		clone.move(Board.Action.CLOCKWISE);
		// Should wallkick against ceiling
		TetrisPiece wallKickedPiece = ((TetrisBoard) clone).getPiece();
		Pivot expectedLocation = new Pivot(2.5, 17.5);
		assertEquals(expectedLocation.getX(), wallKickedPiece.getLocation().getX(), 0);
	    assertEquals(expectedLocation.getY(), wallKickedPiece.getLocation().getY(), 0);
		clone.move(Board.Action.LEFT);
	    clone.move(Board.Action.LEFT);
	    clone.move(Board.Action.LEFT);
	    
	    // Rotate against wall, triggering wall kick
	    clone.move(Board.Action.COUNTERCLOCKWISE);
	    wallKickedPiece = ((TetrisBoard) clone).getPiece();
	    expectedLocation = new Pivot(1.5, 17.5);
	    assertEquals(expectedLocation.getX(), wallKickedPiece.getLocation().getX(), 0);
	    assertEquals(expectedLocation.getY(), wallKickedPiece.getLocation().getY(), 0);
	    clone.move(Board.Action.DROP);
	    
	    // Test wall kicks with non-STICK piece
	    // Move T all the way to left edge, long side facing edge
	    clone.nextPiece(T.nextRotation().nextRotation());
		clone.move(Board.Action.DOWN);
		clone.move(Board.Action.COUNTERCLOCKWISE);
	    clone.move(Board.Action.LEFT);
	    clone.move(Board.Action.LEFT);
	    clone.move(Board.Action.LEFT);
	    clone.move(Board.Action.LEFT);
	    clone.move(Board.Action.LEFT);
	    // Trigger wallkick
	    clone.move(Board.Action.COUNTERCLOCKWISE);
	    wallKickedPiece = ((TetrisBoard) clone).getPiece();
	    expectedLocation = new Pivot(1, 18);
	    assertEquals(expectedLocation.getX(), wallKickedPiece.getLocation().getX(), 0);
	    assertEquals(expectedLocation.getY(), wallKickedPiece.getLocation().getY(), 0);
	}
}
