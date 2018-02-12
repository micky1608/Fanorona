package sample;

public class Game extends Thread {

    private Board board;

    public Game() {

    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.board = new Board();
    }
}
