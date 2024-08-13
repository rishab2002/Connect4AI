import java.util.Scanner;
public class Board {
    String[][] board;
    String winner;
    String player1symbol;
    String player2symbol;

    //Fills in board with _ char. 
    public Board(String a, String b){
        player1symbol = a;
        player2symbol = b;
        board = new String[6][7]; 
        for(int i = 0; i<6; i++)
        {
            for(int j = 0; j<7; j++)
            {
                board[i][j] = "_";
            }
        }
    }

    //Prints board
    public void print(){
        for(int i = 0; i<6; i++)
        {
            for(int j = 0; j<7; j++)
            {
                System.out.print(board[i][j] + " ");
                if(j == 6){
                    System.out.println();
                }
            }
        }
    }

    //Main function for game:
    //Will use loops to iterate until win coniditions are satisfied
    //or the board is filled up. 
    //First it will ask Player 1 which column to place their chip.
    //Chip will "drop" into place with a ADD TO BOARD function call.
    //Then do the same with Player2,3,... and repeat in a loop until win condition satisfied. 
    //After 42(total # of spaces) - 7(number of spaces needed atleast be filled for win)
    //spaces have been filled up with initialized space count int. 
    //
    public void playMultiplayer(){
        int spacesMax = 42;
        int spacesFilled = 0; 
        int whosTurn = 0; //0 for player 1, 1 for player 2
        String p1 = player1symbol;
        String p2 = player2symbol; 
        String col;
        try (Scanner scanner = new Scanner(System.in)) { // Added try catch to fix a problem that didn't stop my code from working. I was annoyed by the problems notifcation. 
            while(spacesFilled != spacesMax){
                
                if(whosTurn == 0){
                    
                    System.out.println("Player 1, what column to drop chip?:");
                    col = scanner.nextLine();
                    int coll = Integer.parseInt(col);
                    addToBoard(coll,p1);
                    print();
                    whosTurn++;
                    spacesFilled++;
                }
                else{
                    System.out.println("Player 2, what column to drop chip?:");
                    col = scanner.nextLine();
                    int coll = Integer.parseInt(col);
                    addToBoard(coll,p2);
                    print();
                    whosTurn--;
                    spacesFilled++;
                }
                
                if(checkWin() == true){
                    if(winner.equals(player1symbol)){
                        winner = "Player 1";
                    }
                    else{
                        winner = "Player 2";
                    }
                    System.out.println("Winner: " + winner);
                    break;
                    
                }
            }
        } catch (NumberFormatException e) {
            System.out.println();
        }
        
    }

    public void playSingleplayer(){
        int spacesMax = 42;
        int spacesFilled = 0; 
        int whosTurn = 0; //0 for player 1, 1 for AI
        String p1 = player1symbol;
        int AIsymbol = 0; 
        AI eric = new AI(AIsymbol);
        String col;
        try (Scanner scanner = new Scanner(System.in)) { // Added try catch to fix a problem that didn't stop my code from working. I was annoyed by the problems notifcation. 
            while(spacesFilled != spacesMax){
                
                if(whosTurn == 0){
                    
                    System.out.println("Player 1, what column to drop chip?:");
                    col = scanner.nextLine();
                    int coll = Integer.parseInt(col);
                    addToBoard(coll,p1);
                    print();
                    whosTurn++;
                    spacesFilled++;
                }
                else{  // Fix this part. 
                    int coll = eric.nextMove(board, p1);
                    //int [][] x = eric.addToBoard(coll,AIsymbol,(eric.convertToInt(board, p1)));
                    //board = convertToString(x,p1,AIsymbol);
                    addToBoard(coll,String.valueOf(AIsymbol));
                    System.out.println("AI Eric put chip in column "+ coll + ".");
                    print();
                    whosTurn--;
                    spacesFilled++;
                }
                
                if(checkWin() == true){
                    if(winner.equals(player1symbol)){
                        winner = "Player 1";
                    }
                    else{
                        winner = "AI Eric";
                    }
                    System.out.println("Winner: " + winner);
                    break;
                    
                }
            }
        } catch (NumberFormatException e) {
            System.out.println();
        }
        
    }

