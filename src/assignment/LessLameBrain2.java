package assignment;

import java.util.*;

import assignment.Board.Action;

/**
 * A Lame Brain implementation for JTetris; tries all possible places to put the
 * piece (but ignoring rotations, because we're lame), trying to minimize the
 * total height of pieces on the board.
 */
public class LessLameBrain2 implements Brain {

    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMoves;

    /**
     * Decide what the next move should be based on the state of the board.
     */
    public Board.Action nextMove(Board currentBoard) {
        // Fill the our options array with versions of the new Board
        options = new ArrayList<>();
        firstMoves = new ArrayList<>();
        enumerateOptions(currentBoard);

        double best = 0;
        int bestIndex = 0;

        // Check all of the options and get the one with the highest score
        for (int i = 0; i < options.size(); i++) {
            double score = scoreBoard(options.get(i), currentBoard);
            if (score > best) {
                best = score;
                bestIndex = i;
            }
        }
        // We want to return the first move on the way to the best Board
        return firstMoves.get(bestIndex);
    }

    /**
     * Test all of the places we can put the current Piece.
     * Since this is just a Lame Brain, we aren't going to do smart
     * things like rotating pieces.
     */
    private void enumerateOptions(Board currentBoard) {
	    for(int i=0; i<4; i++) {
	    	if (i!=0) {
	    		currentBoard = currentBoard.testMove(Action.CLOCKWISE);
	    	}
	    	Action a;
	        // We can always drop our current Piece
	        options.add(currentBoard.testMove(Board.Action.DROP));
	        a = i==0 ? Action.DROP : Action.CLOCKWISE;
	        firstMoves.add(a);
	
	        // Now we'll add all the places to the left we can DROP
	        Board left = currentBoard.testMove(Board.Action.LEFT);
	        while (left.getLastResult() == Board.Result.SUCCESS) {
	            options.add(left.testMove(Board.Action.DROP));
		        a = i==0 ? Action.LEFT : Action.CLOCKWISE;
	            firstMoves.add(a);
	            left = left.testMove(Board.Action.LEFT);
	        }
	
	        // And then the same thing to the right
	        Board right = currentBoard.testMove(Board.Action.RIGHT);
	        while (right.getLastResult() == Board.Result.SUCCESS) {
	            options.add(right.testMove(Board.Action.DROP));
		        a = i==0 ? Action.RIGHT : Action.CLOCKWISE;
	            firstMoves.add(a);
	            right = right.testMove(Board.Action.RIGHT);
	        }
	    }
    }

    /**
     * Since we're trying to avoid building too high,
     * we're going to give higher scores to Boards with
     * MaxHeights close to 0.
     */
    private double scoreBoard(Board newBoard, Board oldBoard) {
    	double score = 100;
    	
    	score -= 100 * Math.pow(((double) newBoard.getMaxHeight())/newBoard.getHeight(), 2);
    	score += 100 * (newBoard.getRowsCleared() - oldBoard.getRowsCleared());
    	score -= 30 * Math.log((getHoles(newBoard)+1));
    	score += 200 * Math.pow(getFill(newBoard), 2);
        return score;
    }
    
    private int getHoles (Board b) {
    	TetrisBoard t = (TetrisBoard) b;
    	int count = 0;
    	for (int x=0; x<t.getWidth(); x++) {
    		int height = t.getColumnHeight(x);
    		int fill = t.getColumnFill(x);
    		count += height - fill;
    	}
    	return count;
    }
    public double getFill (Board b) {
    	TetrisBoard t = (TetrisBoard) b;
    	double count = 0;
    	for (int y=0; y<t.getMaxHeight(); y++) {
    		int fill = t.getRowWidth(y);
    		double gradient = ((double)(t.getMaxHeight()-y)) / t.getMaxHeight();
    		count +=  gradient * ((double)fill) / t.getWidth();
    	}
    	count /= t.getMaxHeight();
    	return count;
    }

}
