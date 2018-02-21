package sample;

public class Computer extends Player {

    public Computer(Game game) {
        super(game);
    }

    @Override
    public void selectNodeBeginning() {
        game.setTextInConsole("Computer PLAY");
        //TODO
    }

    @Override
    public void selectNodeEnd() {
        //TODO
    }
}