    public String[][] convertToString(int[][] b, String p1, int AIsym){
        String AI = String.valueOf(AIsym);
        String[][] n = new String[6][7];
        for(int i = 0; i<6; i++)
        {
            for(int j = 0; j<7; j++)
            {
                if(b[i][j] == 0){
                    n[i][j] = "_";
                }
                else if(b[i][j] == 1){
                    n[i][j] = p1;
                }
                else{
                    n[i][j] = AI;
                }
            }
        }



        return n;
    }

    //"Drops" symbol into column area
    public void addToBoard(int col, String symbol){
        for(int i = 0; i<7; i++)
        {
            
                for(int j = 5; j >= 0; j--){//Iterates up the rows of the column i
                    if((board[j][i].equals("_"))&&(col-1)==i){
                        board[j][i] = symbol;
                        
                        break;
                    }
                }
            
        }

    }

    //checks if win conditions have been satisified
    public boolean checkWin(){
        if(checkVertical()==true || checkHoriz()==true || checkDiag()==true){
            return true;
        }
        return false;
    }

    //check Vertical Win Condition
    public boolean checkVertical(){
        boolean win = false;
        for(int i = 0; i < 7; i++){
            for(int j = 5; j>=3; j--){
                String alpha = board[j][i];
                String bravo = board[j-1][i];
                String charlie = board[j-2][i];
                String delta = board[j-3][i];
                if(alpha.equals(bravo)&&(!(alpha.equals("_")))){
                    if(bravo.equals(charlie)){
                        if(charlie.equals(delta)){
                            win = true; 
                            winner = charlie;
                        }
                    }
                }

            }
        }
        
        return win;
    }

    //check Horizontal Win Condition
    public boolean checkHoriz(){
        boolean win = false;
        for(int i = 0; i < 6; i++){
            //iterate through rows horizontally
            for(int j=0; j<4; j++){
                //only 4 because thats the max first column of a horizontal win before going over board 
                String alpha = board[i][j];
                String bravo = board[i][j+1];
                String charlie = board[i][j+2];
                String delta = board[i][j+3];
                if(alpha.equals(bravo)&&(!(alpha.equals("_")))){
                    if(bravo.equals(charlie)){
                        if(charlie.equals(delta)){
                            win = true;
                            winner = charlie;
                        }
                    }
                }

            }
        }
        return win;
    }

    //check Diagonal Win Condition 
    public boolean checkDiag(){
        //TLBR() Diagonally Top Left to Bottom Right
        //BLTR() Diagonally Bottom Left to Top Right
        if(TLBR() == true || BLTR() == true){
            return true; 
        }
        return false;
    }

    public boolean TLBR(){
        boolean win = false;
        for(int i = 0; i < 3; i++){
            //iterate through rows from topLeft to bottomRight diagonally for 4 slots 
            for(int j=0; j<4; j++){
                //only 4 because that's the max before going off the side.
                String alpha = board[i][j];
                String bravo = board[i+1][j+1];
                String charlie = board[i+2][j+2];
                String delta = board[i+3][j+3];
                if(alpha.equals(bravo)&&(!(alpha.equals("_")))){
                    if(bravo.equals(charlie)){
                        if(charlie.equals(delta)){
                            win = true;
                            winner = charlie;
                        }
                    }
                }

            }
        }
        return win;
    }

    public boolean BLTR(){
        boolean win = false;
        for(int i = 5; i >= 3; i--){
            //iterate through rows from BottomLeft to TopRight diagonally up 3 rows 
            for(int j=0; j<4; j++){
                //only 4 because that's the max before going off the top.
                String alpha = board[i][j];
                String bravo = board[i-1][j+1];
                String charlie = board[i-2][j+2];
                String delta = board[i-3][j+3];
                if(alpha.equals(bravo)&&(!(alpha.equals("_")))){
                    if(bravo.equals(charlie)){
                        if(charlie.equals(delta)){
                            win = true;
                            winner = charlie;
                        }
                    }
                }

            }
        }
        return win;
    }



}
