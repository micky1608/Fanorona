package sample;

public class User extends Player {

    /**
     * Constructor
     * @param game
     */
    public User(Game game) {
        super(game);
    }

    @Override
    public void play() {
        //TODO
        endTurn();
    }

    @Override
    public void selectNodeBeginning() {
        //TODO
    }

    @Override
    public void selectNodeEnd() {
        //TODO
    }
}
