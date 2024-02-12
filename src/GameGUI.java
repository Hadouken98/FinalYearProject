import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameGUI extends JFrame {
    private Game game;
    private JTextArea gameOutput;

    public GameGUI(Game game) {
        this.game = game;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Dungeon Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
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

        // Create a panel to hold both the game output and buttons
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)); // Added spacing between components
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add the main panel to the frame
        add(mainPanel);

        // Update the UI to reflect the initial game state
        updateUI();

        // Make the frame visible
        setVisible(true);
    }

    private JButton createStyledButton(String text, String tooltip, String iconPath) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
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

    private void updateUI() {
        // Clear the game output area
        gameOutput.setText("");

        // Render the dungeon to the game output area
        gameOutput.append(game.getDungeonGraph().renderDungeon(game.getPlayer()));

        // Check if the player has won
        if (game.getPlayer().getRow() == game.getDungeonGraph().getWalls().length - 1 &&
                game.getPlayer().getCol() == game.getDungeonGraph().getWalls().length - 1) {
            gameOutput.append("\nCongratulations! You have reached the final room. You win!");
        }
    }

    private class MoveActionListener implements ActionListener {
        private String direction;

        public MoveActionListener(String direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Handle player movement based on the button pressed
            switch (direction) {
                case "up":
                    game.getPlayer().moveUp();
                    break;
                case "down":
                    game.getPlayer().moveDown();
                    break;
                case "left":
                    game.getPlayer().moveLeft();
                    break;
                case "right":
                    game.getPlayer().moveRight();
                    break;
            }

            // Update the UI after each move
            updateUI();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game(5, 10, 0, 0);

            if (game.isDungeonSolvable()) {
                System.out.println("Dungeon is solvable.");
                new GameGUI(game);
            } else {
                System.out.println("Dungeon is not solvable. Regenerating...");
                main(args); // Restart the game with a new dungeon
            }
        });
    }
}
