package sample;

import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameSimulator {

    // the board representing the state of the game
    private Board board;

    // the number of game simulation we will do from the initial board
    private static final int NB_SIMULATION = 3;

    // the pawns color of the player who is turn to play
    private Color colorPawnToPlay;


    /**
     * Constructor
     * @param board
     */
    public GameSimulator(Board board , PlayerCategory fisrtToPlay) {
        this.board = board;
        colorPawnToPlay = (fisrtToPlay.equals(PlayerCategory.USER) ? Node.getColorUser() : Node.getColorCpu());
        System.out.println("New Game Simulator starting with " + fisrtToPlay + "\n" + board);
    }

    /**
     * This method allows to start the simulation of the game from the initial configuration of the board
     * Random movements will be executed until the end of the game
     * According to winner, the method will have a different return     *
     * @return 0 if the user win
     * @return 1 if the computer win
     */
    public int simulate () {
        while(!isGameOver()) {
            System.out.println("Simulate One Move\n" + board);
            simulateOneMove();
            switchColorPawnToPlay();
        }

        System.out.println("END simulation\n" + board);
        return (getPlayerWinner().equals(PlayerCategory.USER) ? 0 : 1);
    }

    private void simulateOneMove() {

        // get a list which contains all tha pawns that can move this turn
        List<Node> nodesToMove = getPawnsToMove();

        // generate a random number between 0 and the list size less 1
        Random rand = new Random();
        int indice = rand.nextInt(nodesToMove.size());

        // get the random node that is selected to move
        Node nodeBeginning = nodesToMove.get(indice);

        // make this node move randomly
        board.randomMove(nodeBeginning);

    }

    /**
     * Indicate if the game is over
     * @return
     */
    private boolean isGameOver() {
        return (board.getNbPawnOnBoard(PlayerCategory.USER) == 0) || (board.getNbPawnOnBoard(PlayerCategory.COMPUTER) == 0);
    }

    /**
     * Get the winner of the game
     * @return
     */
    private PlayerCategory getPlayerWinner() {
        if(board.getNbPawnOnBoard(PlayerCategory.USER) == 0)
            return PlayerCategory.COMPUTER;
        else if(board.getNbPawnOnBoard(PlayerCategory.COMPUTER) == 0)
            return PlayerCategory.USER;

        return null;
    }

    /**
     * Get the pawns that can move of the active player
     * @return
     */
    private List<Node> getPawnsToMove () {
        List<Node> nodes = new ArrayList<>();

        for(int i=0 ; i<9 ; i++) {
            for(int j=0 ; j<5 ; j++) {

                // get the node with (i,j) coordinate
                Node node = board.getNodes()[i][j];

                // check if the color of the node is correct according to the active player
                if(node.getFill().equals(this.colorPawnToPlay)) {

                    // check if a move is possible
                    if(board.canMove(node))
                        nodes.add(node);
                }
            }
        }
        return nodes;
    }


    /**
     * Method to change the active player by switching the color of the pawns that have to move
     */
    private void switchColorPawnToPlay() {
        if (this.colorPawnToPlay.equals(Node.getColorCpu()))
            colorPawnToPlay = Node.getColorUser();
        else if (this.colorPawnToPlay.equals(Node.getColorUser()))
            colorPawnToPlay = Node.getColorCpu();
    }




}
