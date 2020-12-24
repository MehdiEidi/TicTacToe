package ds.thirdProject;

import java.util.Random;

public class TicTacToe {
    private String[][] currentStatesBoard;
    private GameTree.Node currentStatesNode; //Current state's node among the game tree's nodes.
    private final GameTree gameTree;

    /**
     * Constructing and initializing a Tic Tac Toe game.
     */
    public TicTacToe() {
        //Constructing the game tree, filling it with all possible moves and filling the scores using miniMax algorithm.
        gameTree = new GameTree();
        gameTree.fillTree();
        gameTree.miniMax(gameTree.getRoot(), true);

        //Computer chooses the first move randomly.
        Random random = new Random();
        currentStatesNode = gameTree.getRoot().getChildren().get(random.nextInt(9));
        currentStatesBoard = currentStatesNode.getBoard();
    }

    /**
     * Prints the current game state's board.
     */
    public void printCurrentBoard() {
        currentStatesNode.printTheBoard();
    }

    /**
     * Plays the player's move.
     * @param row row of the player's move.
     * @param column column of the player's move.
     */
    public void playersMove(int row, int column) {
        currentStatesBoard[row][column] = "|  o  |";
    }

    /**
     * Plays the computer's move.
     */
    public void computersMove() {
        GameTree.Node playersMoveNode = gameTree.findNode(currentStatesBoard, currentStatesNode);
        int score = -10;

        //Finds the node with maximum score and plays that move (that node becomes the current state of the game).
        for (GameTree.Node child : playersMoveNode.getChildren()) {
            if (child.getScore() > score) {
                score = child.getScore();
                currentStatesNode = child;
            }
        }

        currentStatesBoard = currentStatesNode.getBoard();
    }

    /**
     * Checks to see if we have a winner or tie or the game is still going on.
     * @return true, if the game is still going on (there is empty spot on the board).
     */
    public boolean checkForWinner() {
        int currentStatesValue = gameTree.evaluateBoard(currentStatesNode);

        if (currentStatesValue == 1) {
            System.out.println("Computer Won! Shame on you!");
        } else if (currentStatesValue == -1) {
            System.out.println("You Won! Cool, you have done the impossible!");
        } else if (currentStatesValue == 0){
            System.out.println("Tie!");
        } else {
            return true;
        }

        return false;
    }
}