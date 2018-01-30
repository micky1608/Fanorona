package sample;

import java.util.ArrayList;
import java.util.List;

public class Plateau {

    // La matrice qui contient tous les noeuds du plateau
    private Noeud[][] noeuds;

    // La liste de qui contient tous les pions en jeu
    private List<Pion> pions;



    public Plateau () {
        this.noeuds = new Noeud[9][5];
        this.pions = new ArrayList<Pion>();

        creerNoeuds();
        creerPions();

        afficheNoeuds();
    }

    /**
     * Instancie tous les noeuds du plateau
      */
    private void creerNoeuds() {
        for(int i=0 ; i<9 ; i++) {
            for(int j=0 ; j<5 ; j++) {
                noeuds[i][j] = new Noeud(i,j);
            }
        }
    }

    /**
     * Instancie tous les pions de la partie et les place sur les bons noeuds au départ de la partie
     */
    private void creerPions() {
        for(int i=0 ; i<9 ; i++) {
            for(int j=0 ; j<5 ; j++) {
                Pion pion = null;
                switch(j) {
                    case 0 : case 1 :
                        pion = new Pion(noeuds[i][j] , Couleur.NOIR);
                        break;
                    case 2 :
                        if(i != 4) {
                            if(i%2 == 0)
                                pion = new Pion(noeuds[i][j] , Couleur.BLANC);
                            else
                                pion = new Pion(noeuds[i][j] , Couleur.NOIR);
                        }
                        break;
                    case 3 : case 4 :
                        pion = new Pion(noeuds[i][j] , Couleur.BLANC);
                        break;
                }
                noeuds[i][j].setPion(pion);
                pions.add(pion);
            }
        }
    }

    private void afficheNoeuds() {
        for(int i=0 ; i<9 ; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.println(noeuds[i][j]);
            }
        }
    }

}
