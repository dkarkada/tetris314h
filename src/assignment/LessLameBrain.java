package assignment;

import java.util.*;
import assignment.Board.Action;
import assignment.Board.Result;

public class LessLameBrain implements Brain{
	HashMap<PieceState, PieceState> links;
	Stack<Action> solution;
	boolean solved;
	
	public LessLameBrain() {
		 links = new HashMap<PieceState, PieceState>();
		 solution = new Stack<Action>();
		 solved = false;
	}
	
	@Override
	public Action nextMove(Board currentBoard) {
		if (! solved) {
			links.clear();
			calcPaths(currentBoard, null, null);
			calcSolution();
			solved = true;
		}
		if (solution.size() == 0) {
			solved = false;
			return Action.DOWN;
		}
		return solution.remove(solution.size()-1);
	}
	private void calcPaths(Board b, PieceState parent, Action prev) {
		TetrisBoard board = (TetrisBoard) b;
		TetrisPiece tp = board.getPiece();
		Pivot loc = tp.location;
		int rot = tp.getThisRotation();
		PieceState p = new PieceState(loc, rot, parent, prev);
		if (links.keySet().contains(p)) {
			if(links.get(p) != null && p.parent != null && 
					links.get(p).getLength() > p.parent.getLength())
				links.put(p, p.parent);
			return;
		}
		links.put(p, p.parent);
		
		Board down;
		try {
		down = board.testMove(Action.DOWN);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(p);
			down = board;
		}
		if (down.getLastResult() == Result.PLACE) {
			p.place();
		}
		else{
			calcPaths(down, p, Action.DOWN);
		}
		
		Action[] actions = {Action.LEFT, Action.RIGHT, Action.CLOCKWISE, Action.COUNTERCLOCKWISE};
		for (Action a : actions) {
			Board nextBoard = board.testMove(a);
			if (nextBoard.getLastResult() == Result.SUCCESS) {
				calcPaths(nextBoard, p, a);
			}
		}
		
	}
	private void calcSolution() {
		if (links.size() > 0) {
			Iterator<PieceState> it = links.keySet().iterator();
			PieceState best = it.next();
			while (! best.isPlaced())
				best = it.next();
			while (links.get(best) != null) {
				solution.add(best.prevAction);
				best = links.get(best);
			}
		}
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

class PieceState {
	private Pivot location;
	private int rotationNum;
//	ArrayList<Action> path;
	PieceState parent;
	Action prevAction;
	private boolean placed;
	private int length;
	
	public PieceState(Pivot loc, int rot, PieceState par, Action a) {
		location = loc;
		rotationNum = rot;
//		path = p;
		parent = par;
		length = par != null ? par.length + 1 : 0;
		prevAction = a;
		placed = false;
	}
	public int getLength() {
		return length;
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
	public String toString() {
		String result = "";
		result += location.x + " " + location.y + " " + rotationNum;
		return result;
	}
	public void place() {
		placed = true;
	}
	public boolean isPlaced() {
		return placed;
	}
}
