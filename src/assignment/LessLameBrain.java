package assignment;

import java.util.*;
import assignment.Board.Action;
import assignment.Board.Result;

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
			calcPaths(currentBoard, null, null);
			calcSolution();
			solved = true;
		}
		Action result = solution.pop();
		if (solution.size() == 0)
			solved = false;
		return result;
	}
	private void calcPaths(Board b, PieceState parent, Action prevMove) {
		TetrisBoard board = (TetrisBoard) b;
		TetrisPiece tp = board.getPiece();
		Pivot loc = tp.location;
		int rot = tp.getThisRotation();
		PieceState p = new PieceState(loc, rot, parent, prevMove);
		if (pieceStates.contains(p)) 
			return;
		pieceStates.add(p);
		
		Board down = board.testMove(Action.DOWN);
		if (down.getLastResult() == Result.PLACE) {
			p.place();
		}
		else{
			calcPaths(down, p, Action.DOWN);
		}
		
		Board left = board.testMove(Action.LEFT);
		if (left.getLastResult() == Result.SUCCESS) {
			calcPaths(left, p, Action.LEFT);
		}
		
		Board right = board.testMove(Action.RIGHT);
		if (right.getLastResult() == Result.SUCCESS) {
			calcPaths(right, p, Action.RIGHT);
		}
		
		Board cw = board.testMove(Action.CLOCKWISE);
		if (cw.getLastResult() == Result.SUCCESS) {
			calcPaths(cw, p, Action.CLOCKWISE);
		}
		
		Board ccw = board.testMove(Action.COUNTERCLOCKWISE);
		if (ccw.getLastResult() == Result.SUCCESS) {
			calcPaths(ccw, p, Action.COUNTERCLOCKWISE);
		}
		
	}
	private void calcSolution() {
		if (pieceStates.size() > 0) {
			Iterator<PieceState> it = pieceStates.iterator();
			PieceState best = it.next();
			while (! best.isPlaced())
				best = it.next();
			while (best.parent != null) {
				solution.add(best.getParentMove());
				best = best.parent;
			}
		}
	}
}

class PieceState{
	private Pivot location;
	private int rotationNum;
	PieceState parent;
	private Action parentMove;
	private boolean placed;
	
	public PieceState(Pivot loc, int rot, PieceState par, Action pMove) {
		location = loc;
		rotationNum = rot;
		parent = par;
		parentMove = pMove;
		placed = false;
	}
	public Action getParentMove() {
		return parentMove;
	}
	@Override
	public boolean equals(Object o) {
		return o instanceof PieceState && 
				((PieceState)o).location.equals(location) &&
				((PieceState)o).rotationNum == rotationNum;		
	}
	public int hashCode() {
		int y = (int)(location.y) << 10;
		int x = (int)(location.x) << 5;
		return y | x | rotationNum;
	}
	public void place() {
		placed = true;
	}
	public boolean isPlaced() {
		return placed;
	}
}
