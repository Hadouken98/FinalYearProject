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

    public void renderDungeon(Player player) {
        for (int i = 0; i < walls.length; i++) {
            for (int j = 0; j < walls[i].length; j++) {
                if (i == player.getRow() && j == player.getCol()) {
                    System.out.print("P"); // Print "P" to represent the player's position
                } else if (walls[i][j] != 0) {
                    System.out.print("#"); // Print "#" to represent walls
                } else {
                    System.out.print("."); // Print "." to represent empty spaces
                }
            }
            System.out.println(); // Move to the next line after printing each row
        }
    }
}