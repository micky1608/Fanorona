package application;

import javafx.scene.paint.Color;

import java.util.*;

public class TreeNode {

    // the board configuration asociated to this tree node
    private Board board;

    // the nodes on the board
    private Node[][] nodes;

    // the parent node in the search tree (null if root)
    private TreeNode father;

    // the sons tree nodes of this
    private ArrayList<TreeNode> sons=new ArrayList<>();

    // evaluation of a board configuration (nb user pawns - nb computer pawns)
    // computer wants to minimize this value
    private int evaluation;

    // the deepness in the tree (0 for root)
    private int deepness;

    // the probability to win if the computer chooses this node.
    private int probabilityToWin;

    private Node beginNode;
    private Node endNode;

    // pawns colors
    private static final Color COLOR_USER = Color.WHITE;
    private static final Color COLOR_CPU = Color.BLACK;

    /**
     * Constructor used to create the root node of the tree.
     */
    public TreeNode(Board board){
        this.board=board;
        this.nodes=this.board.getNodes();
        this.evaluation = 0;
        this.deepness=0;
        this.probabilityToWin=0;
    }

    /**
     * Constructor used to create all the nodes of the tree, except root.
     */
    public TreeNode(Board board, TreeNode father, Node beginNode, Node endNode){
        this.board=board;
        this.nodes=board.getNodes();
        this.evaluation = 0;
        this.probabilityToWin=0;
        this.father=father;
        this.beginNode=beginNode;
        this.endNode=endNode;
        this.deepness=father.getDeepness()+1;

        // evaluation of the board configuration
        for(int i=0;i<9;i++){
            for(int j=0;j<5;j++){
                if(nodes[i][j].getFill().equals(COLOR_USER)){
                    evaluation++;
                }
                else if(nodes[i][j].getFill().equals(COLOR_CPU)){
                    evaluation--;
                }
            }
        }
    }

    /**
     * This method will create all the sons of this tree node.
     * Every son will contain a possibility of how board could be after one move
     */
    public void createSons(Boolean testing){
        Color color;
        if(testing){
            color=COLOR_USER;
        }
        else{
            color=COLOR_CPU;
        }
        // we will store in this list all the empty nodes of the game.
        ArrayList<Node> actualEmptyList = new ArrayList<>();
        boolean possibleCapture=false;

        //Storing all the empty nodes of the game.
        for(int i=0;i<9;i++){
            for(int j=0;j<5;j++){
                if(!nodes[i][j].isContainsPawn()){
                    actualEmptyList.add(nodes[i][j]);
                }
                else if(nodes[i][j].getFill().equals(color)&&!board.possibleCapture(nodes[i][j]).isEmpty()){
                    possibleCapture=true;
                }
            }
        }

        // looking for the empty nodes that can be used as a movement destination
        for(Node emptyNode:actualEmptyList) {

            // get the position of this empty node
            int actualX = emptyNode.getX();
            int actualY = emptyNode.getY();

            //We search, for every empty node, if he got a neighbour of the right color.
            //If it's the case, we add a son, with this neighbour being empty, and our empty node being full.
            // Only looking for neighbour in the matrice (otherwise NullPointerException)

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    // check if this is a correct neighbour
                    // if the node is odd : add the condition either i or j = 0
                    if ((actualX + i) < 9 && (actualX + i) >= 0 && (actualY + j) < 5 && (actualY + j) >= 0 && !(i == 0 && j == 0) && (emptyNode.isEven() ? true : (i == 0 || j == 0))) {

                        //If there is a possible capture, only creates sons where the move makes a capture.
                        if(possibleCapture) {
                            if (nodes[actualX + i][actualY + j].getFill().equals(color) && board.possibleCapture(nodes[actualX + i][actualY + j]).contains(emptyNode)) {
                                createOneSon(emptyNode, i, j);
                            }
                        }
                        //If there arn't any possibility, creates every sons possible.
                        else{
                            if(nodes[actualX + i][actualY + j].getFill().equals(color)){
                                createOneSon(emptyNode, i, j);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * This method will create all the sons of this tree node.
     * Every son will contain a possibility of how board could be after one move
     */
    public void createSons(Boolean testing, Node lastPlayed, ArrayList<Node> alreadyVisited) {
        ArrayList<Node> visited=new ArrayList<>();
        //We recreate the ArrayList with the nodes of the board of the treenode (which is not the same as the one of Computer)
        for(Node node: alreadyVisited){
            visited.add(nodes[node.getX()][node.getY()]);
        }

        for(Node node:board.possibleCapture(lastPlayed)){
            if(!visited.contains(node))
                createOneSon(node, lastPlayed.getX()-node.getX(), lastPlayed.getY()-node.getY());
        }
    }


    /**
     * Create a son tree node with an empty node (destination) and the moveX and moveX to access the node beginning
     * @param emptyNode
     * @param moveX
     * @param moveY
     */
    private void createOneSon(Node emptyNode , int moveX , int moveY) {
        // get the empty node position
        int actualX = emptyNode.getX();
        int actualY = emptyNode.getY();

        //Creating a new Board for the next TreeNode
        Board nextBoard = new Board();

        //Storing in this new board the status of the nodes of this TreeNode.
        for (int k = 0; k < 9; k++) {
            for (int l = 0; l < 5; l++) {
                if (nodes[k][l].isContainsPawn())
                    nextBoard.getNodes()[k][l].setContainsPawn(true, nodes[k][l].getFill().equals(COLOR_USER) ? 0 : 1);
                else {
                    nextBoard.getNodes()[k][l].setContainsPawn(false, 2);
                }
            }
        }

        //Making the movement and excluding the pawns.
        nextBoard.getNodes()[actualX][actualY].setContainsPawn(true, nextBoard.getNodes()[actualX + moveX][actualY + moveY].getFill().equals(COLOR_USER) ? 0 : 1);
        nextBoard.getNodes()[actualX + moveX][actualY + moveY].setContainsPawn(false, 2);

        nextBoard.choosePawnsToExclude(nextBoard.getNodes()[actualX + moveX][actualY + moveY], nextBoard.getNodes()[actualX][actualY],false, true);
        this.sons.add(new TreeNode(nextBoard, this, nextBoard.getNodes()[actualX + moveX][actualY + moveY], nextBoard.getNodes()[actualX][actualY]));
    }

    public ArrayList<TreeNode> getSons() {
        return sons;
    }

    @Override
    /**
     * Writes different informations about this Tree Node.
     * -If it's root, or not
     * -Status of the board
     * -Evaluation
     * -Deepness
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("");

        if(father==null)
            sb.append("I'm root\n");
        else
            sb.append("I'm not root\n");

        sb.append(board.toString());

        sb.append("Evaluation:"+evaluation+"\t Deepness:"+deepness+"\t Probability:"+probabilityToWin);

        return sb.toString();
    }

    public int getDeepness(){
        return this.deepness;
    }

    public int getEvaluation(){
        return this.evaluation;
    }

    public void setProbabilityToWin(int probability){
        this.probabilityToWin+=probability;
    }

    public int getProbabilityToWin(){return probabilityToWin;}

    public Node getBeginNode(){
        return beginNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public Node[][] getNodes() {
        return nodes;
    }
}

