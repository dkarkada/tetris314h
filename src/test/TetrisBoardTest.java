package test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import assignment.TetrisBoard;

public class TetrisBoardTest {
	private static TetrisBoard testBoard;
	
	@BeforeClass
	public static void initBoard() {
		testBoard = new TetrisBoard(20,10);
	}
	
	@Test
	public void validTest() {
		assertFalse(testBoard.valid(-10, 20));
		assertFalse(testBoard.valid(20, 10));
		
		assertTrue(testBoard.valid(0, 0));
		assertFalse(testBoard.valid(19, 9));
	}
	
	@Test
	public void equalsTest() {
		TetrisBoard otherBoard = new TetrisBoard(20,10);
		assertTrue("failed assertTrue", testBoard.equals(otherBoard));
	}

}
