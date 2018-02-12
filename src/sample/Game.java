package sample;

public class Game extends Thread {

    private Board board;
    private Player computer;
    private Player user;



    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.board = new Board();
        this.computer = new Computer();
        this.user = new User();
    }

    /**
     * Indicate if the game is over
     * @return
     */
    private boolean isGameOver() {
        //TODO
        return false;
    }

    /**
     * Return the player who is turn to play
     * @return
     */
    private Player getActivePlayer () {
        //TODO
        return null;
    }






}
