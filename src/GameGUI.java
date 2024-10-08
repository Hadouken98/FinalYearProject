import javax.swing.*;
import java.util.Random;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameGUI extends JFrame {
    private Game game;
    private JTextArea gameOutput;
    private Timer timer;
    private long startTime; // To store the start time when the player starts moving
    private int stepCount; // To count the number of steps/moves

    private JLabel miniMapLabel;

    public GameGUI(Game game) {
        this.game = game;
        this.stepCount = 0;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Dungeon Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        setResizable(true); // Allow the window to be resizable
        getContentPane().setBackground(Color.LIGHT_GRAY); // Set background color

        // Create a panel for the game output
        gameOutput = new JTextArea();
        gameOutput.setEditable(false); // Make it read-only
        JScrollPane scrollPane = new JScrollPane(gameOutput);

        // Create buttons for player movement with some styling
        JButton upButton = createStyledButton("Up", "Move Up", "icons/up.png");
        JButton downButton = createStyledButton("Down", "Move Down", "icons/down.png");
        JButton leftButton = createStyledButton("Left", "Move Left", "icons/left.png");
        JButton rightButton = createStyledButton("Right", "Move Right", "icons/right.png");

        // Add action listeners to the buttons
        upButton.addActionListener(new MoveActionListener("up"));
        downButton.addActionListener(new MoveActionListener("down"));
        leftButton.addActionListener(new MoveActionListener("left"));
        rightButton.addActionListener(new MoveActionListener("right"));

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // Added spacing between buttons
        buttonPanel.add(upButton);
        buttonPanel.add(downButton);
        buttonPanel.add(leftButton);
        buttonPanel.add(rightButton);

        // Mini Map
        miniMapLabel = new JLabel();

        // Create a panel to hold both the game output and buttons
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)); // Added spacing between components
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(miniMapLabel, BorderLayout.EAST);

        // Add the main panel to the frame
        add(mainPanel);

        // Create a timer with a 1-second delay
        timer = new Timer(1000, new TimerActionListener());
        timer.setInitialDelay(0); // Start the timer immediately



        // Update the UI to reflect the initial game state
        updateUI();

        // Make the frame visible
        setVisible(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        movePlayer("up");
                        break;
                    case KeyEvent.VK_DOWN:
                        movePlayer("down");
                        break;
                    case KeyEvent.VK_LEFT:
                        movePlayer("left");
                        break;
                    case KeyEvent.VK_RIGHT:
                        movePlayer("right");
                        break;
                }
            }
        });
        this.setFocusable(true); // Make sure the frame can receive key events

    }

    private JButton createStyledButton(String text, String tooltip, String iconPath) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip + " (" + text.toUpperCase().charAt(0) + ")");
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false); // Remove focus border
        button.setForeground(Color.BLUE); // Set text color
        // Set preferred size to accommodate the text
        button.setPreferredSize(new Dimension(100, 50)); // Adjust the dimensions as needed
        if (iconPath != null) {
            ImageIcon icon = new ImageIcon(iconPath);
            button.setIcon(icon);
        }
        return button;
    }

    private class TimerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.getPlayer().isTimerRunning()) {
                long elapsedTime = game.getPlayer().getElapsedTime();
                setTitle("Dungeon Game - Time: " + elapsedTime + " seconds - Steps: " + stepCount);
            }
        }
    }

    private void updateUI() {
        // Clear the game output area
        gameOutput.setText("");

        // Render the dungeon to the game output area
        gameOutput.append(game.getDungeonGraph().renderDungeon(game.getPlayer()));

        // Display the step count
        gameOutput.append("\nSteps: " + game.getPlayer().getSteps());

        updateMiniMap();

        // Check if the player has won
        if (game.getPlayer().getRow() == game.getFinalRow() && game.getPlayer().getCol() == game.getFinalCol()) {
            if (game.getPlayer().hasKey()) {
                game.getPlayer().stopTimer(); // Stop the timer
                long elapsedTime = game.getPlayer().getElapsedTime();
                gameOutput.append("\nCongratulations! You have reached the final room. You win!");
                gameOutput.append("\nTime taken: " + elapsedTime + " seconds");
            } else {
                gameOutput.append("\nYou need a key to enter the final room.");
            }
        } else if (game.getPlayer().isTimerRunning()) {
            // Display the elapsed time if the player hasn't won yet
            long elapsedTime = game.getPlayer().getElapsedTime();
            setTitle("Dungeon Game - Time: " + elapsedTime + " seconds - Steps: " + stepCount);
        }
    }
    private void updateMiniMap() {
        int[][] walls = game.getDungeonGraph().getWalls();
        int mazeSize = walls.length;

        String miniMap = "<html><font size=\"4\" color=\"blue\">Coordinates:<br>Player Position: (" + game.getPlayer().getRow() + ", " + game.getPlayer().getCol() + ")<br>" +
                "Maze Size: " + mazeSize + " x " + mazeSize + "</font></html>";
        miniMapLabel.setText(miniMap);
    }

    private void movePlayer(String direction) {
        // Save the current player position before moving
        int initialRow = game.getPlayer().getRow();
        int initialCol = game.getPlayer().getCol();

        // Start the timer when the player makes the first move
        if (!game.getPlayer().isTimerRunning()) {
            game.getPlayer().startTimer();
            timer.start();
        }

        // Handle player movement based on the direction
        boolean moved = false;
        switch (direction) {
            case "up":
                if (game.getPlayer().isValidMove(initialRow - 1, initialCol)) {
                    game.getPlayer().moveUp();
                    moved = true;
                }
                break;
            case "down":
                if (game.getPlayer().isValidMove(initialRow + 1, initialCol)) {
                    game.getPlayer().moveDown();
                    moved = true;
                }
                break;
            case "left":
                if (game.getPlayer().isValidMove(initialRow, initialCol - 1)) {
                    game.getPlayer().moveLeft();
                    moved = true;
                }
                break;
            case "right":
                if (game.getPlayer().isValidMove(initialRow, initialCol + 1)) {
                    game.getPlayer().moveRight();
                    moved = true;
                }
                break;
        }

        // Shift the walls randomly after each move
        if (moved) {
            Random random = new Random();
            if (random.nextInt(8) == 0) {
                game.shiftWallsRandomly(game.getPlayer());
            }
        }

        // Update the UI after each move
        if (moved) {
            game.getPlayer().incrementSteps(); // Increment step counter
            game.checkForKey();
        } else {
            gameOutput.append("There is a wall blocking your way.\n");
        }

        updateUI();
    }

    private class MoveActionListener implements ActionListener {
        private String direction;

        public MoveActionListener(String direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Save the current player position before moving
            int initialRow = game.getPlayer().getRow();
            int initialCol = game.getPlayer().getCol();

            // Start the timer when the player makes the first move
            if (!game.getPlayer().isTimerRunning()) {
                game.getPlayer().startTimer();
                startTime = System.currentTimeMillis(); // Save the start time
                timer.start();
            }

            // Handle player movement based on the button pressed
            boolean moved = false;
            switch (direction) {
                case "up":
                    if (game.getPlayer().isValidMove(initialRow - 1, initialCol)) {
                        game.getPlayer().moveUp();
                        moved = true;
                    }
                    break;
                case "down":
                    if (game.getPlayer().isValidMove(initialRow + 1, initialCol)) {
                        game.getPlayer().moveDown();
                        moved = true;
                    }
                    break;
                case "left":
                    if (game.getPlayer().isValidMove(initialRow, initialCol - 1)) {
                        game.getPlayer().moveLeft();
                        moved = true;
                    }
                    break;
                case "right":
                    if (game.getPlayer().isValidMove(initialRow, initialCol + 1)) {
                        game.getPlayer().moveRight();
                        moved = true;
                    }
                    break;
            }

            // Shift the walls randomly after each move
            if (moved) {
                Random random = new Random();
                if (random.nextInt(8) == 0) {
                    game.shiftWallsRandomly(game.getPlayer());
                }
            }

            // Update the UI after each move
            if (moved) {
                game.getPlayer().incrementSteps(); // Increment step counter
            } else {
                gameOutput.append("There is a wall blocking your way.\n");
            }

            updateUI();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game(5, 10, 0, 0);
            new GameGUI(game);
        });
    }
}

