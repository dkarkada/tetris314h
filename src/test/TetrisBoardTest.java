package test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import assignment.TetrisBoard;

public class TetrisBoardTest {
	private static TetrisBoard testBoard;
	public static final String I = "0 0  1 0  2 0  3 0";
	public static final String J = "0 0  0 1  1 0  2 0";
	public static final String L = "0 0  1 0  2 0  2 1";
	public static final String T = "0 0  1 0  2 0  1 1";
	public static final String SQUARE = "0 0  0 1  1 0  1 1";
	public static final String DUCKRIGHT = "0 0  1 0  1 1  2 1";
	public static final String DUCKLEFT = "0 1  1 1  1 0  2 0";
	
	@BeforeClass
	public static void initBoard() {
		// Base board used for testing
		testBoard = new TetrisBoard(10,20);
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
		
		// Same size board with no actions applied should pass
		TetrisBoard otherBoard = new TetrisBoard(10,20);
		assertTrue(testBoard.equals(otherBoard));
		
		// Different size board with no actions applied should fail
		otherBoard = new TetrisBoard(15,10);
		assertFalse(testBoard.equals(otherBoard));
	}

}
