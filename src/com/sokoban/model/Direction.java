package com.sokoban.model;

import java.awt.Point;

/**
 * Wrapper class for Sokoban board movements.
 * Provides constants and utility methods for directions.
 */
public class Direction {
    public static final Point UP = new Point(-1, 0);
    public static final Point RIGHT = new Point(0, 1);
    public static final Point DOWN = new Point(1, 0);
    public static final Point LEFT = new Point(0, -1);

    // Private constructor to prevent instantiation
    private Direction() {
    }

    /**
     * Converts a direction point to its character representation.
     *
     * @param direction The direction to translate
     * @return The corresponding character mapping (u, r, d, l)
     * @throws IllegalStateException if the direction is invalid
     */
    public static char directionToChar(Point direction) {
        if (direction.equals(UP))
            return 'u';
        else if (direction.equals(RIGHT))
            return 'r';
        else if (direction.equals(DOWN))
            return 'd';
        else if (direction.equals(LEFT))
            return 'l';
        else
            throw new IllegalStateException("Non-existent direction: " + direction);
    }
}
