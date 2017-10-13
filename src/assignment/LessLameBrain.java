package assignment;

import java.util.*;
import assignment.Board.Action;
import assignment.Board.Result;

public class LessLameBrain implements Brain{
	HashMap<PieceState, PieceState> links;
	
	public LessLameBrain() {
		 links = new HashMap<PieceState, PieceState>();
	}
	
	@Override
	public Action nextMove(Board currentBoard) {
		links.clear();
		System.out.println((TetrisBoard)currentBoard);
		calcPaths(currentBoard, null, null);		
		return calcSolution(currentBoard);
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
		
	
		Board down = board.testMove(Action.DOWN);
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
	private Action calcSolution(Board b) {
		TetrisBoard board = (TetrisBoard) b;
		HashMap<PieceState, Double> pathScores = new HashMap<PieceState, Double>();
		for (PieceState p : links.keySet()) {
			if (p.isPlaced()) {
				TetrisBoard clone = board.clone();
				clone.setPieceParams(p.location, p.rotationNum);
				clone.place();
				pathScores.put(p, score(clone));
			}
		}
		PieceState best = null;
		for (PieceState p : pathScores.keySet()) {
			if (best==null || pathScores.get(p) > pathScores.get(best) ||
					(pathScores.get(p) == pathScores.get(best) && p.hashCode()>best.hashCode()))
				best = p;
		}
		
		TetrisBoard clone = board.clone();
		clone.setPieceParams(best.location, best.rotationNum);
		clone.place();
		System.out.println(clone);
		
		ArrayList<Action> temp = new ArrayList<Action>();
		while (links.get(links.get(best)) != null) {
			temp.add(best.prevAction);
			best = links.get(best);
		}
		System.out.println(temp);
		return links.get(best)==null ? Action.DOWN : best.prevAction;		
	}
	private double score (TetrisBoard b) {
		int score = 100;
		score -= 100 * Math.pow(((double) b.getMaxHeight()/b.getHeight()), 1);
//		System.out.println(score);
		return score;
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
	Pivot location;
	int rotationNum;
	PieceState parent;
	Action prevAction;
	private boolean placed;
	private int length;
	
	public PieceState(Pivot loc, int rot, PieceState par, Action a) {
		location = loc;
		rotationNum = rot;
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
