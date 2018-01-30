package sample;

public class Pion {

    // Le noeud sur lequel se trouve le pion
    private Noeud noeudActuel;

    // La couleur du pion
    private Couleur couleur;

    public Pion(Noeud noeud , Couleur couleur) {
        this.noeudActuel = noeud;
        this.couleur = couleur;
    }

    /**
     * GETTERS ET SETTERS
     */

    public void setNoeudActuel(Noeud noeudActuel) {
        this.noeudActuel = noeudActuel;
    }

    /**
     * Déplace, si possible le pion dans la direction indiquée
     * @param direction
     */
    public void deplacer(Direction direction) {

    }

    @Override
    public String toString() {
        return "pion " + couleur;
    }
}
