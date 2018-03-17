package application;

import java.util.ArrayList;

public abstract class Player {

    // the gale the player is playing
    protected Game game;

    protected Node nodeSelectedBeginning;

    protected Node nodeSelectedEnd;

    /**
     * Constructor
     * @param game
     */
    public Player(Game game) {
        this.game = game;
    }

    public void play() throws InterruptedException {
        selectNodeBeginning();
        selectNodeEnd();
        endTurn();
    }

    public abstract void selectNodeBeginning() throws InterruptedException;

    public abstract void selectNodeEnd() throws InterruptedException;

    public abstract Node getLastPlayed();

    public abstract ArrayList<Node> getAlreadyVisited();

    public abstract void setLastPlayed(Node node);

    protected void endTurn() {
        game.switchPlayerTurn();
    }
}
