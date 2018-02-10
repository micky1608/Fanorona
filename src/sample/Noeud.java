package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


public class Noeud extends Circle {

    private boolean containsPion;
    private boolean isNoeudSelected;
    private boolean isAspirable;
    private boolean isPercutable;
    private boolean isPaire;
    private Plateau plateau;

    // Positions of the nodes in the grid.
    // The origin is on the top left corner.
    /*
        (0,0) (1,0) (2,0) (3,0) (4,0) (5,0) (6,0) (7,0) (8,0)
        (0,1) (1,1) (2,1) (3,1) (4,1) (5,1) (6,1) (7,1) (8,1)
        (0,2) (1,2) (2,2) (3,2) (4,2) (5,2) (6,2) (7,2) (8,2)
        (0,3) (1,3) (2,3) (3,3) (4,3) (5,3) (6,3) (7,3) (8,3)
        (0,4) (1,4) (2,4) (3,4) (4,4) (5,4) (6,4) (7,4) (8,4)
     */
    private int posX;
    private int posY;

    private static final double RAYON_NOEUD_VIDE = 20;
    private static final double RAYON_NOEUD_PION = 25;
    private static final double RAYON_NOEUD_PION_SELECTED = 30;
    private static final Color COULEUR_UTILISATEUR = Color.WHITE;
    private static final Color COULEUR_ORDINATEUR = Color.BLACK;
    private static final Color COULEUR_VIDE = Color.GREY;



    /**
     * CONSTRUCTOR
     * Create a node considering the position x and y.
     * @param x
     * @param y
     */
    public Noeud(int x , int y , Plateau plateau) throws IllegalArgumentException {
        if(x < 0 || x > 8 || y < 0 || y > 4)
            throw new IllegalArgumentException();

        this.posX = x;
        this.posY = y;
        this.plateau = plateau;
        this.isNoeudSelected = false;

        if((x+y)%2==0){this.isPaire = true; }
        else{this.isPaire = false; }

        //The handler will be different considering the moment of the game.
        this.setOnMouseClicked((event) -> {
                if(this.isPercutable)
                    this.exclure(1);
                if(this.isAspirable){
                    this.exclure(0);
                }
                if(!this.isNoeudSelected()&&!this.plateau.existNoeudAspirable()&&!this.plateau.existNoeudPercutable())
                    this.select(true);
                else
                    this.deselect();
        });

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

    public boolean isNoeudSelected () {
        return this.isNoeudSelected;
    }

    public boolean isContainsPion () {
        return this.containsPion;
    }

    public static double getRayonNoeudPion() {
        return RAYON_NOEUD_PION;
    }

    public static double getRayonNoeudPionSelected() {
        return RAYON_NOEUD_PION_SELECTED;
    }

    public void setPercutable(boolean val){
        this.isPercutable=val;
    }

    public void setAspirable(boolean val){
        this.isAspirable=val;
    }

    public boolean getAspirable(){
        return isAspirable;
    }

    public boolean getPercutable(){
        return isPercutable;
    }

    /**
     * Updates the boolean which indicates if the node contains a pawn or not, and updates he size and color.
     * @param containsPion
     * @param codeCouleur defines the new color of the node.
     *                    0 : Player's color (white)
     *                    1 : CPU's color (black)
     *                    2 : Node is empty (grey)
     */
    public void setContainsPion (boolean containsPion , int codeCouleur) {
        if(codeCouleur < 0 ||  codeCouleur > 2)
            throw new IllegalArgumentException();

        this.containsPion = containsPion;

        Color couleur;
        double rayon = 0;

        switch(codeCouleur) {
            case 0 :
                couleur = COULEUR_UTILISATEUR;
                break;
            case 1 :
                couleur = COULEUR_ORDINATEUR;
                break;
            default :
                couleur = COULEUR_VIDE;
                break;
        }

        if(this.containsPion)
            rayon = RAYON_NOEUD_PION;
        else
            rayon = RAYON_NOEUD_VIDE;

        this.setRadius(rayon);
        this.setFill(couleur);
    }

    /**
     * Allows to select a node
     * @param selectByUser indicates if the action is made by the player or the CPU.
     *                     true : player -> can only select white pawns.
     *                     false : CPU -> can only select black pawns.
     */
    public void select (boolean selectByUser) {
        Paint couleurNoeud = this.getFill();

        if(couleurNoeud.equals(Color.WHITE) || couleurNoeud.equals(Color.BLACK)) {

            // If we try to select a pawn, no other pawn should be selected at the same time.
            if(!plateau.existNoeudSelected()) {

                if (selectByUser) {
                    if (couleurNoeud.equals(Color.WHITE))
                        this.isNoeudSelected = true;
                } else {
                    if (couleurNoeud.equals(Color.BLACK))
                        this.isNoeudSelected = true;
                }
            }
        }
        else {

            // We selected an empty node, so there must be a pawn selected which will make
            // the movement from his position to the empty node selected.
            //The nodes must be neighbours
            if(plateau.existNoeudSelected()) {
                Noeud noeudDepartMouvement = plateau.getNoeudSelected();

                if(this.isVoisinOf(noeudDepartMouvement)) {

                    // We get the color of the initial node.
                    int codeCouleur = 2;
                    if(noeudDepartMouvement.getFill().equals(Color.WHITE)) codeCouleur = 0;
                    if(noeudDepartMouvement.getFill().equals(Color.BLACK)) codeCouleur = 1;

                    // Indicates that the initial node will now be empty.
                    noeudDepartMouvement.setContainsPion(false , 2);

                    // The initial node won't be selected after this movement.
                    noeudDepartMouvement.deselect();

                    // The new node contains now a pawn, with the same color.
                    this.setContainsPion(true , codeCouleur);

                    this.plateau.choisirPionsAExclure(noeudDepartMouvement, this);
                }
            }
        }
    }

    public void deselect() {
        this.isNoeudSelected = false;
    }

    public void exclure(int choix){
        plateau.exclurePions(choix);
    }

    /**
     * Indicates if the node is neighbor.
     * @param otherNoeud
     * @return
     */
    private boolean isVoisinOf(Noeud otherNoeud) {
        int posX_other = otherNoeud.getX();
        int posY_other = otherNoeud.getY();

        //A node can't be neighbor of himself.
        if(otherNoeud == this){
            return false;
        }
        //An even node can have diagonal neighbors, while an odd node can't.
        if(isPaire) {
            if (posX_other == posX || posX_other == posX - 1 || posX_other == posX + 1)
                return (posY_other == posY || posY_other == posY - 1 || posY_other == posY + 1) ? true : false;
        }
        else{
            if(posX_other == posX)
                return (posY_other == posY - 1 || posY_other == posY + 1) ? true : false;
            if(posY_other == posY)
                return (posX_other == posX - 1 || posX_other == posX + 1) ? true : false;
        }
        return false;
    }


    @Override
    public String toString() {
        return "Noeud { " + "containsPion = " + containsPion + ", posX = " + posX + ", posY = " + posY + ", couleur = " + this.getFill().toString() + ", isNoeudSelected = " + isNoeudSelected + " }";
    }

}
