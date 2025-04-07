# Sokoban Solver

A Java application that implements various search algorithms to solve Sokoban puzzles.

## Features

- Multiple search algorithms:
  - Breadth-First Search (BFS)
  - Depth-First Search (DFS)
  - Uniform Cost Search
  - Greedy Best-First Search
  - A* Search

- Advanced features:
  - Deadlock detection
  - Timeout mechanism
  - Multiple heuristic functions
  - Tunnel macro moves

## Project Structure

The project is organized into the following packages:

- `com.sokoban.main`: Main application classes
- `com.sokoban.model`: Data model classes
- `com.sokoban.solver`: Solver algorithm classes
- `com.sokoban.heuristic`: Heuristic classes
- `com.sokoban.util`: Utility classes

## Building the Project

To build the project, run the build script:

```bash
chmod +x build.sh
./build.sh
```

This will compile the source files and create a JAR file.

## Usage

```bash
java -jar SokobanSolver.jar [-option] [Sokoban input file] [-t timeout]
```

### Options

- `-b`: Breadth-first search
- `-d`: Depth-first search
- `-u`: Uniform-cost search (move = 1, push = 2)
- `-gb`: Greedy best-first search with number of boxes on goal heuristic
- `-gm`: Greedy best-first search with Manhattan distances heuristic
- `-gi`: Greedy best-first search with improved Manhattan heuristic
- `-ab`: A* with number of boxes on goal heuristic
- `-am`: A* with goals and boxes Manhattan distances heuristic
- `-ai`: A* with improved Manhattan heuristic

### Optional timeout parameter

- `-t`: Followed by timeout in seconds (default: 30)

### Example

```bash
java -jar SokobanSolver.jar -ai resource/Level1.txt -t 15
```

## Input Format

The Sokoban files must be in the following format:

```
[Number of columns]
[Number of rows]
[Rest of the puzzle]
```

With the following state mappings:

- `#` (hash): Wall (Obstacle)
- `.` (period): Empty goal (Storage)
- `@` (at): Player on floor (Robot)
- `+` (plus): Player on goal
- `$` (dollar): Box on floor (Block)
- `*` (asterisk): Box on goal

## Output

The output includes:

1. String representation of initial state
2. String representation of the final state
3. Move solution
4. Number of nodes explored
5. Number of previously seen nodes
6. Number of nodes at the fringe
7. Number of explored nodes
8. Time elapsed in milliseconds
