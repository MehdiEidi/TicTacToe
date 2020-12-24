package ds.thirdProject;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();

        game.printCurrentBoard();

        Scanner input = new Scanner(System.in);

        while (game.checkForWinner()) {
            System.out.println("Your Turn. ");
            System.out.print("Your moves row: ");
            int row = input.nextByte();
            System.out.print("Your moves column: ");
            int column = input.nextByte();

            game.playersMove(row, column);

            game.computersMove();

            game.printCurrentBoard();
        }
    }
}