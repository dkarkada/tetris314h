package assignment;

public class SimpleTester {
	public static void main(String [] args) {
		TetrisPiece thing = (TetrisPiece) TetrisPiece.getPiece("0 0  1 1  2 1  0 1");
		TetrisPiece.createCircularLL(thing);
	}
}
