# Sokoban Solver Test Cases

## Test Case 1: Simple Puzzle
**File:** TestCase1_Simple.txt
**Description:** A simple Sokoban puzzle with a straightforward solution.
**Expected Behavior:** All search algorithms should find a solution quickly.
**Purpose:** Verifies basic functionality of the solver.

**Test Steps:**
1. Run with BFS: `java SokobanMain -b ../test/TestCase1_Simple.txt`
2. Run with DFS: `java SokobanMain -d ../test/TestCase1_Simple.txt`
3. Run with A* (Manhattan): `java SokobanMain -am ../test/TestCase1_Simple.txt`

**Expected Results:**
- All algorithms should find a solution
- BFS should find the shortest solution in terms of moves
- A* with Manhattan heuristic should explore fewer nodes than BFS

## Test Case 2: Unsolvable Puzzle
**File:** TestCase2_Unsolvable.txt
**Description:** A Sokoban puzzle that has no solution (boxes are arranged in a way that makes it impossible to solve).
**Expected Behavior:** All search algorithms should correctly identify that no solution exists.
**Purpose:** Tests the solver's ability to handle unsolvable puzzles and terminate appropriately.

**Test Steps:**
1. Run with BFS: `java SokobanMain -b ../test/TestCase2_Unsolvable.txt`
2. Run with A* (Manhattan): `java SokobanMain -am ../test/TestCase2_Unsolvable.txt`

**Expected Results:**
- Both algorithms should report "Solution does not exist"
- The program should terminate gracefully without errors

## Test Case 3: Complex Puzzle
**File:** TestCase3_Complex.txt
**Description:** A more complex Sokoban puzzle with multiple boxes and goals.
**Expected Behavior:** Different search algorithms should exhibit different performance characteristics.
**Purpose:** Tests the solver's performance and efficiency with more complex puzzles.

**Test Steps:**
1. Run with BFS: `java SokobanMain -b ../test/TestCase3_Complex.txt`
2. Run with A* (Manhattan): `java SokobanMain -am ../test/TestCase3_Complex.txt`
3. Run with Greedy BFS (Manhattan): `java SokobanMain -gm ../test/TestCase3_Complex.txt`

**Expected Results:**
- A* should explore significantly fewer nodes than BFS
- A* should find a more optimal solution than Greedy BFS
- BFS might take much longer or run out of memory

## Test Case 4: Already Solved Puzzle
**File:** TestCase4_AlreadySolved.txt
**Description:** A Sokoban puzzle that is already in a solved state (all boxes are on goals).
**Expected Behavior:** All search algorithms should immediately recognize the solved state.
**Purpose:** Tests edge case handling for pre-solved puzzles.

**Test Steps:**
1. Run with BFS: `java SokobanMain -b ../test/TestCase4_AlreadySolved.txt`

**Expected Results:**
- The algorithm should immediately recognize the solved state
- The solution should be empty (no moves required)
- Nodes explored should be minimal (ideally just 1)

## Test Case 5: Edge Case with Limited Movement
**File:** TestCase5_EdgeCase.txt
**Description:** A small puzzle with limited movement options, testing edge cases in the movement logic.
**Expected Behavior:** The solver should correctly handle the constrained movement options.
**Purpose:** Tests the solver's ability to handle edge cases in movement and board configuration.

**Test Steps:**
1. Run with BFS: `java SokobanMain -b ../test/TestCase5_EdgeCase.txt`
2. Run with DFS: `java SokobanMain -d ../test/TestCase5_EdgeCase.txt`

**Expected Results:**
- Both algorithms should find a solution
- The solution should correctly navigate the constrained space
- No errors should occur related to boundary conditions

## Test Execution Script

To run all test cases automatically, you can use the following script:

```bash
#!/bin/bash
echo "Running Test Case 1 (Simple Puzzle)"
java SokobanMain -b ../test/TestCase1_Simple.txt
java SokobanMain -d ../test/TestCase1_Simple.txt
java SokobanMain -am ../test/TestCase1_Simple.txt

echo "Running Test Case 2 (Unsolvable Puzzle)"
java SokobanMain -b ../test/TestCase2_Unsolvable.txt
java SokobanMain -am ../test/TestCase2_Unsolvable.txt

echo "Running Test Case 3 (Complex Puzzle)"
java SokobanMain -am ../test/TestCase3_Complex.txt
java SokobanMain -gm ../test/TestCase3_Complex.txt

echo "Running Test Case 4 (Already Solved Puzzle)"
java SokobanMain -b ../test/TestCase4_AlreadySolved.txt

echo "Running Test Case 5 (Edge Case)"
java SokobanMain -b ../test/TestCase5_EdgeCase.txt
java SokobanMain -d ../test/TestCase5_EdgeCase.txt
```
