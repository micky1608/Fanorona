package sample;

import java.util.ArrayList;

public class Plateau {

    // La matrice qui contient tous les noeuds du plateau
    private Noeud[][] noeuds;
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
     * Instancie tous les noeuds du plateau
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
     * indique si un noeud qui contient un pion est selectionne pour faire un mouvement
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
     * indique s'il existe au moins un noeud qui est en position de percussion
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
     * indique s'il existe au moins un noeud qui est en position d'aspiration
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
     * @return le noeud qui contient un pion actuellement selectionne s'il y en a un (un seul)
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
     * Thread qui va ajuster la taille du noeud pour le faire grandir en cas de selection
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
     * Permet de trouver quels sont les pions à exclure, et ensuite de les exclure via la fonction exlurePions.
     * @param noeudDepart
     * @param noeudFin
     */
    public void choisirPionsAExclure(Noeud noeudDepart, Noeud noeudFin){

        int nbPercussion=0;
        int nbAspiration=0;


        //On calcule la différence de coordonnée entre les noeuds
        int diffX=(noeudFin.getX()-noeudDepart.getX());
        int diffY=(noeudFin.getY()-noeudDepart.getY());
        int verifierX=noeudFin.getX()+diffX;
        int verifierY=noeudFin.getY()+diffY;

        //On calcul le nombre de pions pris en cas de percussion
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

        //On calcul le nombre de pions pris en cas d'aspiration
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
        if(nbAspiration>nbPercussion){
            exclurePions(0);
        }
        if(nbPercussion>nbAspiration){
            exclurePions(1);
        }
        if(nbPercussion==nbAspiration&&nbPercussion!=0){
            //On fait grossir tous les pions percutable et aspirables.
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
     * Permet d'exclure les pions aspirés ou les pions percutés.
     * @param choix
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

        //On supprime tous les noeuds des listes pour pas qu'ils ne soient encore présents au prochain coup.
        noeudsPercussion.clear();
        noeudsAspiration.clear();
    }
}
