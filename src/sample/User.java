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
    public void selectNodeBeginning() throws InterruptedException {

        // tell the user that he has to choose a pawn
        game.setTextInConsole("Your turn : Choose a pawn to move");

        // pause the game thread until the user select a pawn
        // this thread will be waked by the JavaFX thread when the user will click on a node containing a pawn
        //(see Node.java in function "select()")
        synchronized (game) {
            game.wait();
        }

        // when arriving here, a pawn is selected
        // tell the user that his selection worked
        game.setTextInConsole("Pawn selected");

        // get the node just selected
        // MAYBE not necessary
        this.nodeSelectedBeginning = game.getNodeSelected();

        if (game.getBoard().possibleCapture(nodeSelectedBeginning).isEmpty()){
            System.out.println("Can't capture with this pawn");
        }
        else{
            System.out.print("Capture possible:");
            for(Node n:game.getBoard().possibleCapture(nodeSelectedBeginning)){
                System.out.print("["+n.getX()+","+n.getY()+"]");
            }
            System.out.println("");
        }
    }

    @Override
    public void selectNodeEnd() throws InterruptedException {
        // tell the user that he has to choose a destination node for the node he selected before
        game.setTextInConsole("Your turn : Choose a correct destination");

        // pause the game thread until the user select a destination node
        // this thread will be waked by the JavaFX thread when the user will click on a empty node
        // this empty node must be a neighbor of the selected node
        //(see Node.java in function "select()")
        synchronized (game) {
            game.wait();
        }

        // when arriving here, a correct destination was chosen
        game.setTextInConsole("Movement done");

        // get this destination node
        // MAYBE not necessary
        this.nodeSelectedEnd = game.getDestinationNodeSelected();
    }
}
