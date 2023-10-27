public class Maze {
    private char[][] grid;
    private int playerX, playerY;
    private int exitX, exitY;
    public Maze(int rows, int cols) {
        grid = new char[rows][cols];
        playerX = 0 /* initial player X-coordinate */;
        playerY = 0 /* initial player Y-coordinate */;
        exitX = 9 /* exit X-coordinate */;
        exitY = 9 /* exit Y-coordinate */;
    }

    public void printMaze() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    public boolean isPlayerAtExit() {
        return playerX == exitX && playerY == exitY;
    }

    public void movePlayer(char direction) {
        int newPlayerX = playerX;
        int newPlayerY = playerY;

        switch (direction) {
            case 'W':
                newPlayerX--;
                break;
            case 'A':
                newPlayerY--;
                break;
            case 'S':
                newPlayerX++;
                break;
            case 'D':
                newPlayerY++;
                break;
            default:
                System.out.println("Invalid input. Use W/A/S/D to move.");
                return; // Invalid input, do not move the player
        }

        if (isValidMove(newPlayerX, newPlayerY)) {
            grid[playerX][playerY] = ' '; // Clear the current player position
            playerX = newPlayerX;
            playerY = newPlayerY;
            grid[playerX][playerY] = 'P'; // Update the new player position
        } else {
            System.out.println("Invalid move. You can't go that way.");
        }
    }

    private boolean isValidMove(int x, int y) {
        // Check if the new position (x, y) is within the bounds of the grid and not a wall
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] != 'X';
    }
}