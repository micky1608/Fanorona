package sample;

public class Noeud {

    private boolean containsPion;

    // Le pion qui se trouve sur ce noeud ( null possible )
    private Pion pion;

    // La position du noeud sur la grille du plateau
    // L'origine du repère est en bas à droite
    private int x;
    private int y;

    /**
     * Affecte un pion sur ce noeud si ce noeud est vide
     * @param pion
     */
    public void setPion (Pion pion) {
        if(!containsPion) {
            this.containsPion = true;
            this.pion = pion;
        }
    }


}
