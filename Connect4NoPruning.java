
import java.util.Scanner;

public class Connect4NoPruning {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final char EMPTY = '_';
    private static final char PLAYER = 'X';
    private static final char AI = 'O';
    private char[][] board = new char[ROWS][COLS];

    public Connect4NoPruning() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    public void printBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean dropChip(int col, char chip) {
        if (col < 0 || col >= COLS || board[0][col] != EMPTY) {
            return false; // Invalid move
        }
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board[i][col] == EMPTY) {
                board[i][col] = chip;
                return true;
            }
        }
        return false;
    }

    public void undoMove(int col) {
        for (int i = 0; i < ROWS; i++) {
            if (board[i][col] != EMPTY) {
                board[i][col] = EMPTY;
                break;
            }
        }
    }

    public boolean isBoardFull() {
        for (int i = 0; i < COLS; i++) {
            if (board[0][i] == EMPTY) {
                return false;
            }
        }
        return true;
    }

    public boolean checkWin(char chip) {
        return checkHorizontalWin(chip) || checkVerticalWin(chip) || checkDiagonalWin(chip);
    }

    private boolean checkHorizontalWin(char chip) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS - 3; j++) {
                if (board[i][j] == chip && board[i][j + 1] == chip && 
                    board[i][j + 2] == chip && board[i][j + 3] == chip) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkVerticalWin(char chip) {
        for (int i = 0; i < ROWS - 3; i++) {
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == chip && board[i + 1][j] == chip && 
                    board[i + 2][j] == chip && board[i + 3][j] == chip) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonalWin(char chip) {
        for (int i = 0; i < ROWS - 3; i++) {
            for (int j = 0; j < COLS - 3; j++) {
                if (board[i][j] == chip && board[i + 1][j + 1] == chip && 
                    board[i + 2][j + 2] == chip && board[i + 3][j + 3] == chip) {
                    return true;
                }
            }
        }
        for (int i = 3; i < ROWS; i++) {
            for (int j = 0; j < COLS - 3; j++) {
                if (board[i][j] == chip && board[i - 1][j + 1] == chip && 
                    board[i - 2][j + 2] == chip && board[i - 3][j + 3] == chip) {
                    return true;
                }
            }
        }
        return false;
    }

    public int minimax(char[][] board, int depth, boolean isMaximizing) {
        if (checkWin(AI)) return 1000;
        if (checkWin(PLAYER)) return -1000;
        if (isBoardFull() || depth == 0) return 0;

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < COLS; col++) {
                if (dropChip(col, AI)) {
                    int eval = minimax(board, depth - 1, false);
                    undoMove(col);
                    maxEval = Math.max(maxEval, eval);
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < COLS; col++) {
                if (dropChip(col, PLAYER)) {
                    int eval = minimax(board, depth - 1, true);
                    undoMove(col);
                    minEval = Math.min(minEval, eval);
                }
            }
            return minEval;
        }
    }

    public int getBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;
        for (int col = 0; col < COLS; col++) {
            if (dropChip(col, AI)) {
                int score = minimax(board, 6, false);
                undoMove(col);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = col;
                }
            }
        }
        return bestMove;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connect4NoPruning game = new Connect4NoPruning();

        System.out.println("Welcome to Connect 4!");
        game.printBoard();
        int turns = 0;
        long totaltime = 0;
        while (true) {
            // Player move
            int playerMove;
            do {
                System.out.print("Enter your move (0-6): ");
                playerMove = scanner.nextInt();
            } while (!game.dropChip(playerMove, PLAYER));

            game.printBoard();
            if (game.checkWin(PLAYER)) {
                System.out.println("You win!");
                break;
            }
            if (game.isBoardFull()) {
                System.out.println("It's a draw!");
                break;
            }

            // AI move
            long start1 = System.currentTimeMillis();  

            int aiMove = game.getBestMove();

            long end1 = System.currentTimeMillis();      
            System.out.println("Elapsed Time in milliseconds: "+ (end1-start1)); 
            totaltime = totaltime + (end1-start1);
            turns++;

            game.dropChip(aiMove, AI);

            System.out.println("AI places at column " + (aiMove));
            game.printBoard();
            if (game.checkWin(AI)) {
                System.out.println("AI wins!");
                break;
            }
            if (game.isBoardFull()) {
                System.out.println("It's a draw!");
                break;
            }
        }

        System.out.println("Time avg in milliseconds: "+ (totaltime/turns));
        scanner.close();
    }
}
