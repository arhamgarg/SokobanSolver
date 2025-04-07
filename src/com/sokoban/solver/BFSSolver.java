package com.sokoban.solver;

import java.util.ArrayList;
import java.util.LinkedList;

import com.sokoban.model.BoardState;

/**
 * Implements Breadth-First Search (BFS) for solving Sokoban puzzles.
 * BFS explores all nodes at the present depth before moving on to nodes at the
 * next depth level.
 */
public class BFSSolver extends AbstractSolver {
    /**
     * Constructs a new BFSSolver with the given initial state.
     *
     * @param initialState The initial board state
     */
    public BFSSolver(BoardState initialState) {
        super(initialState);
        queue = new LinkedList<BoardState>();
    }

    /**
     * Implements the BFS algorithm by adding all valid moves to the end of the
     * queue.
     *
     * @param validMoves List of valid moves
     */
    @Override
    protected void searchFunction(ArrayList<BoardState> validMoves) {
        for (BoardState move : validMoves) {
            backtrack.put(move, currentState);
            queue.add(move);
        }
    }
}
