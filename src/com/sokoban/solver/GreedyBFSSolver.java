package com.sokoban.solver;

import java.util.ArrayList;
import java.util.PriorityQueue;

import com.sokoban.heuristic.Heuristic;
import com.sokoban.model.BoardState;

/**
 * Implements Greedy Best-First Search for solving Sokoban puzzles.
 * Greedy BFS always expands the node that seems closest to the goal
 * according to the heuristic function.
 */
public class GreedyBFSSolver extends AbstractSolver {
    private Heuristic heuristic;

    /**
     * Private constructor for initialization.
     *
     * @param initialState The initial board state
     */
    private GreedyBFSSolver(BoardState initialState) {
        super(initialState);
        queue = new PriorityQueue<BoardState>();
    }

    /**
     * Public constructor that takes a heuristic.
     *
     * @param initialState The initial board state
     * @param heuristic    The heuristic to use
     */
    public GreedyBFSSolver(BoardState initialState, Heuristic heuristic) {
        this(initialState);
        this.heuristic = heuristic;
    }

    /**
     * Initializes the search by scoring the initial state.
     */
    @Override
    protected void searchStart() {
        super.searchStart();
        heuristic.score(currentState);
    }

    /**
     * Implements the Greedy BFS algorithm by scoring moves using the heuristic
     * and adding them to a priority queue.
     *
     * @param validMoves List of valid moves
     */
    @Override
    protected void searchFunction(ArrayList<BoardState> validMoves) {
        for (BoardState move : validMoves) {
            backtrack.put(move, currentState);
            heuristic.score(move);
            if (move.getCost() < currentState.getCost()) {
                queue.add(currentState);
                queue.add(move);
                break;
            }
            queue.add(move);
        }
    }
}
