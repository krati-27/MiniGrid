
import java.awt.*;
import java.awt.event.ActionEvent; //Abstract Window Toolkit for GUI components.
import java.awt.event.ActionListener;
import java.util.ArrayList; //Classes for handling action events (e.g., button clicks).
import java.util.Collections;
import javax.swing.*; //Utility class for performing various operations on collections. 

class Tile {

    int value;
    boolean flipped;

    Tile(int value) {
        this.value = value;
        this.flipped = false;
    }
}

public class MiniGrid extends JFrame { //Extends JFrame to create a window for the game. 

    private static final int GRID_SIZE = 4;
    private ArrayList<Tile> tiles;
    private Tile selectedTile1;
    private Tile selectedTile2;
    private int movesCount;
    private JLabel movesLabel;
    private Timer timer;

    public MiniGrid() { // 
//jframe setup 
        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());
        tiles = new ArrayList<>();
        for (int i = 1; i <= GRID_SIZE * GRID_SIZE / 2; i++) {
            tiles.add(new Tile(i));
            tiles.add(new Tile(i));
        }
        Collections.shuffle(tiles);
        JPanel gamePanel = new JPanel(); //A JPanel is created to represent the game grid, and buttons are added to it based on the shuffled tiles. 
//Creates a JPanel to hold the grid of buttons (game tiles). 
        gamePanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE)); //Sets the layout manager of the gamePanel to a GridLayout with dimensions specified by GRID_SIZE. 
        for (Tile tile : tiles) {
            JButton button = new JButton();
            button.addActionListener(new TileClickListener(tile));
            gamePanel.add(button);
        }
        add(gamePanel, BorderLayout.CENTER);
        movesLabel = new JLabel("Moves: 0");
        add(movesLabel, BorderLayout.SOUTH);
        timer = new Timer(1000, new TimerListener());

        startNewGame();
    }
// 

    private void startNewGame() {
        movesCount = 0;
        selectedTile1 = null;
        selectedTile2 = null;
        for (Tile tile : tiles) {
            tile.flipped = false;
        }
        updateMovesLabel();
        for (Component component : ((JPanel) getContentPane().getComponent(0)).getComponents()) {
            JButton button = (JButton) component;
            button.setText("");
            button.setEnabled(true);
        }
        timer.stop(); //Stops the timer to ensure that any ongoing timed events 
    }
//The TileClickListener class is an ActionListener for the buttons. It handles the logic when a tile (button) is clicked. 

    private class TileClickListener implements ActionListener {

        private Tile clickedTile;

        TileClickListener(Tile tile) {
            this.clickedTile = tile;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!clickedTile.flipped) {
                if (selectedTile1 == null) {
                    selectedTile1 = clickedTile;
                    revealTile((JButton) e.getSource(), selectedTile1);
                } else if (selectedTile2 == null) {
                    selectedTile2 = clickedTile;
                    revealTile((JButton) e.getSource(), selectedTile2);
                    movesCount++;
                    updateMovesLabel();
                    if (selectedTile1.value == selectedTile2.value) {
                        selectedTile1 = null;
                        selectedTile2 = null;
                        if (checkGameComplete()) {
                            showGameCompleteDialog();
                        }
                    } else {
                        timer.restart(); //If the values are not equal, a timer is restarted using timer.restart(). 
//This timer is often used to delay hiding unmatched tiles. 
                    }
                }
            }
        }

        private void revealTile(JButton button, Tile tile) {
            button.setText(Integer.toString(tile.value));
            tile.flipped = true;
        }
    }

//The TimerListener class is an ActionListener for the timer. It is triggered when the timer runs out, hiding the unmatched tiles. 
    private class TimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            hideTiles();
            selectedTile1 = null;
            selectedTile2 = null;
            timer.stop(); //Stops the timer to prevent further actions until the next round.
             } 


        private void hideTiles() {
            for (Component component : ((JPanel) getContentPane().getComponent(0)).getComponents()) {
                JButton button = (JButton) component;
//Type Casting to JButton: 
                Tile tile = tiles.get(((JPanel) getContentPane().getComponent(0)).getComponentZOrder(button));
                if (tile.flipped) {
                    button.setText("");
                    tile.flipped = false;
                }
            }
        }
    }

    private void updateMovesLabel() {
        movesLabel.setText("Moves: " + movesCount);
    }

    private boolean checkGameComplete() {
        for (Tile tile : tiles) {
            if (!tile.flipped) {
                return false;

            }
        }
        return true;
    }

    private void showGameCompleteDialog() {
        JOptionPane.showMessageDialog(this, "Congratulations! You completed the game in " + movesCount + " moves.",
                "Game Complete", JOptionPane.INFORMATION_MESSAGE);
        startNewGame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // is a Swing utility method that ensures the Swing components are created and 
            MiniGrid advancedMiniGrid = new MiniGrid();
            advancedMiniGrid.setVisible(true);
        });
    }
}
