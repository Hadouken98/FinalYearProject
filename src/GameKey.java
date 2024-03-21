public class GameKey {
    private static int row;
    private static int col;

    public GameKey(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static int getRow() {
        return row;
    }

    public static int getCol() {
        return col;
    }
}