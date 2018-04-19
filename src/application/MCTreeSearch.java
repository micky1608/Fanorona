package application;


import java.util.ArrayList;
import java.util.List;

public class MCTreeSearch {

    private List<MCTreeNode> listNodes = new ArrayList<>();


    public MCTreeSearch(Board actualBoard) {
        MCTreeNode root = new MCTreeNode(actualBoard , null);
        listNodes.add(root);
    }

    public MCTreeNode selection() {
        //TODO
        return null;
    }

    public MCTreeNode expansion(MCTreeNode mcTreeNode) {
        //TODO
        return null;
    }

    public int simulation (MCTreeNode mcTreeNode) {
        //TODO
        return 0;
    }

    public void update (MCTreeNode mcTreeNode , int result) {
        //TODO
    }

}
