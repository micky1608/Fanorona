package sample;

import javafx.scene.shape.Circle;

public class Noeud extends Circle {

    private boolean containsPion;

    // Le pion qui se trouve sur ce noeud ( null possible )
    private Pion pion;

    // La position du noeud sur la grille du plateau
    // L'origine du repère est en haut à gauche
    /*
        (0,0) (1,0) (2,0) (3,0) (4,0) (5,0) (6,0) (7,0) (8,0)
        (0,1) (1,1) (2,1) (3,1) (4,1) (5,1) (6,1) (7,1) (8,1)
        (0,2) (1,2) (2,2) (3,2) (4,2) (5,2) (6,2) (7,2) (8,2)
        (0,3) (1,3) (2,3) (3,3) (4,3) (5,3) (6,3) (7,3) (8,3)
        (0,4) (1,4) (2,4) (3,4) (4,4) (5,4) (6,4) (7,4) (8,4)
     */
    private int x;
    private int y;

    private static final double RAYON_NOEUD = 10;
    private static Controller controller = null;


    /**
     * CONSTRUCTEUR
     * Crée un noeuds à la position demandée
     * @param x
     * @param y
     */
    public Noeud(int x , int y) throws IllegalArgumentException {
        if(x < 0 || x > 8 || y < 0 || y > 4)
            throw new IllegalArgumentException();

        this.x = x;
        this.y = y;
        this.setRadius(RAYON_NOEUD);
        this.containsPion = false;
        this.pion = null;

        if(controller == null)
            controller = Main.getController();

    }

    /**
     * GETTERS ANS SETTERS
     */

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Affecte un pion sur ce noeud si ce noeud est vide
     * @param pion
     */
    public void setPion (Pion pion) {
        if(!containsPion && pion != null) {
            this.containsPion = true;
            this.pion = pion;
        }
    }

    @Override
    public String toString() {
        return "Noeud { " + "containsPion = " + containsPion + ", pion = " + pion + ", x = " + x + ", y = " + y + " }";
    }
}
