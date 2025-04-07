package com.sokoban.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import com.sokoban.model.BoardState;
import com.sokoban.model.Direction;
import com.sokoban.model.NoSolutionException;
import com.sokoban.util.DeadlockDetector;

/**
 * Abstract solver class with base search functionality.
 * Provides a template for different search algorithms.
 */
public abstract class AbstractSolver {
    protected BoardState currentState;
    protected HashSet<BoardState> visited;
    protected HashMap<BoardState, BoardState> backtrack;
    protected Queue<BoardState> queue;

    private long startTime;
    private long endTime;
    private int previouslySeen;

    // Timeout in milliseconds (default: 30 seconds)
    private long timeoutMillis = 30000;

    /**
     * Constructs a new AbstractSolver with the given initial state.
     *
     * @param initialState The initial board state
     */
    public AbstractSolver(BoardState initialState) {
        currentState = initialState;
        visited = new HashSet<BoardState>();
        backtrack = new HashMap<BoardState, BoardState>();
        startTime = endTime = -1;
        previouslySeen = 0;
    }

    /**
     * Sets the timeout for the search in milliseconds.
     *
     * @param timeoutMillis The timeout in milliseconds
     */
    public void setTimeout(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    /**
     * Searches for a solution to the Sokoban puzzle.
     *
     * @return The solution as a string of moves
     * @throws NoSolutionException If no solution is found or the search times out
     */
    public String search() throws NoSolutionException {
        startTimer();
        searchStart();
        while (!queue.isEmpty()) {
            // Check for timeout
            if (System.currentTimeMillis() - startTime > timeoutMillis) {
                throw new NoSolutionException("Search timed out after " + timeoutMillis + " milliseconds");
            }

            currentState = queue.poll();
            if (visited.contains(currentState))
                previouslySeen++;
            visited.add(currentState);

            if (currentState.isSolved()) {
                System.out.println(currentState);
                String solution = backtrackMoves(currentState);
                stopTimer();
                return solution;
            }

            // Check for deadlocks
            if (DeadlockDetector.hasDeadlock(currentState)) {
                continue;
            }

            ArrayList<BoardState> validMoves = getValidMoves();
            searchFunction(validMoves);
        }
        throw new NoSolutionException();
    }

    /**
     * Initializes the search.
     * Can be overridden by subclasses to provide specialized initialization.
     */
    protected void searchStart() {
        queue.add(currentState);
    }

    /**
     * Processes valid moves according to the specific search algorithm.
     * Must be implemented by subclasses.
     *
     * @param validMoves List of valid moves
     */
    protected abstract void searchFunction(ArrayList<BoardState> validMoves);

    /**
     * Gets the valid moves from the current state.
     *
     * @return List of valid moves
     */
    protected ArrayList<BoardState> getValidMoves() {
        ArrayList<BoardState> validMoves = new ArrayList<BoardState>(4);
        addIfValid(validMoves, Direction.UP);
        addIfValid(validMoves, Direction.RIGHT);
        addIfValid(validMoves, Direction.DOWN);
        addIfValid(validMoves, Direction.LEFT);
        return validMoves;
    }

    /**
     * Adds a move to the valid moves list if it's valid.
     *
     * @param validMoves List of valid moves
     * @param direction  The direction to check
     */
    private void addIfValid(ArrayList<BoardState> validMoves, java.awt.Point direction) {
        if (currentState.canMove(direction)) {
            BoardState newState = currentState.getMove(direction);
            if (!visited.contains(newState))
                validMoves.add(newState);
        }
    }

    /**
     * Backtracks through the search to find the move sequence.
     *
     * @param finalState The final, goal state
     * @return The solution as a string of moves
     */
    protected String backtrackMoves(BoardState finalState) {
        // Backtracking solutions and adding moves to stack
        LinkedList<Character> moveStack = new LinkedList<Character>();
        BoardState current = finalState;
        while (current.getDirectionTaken() != null) {
            char move = Direction.directionToChar(current.getDirectionTaken());
            moveStack.push(move);
            current = backtrack.get(current);
        }

        // Comma delimiting solution
        StringBuilder solution = new StringBuilder();
        String delim = "";
        for (Character move : moveStack) {
            solution.append(delim);
            solution.append(move);
            delim = ", ";
        }
        return solution.toString();
    }

    /**
     * Starts the timer for measuring search time.
     */
    private void startTimer() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Stops the timer for measuring search time.
     */
    private void stopTimer() {
        endTime = System.currentTimeMillis();
    }

    /**
     * Gets the number of nodes explored during the search.
     *
     * @return The number of nodes explored
     */
    public int getNodesExplored() {
        return visited.size();
    }

    /**
     * Gets the number of previously seen nodes during the search.
     *
     * @return The number of previously seen nodes
     */
    public int getPreviouslySeen() {
        return previouslySeen;
    }

    /**
     * Gets the length of the fringe (queue).
     *
     * @return The length of the fringe
     */
    public int getFringeLength() {
        return queue.size();
    }

    /**
     * Gets the length of the visited set.
     *
     * @return The length of the visited set
     */
    public int getVisitedLength() {
        return visited.size();
    }

    /**
     * Gets the elapsed time in milliseconds.
     *
     * @return The elapsed time in milliseconds
     */
    public long getElapsedTimeMillis() {
        return endTime - startTime;
    }
}
