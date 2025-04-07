package com.sokoban.heuristic;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import com.sokoban.model.BoardState;

/**
 * A heuristic that scores based on Manhattan distances between boxes and goals.
 * For each box, it finds the closest goal and adds the Manhattan distance to
 * the score.
 */
public class ManhattanHeuristic implements Heuristic {
    /**
     * Scores a board state based on Manhattan distances between boxes and goals.
     *
     * @param state The board state to score
     */
    @Override
    public void score(BoardState state) {
        Set<Point> goals = state.getGoals();
        Set<Point> boxes = state.getBoxes();

        // Boxes on a goal are cost 0 so don't check them
        Set<Point> intersection = new HashSet<Point>(goals);
        intersection.retainAll(boxes);
        goals.removeAll(intersection);
        boxes.removeAll(intersection);

        // Calculate the minimum Manhattan distance for each box to any goal
        int cost = 0;
        for (Point box : boxes) {
            int minMarginalCost = Integer.MAX_VALUE;
            for (Point goal : goals) {
                int dist = getManhattanDistance(box, goal);
                if (dist < minMarginalCost)
                    minMarginalCost = dist;
            }
            cost += minMarginalCost;
        }
        state.setCost(cost);
    }

    /**
     * Calculates the Manhattan distance between two points.
     *
     * @param p1 The first point
     * @param p2 The second point
     * @return The Manhattan distance between p1 and p2
     */
    private static int getManhattanDistance(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }
}
