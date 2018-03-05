package sample;

import javafx.scene.paint.Color;

public class Game extends Thread {

    private Board board;
    private Player computer;
    private Player user;

    private PlayerCategory playerTurn;

    private int nbComputerPawnBeginTurn;
    private int nbUserPawnBeginTurn;


    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.board = new Board(this);
        this.computer = new Computer(this);
        this.user = new User(this);
        this.playerTurn = PlayerCategory.USER;
        this.nbComputerPawnBeginTurn = 0;
        this.nbUserPawnBeginTurn = 0;
        new GameSimulator(board.clone() , PlayerCategory.USER).simulate();
        try {
            startGame();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Indicate if the game is over
     * @return
     */
    private boolean isGameOver() {
        return (board.getNbPawnOnBoard(PlayerCategory.USER) == 0) || (board.getNbPawnOnBoard(PlayerCategory.COMPUTER) == 0);
    }


    /**
     * change the player to play
     */
    public void switchPlayerTurn() {
       if (this.playerTurn.equals(PlayerCategory.USER))
           this.playerTurn = PlayerCategory.COMPUTER;

       else if (this.playerTurn.equals(PlayerCategory.COMPUTER))
           this.playerTurn = PlayerCategory.USER;
    }

    /**
     * tell if the player captured pawn during his last movement
     * @return
     */
    public boolean capturePawn(PlayerCategory playerCategory) {
        switch (playerCategory) {
            case USER:
                return board.getNbPawnOnBoard(PlayerCategory.COMPUTER) < nbComputerPawnBeginTurn;
            case COMPUTER:
                return board.getNbPawnOnBoard(PlayerCategory.USER) < nbUserPawnBeginTurn;
        }
        return false;
    }

    /**
     * tell if a node can capture pawn from his position
     * @param nodeBeginning
     * @return
     */
    public boolean existCapture (Node nodeBeginning) {
        // get the opponent color to check if the capture is posible when a neighbour is found
        Color opponentColor = (nodeBeginning.getFill().equals(Node.getColorUser()) ? Node.getColorCpu() : Node.getColorUser());

        // get the position od the beginning node
        int actualX = nodeBeginning.getX();
        int actualY = nodeBeginning.getY();

        // search among the neighbours if exist an empty node to use as a destination
        for(int i=-1 ; i<=1 ; i++) {
            for(int j=-1 ; j<=1 ; j++) {

                // check if this is a correct neighbour
                // if the node id odd, add the condition i==0 || j==0
                if ((actualX + i) < 9 && (actualX + i) >= 0 && (actualY + j) < 5 && (actualY + j) >= 0 && !(i == 0 && j == 0) && (nodeBeginning.isEven() ? true : (i == 0 || j == 0))) {

                    Node potentialDestination = board.getNodes()[actualX + i][actualY + j];

                    // check if this an empty node we can move to
                    if (potentialDestination.getFill().equals(Node.getColorEmpty())) {
                        // check if move to this potential destination allow capture some opponent's pawns

                        // check if capture by percussion is possible
                        Node testPercussionNode = board.getNodes()[potentialDestination.getX() + i][potentialDestination.getY() + j];
                        if (testPercussionNode.getFill().equals(opponentColor))
                                return true;

                        // check if capture by aspiration is possible
                        Node testAspirationNode = board.getNodes()[nodeBeginning.getX() - i][nodeBeginning.getY() - j];
                        if (testAspirationNode.getFill().equals(opponentColor))
                            return true;

                    }
                }
            }
        }

        return false;
    }

    private void startGame() throws InterruptedException {
        while (!isGameOver()) {

            //enable the clicks on the nodes
            board.setDisableAllNodes(false);

            // user plays as much times as the rules allow
            while(playerTurn.equals(PlayerCategory.USER)) {
                this.nbUserPawnBeginTurn = board.getNbPawnOnBoard(PlayerCategory.USER);
                this.nbComputerPawnBeginTurn = board.getNbPawnOnBoard(PlayerCategory.COMPUTER);
                user.play();
                System.out.println("User captured pawns : " + capturePawn(PlayerCategory.USER));
                if(isGameOver())
                    finishGame();
            }

            // disable the clicks by the user
            board.setDisableAllNodes(true);

            // computer plays as much times as the rules allow
            while(playerTurn.equals(PlayerCategory.COMPUTER) && !isGameOver()) {
                this.nbUserPawnBeginTurn = board.getNbPawnOnBoard(PlayerCategory.USER);
                this.nbComputerPawnBeginTurn = board.getNbPawnOnBoard(PlayerCategory.COMPUTER);
                computer.play();
                if(isGameOver())
                    finishGame();
            }
        }
    }

    private void finishGame() {
        board.setTextInConsole("Game finished : " + (getPlayerWinner().equals(PlayerCategory.USER) ? "You win" : "You lose"));
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

    public void setTextInConsole (String text) {
        board.setTextInConsole(text);
    }

    public boolean existNodeSelected() {
        return board.existNodeSelected();
    }

    public Node getNodeSelected() {
        return board.getNodeSelected();
    }

    public Node getDestinationNodeSelected() {
        return board.getNodeDestinationSelected();
    }

    public Board getBoard(){
        return board;
    }

}
