package sample;

import java.util.ArrayList;

public class Board {

    // The matrice that contains all the nodes.
    private Node[][] nodes;

    //Lists that contains the nodes which will be empty after a movement.
    private ArrayList<Node> nodesPercussion;
    private ArrayList<Node> nodesAspiration;

    private static Controller controller = null;


    public Board() {
        this.nodes = new Node[9][5];
        controller = Main.getController();
        createNodes();

        ThreadNode threadNode = new ThreadNode();
        threadNode.setDaemon(true);
        threadNode.start();

        this.nodesAspiration=new ArrayList<>();
        this.nodesPercussion=new ArrayList<>();
    }

    public void setTextInConsole(String text) {
        controller.setTexte(text);
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
     * Instantiate all the nodes
      */
    private void createNodes() {
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
                controller.addNoeud(nodes[i][j] , i , j);
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
     * Thread which will adjust the node' size in case they are selected, or if they are in position of percussion or aspiration.
     */
    private class ThreadNode extends Thread {

        @Override
        public void run() {
            while(true) {
                for(int i=0 ; i<9 ; i++) {
                    for(int j=0 ; j<5 ; j++) {
                        if(nodes[i][j].isContainsPawn()) {

                            if((nodes[i][j].isNodeSelected() || nodes[i][j].getPercutable() || nodes[i][j].getAspirable()) && nodes[i][j].getRadius() != Node.getRadiusNodePawnnSelected())
                                nodes[i][j].setRadius(Node.getRadiusNodePawnnSelected());

                            else if(!(nodes[i][j].isNodeSelected() || nodes[i][j].getPercutable() || nodes[i][j].getAspirable()) && nodes[i][j].getRadius() != Node.getRadiusNodePawn())
                                nodes[i][j].setRadius(Node.getRadiusNodePawn());
                        }
                    }
                }
            }
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
}
