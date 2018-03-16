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
        //This method will be called only if the computer has to chose between aspiration or percussion.
        int maxProbabilityToWin=0;
        int beginX=0;
        int beginY=0;
        int endX=0;
        int endY=0;

        for(TreeNode tn:root.getSons()){
            //Store the value of the most profitable son.
            maxProbabilityToWin=Math.max(maxProbabilityToWin,tn.getProbabilityToWin());
        }
        for (TreeNode tn : root.getSons()) {
            //Search the son with this maximum probability.
            if (tn.getProbabilityToWin() == maxProbabilityToWin) {
                endX=tn.getEndNode().getX();
                endY=tn.getEndNode().getY();
                beginX=tn.getBeginNode().getX();
                beginY=tn.getBeginNode().getY();
                //If there isn't a pawn after the endPawn of the treeNode, it means that the most profitable son would have chosen percussion.
                if(endX+(endX-beginX)<9&endX+(endX-beginX)>=0&&endY+(endY-beginY)<5&&endY+(endY-beginY)>=0){
                    if(game.getBoard().getNodes()[endX+(endX-beginX)][endY+(endY-beginY)].isContainsPawn()&&!tn.getNodes()[endX+(endX-beginX)][endY+(endY-beginY)].isContainsPawn()){
                        game.getBoard().excludePawns(1);
                    }
                }
                //If there isn't a pawn before the beginPawn of the treeNode, it means that the most profitable son would have chosen aspiration.
                if(beginX+(beginX-endX)<9&&beginX+(beginX-endX)>=0&&beginY+(beginY-endY)<5&&beginY+(beginY-endY)>=0) {
                    if (game.getBoard().getNodes()[beginX+(beginX-endX)][beginY+(beginY-endY)].isContainsPawn() && !tn.getNodes()[beginX+(beginX-endX)][beginY+(beginY-endY)].isContainsPawn()) {
                        game.getBoard().excludePawns(0);
                    }
                }

            }
        }
    }
}
