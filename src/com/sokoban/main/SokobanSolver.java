package com.sokoban.main;

import java.io.IOException;

import com.sokoban.heuristic.BoxGoalHeuristic;
import com.sokoban.heuristic.ImprovedManhattanHeuristic;
import com.sokoban.heuristic.ManhattanHeuristic;
import com.sokoban.model.BoardState;
import com.sokoban.model.NoSolutionException;
import com.sokoban.solver.AStarSolver;
import com.sokoban.solver.AbstractSolver;
import com.sokoban.solver.BFSSolver;
import com.sokoban.solver.DFSSolver;
import com.sokoban.solver.GreedyBFSSolver;
import com.sokoban.solver.UniformCostSolver;

/**
 * Command line interface for solving Sokoban with:
 * - BFS
 * - DFS
 * - Uniform cost search
 * - Greedy best first search
 * - A* search
 *
 * Enhanced with:
 * - Deadlock detection
 * - Timeout mechanism
 * - Improved heuristics
 * - Tunnel macro moves
 */
public class SokobanSolver {
    public static void parseArguments(String[] args) {
        try {
            if (args.length < 2) {
                printUsage();
                return;
            }

            String flag = args[0];
            String puzzlePath = args[1];
            BoardState initialBoard = BoardState.parseBoardInput(puzzlePath);
            AbstractSolver solver = null;
            System.out.println(initialBoard);

            if (flag.equals("-b")) {
                solver = new BFSSolver(initialBoard);
            } else if (flag.equals("-d")) {
                solver = new DFSSolver(initialBoard);
            } else if (flag.equals("-u")) {
                solver = new UniformCostSolver(initialBoard);
            } else if (flag.equals("-ab")) {
                solver = new AStarSolver(initialBoard, new BoxGoalHeuristic());
            } else if (flag.equals("-gb")) {
                solver = new GreedyBFSSolver(initialBoard, new BoxGoalHeuristic());
            } else if (flag.equals("-am")) {
                solver = new AStarSolver(initialBoard, new ManhattanHeuristic());
            } else if (flag.equals("-gm")) {
                solver = new GreedyBFSSolver(initialBoard, new ManhattanHeuristic());
            } else if (flag.equals("-ai")) {
                solver = new AStarSolver(initialBoard, new ImprovedManhattanHeuristic());
            } else if (flag.equals("-gi")) {
                solver = new GreedyBFSSolver(initialBoard, new ImprovedManhattanHeuristic());
            } else {
                System.out.println("Invalid command");
                printUsage();
                return;
            }

            if (solver != null) {
                // Set timeout (default 30 seconds, can be overridden with -t flag)
                long timeout = 30000;
                if (args.length > 2 && args[2].equals("-t") && args.length > 3) {
                    try {
                        timeout = Long.parseLong(args[3]) * 1000; // Convert seconds to milliseconds
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid timeout value, using default 30 seconds");
                    }
                }
                solver.setTimeout(timeout);

                try {
                    String solution = solver.search();
                    int nodesExplored = solver.getNodesExplored();
                    int previouslySeen = solver.getPreviouslySeen();
                    int queueLength = solver.getFringeLength();
                    int visitedLength = solver.getVisitedLength();
                    long timeElapsed = solver.getElapsedTimeMillis();
                    System.out.println("Solution: " + solution);
                    System.out.println("Nodes explored: " + nodesExplored);
                    System.out.println("Previously seen: " + previouslySeen);
                    System.out.println("Fringe: " + queueLength);
                    System.out.println("Explored set: " + visitedLength);
                    System.out.println("Millis elapsed: " + timeElapsed);
                } catch (NoSolutionException e) {
                    if (e.getMessage() != null && e.getMessage().contains("timed out")) {
                        System.out.println(e.getMessage());
                    } else {
                        System.out.println("Solution does not exist");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Puzzle file not found");
        }
    }

    /**
     * Prints usage information.
     */
    private static void printUsage() {
        System.out.println("Usage: java -jar SokobanSolver.jar [-option] [Sokoban input file] [-t timeout]");
        System.out.println("Options:");
        System.out.println("  -b      Breadth-first search");
        System.out.println("  -d      Depth-first search");
        System.out.println("  -u      Uniform-cost search (move = 1, push = 2)");
        System.out.println("  -gb     Greedy best-first search with number of boxes on goal heuristic");
        System.out.println("  -gm     Greedy best-first search with Manhattan distances heuristic");
        System.out.println("  -gi     Greedy best-first search with improved Manhattan heuristic");
        System.out.println("  -ab     AStar with number of boxes on goal heuristic");
        System.out.println("  -am     AStar with goals and boxes Manhattan distances heuristic");
        System.out.println("  -ai     AStar with improved Manhattan heuristic");
        System.out.println("Optional timeout parameter:");
        System.out.println("  -t      Followed by timeout in seconds (default: 30)");
        System.out.println("Example:");
        System.out.println("  java -jar SokobanSolver.jar -ai puzzle.txt -t 15");
    }
}
