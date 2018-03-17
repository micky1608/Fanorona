package application;

import javafx.application.Platform;

import java.io.IOException;
import java.lang.management.PlatformLoggingMXBean;

public class Game extends Thread {

    private Board board;
    private Player computer;
    private Player user;

    private PlayerCategory playerTurn;

    private int nbComputerPawnBeginTurn;
    private int nbUserPawnBeginTurn;

    private boolean replay;

    //Used to calculate the number of win by our "main" IA vs an other IA.
    private int nbWin;
    private int nbLose;
    private int evaluation;


    @Override
    public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        this.board = new Board(this);
        this.computer = new Computer(this, false);
        this.user = new User(this);

        this.playerTurn = PlayerCategory.USER;
        this.nbComputerPawnBeginTurn = 0;
        this.nbUserPawnBeginTurn = 0;
        try {
            startGame();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

            /*for(int i=0;i<200;i++) {
                this.board = new Board(this);
                this.computer = new Computer(this, false);
                //First line to play against the IA
                //Second line to test the IA against an IA playing randomly.

                //this.user = new User(this);
                this.user = new Computer(this, true);

                this.playerTurn = PlayerCategory.USER;
                this.nbComputerPawnBeginTurn = 0;
                this.nbUserPawnBeginTurn = 0;
                try {
                    startGame();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Win:"+nbWin);
                System.out.println("Lose:"+nbLose+"\n");
            }
            System.out.println("Win:"+nbWin);
            System.out.println("Lose:"+nbLose+"\n");
*/
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
        //If no pawn was taken last turn, we change player turn.
        if(evaluation == board.getNbPawnOnBoard(PlayerCategory.USER)-board.getNbPawnOnBoard(PlayerCategory.COMPUTER)) {
            if (this.playerTurn.equals(PlayerCategory.USER)) {
                this.playerTurn = PlayerCategory.COMPUTER;
            } else if (this.playerTurn.equals(PlayerCategory.COMPUTER)) {
                this.playerTurn = PlayerCategory.USER;
            }
            replay=false;
        }
        //If pawns was taken but we can't capture any other pawn with the same pawn we used last turn, we change player turn.
        else if(this.playerTurn.equals(PlayerCategory.COMPUTER)&&board.possibleCapture(computer.getLastPlayed()).isEmpty()) {
            this.playerTurn = PlayerCategory.USER;
            replay=false;
        }
        else if(this.playerTurn.equals(PlayerCategory.USER)&&board.possibleCapture(user.getLastPlayed(),getUser().getAlreadyVisited()).isEmpty()) {
            this.playerTurn = PlayerCategory.COMPUTER;
            replay = false;
        }
        else{
            replay=true;
        }
        evaluation= board.getNbPawnOnBoard(PlayerCategory.USER)-board.getNbPawnOnBoard(PlayerCategory.COMPUTER);

    }

    private void startGame() throws InterruptedException, IOException {
        while (!isGameOver()) {
            System.out.println("PlayerTunr :"+playerTurn);
            System.out.println("Replay :"+replay);

            //enable the clicks on the nodes
            board.setDisableAllNodes(false);

            // user plays as much times as the rules allow
            if(playerTurn.equals(PlayerCategory.USER)) {
                System.out.println("a");
                this.nbUserPawnBeginTurn = board.getNbPawnOnBoard(PlayerCategory.USER);
                this.nbComputerPawnBeginTurn = board.getNbPawnOnBoard(PlayerCategory.COMPUTER);
                if (!board.canPlay(Node.getColorUser())){
                    finishGame();
                }
                user.play();
                if(isGameOver())
                    finishGame();
            }

            // disable the clicks by the user
            board.setDisableAllNodes(true);

            // computer plays as much times as the rules allow
            if(playerTurn.equals(PlayerCategory.COMPUTER) && !isGameOver()) {
                sleep(1000);
                System.out.println("b");
                this.nbUserPawnBeginTurn = board.getNbPawnOnBoard(PlayerCategory.USER);
                this.nbComputerPawnBeginTurn = board.getNbPawnOnBoard(PlayerCategory.COMPUTER);
                if (!board.canPlay(Node.getColorCpu())){
                    finishGame();
                }
                computer.play();
                if(isGameOver())
                    finishGame();
            }
        }
    }

    private void finishGame() throws InterruptedException, IOException {
        board.setTextInConsole("Game finished : " + (getPlayerWinner().equals(PlayerCategory.USER) ? "You win" : "You lose"));
        nbWin+=getPlayerWinner().equals(PlayerCategory.COMPUTER)?1:0;
        nbLose+=getPlayerWinner().equals(PlayerCategory.USER)?1:0;

        Platform.runLater(() -> {
            try {
                Windows.changeScene(Main.getController().getStage() , "endGame.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        sleep(300);
        Main.getEndGameController().setGame(this);

    }

    /**
     * Get the winner of the game
     * @return
     */
    public PlayerCategory getPlayerWinner() {
        if(board.getNbPawnOnBoard(PlayerCategory.USER) == 0)
            return PlayerCategory.COMPUTER;
        else if(board.getNbPawnOnBoard(PlayerCategory.COMPUTER) == 0)
            return PlayerCategory.USER;
        else if(!this.board.canPlay(Node.getColorUser()))
            return PlayerCategory.COMPUTER;
        else if(!this.board.canPlay(Node.getColorCpu()))
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

    public PlayerCategory getPlayerTurn() {
        return playerTurn;
    }

    public void setUserPawnDeselected(boolean pawnDeselected) {
        ((User)user).setPawnDeselected(pawnDeselected);
    }

    public Player getComputer(){
        return computer;
    }

    public boolean isReplay() {
        return replay;
    }

    public Player getUser() {
        return user;
    }
}
