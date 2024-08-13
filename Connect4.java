import java.util.Scanner;

public class Connect4 {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final char EMPTY = '_';
    private static final char PLAYER = 'X';
    private static final char AI = 'O';
    private char[][] board = new char[ROWS][COLS];

    public Connect4() {
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

    public boolean isBoardFull() {
        for (int i = 0; i < COLS; i++) {
            if (board[0][i] == EMPTY) {
                return false;
            }
        }
        return true;
    }

    public boolean checkWin(char chip) {
        // Check horizontal, vertical, and diagonal wins
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
        // Check for diagonal wins (both directions)
        for (int i = 0; i < ROWS - 3; i++) {
            for (int j = 0; j < COLS - 3; j++) {
                if (board[i][j] == chip && board[i + 1][j + 1] == chip && 
                    board[i + 2][j + 2] == chip && board[i + 3][j + 3] == chip) {
                    return true;
                }
            }
            for (int j = 3; j < COLS; j++) {
                if (board[i][j] == chip && board[i + 1][j - 1] == chip && 
                    board[i + 2][j - 2] == chip && board[i + 3][j - 3] == chip) {
                    return true;
                }
            }
        }
        return false;
    }

    public void playGame() {
        Scanner scanner = new Scanner(System.in);
        boolean isPlayerTurn = true;

        while (true) {
            printBoard();
            if (isPlayerTurn) {
                System.out.print("Player's turn. Choose a column (0-6): ");
                int col = scanner.nextInt();
                if (!dropChip(col, PLAYER)) {
                    System.out.println("Invalid move. Try again.");
                    continue;
                }
                if (checkWin(PLAYER)) {
                    printBoard();
                    System.out.println("Player wins!");
                    break;
                }
            } else {
                int bestMove = getBestMove();
                dropChip(bestMove, AI);
                System.out.println("AI placed a chip in column " + bestMove);
                if (checkWin(AI)) {
                    printBoard();
                    System.out.println("AI wins!");
                    break;
                }
            }

            if (isBoardFull()) {
                printBoard();
                System.out.println("It's a draw!");
                break;
            }

            isPlayerTurn = !isPlayerTurn;
        }

        scanner.close();
    }

    public int getBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = 0;
        for (int col = 0; col < COLS; col++) {
            if (dropChip(col, AI)) {
                int score = minimax(board, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                undoMove(col);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = col;
                }
            }
        }
        return bestMove;
    }

    public void undoMove(int col) {
        for (int i = 0; i < ROWS; i++) {
            if (board[i][col] != EMPTY) {
                board[i][col] = EMPTY;
                break;
            }
        }
    }

    public int minimax(char[][] board, int depth, int alpha, int beta, boolean isMaximizing) {
        if (checkWin(AI)) return 1000;
        if (checkWin(PLAYER)) return -1000;
        if (isBoardFull() || depth == 0) return 0;

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < COLS; col++) {
                if (dropChip(col, AI)) {
                    int eval = minimax(board, depth - 1, alpha, beta, false);
                    undoMove(col);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < COLS; col++) {
                if (dropChip(col, PLAYER)) {
                    int eval = minimax(board, depth - 1, alpha, beta, true);
                    undoMove(col);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) break;
                }
            }
            return minEval;
        }
    }

    public static void main(String[] args) {
        Connect4 game = new Connect4();
        game.playGame();
    }
}

