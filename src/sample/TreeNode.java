package sample;

import javafx.scene.paint.Color;

import java.util.*;

public class TreeNode {
    private Board board;
    private Node[][] nodes;
    private TreeNode father;
    private ArrayList<TreeNode> sons=new ArrayList<>();
    private int evaluation;
    private int deepness;
    private static final Color COLOR_USER = Color.WHITE;
    private static final Color COLOR_CPU = Color.BLACK;

    /**
     * Methode used to create the root node of the trez.
     */
    public TreeNode(){
        this.board=new Board();
        this.nodes=this.board.getNodes();
        this.deepness=0;
        for(int i=0;i<9;i++){
            for(int j=0;j<5;j++){
                if(board.getNodes()[i][j].getFill().equals(COLOR_USER)){
                    evaluation++;
                }
                else if(board.getNodes()[i][j].getFill().equals(COLOR_CPU)){
                    evaluation--;
                }
            }
        }
    }

    /**
     * Methode used to create all the nodes of the tree, but root.
     */
    public TreeNode(Board board, TreeNode father){
        this.board=board;
        this.nodes=board.getNodes();
        this.father=father;
        this.deepness=father.getDeepness()+1;
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
     * This methode will create all the sons of this root.
     * Every son will contain a possibility of how board could be after one move.
     * We
     */
    public void createSons(){
        //We will store in this list all the empty nodes of the game.
        ArrayList<Node> actualEmpty=new ArrayList<>();
        //Storing all the empty nodes of the game.
        for(int i=0;i<9;i++){
            for(int j=0;j<5;j++){
                if(!nodes[i][j].isContainsPawn()){
                    actualEmpty.add(nodes[i][j]);
                }
            }
        }
        for(Node n:actualEmpty){
            int actualX=n.getX();
            int actualY=n.getY();
            //We search, for every empty node, if he got a neighbour of the right color.
            //If it's the case, we add a son, with this neighbour being empty, and our empty node being full.

            //Case the node is even
            if((n.getX()+n.getY())%2==0){
                for(int i=-1;i<=1;i++){
                    for(int j=-1;j<=1;j++){
                        //Only looking for neighbour in the matrice (otherwise NullPointerException)
                        //Color depends on the deepness, as the player plays once every two moves.
                        if((actualX+i)<9 && (actualY+i)>=0 && (actualY+j)<5 && (actualY+j)>=0 && !(i==0&&j==0)){
                            if(nodes[actualX+i][actualY+j].getFill().equals(deepness%2==0?COLOR_USER:COLOR_CPU)) {

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
                                nextBoard.getNodes()[actualX][actualY].setContainsPawn(true, nextBoard.getNodes()[actualX + i][actualY + j].getFill().equals(COLOR_USER) ? 0 : 1);
                                nextBoard.getNodes()[actualX + i][actualY + j].setContainsPawn(false, 2);

                                nextBoard.choosePawnsToExclude(nextBoard.getNodes()[actualX + i][actualY + j], nextBoard.getNodes()[actualX][actualY]);
                                this.sons.add(new TreeNode(nextBoard, this));
                            }
                        }
                    }
                }
            }
            //Case the node is odd
            else{

                for(int i=-1;i<=1;i=i+2){
                    for(int j=-1;j<=1;j++){

                        //Only looking for neighbour in the matrice (otherwise NullPointerException)
                        //Color depends on the deepness, as the player plays once every two moves.
                        //Add the condition either i or j = 0 (the node is odd)
                        if((actualX+i)<9 && (actualY+i)>=0 && (actualY+j)<5 && (actualY+j)>=0 && !(i==0&&j==0) && (i==0||j==0)) {
                            if (nodes[actualX + i][actualY + j].getFill().equals(deepness%2==0?COLOR_USER:COLOR_CPU)){

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
                                nextBoard.getNodes()[actualX][actualY].setContainsPawn(true, nextBoard.getNodes()[actualX + i][actualY + j].getFill().equals(COLOR_USER) ? 0 : 1);
                                nextBoard.getNodes()[actualX + i][actualY + j].setContainsPawn(false, 2);

                                nextBoard.choosePawnsToExclude(nextBoard.getNodes()[actualX + i][actualY + j], nextBoard.getNodes()[actualX][actualY]);
                                this.sons.add(new TreeNode(nextBoard, this));
                            }
                        }
                    }
                }
            }
        }
    }

    public ArrayList<TreeNode> getSons() {
        return sons;
    }

    @Override
    /**
     * Writes different informations about this Tree Node.
     * -If it's root, or not
     * -Status of the board
     * -Evlatuation
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
