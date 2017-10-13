package assignment;

import java.util.*;

public class SimpleTester {
	public static void main(String [] args) {
		Pivot loc1 = new Pivot(10, 15.5);
		Pivot loc2 = new Pivot(10, 15.5);
		PieceState p1 = new PieceState(loc1, 3, null, Board.Action.LEFT);
		PieceState p2 = new PieceState(loc2, 3, null, Board.Action.DOWN);
		HashSet<PieceState> hs = new HashSet<PieceState>();
		hs.add(p1);
		System.out.println(hs.contains(p2));
		System.out.println(p1.equals(p2));
		System.out.println(p1.hashCode());
		System.out.println(p2.hashCode());
	}
}