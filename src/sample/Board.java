package sample;


import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Board implements Cloneable {

    // The matrice that contains all the nodes.
    private Node[][] nodes;

    //Lists that contains the nodes which will be empty after a movement.
    private ArrayList<Node> nodesPercussion;
    private ArrayList<Node> nodesAspiration;

    // the game this board is related to
    private Game game;

    private static Controller controller = null;

    /**
     * Constructors
     * @param game
     */

    public Board(Game game) {
        this.game = game;
        this.nodes = new Node[9][5];
        controller = Main.getController();
        createNodes(true);

        this.nodesAspiration=new ArrayList<>();
        this.nodesPercussion=new ArrayList<>();
    }

    public Board(){
        this.nodes=new Node[9][5];
        createNodes(false);

        this.game = null;
        this.nodesAspiration=new ArrayList<>();
        this.nodesPercussion=new ArrayList<>();
    }

    /**
     * Getters and setters
     * @param text
     */


    public void setTextInConsole(String text) {
        controller.setTexte(text);
    }

    public Game getGame() {
        return game;
    }

    public Node[][] getNodes(){
        return nodes;
    }

    public void setNodes(Node[][] nodes) {
        this.nodes = nodes;
    }

    @Override
    public Board clone() {
        Board cloneBoard = new Board();
        Node[][] nodesClone = new Node[9][5];

        for(int i=0 ; i<9 ; i++) {
            for(int j=0 ; j<5 ; j++) {
                nodesClone[i][j] = nodes[i][j].clone(cloneBoard);
            }
        }
        cloneBoard.setNodes(nodesClone);
        return cloneBoard;
    }

    /**
     * get the number of pawn of a player still on the board
     * @param playerCategory
     * @return
     */
    public int getNbPawnOnBoard(PlayerCategory playerCategory) {
        int result = 0;
        for(int i=0 ; i<9 ; i++) {
            for(int j=0 ; j<5 ; j++) {
                switch (playerCategory) {
                    case USER:
                        if(nodes[i][j].getFill().equals(Node.getColorUser()))
                            result++;
                        break;
                    case COMPUTER:
                        if(nodes[i][j].getFill().equals(Node.getColorCpu()))
                            result++;
                        break;
                }
            }
        }
        return result;
    }

    /**
     * Instantiate all the nodes. Only add the node to the controller if it's not a board use in the search tree.
     * @param needToShow
     */
    private void createNodes(boolean needToShow) {
        for(int i=0 ; i<9 ; i++) {
            for(int j=0 ; j<5 ; j++) {
                nodes[i][j] = new Node(i,j , this);
                switch(j) {
                    case 0 : case 1 :
                        nodes[i][j].setContainsPawn(true , 1);
                        break;
                    case 2 :
                        if(i != 4) {
                            if(i%2 == 0)
                                nodes[i][j].setContainsPawn(true , 0);
                            else
                                nodes[i][j].setContainsPawn(true , 1);
                        }
                        else {
                            nodes[i][j].setContainsPawn(false , 2);
                        }
                        break;
                    case 3 : case 4 :
                        nodes[i][j].setContainsPawn(true , 0);
                        break;
                }
                if(needToShow) {
                    controller.addNoeud(nodes[i][j], i, j);
                }
            }
        }
    }


    /**
     * Indicates if a node is selected to make a movement.
     * @return
     */
    public boolean existNodeSelected()  {
        boolean result = false;
        for(int i=0 ; i<9 ; i++) {
            for(int j=0 ; j<5 ; j++) {
                if(nodes[i][j].isNodeSelected()) {
                    if(result)
                        System.out.println("Double selection");
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Indicates if a node is in position of percussion.
     * @return
     */
    public boolean existNodePercutable() {
        boolean result = false;
        for(int i=0 ; i<9 ; i++) {
            for (int j = 0; j < 5; j++) {
                if (nodes[i][j].getPercutable()) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * indicates if a node is in position of aspiration.
     * @return
     */
    public boolean existNodeAspirable() {
        boolean result = false;
        for(int i=0 ; i<9 ; i++) {
            for (int j = 0; j < 5; j++) {
                if (nodes[i][j].getAspirable()) {
                    result = true;
                }
            }
        }
        return result;
    }


    /**
     * Get the node destination selected
     * @return
     */
    public Node getNodeDestinationSelected() {
        for(int i=0 ; i<9 ; i++) {
            for (int j = 0; j < 5; j++) {
                if (nodes[i][j].isDestinationNodeSelected()) {
                    nodes[i][j].setDestinationNodeSelected(false);
                   return nodes[i][j];
                }
            }
        }
        return null;
    }

    /**
     * @return the node which is selected, if there's one.
     */
    public Node getNodeSelected() {
        if(existNodeSelected()) {
            for(int i=0 ; i<9 ; i++) {
                for (int j = 0; j < 5; j++) {
                    if(nodes[i][j].isNodeSelected())
                        return nodes[i][j];
                }
            }
        }
        return null;
    }

    /**
     * set the disable property for all the nodes
     * @param choice
     */
    public void setDisableAllNodes (boolean choice) {
        for(int i=0 ; i<9 ; i++) {
            for(int j=0 ; j<4 ; j++)
                nodes[i][j].setDisable(choice);
        }
    }

    /**
     * The method will find which pawns can be excluded of the game, and then exclude them by calling excludePawn().
     * This is called after a pawn is moved to check the possible captures
     * @param nodeBeginning
     * @param nodeEnd
     */
    public void choosePawnsToExclude(Node nodeBeginning, Node nodeEnd){

        int nbPercussion=0;
        int nbAspiration=0;


        // Calculate the difference between the original node and the node we ended in.
        // Doing that, we can get the direction of the deplacement
        int diffX=(nodeEnd.getX()- nodeBeginning.getX());
        int diffY=(nodeEnd.getY()- nodeBeginning.getY());

        // verifyX and verifyY are the coordinates of the nodes that we will check if they are percutables or aspirables after this movement
        int verifyX= nodeEnd.getX()+diffX;
        int verifyY= nodeEnd.getY()+diffY;

        //Calculate how many pawns will be collided
        while(verifyX >= 0 && verifyX <= 8 && verifyY >= 0 && verifyY <= 4){
            if(nodes[verifyX][verifyY].isContainsPawn()&& nodes[verifyX][verifyY].getFill()!= nodeEnd.getFill()){
                nbPercussion++;
                nodesPercussion.add(nodes[verifyX][verifyY]);
                verifyX+=diffX;
                verifyY+=diffY;
            }
            else{
                break;
            }
        }

        verifyX= nodeBeginning.getX()-diffX;
        verifyY= nodeBeginning.getY()-diffY;

        //Calculate how many pawns will be aspirated
        while(verifyX >= 0 && verifyX <= 8 && verifyY >= 0 && verifyY <= 4){
            if(nodes[verifyX][verifyY].isContainsPawn()&& nodes[verifyX][verifyY].getFill()!= nodeEnd.getFill()){
                nbAspiration++;
                nodesAspiration.add(nodes[verifyX][verifyY]);
                verifyX-=diffX;
                verifyY-=diffY;
            }
            else{
                break;
            }
        }
        //If the number of pawns aspirated and collided are not the same, we exclude the highest number.
        if(nbAspiration>nbPercussion){
            excludePawns(0);
        }
        if(nbPercussion>nbAspiration){
            excludePawns(1);
        }

        //If it is the same amounts, we give the choice to the player.
        if(nbPercussion==nbAspiration&&nbPercussion!=0){
            //We make the pawns bigger so the user can see them easily.
            controller.setTexte("Choisissez entre percussion et aspiration");
            for(Node n:nodesPercussion){
                n.setPercutable(true);
            }
            for(Node n:nodesAspiration){
                n.setAspirable(true);
            }
        }
    }

    /**
     * Exclude the pawns depending of the choice.
     * @param choice 0: exclude the panws by aspiration
     *              1: exclude the pawns by collision
     */
    public void excludePawns(int choice){
        if(choice==0){
            for(Node n:nodesAspiration){
                n.setContainsPawn(false,2);
                n.setAspirable(false);
            }
            for(Node n:nodesPercussion){
                n.setPercutable(false);
            }
        }
        if(choice==1){
            for(Node n:nodesPercussion){
                n.setContainsPawn(false,2);
                n.setPercutable(false);
            }
            for(Node n:nodesAspiration){
                n.setAspirable(false);
            }
        }

        //We clear the lists so the same nodes won't be in the lists for the next movement.
        nodesPercussion.clear();
        nodesAspiration.clear();
    }

    /**
     * Return true if the node can move and false otherwise
     * @return
     */
    public boolean canMove (Node node) {
        int posX = node.getX();
        int posY = node.getY();

        // search through the neighbors if there is an empty node
        for(int i=-1 ; i<=1 ; i++) {
            for(int j=-1 ; j<=1 ; j++) {

                // check if this is a correct neighbor
                if( posX + i >= 0 && posX + i < 9 && posY + j >= 0 && posY + j < 5 && !(i==0 && j==0) && (node.isEven() ? true : (i==0 || j==0)) ) {

                    // look if this an empty node
                    if(nodes[posX + i][posY + j].getFill().equals(Node.getColorEmpty()))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * tell if a node can capture pawn from his position
     * @param nodeBeginning
     * @return
     */
    public List<Node> possibleCapture (Node nodeBeginning) {
        // get the opponent color to check if the capture is posible when a neighbour is found
        Color opponentColor = (nodeBeginning.getFill().equals(Node.getColorUser()) ? Node.getColorCpu() : Node.getColorUser());

        ArrayList<Node> possibleNodes=new ArrayList<>();

        // get the position of the beginning node
        int actualX = nodeBeginning.getX();
        int actualY = nodeBeginning.getY();

        // search among the neighbours if exist an empty node to use as a destination
        for(int i=-1 ; i<=1 ; i++) {
            for(int j=-1 ; j<=1 ; j++) {

                // check if this is a correct neighbour
                // if the node id odd, add the condition i==0 || j==0
                if ((actualX + i) < 9 && (actualX + i) >= 0 && (actualY + j) < 5 && (actualY + j) >= 0 && !(i == 0 && j == 0) && (nodeBeginning.isEven() ? true : (i == 0 || j == 0))) {

                    Node potentialDestination = this.nodes[actualX + i][actualY + j];

                    // check if the node is an empty node we can move to
                    if (potentialDestination.getFill().equals(Node.getColorEmpty())) {

                        // check if the move to the potential destiantion allows the capture of an opponent pawn.

                        if((potentialDestination.getX()+i)>=0 && (potentialDestination.getX()+i)<9 && (potentialDestination.getY()+j)>=0 && (potentialDestination.getY())+j<5){
                            Node testPercussionNode = this.nodes[potentialDestination.getX() + i][potentialDestination.getY() + j];

                            // add the node to the list if the check is correct and if this node doesn't exist already in the list.
                            if (testPercussionNode.getFill().equals(opponentColor)&&!possibleNodes.contains(potentialDestination))
                                possibleNodes.add(potentialDestination);
                        }

                        // if the capture's check failed, check if capture by aspiration is possible
                        if(nodeBeginning.getX()-i>=0 && nodeBeginning.getX()-i<9 && nodeBeginning.getY()-j>=0 && nodeBeginning.getY()-j<5) {
                            Node testAspirationNode = this.nodes[nodeBeginning.getX() - i][nodeBeginning.getY() - j];

                            // add the node to the list if the check is correct and if this node doesn't exist already in the list.
                            if (testAspirationNode.getFill().equals(opponentColor) && !possibleNodes.contains(potentialDestination)) {
                                possibleNodes.add(potentialDestination);
                            }
                        }
                    }
                }
            }
        }

        return possibleNodes;
    }

    /**
     * Make a random move from this node
     * This method is called ONLY on a non empty node
     * This method suppose that there is at least one empty node which is a neighbor
     */
    public void randomMove(Node nodeBeginning) {
        int posX = nodeBeginning.getX();
        int posY = nodeBeginning.getY();

        // the list which will contains all the possible destinations
        List<Node> destinations=new ArrayList<>();

        // if there are possible captures, we make the captures.
        if(possibleCapture(nodeBeginning).size()>0){
            destinations=possibleCapture(nodeBeginning);
        }

        // otherwise, we make any move possible
        else{
            // search through the neighbors the empty nodes
            for(int i=-1 ; i<=1 ; i++) {
                for (int j = -1; j <= 1; j++) {

                    // check if this is a correct neighbor
                    if (posX + i >= 0 && posX + i < 9 && posY + j >= 0 && posY + j < 5 && !(i == 0 && j == 0) && (nodeBeginning.isEven() ? true : (i == 0 || j == 0))) {

                        // if the node is empty, it is added to the list
                        if(nodes[posX + i][posY + j].getFill().equals(Node.getColorEmpty()))
                            destinations.add(nodes[posX + i][posY + j]);
                    }
                }
            }
        }

        // generate a random number between 0 and the list size less 1
        Random rand = new Random();
        int indice = rand.nextInt(destinations.size());

        // retrieve the destination node according to the random number
        Node destination = destinations.get(indice);

        // now we can make the move
        destination.setContainsPawn(true , nodeBeginning.getFill().equals(Node.getColorUser()) ? 0 : 1);
        nodeBeginning.setContainsPawn(false , 2);


        // exclude the pawns if necessary
        choosePawnsToExclude(nodeBeginning , destination);
    }


    @Override
    public String toString() {
        StringBuffer sb=new StringBuffer("");

        for(int j=0;j<5;j++){
            for(int i=0;i<9;i++){
                if(nodes[i][j].getFill().equals(Node.getColorUser())){
                    sb.append("+");
                }
                if(nodes[i][j].getFill().equals(Node.getColorCpu())){
                    sb.append("-");
                }
                if(nodes[i][j].getFill().equals(Node.getColorEmpty())){
                    sb.append("0");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

}
