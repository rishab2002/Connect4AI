import java.util.concurrent.ThreadLocalRandom;
public class AI {
    int symbol;
    public AI(int s){
        symbol = s;
        
    }

    //Pass in the board at the current stage.
    //Utilize Java AI library and figure out next best solution
    //to get a connect4. 
    //Return which column number to put chip.  
    //Also implement AI to block the player. 
    public int nextMove(String[][] board, String p1){
        int [][] base = convertToInt(board, p1); //1 for Player ;2 for AI; 0 for None _ 
        for(int i = 0; i < 7; i ++){
            int [][] current = addToBoard(i, 2, base);
            if(checkWin(current)){
                return i;
            }
            else{
                for(int j = 0; j < 7; j++){
                    int [][] current2 = addToBoard(j,1,current);
                    if(checkWin(current2)){ // checks if the player will win if they put it into the current j column. 
                        break;
                    }
                    else{
                        for(int k = 0; k < 7; k++){
                            int [][] current3 = addToBoard(k, 2, current2);
                            if(checkWin(current3)){
                                return i;
                            }
                            else{
                                if(i == 6){
                                    //Generate Random Column because no column number works
                                    return ThreadLocalRandom.current().nextInt(0, 6 + 1);
                                }
                                else{
                                    break;
                                }
                            }
                        }
                    }
                }

            }
        }

        return 0;
    }

    //Checks for Win
    public boolean checkWin(int[][] current){
        if(checkVertical(current)==true || checkHoriz(current)==true || checkDiag(current)==true){
            return true;
        }
        return false;
    }

        //check Vertical Win Condition
        public boolean checkVertical(int[][] board){
            boolean win = false;
            for(int i = 0; i < 7; i++){
                for(int j = 5; j>=3; j--){
                    int alpha = board[j][i];
                    int bravo = board[j-1][i];
                    int charlie = board[j-2][i];
                    int delta = board[j-3][i];
                    if(alpha == (bravo)&&(!(alpha == (0)))){
                        if(bravo ==(charlie)){
                            if(charlie ==(delta)){
                                win = true; 
                                
                            }
                        }
                    }
    
                }
            }
            
            return win;
        }
    
        //check Horizontal Win Condition
        public boolean checkHoriz(int[][] board){
            boolean win = false;
            for(int i = 0; i < 6; i++){
                //iterate through rows horizontally
                for(int j=0; j<4; j++){
                    //only 4 because thats the max first column of a horizontal win before going over board 
                    int alpha = board[i][j];
                    int bravo = board[i][j+1];
                    int charlie = board[i][j+2];
                    int delta = board[i][j+3];
                    if(alpha == (bravo)&&(!(alpha == (0)))){
                        if(bravo ==(charlie)){
                            if(charlie ==(delta)){
                                win = true; 
                                
                            }
                        }
                    }
    
                }
            }
            return win;
        }
    
        //check Diagonal Win Condition 
        public boolean checkDiag(int[][] board){
            //TLBR() Diagonally Top Left to Bottom Right
            //BLTR() Diagonally Bottom Left to Top Right
            if(TLBR(board) == true || BLTR(board) == true){
                return true; 
            }
            return false;
        }
    
        public boolean TLBR(int[][] board){
            boolean win = false;
            for(int i = 0; i < 3; i++){
                //iterate through rows from topLeft to bottomRight diagonally for 4 slots 
                for(int j=0; j<4; j++){
                    //only 4 because that's the max before going off the side.
                    int alpha = board[i][j];
                    int bravo = board[i+1][j+1];
                    int charlie = board[i+2][j+2];
                    int delta = board[i+3][j+3];
                    if(alpha == (bravo)&&(!(alpha ==(0)))){
                        if(bravo ==(charlie)){
                            if(charlie == (delta)){
                                win = true;
                            }
                        }
                    }
    
                }
            }
            return win;
        }
    
        public boolean BLTR(int[][] board){
            boolean win = false;
            for(int i = 5; i >= 3; i--){
                //iterate through rows from BottomLeft to TopRight diagonally up 3 rows 
                for(int j=0; j<4; j++){
                    //only 4 because that's the max before going off the top.
                    int alpha = board[i][j];
                    int bravo = board[i-1][j+1];
                    int charlie = board[i-2][j+2];
                    int delta = board[i-3][j+3];
                    if(alpha==(bravo)&&(!(alpha==(0)))){
                        if(bravo==(charlie)){
                            if(charlie ==(delta)){
                                win = true;
                                
                            }
                        }
                    }
    
                }
            }
            return win;
        }
    //Adds symbol to column number given and returns the board after. 
    public int[][] addToBoard(int col, int symbol, int[][] base){
        for(int i = 0; i<7; i++)
        {
                for(int j = 5; j >= 0; j--){//Iterates up the rows of the column i
                    if((base[j][i]==0)&&(col-1)==i){
                        base[j][i] = symbol;
                        
                        break;
                    }
                }    
        }
        return base;
    }

    //Converts the board at the current stage to integers.
    //1 for Player
    //2 for AI
    //0 for Neither
    public int[][] convertToInt(String[][] b, String p1){
        int [][] matrix = new int[6][7]; 
        for(int i = 0; i<6; i++) //Converts String board into a double matrix of ints. 
        {
            for(int j = 0; j<7; j++)
            {
                if(b[i][j].equals(p1)){
                    matrix[i][j] = 1; 
                }
                else if(b[i][j].equals(String.valueOf(symbol))) {
                    matrix[i][j] = 2;
                }
                else{
                    matrix[i][j] = 0;
                }
            }
        } // _ == 0, P1 == 1 , P2 == 2. */
        return matrix;
    }
}
