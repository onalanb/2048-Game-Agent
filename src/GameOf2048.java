// Baran Onalan
// 10 / 8 / 2022 (Last Edited)
// This class plays through a game of 2048 by reading from "2048_in.txt"
// It uses BFS to identify the next THREE moves that result in the highest
// score and writes the score and the path it took to "2048_out.txt"

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GameOf2048 {

    // Determines the path that results in the highest score for the next three moves using BFS.
    public static Node bestScoreUsingBFS(int[][] arr) {
        Node result = new Node (0, null, null);
        Queue<Node> queue = new LinkedList<Node>();
        Node node = new Node (0, new ArrayList<String>(), arr);
        queue.add(node);
        String[] directions = new String[]{"U", "D", "L", "R"};

        while (!queue.isEmpty()) {
            Node parent = queue.remove();
            Node child;
            // Where BFS is being limited to 3 moves.
            if (parent.nodeLevel() < 3) {
                // Where BFS adds all 4 potential directions to queue.
                for (int i = 0; i < directions.length; i++) {
                    Tuple tuple = moveAndValidate(parent.nodeArr(), directions[i]);
                    int score = tuple.getInt();
                    // Only adds to the queue when the move is valid.
                    if (score >= 0) {
                        List<String> path = parent.nodePath();
                        List<String> newPath = new ArrayList<String>(path);
                        newPath.add(directions[i]);
                        score += parent.nodeScore();
                        child = new Node(score, newPath, tuple.getArr());
                        queue.add(child);
                        // Keeps track of Node with the highest score and its path.
                        // Must be >= because parent and child can have same score, but we want the furthest path.
                        if (child.nodeScore() >= result.nodeScore()) {
                            result = child;
                        }
                    }
                }
            }
        }

        return result;
    }

    // Encapsulates the move logic in 4 sub steps:
    // Step 1: Using arrAfterShift() method, shift all numbers in X direction.
    //         This also returns whether a shift occurred or not.
    // Step 2: Using arrScoreAfterMerge() method, merge all numbers in X direction where possible.
    //         This also returns the score.
    // Step 3: Using arrAfterShift() method, shift all numbers in X direction.
    //         Shift only happens here if a merge occurred in step 2.
    // Step 4: Insert new number if move logic was valid. If invalid score is -1.
    private static Tuple moveAndValidate(int[][] arr, String directionOfShift) {
        Tuple tuple = arrAfterShift(arr, directionOfShift);
        int[][] arrResult = tuple.getArr();
        boolean shifted = tuple.getInt() == 1;
        tuple = arrScoreAfterMerge(arrResult, directionOfShift);
        arrResult = tuple.getArr();
        int score = tuple.getInt();
        tuple = arrAfterShift(arrResult, directionOfShift);
        // Move is only invalid if shift did not occur, and merge did not occur.
        if (score == 0 && shifted == false) {
            // Move is invalid.
            return new Tuple(tuple.getArr(), -1);
        } else {
            // Move is valid.
            insertNewNumber(tuple.getArr());
            return new Tuple(tuple.getArr(), score);
        }
    }

    // Inserts new number (2) in the first available position from left to right, top to bottom.
    private static int[][] insertNewNumber(int[][] arr) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (arr[i][j] == 0) {
                    arr[i][j] = 2;
                    return arr;
                }
            }
        }
        // Array is full, and you lose, or path is illegal.
        // We will never arrive here anyway since we don't insert for invalid moves.
        return arr;
    }

    // Merges and calculates the score based on the merge that occurred.
    // Input: Is the shifted array we get from arrAfterShift()
    // Input: directionOfMerge U = up, D = down, L = left, R = right
    // Output: Tuple of the resulting array and the score.
    private static Tuple arrScoreAfterMerge(int[][] arr, String directionOfMerge) {
        int[][] result = new int[4][4];
        int score = 0;
        if (directionOfMerge.equalsIgnoreCase("U")) {
            // Merge up.
            for (int i = 0; i < 4; i++) {
                int position = 0;
                for (int j = 0; j < 3; j++) {
                    if (arr[j][i] == arr[j + 1][i]) {
                        score += arr[j][i] * 2;
                        result[position][i] = arr[j][i] * 2;
                        arr[j + 1][i] = 0;
                        position++;
                    } else if (arr[j][i] != arr[j + 1][i]) {
                        result[position][i] = arr[j][i];
                        position++;
                    }
                }
                result[3][i] = arr[3][i];
            }
        } else if (directionOfMerge.equalsIgnoreCase("D")) {
            // Merge down.
            for (int i = 0; i < 4; i++) {
                int position = 3;
                for (int j = 3; j > 0; j--) {
                    if (arr[j][i] == arr[j - 1][i]) {
                        score += arr[j][i] * 2;
                        result[position][i] = arr[j][i] * 2;
                        arr[j - 1][i] = 0;
                        position--;
                    } else if (arr[j][i] != arr[j - 1][i]) {
                        result[position][i] = arr[j][i];
                        position--;
                    }
                }
                result[0][i] = arr[0][i];
            }
        } else if (directionOfMerge.equalsIgnoreCase("L")) {
            // Merge left.
            for (int i = 0; i < 4; i++) {
                int position = 0;
                for (int j = 0; j < 3; j++) {
                    if (arr[i][j] == arr[i][j + 1]) {
                        score += arr[i][j] * 2;
                        result[i][position] = arr[i][j] * 2;
                        arr[i][j + 1] = 0;
                        position++;
                    } else if (arr[i][j] != arr[i][j + 1]) {
                        result[i][position] = arr[i][j];
                        position++;
                    }
                }
                result[i][3] = arr[i][3];
            }
        } else if (directionOfMerge.equalsIgnoreCase("R")) {
            // Merge right.
            for (int i = 0; i < 4; i++) {
                int position = 3;
                for (int j = 3; j > 0; j--) {
                    if (arr[i][j] == arr[i][j - 1]) {
                        score += arr[i][j] * 2;
                        result[i][position] = arr[i][j] * 2;
                        arr[i][j - 1] = 0;
                        position--;
                    } else if (arr[i][j] != arr[i][j - 1]) {
                        result[i][position] = arr[i][j];
                        position--;
                    }
                }
                result[i][0] = arr[i][0];
            }
        } else {
            System.out.println("Not a valid direction");
        }
        return new Tuple(result, score);
    }

    // Shifts the input array.
    // Input: Is the original or merged array.
    // Input: directionOfMerge U = up, D = down, L = left, R = right
    // Output: Tuple of the resulting array and the validity of the move.
    // NOTE: The resulting array doesn't get used if the shift is invalid.
    private static Tuple arrAfterShift(int[][] arr, String directionOfShift) {
        int[][] result = new int[4][4];
        int counter = 0;
        boolean shifted = false;
        if (directionOfShift.equalsIgnoreCase("U")) {
            // Shift up.
            for (int i = 0; i < 4; i++) {
                int position = 0;
                for (int j = 0; j < 4; j++) {
                    boolean seenZero = false;
                    shifted = false;
                    if (arr[j][i] != 0) {
                        result[position][i] = arr[j][i];
                        position++;
                        if (seenZero) {
                            shifted = true;
                        }
                    } else {
                        seenZero = true;
                    }
                }
                if (shifted) {
                    counter++;
                }
            }
        } else if (directionOfShift.equalsIgnoreCase("D")) {
            // Shift down.
            for (int i = 0; i < 4; i++) {
                int position = 3;
                for (int j = 3; j >= 0; j--) {
                    boolean seenZero = false;
                    shifted = false;
                    if (arr[j][i] != 0) {
                        result[position][i] = arr[j][i];
                        position--;
                        if (seenZero) {
                            shifted = true;
                        }
                    } else {
                        seenZero = true;
                    }
                }
                if (shifted) {
                    counter++;
                }
            }
        } else if (directionOfShift.equalsIgnoreCase("L")) {
            // Shift left.
            for (int i = 0; i < 4; i++) {
                int position = 0;
                for (int j = 0; j < 4; j++) {
                    boolean seenZero = false;
                    shifted = false;
                    if (arr[i][j] != 0) {
                        result[i][position] = arr[i][j];
                        position++;
                        if (seenZero) {
                            shifted = true;
                        }
                    } else {
                        seenZero = true;
                    }
                }
                if (shifted) {
                    counter++;
                }
            }
        } else if (directionOfShift.equalsIgnoreCase("R")) {
            // Shift right.
            for (int i = 0; i < 4; i++) {
                int position = 3;
                for (int j = 3; j >= 0; j--) {
                    boolean seenZero = false;
                    shifted = false;
                    if (arr[i][j] != 0) {
                        result[i][position] = arr[i][j];
                        position--;
                        if (seenZero) {
                            shifted = true;
                        }
                    } else {
                        seenZero = true;
                    }
                }
                if (shifted) {
                    counter++;
                }
            }
        } else {
            System.out.println("Not a valid direction");
        }
        if (counter != 4) {
            // 1 is true (valid shift)
            return new Tuple(result, 1);
        } else {
            // -1 is false (potentially invalid shift)
            return new Tuple(result, -1);
        }
    }
}