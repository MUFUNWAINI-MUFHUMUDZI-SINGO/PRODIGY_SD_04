import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SudokuSolverGUI extends JFrame {

    private JTextField[][] textFieldGrid;
    private JButton solveButton;
    private JPanel sudokuPanel;

    private int[][] grid = {
        {5, 3, 0, 0, 7, 0, 0, 0, 0},
        {6, 0, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},
        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},
        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    public SudokuSolverGUI() {
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        sudokuPanel = new JPanel();
        sudokuPanel.setLayout(new GridLayout(9, 9));
        textFieldGrid = new JTextField[9][9];

        // Create text fields and add to sudokuPanel
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField textField = new JTextField();
                textField.setHorizontalAlignment(JTextField.CENTER);
                textField.setFont(new Font("Arial", Font.PLAIN, 20));
                if (grid[row][col] != 0) {
                    textField.setText(String.valueOf(grid[row][col]));
                    textField.setEditable(false);
                }
                textFieldGrid[row][col] = textField;
                sudokuPanel.add(textField);
            }
        }

        add(sudokuPanel, BorderLayout.CENTER);

        solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveSudoku();
            }
        });
        add(solveButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    private void solveSudoku() {
        // Update grid based on input values in text fields
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String text = textFieldGrid[row][col].getText().trim();
                if (text.isEmpty()) {
                    grid[row][col] = 0;
                } else {
                    grid[row][col] = Integer.parseInt(text);
                }
            }
        }

        // Solve the Sudoku puzzle
        if (solveSudoku(grid)) {
            // Update text fields with solved values
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    textFieldGrid[row][col].setText(String.valueOf(grid[row][col]));
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No solution exists for the given Sudoku puzzle.", "Solver Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean solveSudoku(int[][] grid) {
        int row, col = 0;

        // Find an empty cell
        boolean isEmpty = false;
        for (row = 0; row < 9; row++) {
            for (col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    isEmpty = true;
                    break;
                }
            }
            if (isEmpty) {
                break;
            }
        }

        // If no empty cell found, puzzle solved
        if (!isEmpty) {
            return true;
        }

        // Try numbers 1-9 for the empty cell
        for (int num = 1; num <= 9; num++) {
            if (isSafe(grid, row, col, num)) {
                grid[row][col] = num;

                // Recursively solve rest of the puzzle
                if (solveSudoku(grid)) {
                    return true;
                }

                // If no solution found, backtrack
                grid[row][col] = 0;
            }
        }

        // No number worked, backtrack
        return false;
    }

    private boolean isSafe(int[][] grid, int row, int col, int num) {
        // Check if num is not already in the current row
        for (int i = 0; i < 9; i++) {
            if (grid[row][i] == num) {
                return false;
            }
        }

        // Check if num is not already in the current column
        for (int i = 0; i < 9; i++) {
            if (grid[i][col] == num) {
                return false;
            }
        }

        // Check if num is not already in the current 3x3 subgrid
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (grid[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SudokuSolverGUI();
            }
        });
    }
}
