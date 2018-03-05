package sample;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Node extends Circle {

    private boolean containsPawn;
    private boolean isNodeSelected;
    private boolean isAspirable;
    private boolean isPercutable;

    // boolean which will become true when an empty node is selected to perform a movement
    private boolean destinationNodeSelected;

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
    private static final Color COLOR_USER = Color.WHITE;
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
        this.destinationNodeSelected = false;

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

    public boolean isEven() { return isEven; }

    public static Color getColorUser() {
        return COLOR_USER;
    }

    public static Color getColorCpu() {
        return COLOR_CPU;
    }

    public static  Color getColorEmpty() {
        return COLOR_EMPTY;
    }

    public void setPercutable(boolean val){
        this.isPercutable=val;
        if (val) setRadius(RADIUS_NODE_PAWN_SELECTED);
        else if (!val) setRadius((containsPawn) ? RADIUS_NODE_PAWN : RADIUS_NODE_EMPTY);
    }

    public void setAspirable(boolean val){
        this.isAspirable=val;
        if (val) setRadius(RADIUS_NODE_PAWN_SELECTED);
        else if (!val) setRadius((containsPawn) ? RADIUS_NODE_PAWN : RADIUS_NODE_EMPTY);
    }

    public boolean getAspirable(){
        return isAspirable;
    }

    public boolean getPercutable(){
        return isPercutable;
    }

    public boolean isDestinationNodeSelected() {return this.destinationNodeSelected; }

    public void setDestinationNodeSelected(boolean destinationNodeSelected) {
        this.destinationNodeSelected = destinationNodeSelected;
    }

    public void setNodeSelected(boolean nodeSelected) {
        isNodeSelected = nodeSelected;
    }

    public void setEven(boolean even) {
        isEven = even;
    }

    /**
     * Get an other Node with the same properties
     * we can specify the board it is related to
     * @param board
     * @return
     */
    public Node clone(Board board) {
        Node cloneNode = new Node(posX, posY, board);
        cloneNode.setContainsPawn(this.containsPawn , this.containsPawn ? (getFill().equals(COLOR_USER) ? 0 : 1) : 2);
        cloneNode.setNodeSelected(this.isNodeSelected);
        cloneNode.setAspirable(this.isAspirable);
        cloneNode.setPercutable(this.isPercutable);
        cloneNode.setEven(this.isEven);
        return cloneNode;
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
                color = COLOR_USER;
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

        if(colorNode.equals(COLOR_USER) || colorNode.equals(COLOR_CPU)) {

            // If we try to select a pawn, no other pawn should be selected at the same time.
            if(!board.existNodeSelected()) {

                if (selectByUser) {
                    if (colorNode.equals(COLOR_USER)) {

                        // user clicked on one of his pawns so we select that
                        this.isNodeSelected = true;

                        // update the radius of the pawn
                        setRadius(RADIUS_NODE_PAWN_SELECTED);

                        // wake the game thread which is waiting for the user choice
                        synchronized (board.getGame()) {
                            board.getGame().notify();
                        }
                    }

                } else {
                    // this pawn is selected by the computer
                    if (colorNode.equals(COLOR_CPU))
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
                    if(nodeBeginningMovement.getFill().equals(COLOR_USER)) colorCode = 0;
                    if(nodeBeginningMovement.getFill().equals(COLOR_CPU)) colorCode = 1;

                    // Indicates that the initial node will now be empty.
                    nodeBeginningMovement.setContainsPawn(false , 2);

                    // The initial node won't be selected after this movement.
                    nodeBeginningMovement.deselect();

                    // The new node contains now a pawn, with the same color.
                    this.setContainsPawn(true , colorCode);

                    // inform that this node is the chosen destination node
                    this.destinationNodeSelected = true;

                    // do the necessary exclusions after this movement
                    this.board.choosePawnsToExclude(nodeBeginningMovement, this);

                    // wake the game thread which is waiting for the user click
                    if(selectByUser) {
                        synchronized (board.getGame()) {
                            board.getGame().notify();
                        }
                    }
                }
            }
        }
    }

    /**
     * If the node contains a pawn AND this pawn is selected, then it is deselected
     */
    public void deselect() {
        this.isNodeSelected = false;
        if(containsPawn) setRadius(RADIUS_NODE_PAWN);
        else if(!containsPawn) setRadius(RADIUS_NODE_EMPTY);
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

        //A node can't be neighbor of himself.
        if(otherNode == this){
            return false;
        }

        int posX_other = otherNode.getX();
        int posY_other = otherNode.getY();

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
