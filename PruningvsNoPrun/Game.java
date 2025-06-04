package PruningvsNoPrun;


public class Game {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final char EMPTY = '_';
    private static final char Pruning = 'X';
    private static final char NonPruning = 'O';
    private char[][] board = new char[ROWS][COLS];
    public int globaldepth = 8;
    public Game() {
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
        
        boolean isNonPruningTurn = false;
        long nptotaltime = 0;
        long ptotaltime = 0;
        int npturns = 0;
        int pturns = 0;
        while (true) {
            printBoard();
            if (isNonPruningTurn) {
                long start1 = System.currentTimeMillis();
                int bestMove = getBestNPMove();
                long end1 = System.currentTimeMillis(); 
                nptotaltime = nptotaltime + (end1-start1);
                npturns++;
                if (!dropChip(bestMove, NonPruning)) {
                    System.out.println("Invalid move. Try again.");
                    continue;
                }
                if (checkWin(NonPruning)) {
                    printBoard();
                    System.out.println("NonPrun      ing wins!");
                    break;
                }
            } else {
                long start1 = System.currentTimeMillis();

                int bestMove = getBestPMove();

                long end1 = System.currentTimeMillis();      
                //System.out.println("Elapsed Time in milliseconds: "+ (end1-start1)); 
                ptotaltime = ptotaltime + (end1-start1);
                pturns++;

                dropChip(bestMove, Pruning);
                System.out.println("AI placed a chip in column " + bestMove);
                if (checkWin(Pruning)) {
                    printBoard();
                    System.out.println("Pruning wins!");
                    break;
                }
            }

            if (isBoardFull()) {
                printBoard();
                System.out.println("It's a draw!");
                break;
            }

            isNonPruningTurn = !isNonPruningTurn;
        }
        System.out.println("Avg ms of NonPruning: " + (nptotaltime/npturns));
        System.out.println("Avg ms of Pruning: " + (ptotaltime/pturns));
    }

    //PRUNING

    public int getBestPMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = 0;
        for (int col = 0; col < COLS; col++) {
            if (dropChip(col, Pruning)) {
                int score = minimaxP(board, globaldepth, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
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

    public int minimaxP(char[][] board, int depth, int alpha, int beta, boolean isMaximizing) {
        if (checkWin(Pruning)) return 1000;
        if (checkWin(NonPruning)) return -1000;
        if (isBoardFull() || depth == 0) return 0;

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < COLS; col++) {
                if (dropChip(col, Pruning)) {
                    int eval = minimaxP(board, depth - 1, alpha, beta, false);
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
                if (dropChip(col, NonPruning)) {
                    int eval = minimaxP(board, depth - 1, alpha, beta, true);
                    undoMove(col);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) break;
                }
            }
            return minEval;
        }
    }

    //NON PRUNING

    public int minimaxNP(char[][] board, int depth, boolean isMaximizing) {
        if (checkWin(NonPruning)) return 1000;
        if (checkWin(Pruning)) return -1000;
        if (isBoardFull() || depth == 0) return 0;

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < COLS; col++) {
                if (dropChip(col, NonPruning)) {
                    int eval = minimaxNP(board, depth - 1, false);
                    undoMove(col);
                    maxEval = Math.max(maxEval, eval);
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < COLS; col++) {
                if (dropChip(col, Pruning)) {
                    int eval = minimaxNP(board, depth - 1, true);
                    undoMove(col);
                    minEval = Math.min(minEval, eval);
                }
            }
            return minEval;
        }
    }

    public int getBestNPMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;
        for (int col = 0; col < COLS; col++) {
            if (dropChip(col, NonPruning)) {
                int score = minimaxNP(board, globaldepth, false);
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
        Game game = new Game();
        game.playGame();
    }
}
