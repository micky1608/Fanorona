package sample;

public class Partie extends Thread {

    private Board board;

    public Partie() {

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
