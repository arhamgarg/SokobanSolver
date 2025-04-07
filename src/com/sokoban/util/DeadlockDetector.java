package com.sokoban.util;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import com.sokoban.model.BoardState;

/**
 * Detects various types of deadlocks in Sokoban puzzles.
 * A deadlock is a state from which the puzzle cannot be solved.
 */
public class DeadlockDetector {
    /**
     * Checks if the current board state contains any deadlocks.
     *
     * @param state The board state to check
     * @return true if a deadlock is detected, false otherwise
     */
    public static boolean hasDeadlock(BoardState state) {
        return hasCornerDeadlock(state) || hasFreezeDeadlock(state);
    }

    /**
     * Detects corner deadlocks - boxes pushed into corners that aren't goals.
     *
     * @param state The board state to check
     * @return true if a corner deadlock is detected, false otherwise
     */
    private static boolean hasCornerDeadlock(BoardState state) {
        Set<Point> boxes = state.getBoxes();
        Set<Point> goals = state.getGoals();

        for (Point box : boxes) {
            // Skip boxes that are already on goals
            if (goals.contains(box)) {
                continue;
            }

            // Check if box is in a corner (two adjacent walls)
            boolean wallUp = state.pointHas(new Point(box.x - 1, box.y), BoardState.WALL);
            boolean wallDown = state.pointHas(new Point(box.x + 1, box.y), BoardState.WALL);
            boolean wallLeft = state.pointHas(new Point(box.x, box.y - 1), BoardState.WALL);
            boolean wallRight = state.pointHas(new Point(box.x, box.y + 1), BoardState.WALL);

            if ((wallUp && wallLeft) || (wallUp && wallRight) ||
                    (wallDown && wallLeft) || (wallDown && wallRight)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Detects freeze deadlocks - boxes that are frozen against walls or other boxes
     * in a way that they can never be moved to goals.
     *
     * @param state The board state to check
     * @return true if a freeze deadlock is detected, false otherwise
     */
    private static boolean hasFreezeDeadlock(BoardState state) {
        Set<Point> boxes = state.getBoxes();
        Set<Point> goals = state.getGoals();
        Set<Point> frozenBoxes = new HashSet<>();

        // First pass: identify boxes that are against walls
        for (Point box : boxes) {
            // Skip boxes that are already on goals
            if (goals.contains(box)) {
                continue;
            }

            // Check if box is against a wall
            boolean wallUp = state.pointHas(new Point(box.x - 1, box.y), BoardState.WALL);
            boolean wallDown = state.pointHas(new Point(box.x + 1, box.y), BoardState.WALL);
            boolean wallLeft = state.pointHas(new Point(box.x, box.y - 1), BoardState.WALL);
            boolean wallRight = state.pointHas(new Point(box.x, box.y + 1), BoardState.WALL);

            // If box is against a wall and another box or wall in perpendicular direction
            if (wallUp || wallDown) {
                boolean boxOrWallLeft = wallLeft || boxes.contains(new Point(box.x, box.y - 1));
                boolean boxOrWallRight = wallRight || boxes.contains(new Point(box.x, box.y + 1));

                if (boxOrWallLeft && boxOrWallRight) {
                    frozenBoxes.add(box);
                }
            }

            if (wallLeft || wallRight) {
                boolean boxOrWallUp = wallUp || boxes.contains(new Point(box.x - 1, box.y));
                boolean boxOrWallDown = wallDown || boxes.contains(new Point(box.x + 1, box.y));

                if (boxOrWallUp && boxOrWallDown) {
                    frozenBoxes.add(box);
                }
            }
        }

        // If any box is frozen and not on a goal, it's a deadlock
        return !frozenBoxes.isEmpty();
    }
}
