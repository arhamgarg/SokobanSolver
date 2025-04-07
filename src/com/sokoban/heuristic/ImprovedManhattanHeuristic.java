package com.sokoban.heuristic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sokoban.model.BoardState;

/**
 * An improved Manhattan heuristic that considers box-box interactions
 * and assigns boxes to goals optimally.
 */
public class ImprovedManhattanHeuristic implements Heuristic {
    /**
     * Scores a board state using an improved Manhattan distance heuristic.
     *
     * @param state The board state to score
     */
    @Override
    public void score(BoardState state) {
        Set<Point> goals = state.getGoals();
        Set<Point> boxes = state.getBoxes();

        // Boxes on a goal are cost 0 so don't check them
        Set<Point> boxesOnGoals = new HashSet<>(goals);
        boxesOnGoals.retainAll(boxes);

        Set<Point> remainingGoals = new HashSet<>(goals);
        remainingGoals.removeAll(boxesOnGoals);

        Set<Point> remainingBoxes = new HashSet<>(boxes);
        remainingBoxes.removeAll(boxesOnGoals);

        // If no remaining boxes or goals, cost is 0
        if (remainingBoxes.isEmpty() || remainingGoals.isEmpty()) {
            state.setCost(0);
            return;
        }

        // Calculate optimal assignment of boxes to goals
        int cost = calculateOptimalAssignment(remainingBoxes, remainingGoals);

        // Add penalty for box-box interactions
        cost += calculateBoxInteractionPenalty(remainingBoxes, state);

        state.setCost(cost);
    }

    /**
     * Calculates the optimal assignment of boxes to goals using a greedy approach.
     *
     * @param boxes The set of boxes
     * @param goals The set of goals
     * @return The total Manhattan distance
     */
    private int calculateOptimalAssignment(Set<Point> boxes, Set<Point> goals) {
        // Convert sets to lists for easier manipulation
        List<Point> boxList = new ArrayList<>(boxes);
        List<Point> goalList = new ArrayList<>(goals);

        // Calculate all distances
        int[][] distances = new int[boxList.size()][goalList.size()];
        for (int i = 0; i < boxList.size(); i++) {
            for (int j = 0; j < goalList.size(); j++) {
                distances[i][j] = getManhattanDistance(boxList.get(i), goalList.get(j));
            }
        }

        // Greedy assignment - assign each box to its closest goal
        int totalDistance = 0;
        boolean[] assignedGoals = new boolean[goalList.size()];

        // Sort boxes by minimum distance to any goal
        List<BoxDistancePair> boxDistances = new ArrayList<>();
        for (int i = 0; i < boxList.size(); i++) {
            int minDist = Integer.MAX_VALUE;
            for (int j = 0; j < goalList.size(); j++) {
                minDist = Math.min(minDist, distances[i][j]);
            }
            boxDistances.add(new BoxDistancePair(i, minDist));
        }

        // Sort by minimum distance (ascending)
        Collections.sort(boxDistances);

        // Assign boxes to goals
        for (BoxDistancePair pair : boxDistances) {
            int boxIndex = pair.boxIndex;

            // Find closest unassigned goal
            int closestGoal = -1;
            int minDist = Integer.MAX_VALUE;
            for (int j = 0; j < goalList.size(); j++) {
                if (!assignedGoals[j] && distances[boxIndex][j] < minDist) {
                    minDist = distances[boxIndex][j];
                    closestGoal = j;
                }
            }

            if (closestGoal != -1) {
                assignedGoals[closestGoal] = true;
                totalDistance += minDist;
            }
        }

        return totalDistance;
    }

    /**
     * Calculates a penalty for box-box interactions.
     * Boxes that are adjacent to each other are harder to move.
     *
     * @param boxes The set of boxes
     * @param state The board state
     * @return The interaction penalty
     */
    private int calculateBoxInteractionPenalty(Set<Point> boxes, BoardState state) {
        int penalty = 0;

        for (Point box : boxes) {
            // Check adjacent positions for other boxes
            Point[] adjacentPositions = {
                    new Point(box.x - 1, box.y),
                    new Point(box.x + 1, box.y),
                    new Point(box.x, box.y - 1),
                    new Point(box.x, box.y + 1)
            };

            for (Point adj : adjacentPositions) {
                if (boxes.contains(adj)) {
                    penalty += 2; // Penalty for adjacent boxes
                }
            }

            // Check if box is against a wall
            boolean wallUp = state.pointHas(new Point(box.x - 1, box.y), BoardState.WALL);
            boolean wallDown = state.pointHas(new Point(box.x + 1, box.y), BoardState.WALL);
            boolean wallLeft = state.pointHas(new Point(box.x, box.y - 1), BoardState.WALL);
            boolean wallRight = state.pointHas(new Point(box.x, box.y + 1), BoardState.WALL);

            if (wallUp || wallDown || wallLeft || wallRight) {
                penalty += 1; // Penalty for being against a wall
            }
        }

        return penalty;
    }

    /**
     * Calculates the Manhattan distance between two points.
     *
     * @param p1 The first point
     * @param p2 The second point
     * @return The Manhattan distance
     */
    private static int getManhattanDistance(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    /**
     * Helper class to store box index and its minimum distance to any goal.
     */
    private static class BoxDistancePair implements Comparable<BoxDistancePair> {
        int boxIndex;
        int minDistance;

        public BoxDistancePair(int boxIndex, int minDistance) {
            this.boxIndex = boxIndex;
            this.minDistance = minDistance;
        }

        @Override
        public int compareTo(BoxDistancePair other) {
            return Integer.compare(this.minDistance, other.minDistance);
        }
    }
}
