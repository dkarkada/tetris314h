package assignment;

import java.awt.*;
import java.util.*;

import assignment.TetrisPiece.TetrisType; 

//TODO: figure out strange bug where newly spawned piece cant rotate and throws errors, also when stick is in vertical slot and tries to rotate also throws weird errors
//TODO: split out logic that checks whether or not to place piece into its own method

/**
 * Represents a Tetris board -- essentially a 2-d grid of booleans. Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {
	private boolean[][] state;
	private int width;
	private int height;
	private int[] rowFillNums;
	private int[] colFillNums;
	private int maxHeight;
	TetrisPiece curPiece;
	TetrisPiece lastPlacedPiece;
	Board.Action lastAction;
	Board.Result lastResult;
	private int rowsCleared;
	
	
    // JTetris will use this constructor
    public TetrisBoard(int w, int h) {
    	if (w < 4) {
    		throw new IllegalArgumentException("Width must be greater than 4");
    	}
    	if (h < 4) {
    		throw new IllegalArgumentException("Height must be greater than 4");
    	}
    	width = w;
    	height = h;
    	rowFillNums = new int[height];
    	colFillNums = new int[width];
    	state = new boolean[height][width];
    	curPiece = null;
    	lastAction = Board.Action.NOTHING;
    	lastResult = Board.Result.NO_PIECE;
    	lastPlacedPiece = null;
    }

    @Override
    public Result move(Action act) {
    	if (act==null)
    		throw new IllegalArgumentException();
    	lastAction = act;
    	lastResult = Result.SUCCESS;
    	if (curPiece == null)
    		lastResult = Result.NO_PIECE;
    	else {    	
	    	Pivot loc = curPiece.location;
	    	switch(act) {
	    		case NOTHING:
	    			lastResult = Result.SUCCESS;
	    			break;
	    		case DOWN:
	    			loc.y -= 1;
	    			if (! pieceValid()) {
						loc.y += 1;
						place();
		    			lastResult = Result.PLACE;	    				
	    			}
	    			break;
	    		case LEFT:
	    			loc.x -= 1;
	    			if (! pieceValid()) {
		    			loc.x += 1;
		    			lastResult = Result.OUT_BOUNDS;	    				
	    			}
	    			break;
	    		case RIGHT:
	    			loc.x += 1;
	    			if (! pieceValid()) {
		    			loc.x -= 1;
		    			lastResult = Result.OUT_BOUNDS;
	    			}
	    			break;
	    		case COUNTERCLOCKWISE: {
	    			TetrisPiece beforeRotationPiece = curPiece;
	    			Pivot beforeRotationLoc = new Pivot(loc.x, loc.y);
	    			curPiece = (TetrisPiece) curPiece.nextRotation();
	    			curPiece.location = loc;
	    			
	    			if (!pieceValid()) {
	    				// Get rotation of current piece
	    				int rotationNum = curPiece.getThisRotation();    				
	    				if (curPiece.getType() == TetrisType.STICK)
	    					rotationNum += 4;    				
	    				Point[] wallKickData = TetrisPiece.wallKickData.get(rotationNum);
	    				
	    				// try wall kicks until it works or there are no more wall kicks to try
	    				boolean valid = false;
	    				int ind = 0;
	    				while (!valid && ind < wallKickData.length) {
	    					Point p = wallKickData[ind];
		    				// Make copy of current location settings
		    				loc.x += p.x;
		    				loc.y += p.y;
		    				// Test wall kick scenario, if not ok reset to original location and try again
		        			if (pieceValid()) {
		        				valid = true;
		        			}
		    				else {
		    					loc.x -= p.x;
		    					loc.y -= p.y;
			        			ind++;
		    				}
	    				}
		    			// Reset piece to original state if no wall kicks work
		    			if (!valid) {
			    			curPiece = beforeRotationPiece;
			    			curPiece.location = beforeRotationLoc;
			    			lastResult = Result.OUT_BOUNDS;
		    			}
	    			}
	    			break;
	    		}
	    		case CLOCKWISE: {
	    			TetrisPiece beforeRotationPiece = curPiece;
	    			Pivot beforeRotationLoc = new Pivot(loc.x, loc.y);
	    			curPiece = (TetrisPiece) curPiece.prevRotation();
	    			curPiece.location = loc;

	    			if (!pieceValid()) {
	    				// Get rotation of non-rotated piece
	    				int rotationNum = beforeRotationPiece.getThisRotation();
	    				if (curPiece.getType() == TetrisType.STICK)
	    					rotationNum += 4;
	    				Point[] wallKickData = TetrisPiece.wallKickData.get(rotationNum);
	    				
	    				// try wall kicks until it works or there are no more wall kicks to try
	    				boolean valid = false;
	    				int ind = 0;
	    				while (!valid && ind < wallKickData.length) {
	    					Point p = wallKickData[ind];
		    				loc.x -= p.x;
		    				loc.y -= p.y;
		    				// Test wall kick scenario, if not ok reset to original location and try again
		        			if (pieceValid()) {
		        				valid = true;
		        			}
		    				else {
		    					loc.x += p.x;
		    					loc.y += p.y;
			        			ind++;
		    				}
		    			}	
		    			// Reset piece to original state if no wall kicks work
	    				if (!valid) {
			    			curPiece = beforeRotationPiece;
			    			curPiece.location = beforeRotationLoc;
			    			lastResult = Result.OUT_BOUNDS;
		    			}
	    			}
	    			break;
	    		}
	    		case DROP:
	    			Result r = null;
	    			while(r != Result.PLACE) {
	    				r = move(Action.DOWN);
	    			}
	    			lastResult = r;
	    	}
    	}
    	return lastResult;
    }
    @Override
    public Board testMove(Action act) {
    	TetrisBoard duplicate = clone();
    	duplicate.move(act);
    	return duplicate;
    }
    @Override
    public void nextPiece(Piece p) {
    	curPiece = (TetrisPiece) p;
    	if (curPiece != null && curPiece.getBody() != null) {
    		TetrisPiece.createCircularLL(curPiece);
        	curPiece.initLocation(height, width/2);
    	}
    	else {
    		System.out.println("Null piece passed to board.");
    	}
    }
    @Override
    public boolean equals(Object other) {
    	TetrisBoard otherBoard = (TetrisBoard) other;
    	
    	for (int y = 0; y < height; y++) {
    		for (int x = 0; x < width; x++) {
    			if (!valid(x,y) || !(otherBoard.valid(x,y))) {
    				return false;
    			}
    			else if (getGrid(x,y) != otherBoard.getGrid(x,y)) {
    				return false;
    			}
    		}
    	}
    	
    	return true;
    }   
    public boolean[][] getState() {
    	boolean[][] cloneState = new boolean[height][];
    	for (int i=0; i<state.length; i++)
    		cloneState[i] = state[i].clone();
    	return cloneState;
    }
    @Override
    public Result getLastResult() { 
    	return lastResult; 
    }
    @Override
    public Action getLastAction() { 
    	return lastAction; 
    }
    @Override
    public int getRowsCleared() { 
    	return rowsCleared; 
    }
    @Override
    public int getWidth() { 
    	return width; 
    }
    @Override
    public int getHeight() {
    	return height;
    }
    @Override
    public int getMaxHeight() {
    	return maxHeight;
    }
    @Override
    public int dropHeight(Piece piece, int x) {
    	TetrisPiece droppingPiece = (TetrisPiece) piece;
    	TetrisPiece.createCircularLL(droppingPiece);
    	droppingPiece.initLocation(height, x);
    	
    	TetrisPiece temp = null;
    	if (curPiece != null) {
    		temp = curPiece.clone();
    	}
    	curPiece = droppingPiece;
    	
    	TetrisBoard boardAfterDrop = (TetrisBoard) testMove(Action.DROP);
    	Result result = boardAfterDrop.getLastResult();
    	curPiece = boardAfterDrop.lastPlacedPiece;
    	
    	int height = -1;
    	if (result == Result.PLACE) {
    		System.out.println(result);
    		System.out.println(curPiece.location.y);
    		height = (int) Math.ceil(curPiece.location.y);
    	}
    	
    	// reset curPiece
    	curPiece = temp;
    	
    	//System.out.println(yToRow(height));
    	return height;
    }
    @Override
    public int getColumnHeight(int x) {
    	return colFillNums[x];
    }
    public int getColumnFill(int x) {
    	int count = 0;
    	for (int y=0; y<height; y++) {
    		if (state[yToRow(y)][xToCol(x)])
    			count++;
    	}
    	return count;
    }
    @Override
    public int getRowWidth(int y) {
    	return rowFillNums[y];
    }
    @Override
    public boolean getGrid(int x, int y) {
    	if (!valid(x, y))
    		return true;
    	if (curPiece != null) {
	    	Point[] piece = curPiece.getBody();
	    	Pivot center = curPiece.getPivot();
	    	for (Point p : piece) {
	    		int px = (int) (p.x - center.x + curPiece.location.x);
	    		int py = (int) (p.y - center.y + curPiece.location.y);
	    		if (px == x && py == y)
	    			return true;
	    	}
    	}
    	return state[yToRow(y)][xToCol(x)];
    }
    public void setPieceParams(Pivot location, int rotationNum) {
    	while (curPiece.getThisRotation() != rotationNum)
    		curPiece = (TetrisPiece) curPiece.next;
    	curPiece.location = location;
    }
    public String toString() {
    	String result = "Board:\n";
	  	for (int y = height - 1; y>=0; y--) {
    		for (int x = 0; x < width; x++)
   			result += getGrid(x, y) ? "# " : "  ";
     		result += "\n";
    	}
    	return result;
    }
    public TetrisBoard clone() {
    	TetrisBoard clone = new TetrisBoard(width, height);
    	clone.state = getState();
    	clone.rowFillNums = rowFillNums.clone();
    	clone.colFillNums = colFillNums.clone();
    	clone.maxHeight = maxHeight;
    	clone.curPiece = curPiece.clone();
    	clone.lastAction = lastAction;
    	clone.lastResult = lastResult;
    	clone.rowsCleared = rowsCleared;
    	return clone;
    }
    public TetrisPiece getPiece() {
    	return curPiece;
    }
    public boolean valid(int x, int y) {
    	return x >= 0 && x < width && y >=0 && y < height;
    }
    private int xToCol(int x) {
    	return x;
    }
    private int yToRow(int y) {
    	return height - y - 1;
    }
    private boolean pieceValid() {
    	Pivot loc = curPiece.location;
    	Pivot center = curPiece.getPivot();
    	Point[] piece = curPiece.getBody();
    	for (Point p : piece) {
    		int x = (int) (p.x - center.x + loc.x);
    		int y = (int) (p.y - center.y + loc.y);
    		if(!valid(x,y) || state[yToRow(y)][xToCol(x)])
    			return false;
    	}
    	return true;
    }
    public void place() {
    	Point[] piece = curPiece.getBody();
    	Pivot center = curPiece.getPivot();
    	for (Point p : piece) {
    		int x = (int) (p.x - center.x + curPiece.location.x);
    		int y = (int) (p.y - center.y + curPiece.location.y);
    		
    		try {
    		state[yToRow(y)][xToCol(x)] = true;
    		}catch(Exception e) {
    			e.printStackTrace();
    			throw e;
    		}
    	}
    	lastPlacedPiece = curPiece.clone();
    	curPiece = null;
    	
    	ArrayList<Integer> filledRows = new ArrayList<Integer>();
    	for(int y=0; y<height; y++) {
    		boolean filled = true;
    		for(int x=0; x<width && filled; x++)
    			if(! state[yToRow(y)][xToCol(x)])
    				filled = false;
    		if (filled)
    			filledRows.add(y);
    	}
    	for (int i=filledRows.size()-1; i>=0; i--) {
    		int row = filledRows.get(i);
    		for(int y=row+1; y<height; y++) {
        		for(int x=0; x<width; x++){
        			state[yToRow(y-1)][xToCol(x)] = state[yToRow(y)][xToCol(x)];
        			state[yToRow(y)][xToCol(x)] = false;
        		}
        	}
    	}
    	rowsCleared += filledRows.size();
    	
    	for(int y=0; y<height; y++) {
    		int count = 0;
    		for(int x=0; x<width; x++)
    			if (state[yToRow(y)][xToCol(x)])
    				count++;
    		rowFillNums[y] = count;
    	}
    	maxHeight = 0;
		for(int x=0; x<width; x++) {
    		int max = 0;
        	for(int y=0; y<height; y++)
    			if (state[yToRow(y)][xToCol(x)])
    				max = Math.max(y+1, max);
    		colFillNums[x] = max;
    		maxHeight = Math.max(max, maxHeight);
    	}
    }
}
