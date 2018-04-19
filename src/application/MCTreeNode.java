package application;

public class MCTreeNode {

    // the board configuration asociated to this tree node
    private Board board;

    // the nodes on the board
    private Node[][] nodes;

    // the father in the tree (null for root)
    private MCTreeNode father;

    private int nbVisit;

    private int nbWin;

    public MCTreeNode (Board board , MCTreeNode father) {
        this.board = board;
        this.nodes = board.getNodes();
        this.father = father;
        this.nbVisit = 0;
        this.nbWin = 0;
    }





}
