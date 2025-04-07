package com.sokoban.solver;

import java.util.ArrayList;
import java.util.PriorityQueue;

import com.sokoban.model.BoardState;

/**
 * Implements Uniform Cost Search for solving Sokoban puzzles.
 * This algorithm assigns different costs to different types of moves:
 * - Moving the player costs 1
 * - Pushing a box costs 2
 */
public class UniformCostSolver extends AbstractSolver {
    /**
     * Constructs a new UniformCostSolver with the given initial state.
     *
     * @param initialState The initial board state
     */
    public UniformCostSolver(BoardState initialState) {
        super(initialState);
        queue = new PriorityQueue<BoardState>();
    }

    /**
     * Implements the Uniform Cost Search algorithm by assigning costs to moves
     * and adding them to a priority queue.
     *
     * @param moves List of valid moves
     */
    @Override
    protected void searchFunction(ArrayList<BoardState> moves) {
        for (BoardState move : moves) {
            backtrack.put(move, currentState);
            uniformCostFunction(move, currentState.getCost());
            queue.add(move);
        }
    }

    /**
     * Assigns a cost to a move based on whether it involves pushing a box.
     *
     * @param state    The board state after the move
     * @param baseCost The cost of the current state
     */
    private void uniformCostFunction(BoardState state, int baseCost) {
        if (currentState.nextMoveHas(BoardState.BOX, state.getDirectionTaken()))
            state.setCost(baseCost + 2);
        else
            state.setCost(baseCost + 1);
    }
}
