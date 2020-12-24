package ds.thirdProject;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class GameTree {
    /**
     * Each node represents a game configuration (board, score). Each node's children represent all possible next moves.
     */
    static class Node {
        private final List<Node> children = new ArrayList<>();
        private final String[][] board;
        private int score;
        private boolean filledWithChildren = false;

        /**
         * Constructing a node using a board.
         * @param board will be the node's board.
         */
        public Node(String[][] board) {
            this.board = board;
        }

        public boolean isFilledWithChildren() {
            return filledWithChildren;
        }

        public List<Node> getChildren() {
            return children;
        }

        public String[][] getBoard() {
            return board;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public void setFilledWithChildren(boolean filledWithChildren) {
            this.filledWithChildren = filledWithChildren;
        }

        //Prints a node's board in a Tic Tac Toa fashion!
        public void printTheNode() {
            System.out.println("\n");

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(board[i][j]);
                }
                System.out.println("\n");
            }

            System.out.println("-------------------------------------------------------");
        }

        private void addChild(String[][] board) {
            this.getChildren().add(new Node(board));
        }
    }

    private final Node root; //Initial configuration of the game (Empty Board).

    /**
     * Constructs a game tree (makes all spots empty).
     */
    public GameTree() {
        String[][] board = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "|empty|";
            }
        }

        root = new Node(board);
    }

    public Node getRoot() {
        return root;
    }

    //Copies one array to other.
    private void copyArray(String[][] board, String[][] board2) {
        for (int i = 0; i < 3; i++) {
            System.arraycopy(board2[i], 0, board[i], 0, 3);
        }
    }

    //Fills all children of a node based on the current configuration of the board.
    private void fillChildren(Node current, boolean isX) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (current.getBoard()[i][j].equals("|empty|")) {
                    String[][] child = new String[3][3];
                    copyArray(child, current.getBoard());

                    if (isX) {
                        child[i][j] = "|  x  |";
                    } else {
                        child[i][j] = "|  o  |";
                    }

                    current.addChild(child);
                }
            }
        }
    }

    /**
     * Fills the tree with all possible moves.
     */
    public void fillTree() {
        boolean isX = true;
        int depth = 0;

        while (depth < 10) {
            findAndFillNodes(this.getRoot(), isX);
            isX = !isX;
            depth++;
        }
    }

    //Traverses the tree and finds the nodes which haven't been filled yet and fills them.
    private void findAndFillNodes(Node node, boolean isX) {
        for (Node child : node.getChildren()) {
            findAndFillNodes(child, isX);
        }

        if (!node.isFilledWithChildren()) {
            fillChildren(node, isX);
            node.setFilledWithChildren(true);
        }
    }

    /**
     * Traverses the tree and finds scores of each node using minimax algorithm.
     * @param node current node.
     * @param isMaximiser if true, the current player is the maximiser.
     * @return score of the node.
     */
    public int miniMax(Node node, boolean isMaximiser) {
        int currentScore = evaluateBoard(node);

        if (currentScore != -5) {
            return currentScore;
        }

        int bestScore;
        if (isMaximiser) {
            bestScore = (int) Double.NEGATIVE_INFINITY;

            for (Node child : node.getChildren()) {
                int score = miniMax(child, false);
                bestScore = Math.max(score, bestScore);
                child.setScore(bestScore);
            }
        } else {
            bestScore = (int) Double.POSITIVE_INFINITY;

            for (Node child : node.getChildren()) {
                int score = miniMax(child, true);
                bestScore = Math.min(score, bestScore);
                child.setScore(bestScore);
            }
        }

        return bestScore;
    }

    /**
     * Evaluates the current situation of the board.
     * @param node current node.
     * @return 1, if x has won. -1, if o has won. -5, if game is still running.
     */
    public int evaluateBoard(Node node) {
        String[][] board = node.getBoard();

        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                if (board[i][0].equals("|  x  |")) {
                    return 1;
                } else if (board[i][0].equals("|  o  |")) {
                    return -1;
                }
            }
        }

        for (int j = 0; j < 3; j++) {
            if (board[0][j].equals(board[1][j]) && board[1][j].equals(board[2][j])) {
                if (board[0][j].equals("|  x  |")) {
                    return 1;
                } else if (board[0][j].equals("|  o  |")) {
                    return -1;
                }
            }
        }

        if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            if (board[0][0].equals("|  x  |")) {
                return 1;
            } else if (board[0][0].equals("|  o  |")) {
                return -1;
            }
        }

        if (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            if (board[0][2].equals("|  x  |")) {
                return 1;
            } else if (board[0][2].equals("|  o  |")) {
                return -1;
            }
        }

        int countOfEmptySpots = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equals("|empty|")) {
                    countOfEmptySpots++;
                }
            }
        }

        if (countOfEmptySpots == 0) {
            return 0;
        }

        //-5 means game is going on
        return -5;
    }

    private boolean areBoardsSame(String[][] board1, String[][] board2) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!board1[i][j].equals(board2[i][j])) {
                    return false;
                }
            }
        }

        return true;
    }

    //Finds the node among other node's children using the given board.
    public Node findNode(String[][] board, Node currentNode) {
        for (Node child : currentNode.getChildren()) {
            if (areBoardsSame(child.getBoard(), board)) {
                return child;
            }
        }

        throw new NoSuchElementException("Couldn't find the node among children using the given board.");
    }
}