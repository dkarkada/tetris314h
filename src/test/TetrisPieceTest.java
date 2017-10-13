package test;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

import assignment.Piece;
import assignment.TetrisPiece;
import assignment.TetrisPiece.TetrisType;

public class TetrisPieceTest {
	public static final TetrisPiece I = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  3 0");
	public static final TetrisPiece J = (TetrisPiece) TetrisPiece.getPiece("0 0  0 1  1 0  2 0");
	public static final TetrisPiece L = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  2 1");
	public static final TetrisPiece T = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  1 1");
	public static final TetrisPiece SQUARE = (TetrisPiece) TetrisPiece.getPiece("0 0  0 1  1 0  1 1");
	public static final TetrisPiece DUCKRIGHT = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  1 1  2 1");
	public static final TetrisPiece DUCKLEFT = (TetrisPiece) TetrisPiece.getPiece("0 1  1 1  1 0  2 0");
	
	@Test
	public void validatePieceStringTest() {
		// Make sure we can handle weird input strings
		
		// Non-number inputs
		assertFalse(TetrisPiece.validatePieceString("0 0  1 b  2 0  a 1"));
		
		// Not enough whitespace
		assertFalse(TetrisPiece.validatePieceString("00  122 0  0 1"));
				
		// Negative numbers
		assertFalse(TetrisPiece.validatePieceString("0 0  1 -1  2 0  2 1"));
		
		// Extra numbers
		assertFalse(TetrisPiece.validatePieceString("0 0  1 -1  2 0  2 1 0 1"));
		
		// Too few numbers
		assertFalse(TetrisPiece.validatePieceString("0 0  1 -1  2 "));
		
		// Escaped characters
		assertFalse(TetrisPiece.validatePieceString("0 0 \n 1 0  1 1 \n\t 2 1"));
		
		// Only one space between points is ok
		assertTrue(TetrisPiece.validatePieceString("0 0 1 0 1 1 2 1"));
		
		// Trailing/leading whitespace ok
		assertTrue(TetrisPiece.validatePieceString("        0 0  1 0  1 1  2 1    "));
		
	}
	
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
		// First rotation of J should equal hard-coded string
		Piece testPiece = TetrisPiece.getPiece("0 0  1 0  1 1  1 2"); // 1st rotation of J counterclockwise
		assertEquals(J.nextRotation(), testPiece);
		// First rotation of J rotated three more times should equal J 
		testPiece = testPiece.nextRotation().nextRotation().nextRotation();
		assertEquals(J, testPiece);
		
		// First rotation of I should equal hard-coded string
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
		
