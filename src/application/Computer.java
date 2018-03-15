package application;

import java.util.ArrayList;
import java.util.Random;

public class Computer extends Player {
    private TreeNode root;
    private GameSimulator gameSimulator;

    private Boolean testing;


    public Computer(Game game, Boolean testing) {
        super(game);
        this.testing=testing;
        createTreeSearch();

    }

    private void createTreeSearch() {
        root = new TreeNode(game.getBoard().clone());
        root.createSons(testing);

        //TODO
    }

    @Override
    public void selectNodeBeginning() {
        if(testing){
            game.setTextInConsole("Computer test PLAY");
        }
        else{
            game.setTextInConsole("Computer PLAY");
        }
        createTreeSearch();
        int maxProbabilityToWin=0;
        ArrayList<TreeNode> sons;
        sons=root.getSons();

        //If it's the main IA, makes X simulations/treenode and chose the one with the most chances to win.
        if(!testing) {
            for(TreeNode tn:sons){
                for(int i=0;i<1000; i++){
                    gameSimulator=new GameSimulator(game.getBoard().clone(), testing?PlayerCategory.USER:PlayerCategory.COMPUTER);
                    tn.setProbabilityToWin(gameSimulator.simulate(testing));
                }
                maxProbabilityToWin = Math.max(maxProbabilityToWin, tn.getProbabilityToWin());
            }

            for (TreeNode tn : sons) {
                if (tn.getProbabilityToWin() == maxProbabilityToWin) {
                    try {
                        game.getBoard().getNodes()[tn.getBeginNode().getX()][tn.getBeginNode().getY()].select(testing);
                        game.getBoard().getNodes()[tn.getEndNode().getX()][tn.getEndNode().getY()].select(testing);
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //If it's the test IA, chose a random son.
        else{
            TreeNode randomSon = sons.get(new Random().nextInt(sons.size()));
            try {
                game.getBoard().getNodes()[randomSon.getBeginNode().getX()][randomSon.getBeginNode().getY()].select(testing);
                game.getBoard().getNodes()[randomSon.getEndNode().getX()][randomSon.getEndNode().getY()].select(testing);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void selectNodeEnd() {
        //TODO
    }
}
