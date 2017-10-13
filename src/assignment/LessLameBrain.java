package assignment;

import java.util.*;
import assignment.Board.Action;

public class LessLameBrain implements Brain{
	HashSet<PieceState> pieceStates;
	Stack<Action> solution;
	boolean solved;
	
	public LessLameBrain() {
		 pieceStates = new HashSet<PieceState>();
		 solution = new Stack<Action>();
		 solved = false;
	}
	
	@Override
	public Action nextMove(Board currentBoard) {
		if (! solved) {
			calcPaths((TetrisBoard)currentBoard, null);
			solved = true;
		}
		Action result = solution.pop();
		if (solution.size() == 0)
			solved = false;
		return result;
	}
	private boolean calcPaths(TetrisBoard b, PieceState parent) {
		TetrisPiece tp = b.getPiece();
		Pivot loc = tp.location;
		int rot = tp.getThisRotation();
		PieceState p = new PieceState(loc, rot, parent);
		return true;
	}
	
	private int countHolesInBoard(TetrisBoard b) {
		int count = 0;
		for (int i = 0; i < b.getHeight(); i++) {
			for (int j = 0; j < b.getWidth(); j++) {
				
			}
		}
		
		return 0;
	}
}

class PieceState{
	private Pivot location;
	private int rotationNum;
	PieceState parent;
	private Action parentMove;
	private boolean placed;
	
	
	public PieceState(Pivot loc, int rot, PieceState par) {
		location = loc;
		rotationNum = rot;
		parent = par;
	}
	public boolean equals(PieceState p) {
		return p.location == location && p.rotationNum == rotationNum;		
	}
	public int hashCode() {
		return location.hashCode() ^ rotationNum;
	}
}
