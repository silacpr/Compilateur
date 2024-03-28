package plic.repint;

public class SymboleTableau extends Symbole{


    private String type;
    private int deplacement;
    private int taille;

    public SymboleTableau(String type, int deplacement, int taille){
        this.type=type;
        this.deplacement=deplacement;
        this.taille = taille;
    }

    @Override
    public int getDeplacement() {
        return 0;
    }

    @Override
    public String getType() {
        return null;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }
}
