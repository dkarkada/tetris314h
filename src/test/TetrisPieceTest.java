package test;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

import assignment.Piece;
import assignment.TetrisPiece;

public class TetrisPieceTest {
	public static final TetrisPiece I = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  3 0");
	public static final TetrisPiece J = (TetrisPiece) TetrisPiece.getPiece("0 0  0 1  1 0  2 0");
	public static final TetrisPiece L = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  2 1");
	public static final TetrisPiece T = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  1 1");
	public static final TetrisPiece SQUARE = (TetrisPiece) TetrisPiece.getPiece("0 0  0 1  1 0  1 1");
	public static final TetrisPiece DUCKRIGHT = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  1 1  2 1");
	public static final TetrisPiece DUCKLEFT = (TetrisPiece) TetrisPiece.getPiece("0 1  1 1  1 0  2 0");
	
	@Test
	public void equalsTest() {
		TetrisPiece testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  0 1  1 0  2 0"); // same rotation as J
		// Should equal itself
		assertEquals(testPiece, testPiece);
		// "0 0  0 1  1 0  2 0" (J) should equal J
		assertEquals(testPiece, J);
		// "0 0  0 1  1 0  2 0" (J) should NOT equal L
		assertNotEquals(testPiece, L);
		// "0 0  0 1  1 0  2 0" (J) should NOT equal T
		assertNotEquals(testPiece, T);
		
		// "0 0  1 0  1 1  2 1" (DUCKRIGHT) should equal DUCKRIGHT
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  1 1  2 1"); // same rotation as DUCKRIGHT
		assertEquals(testPiece, DUCKRIGHT);
		// "0 0  0 1  1 0  2 0" (DUCKLEFT) should NOT equal DUCKLEFT
		assertNotEquals(testPiece, DUCKLEFT);
		// "0 0  0 1  1 0  2 0" (J) should NOT equal T
		assertNotEquals(testPiece, T);
		
		// T should not equal anything except itself
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  1 1"); // same rotation as T
		assertEquals(testPiece, T);
		assertNotEquals(testPiece, L);
		assertNotEquals(testPiece, J);
		assertNotEquals(testPiece, SQUARE);
		assertNotEquals(testPiece, I);
		assertNotEquals(testPiece, DUCKRIGHT);
		assertNotEquals(testPiece, DUCKLEFT);
		// Should equal itself
		assertEquals(testPiece, testPiece);
		
		// Random group of points should not equal any of the pieces
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 1  1 1  2 1  1 3");
		assertNotEquals(testPiece, L);
		assertNotEquals(testPiece, J);
		assertNotEquals(testPiece, SQUARE);
		assertNotEquals(testPiece, I);
		assertNotEquals(testPiece, DUCKRIGHT);
		assertNotEquals(testPiece, DUCKLEFT);
		assertNotEquals(testPiece, T);
	}
	
	@Test
	public void bodyEqualsTest() {
		Piece testPiece = TetrisPiece.getPiece("0 0  0 1  1 0  2 0"); // same rotation as J
		Point[] body = testPiece.getBody();
		
		// J should have same body as J in same rotation
		boolean testEquals = ((TetrisPiece) TetrisPieceTest.J).bodyEquals(new HashSet<Point>(Arrays.asList(body)));
		assertTrue(testEquals);
		// J should not have same body as L in same rotation
		testEquals = ((TetrisPiece) TetrisPieceTest.L).bodyEquals(new HashSet<Point>(Arrays.asList(body)));
		assertFalse(testEquals);
		
		// DUCKRIGHT should have same body as DUCKRIGHT in same rotation
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  1 1  2 1"); // same rotation as DUCKRIGHT
		body = testPiece.getBody();
		testEquals = ((TetrisPiece) TetrisPieceTest.DUCKRIGHT).bodyEquals(new HashSet<Point>(Arrays.asList(body)));
		assertTrue(testEquals);
		// DUCKLEFT should not have same body as DUCKRIGHT in same rotation
		testEquals = ((TetrisPiece) TetrisPieceTest.DUCKLEFT).bodyEquals(new HashSet<Point>(Arrays.asList(body)));
		assertFalse(testEquals);
	}
	
	@Test
	public void rotateTest() { // No wall kicks, just static testing of piece rotations
		// First rotation of J should equal hardcoded string
		Piece testPiece = TetrisPiece.getPiece("0 0  1 0  1 1  1 2"); // 1st rotation of J counterclockwise
		assertEquals(J.nextRotation(), testPiece);
		// First rotation of J rotated three more times should equal J 
		testPiece = testPiece.nextRotation().nextRotation().nextRotation();
		assertEquals(J, testPiece);
		
		// First rotation of I should equal hardcoded string
		testPiece = TetrisPiece.getPiece("0 0  0 1  0 2  0 3"); // 1st rotation of I counterclockwise
		assertEquals(I.nextRotation(), testPiece);
		// First rotation of J rotated three more times should equal J 
		testPiece = testPiece.nextRotation().nextRotation().nextRotation();
		assertEquals(I, testPiece);
		
		// Pieces should not be equal to any of its rotations (except in case of SQUARE)
		testPiece = TetrisPiece.getPiece("0 0  1 0  2 0  1 1"); // Initial state of T
		assertEquals(T, testPiece);
		assertNotEquals(T, testPiece.nextRotation());
		assertNotEquals(T, testPiece.nextRotation().nextRotation());
		assertNotEquals(T, testPiece.nextRotation().nextRotation().nextRotation());
		// Should equal itself after rotating 4 times
		assertEquals(T, testPiece.nextRotation().nextRotation().nextRotation().nextRotation());
	}
}
