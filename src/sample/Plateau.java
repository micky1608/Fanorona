package sample;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Plateau {

    // La matrice qui contient tous les noeuds du plateau
    private Noeud[][] noeuds;


    public Plateau () {
        this.noeuds = new Noeud[9][5];
        creerNoeuds();
        afficheNoeuds();
    }

    /**
     * Instancie tous les noeuds du plateau
      */
    private void creerNoeuds() {
        for(int i=0 ; i<9 ; i++) {
            for(int j=0 ; j<5 ; j++) {
                noeuds[i][j] = new Noeud(i,j);
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
     * indique si au moins un pion est selectionne pour faire un mouvement
     * @return
     */
    public boolean existPionSelected() {
        //TODO
        return false;
    }

    /**
     * @return le pion actuellement selectionne s'il y en a un (un seul)
     * @return null si aucun pion n'est selectionne ou plusieurs sont selectionnés
     */
    public Noeud getNoeudSelected() {
        //TODO
        return null;
    }

    private void afficheNoeuds() {
        for(int i=0 ; i<9 ; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.println(noeuds[i][j]);
            }
        }
    }

}
