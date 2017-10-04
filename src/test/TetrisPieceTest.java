package test;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

import assignment.Piece;
import assignment.TetrisPiece;

public class TetrisPieceTest {
	private static Piece J;
	private static Piece L;
	private static Piece T;
	private static Piece SQUARE;
	private static Piece STICK;
	private static Piece LEFTDOG;
	private static Piece RIGHTDOG;
	
	@BeforeClass
	public static void initPieces() {
		J = TetrisPiece.getPiece("0 0  1 0  1 1  1 2");
		L = TetrisPiece.getPiece("0 0  0 1  0 2  1 0");
		T = TetrisPiece.getPiece("0 0  1 0  2 0  1 1");
		SQUARE = TetrisPiece.getPiece("0 0  1 0  0 1  1 1");
		STICK = TetrisPiece.getPiece("0 0  1 0  2 0  3 0");
		LEFTDOG = TetrisPiece.getPiece("1 0  2 0  0 1  1 1");
		RIGHTDOG = TetrisPiece.getPiece("0 0  1 0  1 1  2 1");
	}
	
	@Test
	public void equalsTest() {
		System.out.println("0 0  1 0  1 1  1 2 should equal J");
		Piece testPiece = TetrisPiece.getPiece("0 0  1 0  1 1  1 2"); // same rotation as J
		boolean testEquals = testPiece.equals(TetrisPieceTest.J);
		assertTrue(testEquals);
		
		System.out.println("0 0  0 1  0 2  1 0 should NOT equal L");
		testEquals = testPiece.equals(TetrisPieceTest.L);
		assertFalse(testEquals);
		
		System.out.println();
	}
	
	@Test
	public void bodyEqualsTest() {
		System.out.println("0 0  1 0  1 1  1 2 should have same body as J");
		Piece testPiece = TetrisPiece.getPiece("0 0  1 0  1 1  1 2"); // same rotation as J
		Point[] body = testPiece.getBody();
		boolean testEquals = ((TetrisPiece) TetrisPieceTest.J).bodyEquals(new HashSet<Point>(Arrays.asList(body)));
		assertTrue(testEquals);
		
		System.out.println("0 0  1 0  1 1  1 2 should NOT have same body as L");
		testEquals = ((TetrisPiece) TetrisPieceTest.L).bodyEquals(new HashSet<Point>(Arrays.asList(body)));
		assertFalse(testEquals);
		
		System.out.println();
	}
}
