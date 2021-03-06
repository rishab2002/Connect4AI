import java.util.Scanner;


public class Main {
    public static void main(String [] args){
    Scanner sc = new Scanner(System.in);
   
    System.out.println("Connect 4");
    System.out.println("Enter player 1 symbol: ");
    String P1Sym = sc.nextLine();
    System.out.println("Enter player 2 symbol: ");
    String P2Sym = sc.nextLine();
    Player p1 = new Player(P1Sym);// Using Player Instances to allow for more players in the future. 
    Player p2 = new Player(P2Sym);
    System.out.println("Player 1: "+p1.symbol+" Player 2: "+p2.symbol);
    Board b = new Board(p1.symbol,p2.symbol);
    System.out.print("Singleplayer 0 , Multiplayer 1: ");
    int answer = sc.nextInt();
    if(answer == 0){
        b.playSingleplayer();
    }
    else{
        b.playMultiplayer(); //Play Multiplayer

    }
    sc.close();


    }
}
