package sample;

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

    // pawns colors
    private static final Color COLOR_USER = Color.WHITE;
    private static final Color COLOR_CPU = Color.BLACK;

    /**
     * Constructor used to create the root node of the tree.
     */
    public TreeNode(){
        this.board=new Board();
        this.nodes=this.board.getNodes();
        this.evaluation = 0;
        this.deepness=0;
    }

    /**
     * Constructor used to create all the nodes of the tree, except root.
     */
    public TreeNode(Board board, TreeNode father){
        this.board=board;
        this.nodes=board.getNodes();
        this.evaluation = 0;
        this.father=father;
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
    public void createSons(){
        // we will store in this list all the empty nodes of the game.
        ArrayList<Node> actualEmptyList = new ArrayList<>();

        //Storing all the empty nodes of the game.
        for(int i=0;i<9;i++){
            for(int j=0;j<5;j++){
                if(!nodes[i][j].isContainsPawn()){
                    actualEmptyList.add(nodes[i][j]);
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
                        if ((actualX + i) < 9 && (actualY + i) >= 0 && (actualY + j) < 5 && (actualY + j) >= 0 && !(i == 0 && j == 0) && (emptyNode.isEven() ? true : (i == 0 || j == 0))) {

                            //Color depends on the deepness, as the player plays once every two moves.
                            if (nodes[actualX + i][actualY + j].getFill().equals(deepness % 2 == 0 ? COLOR_USER : COLOR_CPU))
                                createOneSon(emptyNode, i, j);
                        }
                }
            }
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

        nextBoard.choosePawnsToExclude(nextBoard.getNodes()[actualX + moveX][actualY + moveY], nextBoard.getNodes()[actualX][actualY]);
        this.sons.add(new TreeNode(nextBoard, this));
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
        StringBuffer sb=new StringBuffer();
        if(father==null){
            sb.append("I'm root\n");
        }
        else{
            sb.append("I'm not root\n");
        }
        for(int j=0;j<5;j++){
            for(int i=0;i<9;i++){
                if(nodes[i][j].getFill().equals(COLOR_USER)){
                    sb.append("+");
                }
                if(nodes[i][j].getFill().equals(COLOR_CPU)){
                    sb.append("-");
                }
                if(nodes[i][j].getFill().equals(Color.GREY)){
                    sb.append("0");
                }
            }
            sb.append("\n");
        }
        sb.append("Evaluation:"+evaluation+"\t Deepness:"+deepness);

        return sb.toString();
    }

    public int getDeepness(){
        return this.deepness;
    }

    public int getEvaluation(){
        return this.evaluation;
    }
}
