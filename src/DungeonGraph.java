public class DungeonGraph {
    private int[][] walls; // 2D array representing the dungeon walls

    public void setWalls(int[][] walls) {
        this.walls = walls;
    }

    public int[][] getWalls() {
        return walls;
    }

    public int getNumRooms() {
        return walls.length; // Returns the number of rooms in the dungeon based on the size of the walls array
    }

    public String renderDungeon(Player player) {
        StringBuilder output = new StringBuilder();

        int mazeSize = walls.length;

        // Top border
        output.append("+");
        for (int i = 0; i < mazeSize; i++) {
            output.append("---+");
        }
        output.append("\n");

        // Maze cells
        for (int i = 0; i < mazeSize; i++) {
            output.append("|");
            for (int j = 0; j < mazeSize; j++) {
                if (i == player.getRow() && j == player.getCol()) {
                    output.append(" 🤠 "); // Player's position
                } else if (walls[i][j] != 0) {
                    output.append("█"); // Wall
                } else {
                    output.append("   "); // Empty space
                }
                output.append("|");
            }
            output.append(" " + i + "\n"); // Row index
            output.append("+");
            for (int j = 0; j < mazeSize; j++) {
                output.append("---+");
            }
            output.append("\n");
        }

        // Column indices
        output.append(" ");
        for (int i = 0; i < mazeSize; i++) {
            output.append(" " + i + "  ");
        }
        output.append("\n");

        return output.toString();
    }
}