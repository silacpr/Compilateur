package plic.repint;

public class SymboleEntier extends Symbole{

    private String type;
    private int deplacement;

    public SymboleEntier(String type, int deplacement) {
        this.type = type;
        this.deplacement = deplacement;
    }

    @Override
    public int getDeplacement() {
        return deplacement;
    }

    @Override
    public String getType() {
        return type;
    }
}
