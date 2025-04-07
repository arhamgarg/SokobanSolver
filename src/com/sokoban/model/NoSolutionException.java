package com.sokoban.model;

/**
 * Exception thrown when no solution exists for a Sokoban puzzle.
 */
public class NoSolutionException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a NoSolutionException with no detail message.
     */
    public NoSolutionException() {
        super();
    }

    /**
     * Constructs a NoSolutionException with the specified detail message.
     *
     * @param message The detail message
     */
    public NoSolutionException(String message) {
        super(message);
    }
}
