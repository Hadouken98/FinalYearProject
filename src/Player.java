public class Player {
    private int row;
    private int col;
    private int steps; // Step counter
    private long startTime; // To store the start time when the player starts moving
    private boolean timerRunning;

    private DungeonGraph dungeonGraph;

    public Player(int initialRow, int initialCol, DungeonGraph dungeonGraph) {
        this.row = initialRow;
        this.col = initialCol;
        this.dungeonGraph = dungeonGraph;
        this.steps = 0;
        this.timerRunning = false;
    }

    public boolean isValidMove(int newRow, int newCol) {
        // Check if the new position is within the boundaries of the dungeon
        if (newRow < 0 || newRow >= dungeonGraph.getWalls().length || newCol < 0 || newCol >= dungeonGraph.getWalls()[0].length) {
            return false;
        }

        // Check if there is a wall in the new position
        if (dungeonGraph.getWalls()[newRow][newCol] != 0) {
            return false;
        }

        // Check if there is a wall between the current position and the new position
        if (row < newRow && dungeonGraph.getWalls()[row + 1][col] != 0) {
            return false;
        }
        if (row > newRow && dungeonGraph.getWalls()[row - 1][col] != 0) {
            return false;
        }
        if (col < newCol && dungeonGraph.getWalls()[row][col + 1] != 0) {
            return false;
        }
        if (col > newCol && dungeonGraph.getWalls()[row][col - 1] != 0) {
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

    public int getSteps() {
        return steps;
    }

    public void incrementSteps() {
        steps++;
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
        timerRunning = true;
    }

    public void stopTimer() {
        timerRunning = false;
    }

    public long getElapsedTime() {
        if (timerRunning) {
            return (System.currentTimeMillis() - startTime) / 1000; // Convert to seconds
        } else {
            return 0;
        }
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }
}
