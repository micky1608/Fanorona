package sample;

public class Plateau {

    // La matrice qui contient tous les noeuds du plateau
    private Noeud[][] noeuds;

    private static Controller controller = null;


    public Plateau () {
        this.noeuds = new Noeud[9][5];
        controller = Main.getController();
        creerNoeuds();
        afficheNoeuds();
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
                System.out.println(noeuds[i][j]);
                controller.addNoeud(noeuds[i][j] , i , j);
            }
        }
    }

}
