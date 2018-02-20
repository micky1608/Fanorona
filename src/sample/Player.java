package sample;

public abstract class Player {

    // the gale the player is playing
    protected Game game;

    /**
     * Constructor
     * @param game
     */
    public Player(Game game) {
        this.game = game;
    }

    public abstract void play();

    public abstract void selectNodeBeginning();

    public abstract void selectNodeEnd();

    private void endturn() {
        game.switchPlayerTurn();
    }
}
