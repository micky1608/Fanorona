package sample;

import javafx.scene.paint.Color;

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
    public GameSimulator(Board board) {
        this.board = board;
        colorPawnToPlay = Node.getColorCpu();
    }

    /**
     * This method allows to start the simulation of the game from the initial configuration of the board
     * Random movements will be executed until the end of the game
     * According to winner, the method will have a different return     *
     * @return 0 if the user win
     * @return 1 if the computer win
     */
    public int simule () {
        //TODO
        return 0;
    }





}
