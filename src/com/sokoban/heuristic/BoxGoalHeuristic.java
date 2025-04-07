package com.sokoban.heuristic;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import com.sokoban.model.BoardState;

/**
 * A heuristic that scores based on the number of boxes on goals.
 * The score is the number of boxes that are not on goals.
 */
public class BoxGoalHeuristic implements Heuristic {
    /**
     * Scores a board state based on the number of boxes on goals.
     * 
     * @param state The board state to score
     */
    @Override
    public void score(BoardState state) {
        Set<Point> goals = state.getGoals();
        Set<Point> boxes = state.getBoxes();

        // Find the intersection of goals and boxes (boxes on goals)
        Set<Point> intersection = new HashSet<Point>(goals);
        intersection.retainAll(boxes);

        // Difference because lower costs are better
        // Cost = total goals - boxes on goals
        state.setCost(goals.size() - intersection.size());
    }
}
