package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Noeud extends Circle {

    private boolean containsPion;

    // La position du noeud sur la grille du plateau
    // L'origine du repère est en haut à gauche
    /*
        (0,0) (1,0) (2,0) (3,0) (4,0) (5,0) (6,0) (7,0) (8,0)
        (0,1) (1,1) (2,1) (3,1) (4,1) (5,1) (6,1) (7,1) (8,1)
        (0,2) (1,2) (2,2) (3,2) (4,2) (5,2) (6,2) (7,2) (8,2)
        (0,3) (1,3) (2,3) (3,3) (4,3) (5,3) (6,3) (7,3) (8,3)
        (0,4) (1,4) (2,4) (3,4) (4,4) (5,4) (6,4) (7,4) (8,4)
     */
    private int posX;
    private int posY;

    private static final double RAYON_NOEUD_VIDE = 10;
    private static final double RAYON_NOEUD_PION = 15;
    private static final Color COULEUR_UTILISATEUR = Color.WHITE;
    private static final Color COULEUR_ORDINATEUR = Color.BLACK;
    private static final Color COULEUR_VIDE = Color.GREY;

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

        this.posX = x;
        this.posY = y;
        this.setRadius(RAYON_NOEUD_VIDE);
        this.containsPion = false;

        if(controller == null)
            controller = Main.getController();

    }

    /**
     * GETTERS ANS SETTERS
     */

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    /**
     * Met a jour le booleen qui indique si le noeud contient un pion et met a jour le rayon du cercle ainsi que sa couleur
     * @param containsPion
     * @param codeCouleur permet de definir la nouvelle couleur du noeud
     *                    0 : Couleur des pions utilisateur
     *                    1 : Couleur des pions ordinateur
     *                    2 : Couleur noeud vide
     */
    public void setContainsPion (boolean containsPion , int codeCouleur) {
        if(codeCouleur < 0 ||  codeCouleur > 2)
            throw new IllegalArgumentException();

        this.containsPion = containsPion;

        Color couleur = null;
        double rayon = 0;
        if (codeCouleur == 0) couleur = COULEUR_UTILISATEUR;
        if (codeCouleur == 1) couleur = COULEUR_ORDINATEUR;
        if (codeCouleur == 2) couleur = COULEUR_VIDE;

        if(this.containsPion == true)
            rayon = RAYON_NOEUD_PION;
        else
            rayon = RAYON_NOEUD_VIDE;

        this.setRadius(rayon);
        this.setFill(couleur);

    }

    @Override
    public String toString() {
        return "Noeud { " + "containsPion = " + containsPion + ", posX = " + posX + ", posY = " + posY + ", couleur = " + this.getFill().toString() + '}';
    }
}
