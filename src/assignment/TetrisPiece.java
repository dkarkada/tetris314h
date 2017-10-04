package assignment;

import java.awt.*;
import java.util.*;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * Each piece is defined by the blocks that make up its body.
 *
 * You need to implement this.
 */
public final class TetrisPiece extends Piece {
	public static enum TetrisType{
		T,
		SQUARE,
		STICK,
		L,
		J,
		DUCKRIGHT,
		DUCKLEFT,
		OTHER;
	}
	public static HashMap<HashSet<Point>, TetrisType> templates;
	
	static {
		templates = new HashMap<HashSet<Point>, TetrisType>();
		String[] pieceStrings = { "0 0  1 0  2 0  3 0",
                "0 1  1 1  2 1  2 0",
                "0 0  0 1  1 1  2 1",
                "0 0  1 0  1 1  2 1",
                "0 1  1 1  1 0  2 0",
                "0 0  0 1  1 0  1 1",
                "0 1  1 0  1 1  2 1" };
		templates.put(new HashSet<Point>(Arrays.asList(Piece.parsePoints(pieceStrings[0]))),
				TetrisType.STICK);
		templates.put(new HashSet<Point>(Arrays.asList(Piece.parsePoints(pieceStrings[1]))),
				TetrisType.J);
		templates.put(new HashSet<Point>(Arrays.asList(Piece.parsePoints(pieceStrings[2]))),
				TetrisType.L);
		templates.put(new HashSet<Point>(Arrays.asList(Piece.parsePoints(pieceStrings[3]))),
				TetrisType.DUCKRIGHT);
		templates.put(new HashSet<Point>(Arrays.asList(Piece.parsePoints(pieceStrings[4]))),
				TetrisType.DUCKLEFT);
		templates.put(new HashSet<Point>(Arrays.asList(Piece.parsePoints(pieceStrings[5]))),
				TetrisType.SQUARE);
		templates.put(new HashSet<Point>(Arrays.asList(Piece.parsePoints(pieceStrings[6]))),
				TetrisType.T);
	}
	public static void createCircularLL(TetrisPiece head) {
		
	}
	
	TetrisType type;
	Pivot center;
	private int height;
	private int width;
	private Point[] body;
	private int[] skirt;
	private int index;
	private ArrayList<TetrisPiece> rotations;
	
	public TetrisPiece(Point[] points) {
		body = points;
		calcHeight();
		calcWidth();
		calcSkirt();
	}
	
	public TetrisPiece(TetrisPiece orig, int rotationNum, ArrayList<TetrisPiece> rotations) {
		//	body = rotate(orig, rotationNum);
		calcHeight();
		calcWidth();
		calcSkirt();
		
		index = rotationNum;
		rotations.add(this);
		if (index == 3)
			next = orig;
		else
			next = new TetrisPiece(orig, rotationNum+1, rotations);
	}
	
	public void calcRotations() {
		index = 0;
		rotations = new ArrayList<TetrisPiece>();
		rotations.add(this);
		next = new TetrisPiece(this, index+1, rotations);
		calcType();		
	}
	public void calcType() {
		int ind = 0;
		boolean done = false;
		TetrisType typeVar  = TetrisType.OTHER;
		while(!done && ind<rotations.size()) {
			TetrisPiece t = rotations.get(ind);
			for(Set<Point> template : TetrisPiece.templates.keySet()) {
				if (t.bodyEquals(template)) {
					done = true;
					typeVar = TetrisPiece.templates.get(template);
				}
			}
			ind++;
		}
		for(TetrisPiece t : rotations) {
			t.setType(typeVar);
		}

		System.out.println(type);
	}

	/**
     * Parse a "piece string" of the form "x1 y1 x2 y2 ..." into a TetrisPiece
     * where the corresponding (x1, y1), (x2, y2) positions have been filled in.
     */
	
	//TODO check if can make the return type tetris piece
    public static Piece getPiece(String pieceString) {
    	return new TetrisPiece(Piece.parsePoints(pieceString));
    }
    
    public TetrisType getType() {
    	return type;
    }
    public void setType(TetrisType t) {
    	type = t;
    }
    
    @Override
    public int getWidth() { return width; }

    @Override
    public int getHeight() { return height; }

    @Override
    public Point[] getBody() { 
    	return body;
    }

    @Override
    public int[] getSkirt() { return skirt; }

    @Override
    public boolean equals(Object other) {
    	if (other instanceof TetrisPiece) {
    		return bodyEquals(new HashSet<Point>(Arrays.asList(((TetrisPiece) other).getBody())));
    	}
    	return false;
    }
    public boolean bodyEquals(Set<Point> other) {
    	return new HashSet<Point>(Arrays.asList(body)).equals(other);
    }
    
    private int calcHeight() {
    	return 0;
    }
    
    private int calcWidth() {
    	return 0;
    }
    
    private int[] calcSkirt() {
    	return null;
    }
    public String toString() {
    	return Arrays.toString(body);
    }
}
