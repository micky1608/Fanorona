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
        this.board = new Board(this);
        this.computer = new Computer(this);
        this.user = new User(this);
        this.playerTurn = PlayerCategory.USER;
        this.nbComputerPawnBeginTurn = 0;
        this.nbUserPawnBeginTurn = 0;
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

    public Node getNodeSelected() {
        return board.getNodeSelected();
    }

    public Node getDestinationNodeSelected() {
        return board.getNodeDestinationSelected();
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board){
        this.board=board;
    }

    public void setUserPawnDeselected(boolean pawnDeselected) {
        ((User)user).setPawnDeselected(pawnDeselected);
    }
}
