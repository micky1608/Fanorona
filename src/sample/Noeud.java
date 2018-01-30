package sample;

import javafx.scene.shape.Circle;

public class Noeud extends Circle {

    private boolean containsPion;

    // Le pion qui se trouve sur ce noeud ( null possible )
    private Pion pion;

    // La position du noeud sur la grille du plateau
    // L'origine du repère est en haut à gauche
    /*
        (1,1) (2,1) (3,1) (4,1) (5,1) (6,1) (7,1) (8,1) (9,1)
        (1,2) (2,2) (3,2) (4,2) (5,2) (6,2) (7,2) (8,2) (9,2)
        (1,3) (2,3) (3,3) (4,3) (5,3) (6,3) (7,3) (8,3) (9,3)
        (1,4) (2,4) (3,4) (4,4) (5,4) (6,4) (7,4) (8,4) (9,4)
        (1,5) (2,5) (3,5) (4,5) (5,5) (6,5) (7,5) (8,5) (9,5)
     */
    private int x;
    private int y;

    static final double RAYON_NOEUD = 10;


    /**
     * Crée un noeuds à la position demandée
     * @param x
     * @param y
     */
    public Noeud(int x , int y) throws IllegalArgumentException {
        if(x < 1 || x > 9 || y < 1 || y > 5)
            throw new IllegalArgumentException();
        this.x = x;
        this.y = y;
        this.setRadius(RAYON_NOEUD);
    }

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
