package sample;

import java.util.ArrayList;

public class Plateau {

    // The matrice that contains all the nodes.
    private Noeud[][] noeuds;

    //Lists that contains the nodes which will be empty after a movement.
    private ArrayList<Noeud> noeudsPercussion;
    private ArrayList<Noeud> noeudsAspiration;

    private static Controller controller = null;


    public Plateau () {
        this.noeuds = new Noeud[9][5];
        controller = Main.getController();
        creerNoeuds();

        ThreadNoeud threadNoeud = new ThreadNoeud();
        threadNoeud.setDaemon(true);
        threadNoeud.start();

        afficheNoeuds();

        this.noeudsAspiration=new ArrayList<>();
        this.noeudsPercussion=new ArrayList<>();
    }

    /**
     * Instantiate all the nodes
      */
    private void creerNoeuds() {
        for(int i=0 ; i<9 ; i++) {
            for(int j=0 ; j<5 ; j++) {
                noeuds[i][j] = new Noeud(i,j , this);
                switch(j) {
                    case 0 : case 1 :
                        noeuds[i][j].setContainsPion(true , 1);
                        break;
                    case 2 :
                        if(i != 4) {
                            if(i%2 == 0)
                                noeuds[i][j].setContainsPion(true , 0);
                            else
                                noeuds[i][j].setContainsPion(true , 1);
                        }
                        else {
                            noeuds[i][j].setContainsPion(false , 2);
                        }
                        break;
                    case 3 : case 4 :
                        noeuds[i][j].setContainsPion(true , 0);
                        break;
                }
                controller.addNoeud(noeuds[i][j] , i , j);
            }
        }
    }


    /**
     * Indicates if a node is selected to make a movement.
     * @return
     */
    public boolean existNoeudSelected()  {
        boolean result = false;
        for(int i=0 ; i<9 ; i++) {
            for(int j=0 ; j<5 ; j++) {
                if(noeuds[i][j].isNoeudSelected()) {
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
    public boolean existNoeudPercutable() {
        boolean result = false;
        for(int i=0 ; i<9 ; i++) {
            for (int j = 0; j < 5; j++) {
                if (noeuds[i][j].getPercutable()) {
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
    public boolean existNoeudAspirable() {
        boolean result = false;
        for(int i=0 ; i<9 ; i++) {
            for (int j = 0; j < 5; j++) {
                if (noeuds[i][j].getAspirable()) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * @return the node which is selected, if there's one.
     */
    public Noeud getNoeudSelected() {
        if(existNoeudSelected()) {
            for(int i=0 ; i<9 ; i++) {
                for (int j = 0; j < 5; j++) {
                    if(noeuds[i][j].isNoeudSelected())
                        return noeuds[i][j];
                }
            }
        }
        return null;
    }

    private void afficheNoeuds() {
        for(int i=0 ; i<9 ; i++) {
            for (int j = 0; j < 5; j++) {
                // Affichage dans la console
                //System.out.println(noeuds[i][j]);

            }
        }
    }

    /**
     * Thread which will adjust the node' size in case they are selected, or if they are in position of percussion or aspiration.
     */
    private class ThreadNoeud extends Thread {

        @Override
        public void run() {
            while(true) {
                for(int i=0 ; i<9 ; i++) {
                    for(int j=0 ; j<5 ; j++) {
                        if(noeuds[i][j].isContainsPion()) {

                            if((noeuds[i][j].isNoeudSelected() || noeuds[i][j].getPercutable() || noeuds[i][j].getAspirable()) && noeuds[i][j].getRadius() != Noeud.getRayonNoeudPionSelected())
                                noeuds[i][j].setRadius(Noeud.getRayonNoeudPionSelected());

                            else if(!(noeuds[i][j].isNoeudSelected() || noeuds[i][j].getPercutable() || noeuds[i][j].getAspirable()) && noeuds[i][j].getRadius() != Noeud.getRayonNoeudPion())
                                noeuds[i][j].setRadius(Noeud.getRayonNoeudPion());
                        }
                    }
                }
            }
        }
    }

    /**
     * The method will find which pawns can be excluded of the game, and then exclude them by calling exclurePions().
     *
     * @param noeudDepart
     * @param noeudFin
     */
    public void choisirPionsAExclure(Noeud noeudDepart, Noeud noeudFin){

        int nbPercussion=0;
        int nbAspiration=0;


        //Calculate the difference between the original node and the node we ended in.
        int diffX=(noeudFin.getX()-noeudDepart.getX());
        int diffY=(noeudFin.getY()-noeudDepart.getY());

        int verifierX=noeudFin.getX()+diffX;
        int verifierY=noeudFin.getY()+diffY;

        //Calculate how many pawns will be collided
        while(verifierX >= 0 && verifierX <= 8 && verifierY >= 0 && verifierY <= 4){
            if(noeuds[verifierX][verifierY].isContainsPion()&&noeuds[verifierX][verifierY].getFill()!=noeudFin.getFill()){
                nbPercussion++;
                noeudsPercussion.add(noeuds[verifierX][verifierY]);
                verifierX+=diffX;
                verifierY+=diffY;
            }
            else{
                break;
            }
        }

        verifierX=noeudDepart.getX()-diffX;
        verifierY=noeudDepart.getY()-diffY;

        //Calculate how many pawns will be aspirated
        while(verifierX >= 0 && verifierX <= 8 && verifierY >= 0 && verifierY <= 4){
            if(noeuds[verifierX][verifierY].isContainsPion()&&noeuds[verifierX][verifierY].getFill()!=noeudFin.getFill()){
                nbAspiration++;
                noeudsAspiration.add(noeuds[verifierX][verifierY]);
                verifierX-=diffX;
                verifierY-=diffY;
            }
            else{
                break;
            }
        }
        //If the number of pawns aspirated and collided are not the same, we exclude the highest number.
        if(nbAspiration>nbPercussion){
            exclurePions(0);
        }
        if(nbPercussion>nbAspiration){
            exclurePions(1);
        }

        //If it is the same amounts, we give the choice to the player.
        if(nbPercussion==nbAspiration&&nbPercussion!=0){
            //We make the pawns bigger so the user can see them easily.
            controller.setTexte("Choisissez entre percussion et aspiration");
            for(Noeud n:noeudsPercussion){
                n.setPercutable(true);
            }
            for(Noeud n:noeudsAspiration){
                n.setAspirable(true);
            }
        }
    }

    /**
     * Exclude the pawns.
     * @param choix 0: exclude de panws in noeudsAspiration
     *              1: exclude de pawns in noeudsPerussion
     */
    public void exclurePions(int choix){
        if(choix==0){
            for(Noeud n:noeudsAspiration){
                n.setContainsPion(false,2);
                n.setAspirable(false);
            }
            for(Noeud n:noeudsPercussion){
                n.setPercutable(false);
            }
        }
        if(choix==1){
            for(Noeud n:noeudsPercussion){
                n.setContainsPion(false,2);
                n.setPercutable(false);
            }
            for(Noeud n:noeudsPercussion){
                n.setAspirable(false);
            }
        }

        //We clear the lists so the same nodes won't be in the lists for the next movement.
        noeudsPercussion.clear();
        noeudsAspiration.clear();
    }
}
