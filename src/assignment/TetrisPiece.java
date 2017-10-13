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
	public static ArrayList<Point[]> wallKickData;
	
	static {
		templates = new HashMap<HashSet<Point>, TetrisType>();
		String[] pieceStrings = { 
				"0 0  1 0  2 0  3 0",
                "0 0  0 1  1 0  2 0",
                "0 0  1 0  2 0  2 1",
                "0 0  1 0  1 1  2 1",
                "0 1  1 1  1 0  2 0",
                "0 0  0 1  1 0  1 1",
                "0 0  1 0  2 0  1 1" };
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
		
		String[] wallKickStrings = {
				"1 0  1 -1  0 2  1 2", // 3>0
				"1 0  1 1  0 -2  1 -2", // 0>1
				"-1 0  -1 -1  0 2  -1 2", // 1>2
				"-1 0  -1 1  0 -2  -1 -2" // 2>3
		};
		
		String[] wallKickStringsStick = {
				"2 0  -1 0  2 1  -1 -2", // 3>0
				"-1 0  2 0  -1 2  2 -1", // 0>1
				"-2 0  1 0  -2 -1  1 2", // 1>2
				"1 0  -2 0  1 -2  -2 1" // 2>3
		};
		
		wallKickData = new ArrayList<Point[]>();
		
		for (String s : wallKickStrings) {
			wallKickData.add(Piece.parsePoints(s));
		}
		
		for (String s : wallKickStringsStick) {
			wallKickData.add(Piece.parsePoints(s));
		}
	}

	/**
     * Parse a "piece string" of the form "x1 y1 x2 y2 ..." into a TetrisPiece
     * where the corresponding (x1, y1), (x2, y2) positions have been filled in.
     */

    public static Piece getPiece(String pieceString) {
    	return new TetrisPiece(Piece.parsePoints(pieceString));
    }
    
	public static void createCircularLL(TetrisPiece t1) {
		TetrisPiece t2 = new TetrisPiece(rotate(t1.getBody()), false);
		TetrisPiece t3 = new TetrisPiece(rotate(t2.getBody()), false);
		TetrisPiece t4 = new TetrisPiece(rotate(t3.getBody()), false);
		t1.next = t2;
		t2.next = t3;
		t3.next = t4;
		t4.next = t1;
		
		
		TetrisPiece tNext = t1;
		boolean done = false;
		TetrisType typeVar  = TetrisType.OTHER;
		
		// Find the rotation of the piece that is the correct spawn position (rotation 0)
		do {
			for(Set<Point> template : TetrisPiece.templates.keySet()) {
				if (tNext.bodyEquals(template)) {
					done = true;
					typeVar = TetrisPiece.templates.get(template);
				}
			}
			
			if (!done)
				tNext = (TetrisPiece) tNext.next;
			
		} while (!done && tNext!=t1);
		
		if (done) {
			TetrisPiece head = tNext;
			Pivot center = new Pivot(0,0);
			
			switch (typeVar) {
				case STICK:
					center = new Pivot(1.5, -0.5);
					break;
				case J:
					center = new Pivot(1, 0);
					break;
				case L:
					center = new Pivot(1, 0);
					break;
				case DUCKRIGHT:
					center = new Pivot(1, 0);
					break;
				case DUCKLEFT:
					center = new Pivot(1, 0);
					break;
				case SQUARE:
					center = new Pivot(0.5, 0.5);
					break;
				case T:
					center = new Pivot(1, 0);
					break;
				default:
					break;				
			}
			
			int rotationNum = 0;
			do {
				tNext.setType(typeVar);
				// Set rotation number (from 0 to 3)
				tNext.thisRotation = rotationNum;
				rotationNum++;
				
				int offsetX = (tNext.thisRotation == 1 || tNext.thisRotation == 2) ? tNext.width - 1 : 0;
				int offsetY = (tNext.thisRotation == 2 || tNext.thisRotation == 3) ? tNext.height - 1 : 0;
				
				Pivot temp = new Pivot(center.x + offsetX, center.y + offsetY);
				tNext.center = temp;
				
				rotate(center);
				
				tNext = (TetrisPiece) tNext.next;
			} while (tNext!=head);
		}
		
		else
			System.out.println("AAAAAAAAAA!!!!!!! u fucked up!!!!!");
	}
	
	public static Point[] rotate(Point[] input) {
		Point[] result = new Point[input.length];
		int minx = Integer.MAX_VALUE;
		int miny = Integer.MAX_VALUE;
		for (int i=0; i<input.length; i++) {
			Point p = input[i];
			int x = - p.y;
			int y =  p.x;
			minx = Math.min(minx, x);
			miny = Math.min(miny, y);
			result[i] = new Point(x, y);  
		}
		for (Point p : result) {
			p.setLocation(p.x - minx, p.y - miny);
		}
		return result;
	}
	
	public static void rotate(Pivot center) {
		double x = - center.y;
		double y = center.x;
		center.x = x;
		center.y = y;
	}
	
	private TetrisType type;
	private Pivot center;
	private int height;
	private int width;
	private Point[] body;
	private int[] skirt;
	private int thisRotation;
	Pivot location;
	
	public TetrisPiece(Point[] points) {
		body = points;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		
		for (Point p : body) {
			maxX = Math.max(maxX, p.x);
			maxY = Math.max(maxY, p.y);
		}
		
		width = maxX + 1;
		height = maxY + 1;
		calcSkirt(width);
	}
	
	public TetrisPiece(Point[] points, boolean createLinkedList) {
		body = points;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		
		for (Point p : body) {
			maxX = Math.max(maxX, p.x);
			maxY = Math.max(maxY, p.y);
		}
		
		width = maxX + 1;
		height = maxY + 1;
		calcSkirt(width);
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
    
    public Piece prevRotation() {
    	return nextRotation().nextRotation().nextRotation();
    }
    
    public void initLocation(int topEdgeHeight, int midAlignWidth) {
    	double y = topEdgeHeight - height + center.y;
    	double x = midAlignWidth + center.x - width/2;
    	location = new Pivot(x, y);
    }
    public Pivot getPivot() {
    	return center;
    }

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
    
    private void calcSkirt(int width) {
    	skirt = new int[width];
    	Arrays.fill(skirt, Integer.MAX_VALUE);
    	for(Point p : body) {
    		skirt[p.x] = Math.min(skirt[p.x], p.y);
    	}
    }
    
    public int getThisRotation() {
    	return thisRotation;
    }
    
    public String toString() {
    	return Arrays.toString(body);
    }
    
    public TetrisPiece clone() {
    	TetrisPiece clone = new TetrisPiece(body.clone());
    	clone.location = new Pivot(location.x, location.y);
    	clone.thisRotation = thisRotation;
    	clone.center = new Pivot(center.x, center.y);

		TetrisPiece t2 = new TetrisPiece(rotate(clone.getBody()), false);
		Pivot c2 = new Pivot(center.x, center.y);
		rotate(c2);
		t2.center = c2;
		t2.thisRotation = (thisRotation + 1) % 4;
		
		TetrisPiece t3 = new TetrisPiece(rotate(t2.getBody()), false);
		Pivot c3 = new Pivot(t2.center.x, t2.center.y);
		rotate(c3);
		t3.center = c3;
		t3.thisRotation = (thisRotation + 2) % 4;
		
		TetrisPiece t4 = new TetrisPiece(rotate(t3.getBody()), false);
		Pivot c4 = new Pivot(t3.center.x, t3.center.y);
		rotate(c4);
		t4.center = c4;
		t4.thisRotation = (thisRotation + 3) % 4;
		
		clone.next = t2;
		t2.next = t3;
		t3.next = t4;
		t4.next = clone;
    	return clone;
    }
}