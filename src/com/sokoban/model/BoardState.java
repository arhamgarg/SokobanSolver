package com.sokoban.model;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a single Sokoban board state.
 * This class is immutable - once created, its state cannot be changed.
 * New BoardState objects are created for each move.
 */
public class BoardState implements Comparable<BoardState> {
    // Board position bitfields
    public static final byte PLAYER = 1 << 0;
    public static final byte WALL = 1 << 1;
    public static final byte BOX = 1 << 2;
    public static final byte GOAL = 1 << 3;

    // Character to bitfield mapping
    private static HashMap<Character, Byte> charToField;
    private static HashMap<Byte, Character> fieldToChar;

    static {
        charToField = new HashMap<Character, Byte>();
        charToField.put('#', WALL);
        charToField.put('.', GOAL);
        charToField.put('@', PLAYER);
        charToField.put('+', (byte) (PLAYER | GOAL));
        charToField.put('$', BOX);
        charToField.put('*', (byte) (BOX | GOAL));
        charToField.put(' ', (byte) 0);

        fieldToChar = new HashMap<Byte, Character>();
        fieldToChar.put(WALL, '#');
        fieldToChar.put(GOAL, '.');
        fieldToChar.put(PLAYER, '@');
        fieldToChar.put((byte) (PLAYER | GOAL), '+');
        fieldToChar.put(BOX, '$');
        fieldToChar.put((byte) (BOX | GOAL), '*');
        fieldToChar.put((byte) 0, ' ');
    }

    // Private fields
    private byte[][] board;
    private Point player;
    private Set<Point> boxes;
    private Set<Point> goals;
    private Point directionTaken;
    private int cost;
    private int depth; // Depth in the search tree

    /**
     * Constructor for BoardState.
     *
     * @param board          The board state
     * @param player         The player position
     * @param boxes          The box positions
     * @param goals          The goal positions
     * @param directionTaken The direction taken to get to this state
     */
    public BoardState(byte[][] board, Point player, Set<Point> boxes,
            Set<Point> goals, Point directionTaken) {
        this.board = board;
        this.player = player;
        this.boxes = boxes;
        this.goals = goals;
        this.directionTaken = directionTaken;
        this.cost = 0;
        this.depth = 0;
    }

    /**
     * Constructor that also sets the depth.
     *
     * @param board          The board state
     * @param player         The player position
     * @param boxes          The box positions
     * @param goals          The goal positions
     * @param directionTaken The direction taken to get to this state
     * @param depth          The depth in the search tree
     */
    public BoardState(byte[][] board, Point player, Set<Point> boxes,
            Set<Point> goals, Point directionTaken, int depth) {
        this(board, player, boxes, goals, directionTaken);
        this.depth = depth;
    }

