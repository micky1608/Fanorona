package sample;

public class Plateau {

    // La matrice qui contient tous les noeuds du plateau
    Noeud[][] noeuds;

    public Plateau () {
        this.noeuds = new Noeud[9][5];
        creerNoeuds();
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
}
