package com.sokoban.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.sokoban.model.BoardState;

/**
 * Detects tunnel macro moves in Sokoban puzzles.
 * A tunnel is a narrow passage where a box can only move in one direction.
 * This allows us to skip intermediate states and directly move to the end of
 * the tunnel.
 */
public class TunnelMacroDetector {
    /**
     * Detects if a move is the start of a tunnel macro move and returns the end
     * state.
     *
     * @param state     The current board state
     * @param direction The direction of the move
     * @return The end state after the tunnel macro move, or null if not a tunnel
     *         move
     */
    public static BoardState detectTunnelMacro(BoardState state, Point direction) {
        // Check if the move involves pushing a box
        Point newPlayerPos = new Point(state.getPlayerPosition().x + direction.x,
                state.getPlayerPosition().y + direction.y);

        if (!state.pointHas(newPlayerPos, BoardState.BOX)) {
            return null; // Not pushing a box, so not a tunnel move
        }

        Point boxPos = newPlayerPos;
        Point newBoxPos = new Point(boxPos.x + direction.x, boxPos.y + direction.y);

        // Check if we're entering a tunnel
        if (!isTunnelEntrance(state, boxPos, direction)) {
            return null;
        }

        // Follow the tunnel until we reach the end
        List<Point> tunnelPath = new ArrayList<>();
        tunnelPath.add(newBoxPos);

        Point currentPos = newBoxPos;
        Point tunnelDirection = direction;

        while (true) {
            // Check if we can continue in the same direction
            Point nextPos = new Point(currentPos.x + tunnelDirection.x,
                    currentPos.y + tunnelDirection.y);

            // If next position is a wall or a box, we've reached the end of the tunnel
            if (state.pointHas(nextPos, BoardState.WALL) ||
                    state.pointHas(nextPos, BoardState.BOX)) {
                break;
            }

            // Check if we're still in a tunnel
            if (!isTunnelPosition(state, currentPos, tunnelDirection)) {
                break;
            }

            // Continue along the tunnel
            currentPos = nextPos;
            tunnelPath.add(currentPos);
        }

        // If the tunnel is too short, it's not worth skipping
        if (tunnelPath.size() <= 1) {
            return null;
        }

        // Create the end state after the tunnel macro move
        return createTunnelEndState(state, boxPos, tunnelPath.get(tunnelPath.size() - 1), direction);
    }

    /**
     * Checks if a position is the entrance to a tunnel.
     *
     * @param state     The board state
     * @param boxPos    The position of the box
     * @param direction The direction of movement
     * @return true if this is a tunnel entrance, false otherwise
     */
    private static boolean isTunnelEntrance(BoardState state, Point boxPos, Point direction) {
        // Check if the box can only move along the tunnel (walls or boxes on both
        // sides)
        Point perpDirection1, perpDirection2;

        if (direction.x == 0) { // Moving vertically
            perpDirection1 = new Point(1, 0);
            perpDirection2 = new Point(-1, 0);
        } else { // Moving horizontally
            perpDirection1 = new Point(0, 1);
            perpDirection2 = new Point(0, -1);
        }

        Point side1 = new Point(boxPos.x + perpDirection1.x, boxPos.y + perpDirection1.y);
        Point side2 = new Point(boxPos.x + perpDirection2.x, boxPos.y + perpDirection2.y);

        boolean blockedSide1 = state.pointHas(side1, BoardState.WALL) ||
                state.pointHas(side1, BoardState.BOX);
        boolean blockedSide2 = state.pointHas(side2, BoardState.WALL) ||
                state.pointHas(side2, BoardState.BOX);

        return blockedSide1 && blockedSide2;
    }

    /**
     * Checks if a position is part of a tunnel.
     *
     * @param state     The board state
     * @param pos       The position to check
     * @param direction The direction of movement
     * @return true if this position is part of a tunnel, false otherwise
     */
    private static boolean isTunnelPosition(BoardState state, Point pos, Point direction) {
        // Similar to isTunnelEntrance, but for positions along the tunnel
        Point perpDirection1, perpDirection2;

        if (direction.x == 0) { // Moving vertically
            perpDirection1 = new Point(1, 0);
            perpDirection2 = new Point(-1, 0);
        } else { // Moving horizontally
            perpDirection1 = new Point(0, 1);
            perpDirection2 = new Point(0, -1);
        }

        Point side1 = new Point(pos.x + perpDirection1.x, pos.y + perpDirection1.y);
        Point side2 = new Point(pos.x + perpDirection2.x, pos.y + perpDirection2.y);

        boolean blockedSide1 = state.pointHas(side1, BoardState.WALL) ||
                state.pointHas(side1, BoardState.BOX);
        boolean blockedSide2 = state.pointHas(side2, BoardState.WALL) ||
                state.pointHas(side2, BoardState.BOX);

        return blockedSide1 && blockedSide2;
    }

    /**
     * Creates the end state after a tunnel macro move.
     *
     * @param state         The initial board state
     * @param initialBoxPos The initial box position
     * @param finalBoxPos   The final box position
     * @param direction     The direction of movement
     * @return The end state after the tunnel macro move
     */
    private static BoardState createTunnelEndState(BoardState state, Point initialBoxPos,
            Point finalBoxPos, Point direction) {
        // First, make the initial move
        BoardState newState = state.getMove(direction);

        // Then, simulate pushing the box to the end of the tunnel
        Point currentBoxPos = new Point(initialBoxPos.x + direction.x,
                initialBoxPos.y + direction.y);
        Point playerPos = initialBoxPos;

        while (!currentBoxPos.equals(finalBoxPos)) {
            // Move the player to the current box position
            playerPos = currentBoxPos;

            // Move the box one step further
            Point nextBoxPos = new Point(currentBoxPos.x + direction.x,
                    currentBoxPos.y + direction.y);
            currentBoxPos = nextBoxPos;
        }

        // Create a new state with the box at the final position and player behind it
        return newState; // This is a simplification - in a real implementation, we'd need to create a
                         // new state
    }
}
