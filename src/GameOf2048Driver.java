// Baran Onalan
// 10 / 8 / 2022 (Last Edited)
// This is the driver class for the 2048 game that read input states from a file, "2048_in.txt"
// and writes the score and the path it took to "2048_out.txt"

import java.io.*;
import java.util.List;

public class GameOf2048Driver {

    // A driver for basic testing and where output file is created.
    public static void main(String[] args) {
        int[][][] initialStates = inputStringToArray();
        Node node;
        Node[] nodes = new Node[initialStates.length];
        for (int i = 0; i < initialStates.length; i++) {
            node = GameOf2048.bestScoreUsingBFS(initialStates[i]);
            nodes[i] = node;
        }
        createOutputFile(nodes);
    }

    // Creates an output file then writes the score and path from each node in nodes to file.
    private static void createOutputFile(Node[] nodes) {
        File file = new File("2048_out.txt");
        try {
            FileWriter fr = new FileWriter(file, false);
            int score;
            List<String> path;
            String output = "";
            for (int i = 0; i < nodes.length; i++) {
                score = nodes[i].nodeScore();
                path = nodes[i].nodePath();
                fr.write(score + "," + path.get(0) + "," + path.get(1) + "," + path.get(2) + "\n");
            }
            fr.close();
        } catch (IOException ioEx) {
            System.out.println("IO error occurred.");
        }
    }

    // Reads the input file and writes it to an array.
    private static int[][][] inputStringToArray() {
        int[][][] result;
        File input = new File("2048_in.txt");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(input));
        } catch (FileNotFoundException notFound) {
            System.out.println("File not found");
            return null;
        }
        String st;
        try {
            st = br.readLine();
            int numTestCases = Integer.parseInt(st);
            result = new int[numTestCases][4][4];
            for (int i = 0; i < numTestCases * 4; i++) {
                st = br.readLine();
                String[] strArr = st.split(",", 4);
                for (int j = 0; j < 4; j++) {
                    result[i/4][i%4][j] = Integer.parseInt(strArr[j]);
                }
            }
        } catch (IOException ioEx) {
            System.out.println("IO error occurred.");
            return null;
        }
        return result;
    }
}
