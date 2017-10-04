package assignment;

public class SimpleTester {
	public static void main(String [] args) {
		TetrisPiece thing = (TetrisPiece) TetrisPiece.getPiece("0 0  1 0  2 0  3 0");
		thing.calcRotations();
	}
}
