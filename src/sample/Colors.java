package sample;

public enum Colors {

    WHITE("WHITE") ,
    BLACK("BLACK"),
    EMPTY("EMPTY");

    private String stringCouleur;

    Colors(String stringCouleur) {
        this.stringCouleur = stringCouleur;
    }

    @Override
    public String toString() {
        return stringCouleur;
    }


}
