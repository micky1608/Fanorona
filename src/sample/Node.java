package sample;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


public class Node extends Circle {

    private boolean containsPawn;
    private boolean isNodeSelected;
    private boolean isAspirable;
    private boolean isPercutable;

    // A node is even when having 8 neighbours (or 3 when is in a corner)
    private boolean isEven;

    private Board board;

    // Positions of the nodes in the grid.
    // The origin is on the top left corner.
    /*
        (0,0) (1,0) (2,0) (3,0) (4,0) (5,0) (6,0) (7,0) (8,0)
        (0,1) (1,1) (2,1) (3,1) (4,1) (5,1) (6,1) (7,1) (8,1)
        (0,2) (1,2) (2,2) (3,2) (4,2) (5,2) (6,2) (7,2) (8,2)
        (0,3) (1,3) (2,3) (3,3) (4,3) (5,3) (6,3) (7,3) (8,3)
        (0,4) (1,4) (2,4) (3,4) (4,4) (5,4) (6,4) (7,4) (8,4)
     */
    private int posX;
    private int posY;

    private static final double RADIUS_NODE_EMPTY = 15;
    private static final double RADIUS_NODE_PAWN = 25;
    private static final double RADIUS_NODE_PAWN_SELECTED = 30;
    private static final Color COLOR_PLAYER = Color.WHITE;
    private static final Color COLOR_CPU = Color.BLACK;
    private static final Color COLOR_EMPTY = Color.GREY;



    /**
     * CONSTRUCTOR
     * Create a node considering the position x and y.
     * @param x
     * @param y
     */
    public Node(int x , int y , Board board) throws IllegalArgumentException {
        if(x < 0 || x > 8 || y < 0 || y > 4)
            throw new IllegalArgumentException();

        this.posX = x;
        this.posY = y;
        this.board = board;
        this.isNodeSelected = false;

        if((x+y)%2==0){this.isEven = true; }
        else{this.isEven = false; }

        //The handler will be different considering the moment of the game.
        this.setOnMouseClicked((event) -> {

                // The button was clicked when choosing beetween percussion and aspiration
                // Here we don't select a node to move
                if(this.isPercutable)
                    this.exclude(1);
                if(this.isAspirable){
                    this.exclude(0);
                }

                // The button is clicked to be selected or deselected
                // We can do this action only when all the nodes are not percutables nor aspirables
                if(!this.isNodeSelected()&&!this.board.existNodeAspirable()&&!this.board.existNodePercutable())
                    this.select(true);
                else
                    this.deselect();
        });

        }

    /**
     * GETTERS ANS SETTERS
     */

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public boolean isNodeSelected () {
        return this.isNodeSelected;
    }

    public boolean isContainsPawn () {
        return this.containsPawn;
    }

    public static double getRadiusNodePawn() {
        return RADIUS_NODE_PAWN;
    }

    public static double getRadiusNodePawnnSelected() {
        return RADIUS_NODE_PAWN_SELECTED;
    }

    public void setPercutable(boolean val){
        this.isPercutable=val;
    }

    public void setAspirable(boolean val){
        this.isAspirable=val;
    }

    public boolean getAspirable(){
        return isAspirable;
    }

    public boolean getPercutable(){
        return isPercutable;
    }

    /**
     * Updates the boolean which indicates if the node contains a pawn or not, and updates he size and color.
     * @param containsPawn
     * @param colorCode defines the new color of the node.
     *                    0 : Player's color (white)
     *                    1 : CPU's color (black)
     *                    2 : Node is empty (grey)
     */
    public void setContainsPawn (boolean containsPawn , int colorCode) {
        if(colorCode < 0 ||  colorCode > 2)
            throw new IllegalArgumentException();

        this.containsPawn = containsPawn;

        Color color;
        double radius = 0;

        switch(colorCode) {
            case 0 :
                color = COLOR_PLAYER;
                break;
            case 1 :
                color = COLOR_CPU;
                break;
            default :
                color = COLOR_EMPTY;
                break;
        }

        if(this.containsPawn)
            radius = RADIUS_NODE_PAWN;
        else
            radius = RADIUS_NODE_EMPTY;

        this.setRadius(radius);
        this.setFill(color);
    }

    /**
     * Allows to select a node
     * @param selectByUser indicates if the action is made by the player or the CPU.
     *                     true : player -> can only select white pawns.
     *                     false : CPU -> can only select black pawns.
     */
    public void select (boolean selectByUser) {
        Paint colorNode = this.getFill();

        if(colorNode.equals(Color.WHITE) || colorNode.equals(Color.BLACK)) {

            // If we try to select a pawn, no other pawn should be selected at the same time.
            if(!board.existNodeSelected()) {

                if (selectByUser) {
                    if (colorNode.equals(Color.WHITE))
                        this.isNodeSelected = true;
                } else {
                    if (colorNode.equals(Color.BLACK))
                        this.isNodeSelected = true;
                }
            }
        }
        else {

            // We selected an empty node, so there must be a pawn selected which will make
            // the movement from his position to the empty node selected.
            //The nodes must be neighbours
            if(board.existNodeSelected()) {
                Node nodeBeginningMovement = board.getNodeSelected();

                if(this.isNeighborOf(nodeBeginningMovement)) {

                    // We get the color of the initial node.
                    int colorCode = 2;
                    if(nodeBeginningMovement.getFill().equals(Color.WHITE)) colorCode = 0;
                    if(nodeBeginningMovement.getFill().equals(Color.BLACK)) colorCode = 1;

                    // Indicates that the initial node will now be empty.
                    nodeBeginningMovement.setContainsPawn(false , 2);

                    // The initial node won't be selected after this movement.
                    nodeBeginningMovement.deselect();

                    // The new node contains now a pawn, with the same color.
                    this.setContainsPawn(true , colorCode);

                    this.board.choosePawnsToExclude(nodeBeginningMovement, this);
                }
            }
        }
    }

    public void deselect() {
        this.isNodeSelected = false;
    }

    public void exclude(int choice){
        board.excludePawns(choice);
    }

    /**
     * Indicates if the node is neighbor.
     * @param otherNode
     * @return
     */
    private boolean isNeighborOf(Node otherNode) {
        int posX_other = otherNode.getX();
        int posY_other = otherNode.getY();

        //A node can't be neighbor of himself.
        if(otherNode == this){
            return false;
        }
        //An even node can have diagonal neighbors, while an odd node can't.
        if(isEven) {
            if (posX_other == posX || posX_other == posX - 1 || posX_other == posX + 1)
                return (posY_other == posY || posY_other == posY - 1 || posY_other == posY + 1) ? true : false;
        }
        else{
            if(posX_other == posX)
                return (posY_other == posY - 1 || posY_other == posY + 1) ? true : false;
            if(posY_other == posY)
                return (posX_other == posX - 1 || posX_other == posX + 1) ? true : false;
        }
        return false;
    }


    @Override
    public String toString() {
        return "Node { " + "containsPion = " + containsPawn + ", posX = " + posX + ", posY = " + posY + ", couleur = " + this.getFill().toString() + ", isNoeudSelected = " + isNodeSelected + " }";
    }

}
