package sample;

import java.util.ArrayList;

public class Computer extends Player {
    private TreeNode root;
    private GameSimulator gameSimulator;


    public Computer(Game game) {
        super(game);
        createTreeSearch();
    }

    private void createTreeSearch() {
        root = new TreeNode(game.getBoard().clone());
        root.createSons();

        //TODO
    }

    @Override
    public void selectNodeBeginning() {
        game.setTextInConsole("Computer PLAY");
        createTreeSearch();
        int maxProbabilityToWin=0;
        ArrayList<TreeNode> sons;
        sons=root.getSons();

        for(int j=0;j<sons.size();j++){
            TreeNode tn=sons.get(j);
            for(int i=0;i<(30);i++){
                gameSimulator=new GameSimulator(game.getBoard().clone(), PlayerCategory.COMPUTER);
                tn.setProbabilityToWin(gameSimulator.simulate());
                maxProbabilityToWin=maxProbabilityToWin<tn.getProbabilityToWin()?tn.getProbabilityToWin():maxProbabilityToWin;
            }
            System.out.println(tn.toString());
            System.out.println("Max proba:"+maxProbabilityToWin);
        }
        for(TreeNode tn:sons){
            if(tn.getProbabilityToWin()==maxProbabilityToWin){
                try {
                    game.getBoard().getNodes()[tn.getBeginNode().getX()][tn.getBeginNode().getY()].select(false);
                    game.getBoard().getNodes()[tn.getEndNode().getX()][tn.getEndNode().getY()].select(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void selectNodeEnd() {
        //TODO
    }
}
