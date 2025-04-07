package com.sokoban.heuristic;

import com.sokoban.model.BoardState;

/**
 * Interface for heuristic functions used in search algorithms.
 * Defines a contract that all heuristic implementations must follow.
 */
public interface Heuristic {
    /**
     * Scores a board state based on the heuristic.
     * Different implementations will provide different scoring algorithms.
     * 
     * @param state The board state to score
     */
    void score(BoardState state);
}
