// Baran Onalan
// 10 / 8 / 2022 (Last Edited)
// This class is used to encapsulate the result of a shift or merge.
public class Tuple {
    // Resulting array of the shift or merge.
    private int[][] arr;
    // Resulting score if merge, resulting validity if shift (-1 means invalid).
    private int integer;

    public Tuple (int[][] arr, int integer) {
        this.arr = arr;
        this.integer = integer;
    }

    // Getter for the result array.
    public int[][] getArr() {
        return this.arr;
    }

    // Getter for the score if merge, getter of the validity if shift (-1 means invalid).
    public int getInt() {
        return this.integer;
    }
}