    /**
     * Parses a Sokoban text file into a BoardState object.
     *
     * @param boardInput The path to the Sokoban text file
     * @return The parsed BoardState
     * @throws IOException If the file cannot be read
     */
    public static BoardState parseBoardInput(String boardInput) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(boardInput));
        int width = Integer.parseInt(reader.readLine());
        int height = Integer.parseInt(reader.readLine());
        byte[][] boardPoints = new byte[height][width];
        Point player = new Point();
        Set<Point> goals = new HashSet<Point>();
        Set<Point> boxes = new HashSet<Point>();

        for (int row = 0; row < height; row++) {
            String line = reader.readLine();
            for (int col = 0; col < width; col++) {
                char c = col < line.length() ? line.charAt(col) : ' ';
                byte field = charToField.get(c);
                boardPoints[row][col] = field;
                if ((field & PLAYER) != 0) {
                    player.x = row;
                    player.y = col;
                }
                if ((field & BOX) != 0) {
                    boxes.add(new Point(row, col));
                }
                if ((field & GOAL) != 0) {
                    goals.add(new Point(row, col));
                }
            }
        }
        reader.close();
        return new BoardState(boardPoints, player, boxes, goals, null);
    }

    /**
     * Checks if the player can move in a certain direction.
     *
     * @param direction The direction to check
     * @return True if the player can move in that direction, false otherwise
     */
    public boolean canMove(Point direction) {
        Point newPos = new Point(player.x + direction.x, player.y + direction.y);
        Point oneOutPos = new Point(newPos.x + direction.x, newPos.y + direction.y);
        if (pointHas(newPos, BOX)) {
            // Box can't be pushed if there's a wall or box
            if (pointHas(oneOutPos, WALL) || pointHas(oneOutPos, BOX))
                return false;
            // Goal or empty
            else
                return true;
        }
        // Can't move into a wall
        else if (pointHas(newPos, WALL))
            return false;
        // Goal or empty
        else
            return true;
    }

    /**
     * Returns a new BoardState after moving in a certain direction.
     *
     * @param direction The direction to move
     * @return The new BoardState
     */
    public BoardState getMove(Point direction) {
        Point newPos = new Point(player.x + direction.x, player.y + direction.y);
        Point oneOutPos = new Point(newPos.x + direction.x, newPos.y + direction.y);
        Set<Point> newBoxes = boxes;

        // Deep copy board
        byte[][] newBoard = new byte[board.length][];
        for (int i = 0; i < newBoard.length; i++)
            newBoard[i] = board[i].clone();

        // Update player position
        newBoard[player.x][player.y] &= ~PLAYER;
        newBoard[newPos.x][newPos.y] |= PLAYER;

        // If pushing a box, update box position
        if (pointHas(newPos, BOX)) {
            newBoard[newPos.x][newPos.y] &= ~BOX;
            newBoard[oneOutPos.x][oneOutPos.y] |= BOX;

            // Update box set
            newBoxes = new HashSet<Point>(boxes);
            newBoxes.remove(newPos);
            newBoxes.add(oneOutPos);
        }

        // Create new state with incremented depth
        return new BoardState(newBoard, newPos, newBoxes, goals, direction, this.depth + 1);
    }

    /**
     * Checks if the board is in a solved state (all boxes on goals).
     *
     * @return True if the board is solved, false otherwise
     */
    public boolean isSolved() {
        for (Point box : boxes) {
            if (!goals.contains(box))
                return false;
        }
        return true;
    }

    /**
     * Checks if the next position in a direction has a certain field.
     *
     * @param field     The field to check for
     * @param direction The direction to check
     * @return True if the next position has the field, false otherwise
     */
    public boolean nextMoveHas(byte field, Point direction) {
        Point newPos = new Point(player.x + direction.x, player.y + direction.y);
        return pointHas(newPos, field);
    }

    /**
     * Checks if a point on the board has a certain field.
     *
     * @param point The point to check
     * @param field The field to check for
     * @return True if the point has the field, false otherwise
     */
    public boolean pointHas(Point point, byte field) {
        return (board[point.x][point.y] & field) != 0;
    }

    /**
     * Gets the direction taken to reach this state.
     *
     * @return The direction taken
     */
    public Point getDirectionTaken() {
        return directionTaken;
    }

    /**
     * Gets the player position.
     *
     * @return The player position
     */
    public Point getPlayerPosition() {
        return new Point(player.x, player.y);
    }

    /**
     * Gets the depth in the search tree.
     *
     * @return The depth
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Sets the depth in the search tree.
     *
     * @param depth The depth
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Sets the cost of this state.
     *
     * @param cost The cost
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * Gets the cost of this state.
     *
     * @return The cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Gets a copy of the goals set.
     *
     * @return A copy of the goals set
     */
    public Set<Point> getGoals() {
        return new HashSet<Point>(goals);
    }

    /**
     * Gets a copy of the boxes set.
     *
     * @return A copy of the boxes set
     */
    public Set<Point> getBoxes() {
        return new HashSet<Point>(boxes);
    }

    /**
     * Compares this BoardState to another based on cost.
     *
     * @param other The other BoardState
     * @return -1 if this state has lower cost, 1 if higher, 0 if equal
     */
    @Override
    public int compareTo(BoardState other) {
        if (this.getCost() < other.getCost())
            return -1;
        else if (this.getCost() > other.getCost())
            return 1;
        else
            return 0;
    }

    /**
     * Checks if this BoardState is equal to another object.
     * Two BoardStates are equal if they have the same player position and box
     * positions.
     *
     * @param obj The object to compare to
     * @return True if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BoardState))
            return false;
        BoardState other = (BoardState) obj;
        return player.equals(other.player) && boxes.equals(other.boxes);
    }

    /**
     * Generates a hash code for this BoardState.
     *
     * @return The hash code
     */
    @Override
    public int hashCode() {
        return player.hashCode() + boxes.hashCode();
    }

    /**
     * Returns a string representation of this BoardState.
     *
     * @return A string representation of the board
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                sb.append(fieldToChar.get(board[i][j]));
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
