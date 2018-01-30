package sample;

public enum Couleur {

    BLANC("BLANC") ,
    NOIR("NOIR");

    private String stringCouleur;

    Couleur(String stringCouleur) {
        this.stringCouleur = stringCouleur;
    }

    @Override
    public String toString() {
        return stringCouleur;
    }
}
