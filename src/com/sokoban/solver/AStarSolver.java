package com.sokoban.solver;

import java.util.ArrayList;
import java.util.PriorityQueue;

import com.sokoban.heuristic.Heuristic;
import com.sokoban.model.BoardState;

/**
 * Implements A* Search for solving Sokoban puzzles.
 * A* uses a heuristic function to guide the search towards the goal.
 */
public class AStarSolver extends AbstractSolver {
    private Heuristic heuristic;

    /**
     * Private constructor for initialization.
     *
     * @param initialBoard The initial board state
     */
    private AStarSolver(BoardState initialBoard) {
        super(initialBoard);
        queue = new PriorityQueue<BoardState>();
    }

    /**
     * Public constructor that takes a heuristic.
     *
     * @param initialBoard The initial board state
     * @param heuristic    The heuristic to use
     */
    public AStarSolver(BoardState initialBoard, Heuristic heuristic) {
        this(initialBoard);
        this.heuristic = heuristic;
    }

    /**
     * Implements the A* algorithm by scoring moves using the heuristic
     * and adding them to a priority queue.
     *
     * @param validMoves List of valid moves
     */
    @Override
    protected void searchFunction(ArrayList<BoardState> validMoves) {
        for (BoardState move : validMoves) {
            backtrack.put(move, currentState);
            heuristic.score(move);
            queue.add(move);
        }
    }
}
