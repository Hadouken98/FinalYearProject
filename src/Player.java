public class Player {
    private int row;
    private int col;

    private DungeonGraph dungeonGraph;

    public Player(int initialRow, int initialCol, DungeonGraph dungeonGraph) {
        this.row = initialRow;
        this.col = initialCol;
        this.dungeonGraph = dungeonGraph;
    }

    public boolean isValidMove(int newRow, int newCol) {
        // Check if the new position is within the boundaries of the dungeon
        if (newRow < 0 || newRow >= dungeonGraph.getWalls().length || newCol < 0 || newCol >= dungeonGraph.getWalls()[0].length) {
            return false;
        }

        // Check if there is a wall between the current position and the new position
        if (row < newRow && dungeonGraph.getWalls()[newRow][col] != 0) {
            return false;
        }
        if (row > newRow && dungeonGraph.getWalls()[row][col] != 0) {
            return false;
        }
        if (col < newCol && dungeonGraph.getWalls()[row][newCol] != 0) {
            return false;
        }
        if (col > newCol && dungeonGraph.getWalls()[row][col] != 0) {
            return false;
        }

        return true;
    }

    public void moveUp() {
        if (isValidMove(row - 1, col)) {
            row--; // Move the player up by decreasing the row coordinate
        }
    }

    public void moveDown() {
        if (isValidMove(row + 1, col)) {
            row++; // Move the player down by increasing the row coordinate
        }
    }

    public void moveLeft() {
        if (isValidMove(row, col - 1)) {
            col--; // Move the player left by decreasing the column coordinate
        }
    }

    public void moveRight() {
        if (isValidMove(row, col + 1)) {
            col++; // Move the player right by increasing the column coordinate
        }
    }

    public int getRow() {
        return row; // Return the current row coordinate of the player
    }

    public int getCol() {
        return col; // Return the current column coordinate of the player
    }
}