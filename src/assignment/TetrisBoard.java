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
	private int maxHeight;
	private int[] colFillNums;
	TetrisPiece nextPiece;
	Board.Action lastAction;
	Board.Result lastResult;
	private int rowsCleared;
	
	
    // JTetris will use this constructor
    public TetrisBoard(int w, int h) {
    	width = w;
    	height = h;
    	rowFillNums = new int[height];
    	colFillNums = new int[width];
    	state = new boolean[height][width];
    	nextPiece = null;
    	lastAction = Board.Action.NOTHING;
    	lastResult = Board.Result.NO_PIECE;
    }

    @Override
    public Result move(Action act) {
    	
    	if (nextPiece == null)
    		return Result.NO_PIECE;
    	
    	Pivot loc = nextPiece.location;
    	
    	switch(act) {
    		case NOTHING:
    			return Result.SUCCESS;
    		case DOWN:
    			loc.y -= 2;
    			if (pieceValid()) {
    				loc.y += 1;
    				return Result.SUCCESS;
    			}
				loc.y += 1;
				place();
    			return Result.PLACE;
    		case LEFT:
    			loc.x -= 1;
    			if (pieceValid()) {
    				loc.y -= 1;
        			if (pieceValid()) {
        				loc.y += 1;
        				return Result.SUCCESS;
        			}
    				loc.y += 1;
    				place();
        			return Result.PLACE;
    			}
    			loc.x += 1;
    			return Result.OUT_BOUNDS;
    		case RIGHT:
    			nextPiece.location.x += 1;
    			if (pieceValid()) {
    				loc.y -= 1;
        			if (pieceValid()) {
        				loc.y += 1;
        				return Result.SUCCESS;
        			}
    				loc.y += 1;
    				place();
        			return Result.PLACE;
    			}
    			loc.x -= 1;
    			return Result.OUT_BOUNDS;
    		case COUNTERCLOCKWISE: {
    			TetrisPiece currentPiece = nextPiece;
    			Pivot currentLoc = new Pivot(loc.x, loc.y);
    			nextPiece = (TetrisPiece) nextPiece.nextRotation();
    			nextPiece.location = loc;
    			
    			if (!pieceValid()) {
    				// Get rotation of current piece
    				int thisRotation = currentPiece.getThisRotation();
    				
    				if (nextPiece.getType() == TetrisType.STICK)
    					thisRotation += 4;
    				
    				Point[] wallKickData = TetrisPiece.wallKickData.get(thisRotation);
	    			for (Point p : wallKickData) {
	    				// Make copy of current location settings
	    				loc.x += p.x;
	    				loc.y += p.y;
	    				
	    				// Test wall kick scenario, return if it is ok
	    				nextPiece.location = loc;
	        			if (pieceValid()) {
	        				loc.y -= 1;
	            			if (pieceValid()) {
	            				loc.y += 1;
	            				return Result.SUCCESS;
	            			}
	            			loc.y += 1;
	        				place();
	            			return Result.PLACE;
	        			}
	    				// Otherwise, reset to original location and try again with next wall kick data point
	    				else {
	    					loc.x -= p.x;
	    					loc.y -= p.y;
	    				}
	    			}
	    			System.out.println("Invalid rotation counterclockwise");
	    			
	    			// Reset piece to original state if no wall kicks work
	    			nextPiece = currentPiece;
	    			nextPiece.location = currentLoc;
	    			return Result.OUT_BOUNDS;
    			}
    			loc.y -= 1;
    			if (pieceValid()) {
    				loc.y += 1;
    				return Result.SUCCESS;
    			}
				loc.y += 1;
				place();
    			return Result.PLACE;
    		}
    		case CLOCKWISE: {
    			TetrisPiece currentPiece = nextPiece;
    			Pivot currentLoc = new Pivot(loc.x, loc.y);
    			nextPiece = (TetrisPiece) nextPiece.prevRotation();
    			nextPiece.location = loc;
    			
    			if (!pieceValid()) {
    				// Get rotation of current piece
    				int thisRotation = (currentPiece.getThisRotation()+3)%4;
    				
    				if (nextPiece.getType() == TetrisType.STICK)
    					thisRotation += 4;
    				
    				Point[] wallKickData = TetrisPiece.wallKickData.get(thisRotation);
	    			for (Point p : wallKickData) {
	    				// Make copy of current location settings
	    				loc.x += (-1*p.x);
	    				loc.y += (-1*p.y);
	    				
	    				// Test wall kick scenario, return if it is ok
	    				nextPiece.location = loc;
	        			if (pieceValid()) {
	        				loc.y -= 1;
	            			if (pieceValid()) {
	            				loc.y += 1;
	            				return Result.SUCCESS;
	            			}
	            			loc.y += 1;
	        				place();
	            			return Result.PLACE;
	        			}
	    				// Otherwise, reset to original location and try again with next wall kick data point
	    				else {
	    					loc.x -= p.x;
	    					loc.y -= p.y;
	    				}
	    			}

	    			// Reset piece to original state if no wall kicks work
	    			nextPiece = currentPiece;
	    			nextPiece.location = currentLoc;
	    			System.out.println(nextPiece.location.x+" "+nextPiece.location.y);
	    			System.out.println("Invalid rotation clockwise");
	    			return Result.OUT_BOUNDS;
    			}
    			loc.y -= 1;
    			if (pieceValid()) {
    				loc.y += 1;
    				return Result.SUCCESS;
    			}
				loc.y += 1;
				place();
    			return Result.PLACE;
    		}
    		case DROP:
    			Result r = null;
    			while(r != Result.PLACE) {
    				r = move(Action.DOWN);
    			}
    			return r;
    	}
    	return Result.SUCCESS;
    }

    @Override
    public Board testMove(Action act) { 
    	return null;
    	//TODO implement
    }

    @Override
    public void nextPiece(Piece p) {
    	nextPiece = (TetrisPiece) p;
		TetrisPiece.createCircularLL(nextPiece);
    	nextPiece.initLocation(height, width/2);
    }

    @Override
    public boolean equals(Object other) {
    	TetrisBoard otherBoard = (TetrisBoard) other;
    	boolean[][] otherBoardState = otherBoard.getState();
    	
    	for (int r = 0; r < height; r++) {
    		for (int c = 0; c < width; c++) {
    			if (!valid(r,c) || !(otherBoard.valid(r,c))) {
    				return false;
    			}
    			else if (state[r][c] != otherBoardState[r][c]) {
    				return false;
    			}
    		}
    	}
    	
    	return true;
    }
    
    public boolean[][] getState() {
    	return state;
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
    	return -1; 
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
    	return -1;
    }

    @Override
    public int getColumnHeight(int x) {
    	return colFillNums[x];
    }

    @Override
    public int getRowWidth(int y) {
    	return rowFillNums[y];
    }

    @Override
    public boolean getGrid(int x, int y) {
    	if (!valid(x, y))
    		return true;
    	if (nextPiece != null) {
	    	Point[] piece = nextPiece.getBody();
	    	Pivot center = nextPiece.getPivot();
	    	for (Point p : piece) {
	    		int px = (int) (p.x - center.x + nextPiece.location.x);
	    		int py = (int) (p.y - center.y + nextPiece.location.y);
	    		if (px == x && py == y)
	    			return true;
	    	}
    	}
    	return state[yToRow(y)][xToCol(x)];
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
    	Pivot loc = nextPiece.location;
    	Pivot center = nextPiece.getPivot();
    	Point[] piece = nextPiece.getBody();
    	for (Point p : piece) {
    		int x = (int) (p.x - center.x + loc.x);
    		int y = (int) (p.y - center.y + loc.y);
    		if(!valid(x,y) || state[yToRow(y)][xToCol(x)])
    			return false;
    	}
    	return true;
    }
    private void place() {
    	Point[] piece = nextPiece.getBody();
    	Pivot center = nextPiece.getPivot();
    	for (Point p : piece) {
    		int x = (int) (p.x - center.x + nextPiece.location.x);
    		int y = (int) (p.y - center.y + nextPiece.location.y);
    		//System.out.println(x+" "+y);
    		state[yToRow(y)][xToCol(x)] = true;
    	}
    	nextPiece = null;
    	
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
		System.out.println();
    }
}