		// Previous rotation should equal rotating the piece three times forward
		// Rotating back and then forwards should equal same piece
		testPiece = TetrisPiece.getPiece("0 1  1 1  1 0  2 0"); // Initial state of DUCKLEFT
		assertEquals(DUCKLEFT, testPiece);
		assertEquals(DUCKLEFT.prevRotation(), DUCKLEFT.nextRotation().nextRotation().nextRotation());
		assertEquals(DUCKLEFT, ((TetrisPiece) testPiece.nextRotation()).prevRotation());
	}
	
	@Test
	public void getWidthTest() {
		// I piece should have width 4 at beginning
		assertEquals(4, I.getWidth());
		// I piece should have width 1 after one rotation
		assertEquals(1, I.nextRotation().getWidth());
		// I piece can't ever have width of 2
		assertNotEquals(2, I.getWidth());
		assertNotEquals(2, I.nextRotation().getWidth());
		assertNotEquals(2, I.nextRotation().nextRotation().getWidth());
		assertNotEquals(2, I.nextRotation().nextRotation().nextRotation().getWidth());
		
		// DUCKRIGHT piece should have width 3 at beginning and 2 in L and R orientations
		assertEquals(3, DUCKRIGHT.getWidth());
		assertEquals(2, DUCKRIGHT.nextRotation().getWidth());
		assertEquals(3, DUCKRIGHT.nextRotation().nextRotation().getWidth());
		assertEquals(2, DUCKRIGHT.nextRotation().nextRotation().nextRotation().getWidth());
	}
	
	@Test
	public void getHeightTest() {
		// I piece should have height 1 at beginning
		assertEquals(1, I.getHeight());
		// I piece should have height 1 after one rotation
		assertEquals(4, I.nextRotation().getHeight());
		// I piece can't ever have height of 2
		assertNotEquals(2, I.getHeight());
		assertNotEquals(2, I.nextRotation().getHeight());
		assertNotEquals(2, I.nextRotation().nextRotation().getHeight());
		assertNotEquals(2, I.nextRotation().nextRotation().nextRotation().getHeight());
		
		// DUCKRIGHT piece should have height 2 at beginning and 3 in L and R orientations
		assertEquals(2, DUCKRIGHT.getHeight());
		assertEquals(3, DUCKRIGHT.nextRotation().getHeight());
		assertEquals(2, DUCKRIGHT.nextRotation().nextRotation().getHeight());
		assertEquals(3, DUCKRIGHT.nextRotation().nextRotation().nextRotation().getHeight());
	}
	
	@Test
	public void getThisRotationTest() {
		// Make sure our rotation counting works
		assertEquals(0, T.getThisRotation());
		assertEquals(1, ((TetrisPiece) T.nextRotation()).getThisRotation());
		assertEquals(2, ((TetrisPiece) T.nextRotation().nextRotation()).getThisRotation());
		assertEquals(3, ((TetrisPiece) T.nextRotation().nextRotation().nextRotation()).getThisRotation());
		assertEquals(0, ((TetrisPiece) T.nextRotation().nextRotation().nextRotation().nextRotation()).getThisRotation());
		assertEquals(3, ((TetrisPiece) T.prevRotation()).getThisRotation());
	}
	
	@Test
	public void getTypeTest() {
		// Make sure our type setting works, with rotations as well
		Piece testPiece = TetrisPiece.getPiece("0 0  0 1  1 0  2 0"); // same rotation as J
		assertEquals(TetrisType.J, ((TetrisPiece) testPiece).getType());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  1 1  2 1"); // same rotation as DUCKRIGHT
		assertEquals(TetrisType.DUCKRIGHT, ((TetrisPiece) testPiece.nextRotation()).getType());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  3 0"); // same rotation as I
		assertEquals(TetrisType.STICK, ((TetrisPiece) testPiece).getType());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  2 1"); // same rotation as L
		assertEquals(TetrisType.L, ((TetrisPiece) testPiece.nextRotation().nextRotation()).getType());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 1  1 1  1 0  2 0"); // same rotation as DUCKLEFT
		assertEquals(TetrisType.DUCKLEFT, ((TetrisPiece) testPiece).getType());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  0 1  1 0  1 1"); // same rotation as SQUARE
		assertEquals(TetrisType.SQUARE, ((TetrisPiece) ((TetrisPiece) testPiece).prevRotation()).getType());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  1 1"); // same rotation as T
		assertEquals(TetrisType.T, ((TetrisPiece) ((TetrisPiece) testPiece).prevRotation().nextRotation()).getType());
	}
	
	@Test
	public void getBodyTest() {
		// Make sure our body is actually set during piece creation
		// Making sure the bodies can be compared to each other is checked in bodyEqualsTest
		Piece testPiece = TetrisPiece.getPiece("0 0  0 1  1 0  2 0"); // same rotation as J
		assertNotNull(testPiece.getBody());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  1 1  2 1"); // same rotation as DUCKRIGHT
		assertNotNull(testPiece.getBody());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  3 0"); // same rotation as I
		assertNotNull(testPiece.getBody());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  2 1"); // same rotation as L
		assertNotNull(testPiece.getBody());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 1  1 1  1 0  2 0"); // same rotation as DUCKLEFT
		assertNotNull(testPiece.getBody());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  0 1  1 0  1 1"); // same rotation as SQUARE
		assertNotNull(testPiece.getBody());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  1 1"); // same rotation as T
		assertNotNull(testPiece.getBody());
	}
	
	@Test
	public void getSkirtTest() {
		// Check to see if our skirt calculation is correct
		// Also check that skirt changes with rotation
		Piece testPiece = TetrisPiece.getPiece("0 0  0 1  1 0  2 0"); // same rotation as J
		assertArrayEquals(new int[] {0,0,0}, testPiece.getSkirt());
		assertArrayEquals(new int[] {0,0}, testPiece.nextRotation().getSkirt());
		assertArrayEquals(new int[] {1,1,0}, testPiece.nextRotation().nextRotation().getSkirt());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  1 1  2 1"); // same rotation as DUCKRIGHT
		assertArrayEquals(new int[] {0,0,1}, testPiece.getSkirt());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  3 0"); // same rotation as I
		assertArrayEquals(new int[] {0,0,0,0}, testPiece.getSkirt());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  2 1"); // same rotation as L
		assertArrayEquals(new int[] {0,0,0}, testPiece.getSkirt());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 1  1 1  1 0  2 0"); // same rotation as DUCKLEFT
		assertArrayEquals(new int[] {1,0,0}, testPiece.getSkirt());
		
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  0 1  1 0  1 1"); // same rotation as SQUARE
		assertArrayEquals(new int[] {0,0}, testPiece.getSkirt());
		
		// Also check that skirt changes with rotation
		testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  1 1"); // same rotation as T
		assertArrayEquals(new int[] {0,0,0}, testPiece.getSkirt());
		assertArrayEquals(new int[] {1,0}, testPiece.nextRotation().getSkirt());
		assertArrayEquals(new int[] {1,0,1}, testPiece.nextRotation().nextRotation().getSkirt());
	}
	
	@Test
	public void cloneTest() {
		TetrisPiece testPiece = (TetrisPiece) TetrisPiece.getPiece("0 0  0 1  1 0  2 0"); // same rotation as J
		testPiece.initLocation(10, 10); // dummy location data for cloning purposes
		TetrisPiece clone = testPiece.clone();
		assertEquals(clone, testPiece);
		
		// Modifying clone should make the two not equal (clone should be independent from original)
		clone = (TetrisPiece) clone.nextRotation();
		assertNotEquals(clone, testPiece);
	}
}
