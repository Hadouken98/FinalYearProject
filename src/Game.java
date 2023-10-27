import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        Maze maze = new Maze(10, 10); // Create a 10x10 maze (you can adjust the size)
        Scanner scanner = new Scanner(System.in);

        while (!maze.isPlayerAtExit()) {
            maze.printMaze();
            System.out.print("Enter a direction (W/A/S/D): ");
            char input = scanner.next().charAt(0);
            maze.movePlayer(input);
        }

        System.out.println("Congratulations! You've reached the exit!");
    }
}
