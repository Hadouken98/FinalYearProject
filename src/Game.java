import java.util.Random;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
import java.util.List;
public class Game {
    private DungeonGraph dungeonGraph;
    private Player player;

    private int[] finalRoom;

    public Game(int minSize, int maxSize, int initialRow, int initialCol) {
        int size = generateRandomSize(minSize, maxSize);
        int[][] walls = generateDungeonWalls(size);
        dungeonGraph = new DungeonGraph();
        dungeonGraph.setWalls(walls);
        player = new Player(initialRow, initialCol, dungeonGraph);
    }

    // Uses recursive backtracking to generate the maze, essentially DFS, Depth-First-Search
    // Explore possible directions from a given room and remove walls between rooms.
    // Mark the final room as the goal to reach.
    // Recursively call the method to continue generating the maze from new rooms.
    private int[] generateMaze(int row, int col, int[][] walls, Random random, List<int[]> accessibleRooms) {
        int[][] directions = {{0, -2}, {0, 2}, {-2, 0}, {2, 0}};

        walls[row][col] = 0;
        accessibleRooms.add(new int[]{row, col});

        int[] lastRoom = new int[]{row, col};

        for (int i = directions.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int[] temp = directions[index];
            directions[index] = directions[i];
            directions[i] = temp;
        }

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            if (newRow >= 0 && newRow < walls.length && newCol >= 0 && newCol < walls.length && walls[newRow][newCol] != 0) {
                walls[row + direction[0] / 2][col + direction[1] / 2] = 0;

                int[] result = generateMaze(newRow, newCol, walls, random, accessibleRooms);
                lastRoom = result != null ? result : lastRoom;
            }
        }

        return lastRoom;
    }

    public DungeonGraph getDungeonGraph() {
        return dungeonGraph;
    }
    public Player getPlayer() {
        return player;
    }
    private int generateRandomSize(int minSize, int maxSize) {
        Random random = new Random();
        return random.nextInt(maxSize - minSize + 1) + minSize;
    }


    // Use breadth-first search (BFS) to determine if there exists a path from the starting room to the final room.
// Return true if a path exists, false otherwise.
    public boolean isDungeonSolvable() {
        int[][] walls = dungeonGraph.getWalls();

        int startRoom = 0;
        int finalRoom = walls.length - 1; // Use the class member variable instead

        boolean[] visited = new boolean[walls.length];
        Queue<Integer> queue = new LinkedList<>();

        visited[startRoom] = true; // Mark the starting room as visited
        queue.add(startRoom); // Add the starting room to the queue

        while (!queue.isEmpty()) {
            int currentRoom = queue.poll(); // Remove and retrieve the room at the front of the queue

            // If the current room is the final room, a path from start to final room has been found
            if (currentRoom == finalRoom) {
                return true;
            }

            // Iterate through all neighboring rooms
            for (int neighbor = 0; neighbor < walls.length; neighbor++) {
                // Check if the neighboring room is accessible and hasn't been visited
                if (walls[currentRoom][neighbor] != 0 && !visited[neighbor]) {
                    visited[neighbor] = true; // Mark the neighboring room as visited
                    queue.add(neighbor); // Add the neighboring room to the queue for further exploration
                }
            }
        }

        // No path from start to final room found
        return false;
    }
    private int[][] generateDungeonWalls(int size) {
        int[][] walls = new int[size][size];
        Random random = new Random();

        // Generate the dungeon walls as all walls initially
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                walls[i][j] = 1; // Initialize all rooms as walls
            }
        }

        List<int[]> accessibleRooms = new ArrayList<>();
        int[] finalRoom = generateMaze(0, 0, walls, random, accessibleRooms);

        // Print accessible rooms
        System.out.println("Accessible Rooms:");
        for (int[] room : accessibleRooms) {
            System.out.println("(" + room[0] + ", " + room[1] + ")");
        }

        // Randomize the final room
        int randomIndex = random.nextInt(accessibleRooms.size());
        finalRoom = accessibleRooms.get(randomIndex);

        walls[finalRoom[0]][finalRoom[1]] = 0; // Open the final room

        return walls;
    }
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean gameRunning = true;
        int lastRoomIndex = dungeonGraph.getNumRooms() - 1;

        while (gameRunning) {
            dungeonGraph.renderDungeon(player);
            System.out.println("Enter your move (up, down, left, right): ");
            String move = scanner.nextLine();

            int playerRow = player.getRow();
            int playerCol = player.getCol();

            switch (move.toLowerCase()) {
                case "up":
                    if (playerRow > 0 && dungeonGraph.getWalls()[playerRow - 1][playerCol] != 1) {
                        player.moveUp();
                    } else {
                        System.out.println("There is a wall blocking your way.");
                    }
                    break;
                case "down":
                    if (playerRow < lastRoomIndex && dungeonGraph.getWalls()[playerRow + 1][playerCol] != 1) {
                        player.moveDown();
                    } else {
                        System.out.println("There is a wall blocking your way.");
                    }
                    break;
                case "left":
                    if (playerCol > 0 && dungeonGraph.getWalls()[playerRow][playerCol - 1] != 1) {
                        player.moveLeft();
                    } else {
                        System.out.println("There is a wall blocking your way.");
                    }
                    break;
                case "right":
                    if (playerCol < lastRoomIndex && dungeonGraph.getWalls()[playerRow][playerCol + 1] != 1) {
                        player.moveRight();
                    } else {
                        System.out.println("There is a wall blocking your way.");
                    }
                    break;
                case "quit":
                    gameRunning = false;
                    break;
                default:
                    System.out.println("Invalid move. Please try again.");
                    continue;
            }

            // Check if the player has reached the last room
            if (player.getRow() == dungeonGraph.getWalls().length - 1 && player.getCol() == dungeonGraph.getWalls().length - 1) {
                System.out.println("Congratulations! You have reached the final room. You win!");
                gameRunning = false;
                System.out.println("Player position: (" + player.getRow() + ", " + player.getCol() + ")");
                System.out.println("Last room position: (" + (dungeonGraph.getWalls().length - 1) + ", " + (dungeonGraph.getWalls().length - 1) + ")");
            } else {
                System.out.println("Player position: (" + player.getRow() + ", " + player.getCol() + ")");
                System.out.println("Last room position: (" + (dungeonGraph.getWalls().length - 1) + ", " + (dungeonGraph.getWalls().length - 1) + ")");
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        Game game = new Game(5, 10, 0, 0); // Example: 5 rooms, size range 5-10, starting position (0,0)

        if (game.isDungeonSolvable()) {
            System.out.println("Dungeon is solvable.");
            game.run();
        } else {
            System.out.println("Dungeon is not solvable. Regenerating...");
            main(args); // Restart the game with a new dungeon
        }
    }
}

