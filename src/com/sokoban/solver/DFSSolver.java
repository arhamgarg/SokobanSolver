package com.sokoban.solver;

import java.util.ArrayList;
import java.util.LinkedList;

import com.sokoban.model.BoardState;

/**
 * Implements Depth-First Search (DFS) for solving Sokoban puzzles.
 * DFS explores as far as possible along each branch before backtracking.
 */
public class DFSSolver extends AbstractSolver {
    /**
     * Constructs a new DFSSolver with the given initial state.
     *
     * @param initialState The initial board state
     */
    public DFSSolver(BoardState initialState) {
        super(initialState);
        queue = new LinkedList<BoardState>();
    }

    /**
     * Implements the DFS algorithm by adding all valid moves to the front of the
     * queue.
     *
     * @param validMoves List of valid moves
     */
    @Override
    protected void searchFunction(ArrayList<BoardState> validMoves) {
        for (BoardState move : validMoves) {
            backtrack.put(move, currentState);
            ((LinkedList<BoardState>) queue).push(move);
        }
    }
}
