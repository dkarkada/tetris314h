package test;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.*;

import org.junit.BeforeClass;
import org.junit.Test;

import assignment.Piece;
import assignment.TetrisPiece;

public class TetrisPieceTest {
	private static Piece leftL;
	private static Piece rightL;
	private static Piece tee;
	private static Piece square;
	private static Piece stick;
	private static Piece leftDog;
	private static Piece rightDog;
	
	@BeforeClass
	public static void initPieces() {
		leftL = TetrisPiece.getPiece("0 0  1 0  1 1  1 2");
		rightL = TetrisPiece.getPiece("0 0  0 1  0 2  1 0");
		tee = TetrisPiece.getPiece("0 0  1 0  2 0  1 1");
		square = TetrisPiece.getPiece("0 0  1 0  0 1  1 1");
		stick = TetrisPiece.getPiece("0 0  1 0  2 0  3 0");
		leftDog = TetrisPiece.getPiece("1 0  2 0  0 1  1 1");
		rightDog = TetrisPiece.getPiece("0 0  1 0  1 1  2 1");
	}
	
	@Test
	public void equalsTest() {
	}

}
