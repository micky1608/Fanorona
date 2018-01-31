package sample;

public class Partie extends Thread {

    private Plateau plateau;

    public Partie() {

    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.plateau = new Plateau();
    }
}
