package sample;

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
        this.board = new Board();
        this.computer = new Computer(this);
        this.user = new User(this);
        this.playerTurn = PlayerCategory.USER;
        startGame();
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
     * tell if the player captured pawn during his last mouvment
     * @return
     */
    public boolean capturePawn(PlayerCategory playerCategory) {
        //TODO
        return false;
    }

    /**
     * tell if a node can capture pawn from his position
     * @param nodeBeginning
     * @return
     */
    public boolean existCapture (Node nodeBeginning) {
        //TODO
        return false;
    }

    private void startGame() {
        while (!isGameOver()) {
            while(playerTurn.equals(PlayerCategory.USER)) {
                user.play();
                if(isGameOver())
                    finishGame();
            }

            while(playerTurn.equals(PlayerCategory.COMPUTER)) {
                computer.play();
                if(isGameOver())
                    finishGame();
            }
        }
        finishGame();
    }

    private void finishGame() {
        //TODO
    }


}
