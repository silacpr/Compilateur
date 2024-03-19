package plic.repint;

public class AccesTableau extends Expression {

    private String nom;
    private int index;

    public AccesTableau(String nom, int index) {
        this.nom = nom;
        this.index = index;
    }

    @Override
    public String toString() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(nom));
        int baseDepl = symbole.getDeplacement();

        int offset = index * 4;
        int totalDepl = baseDepl - offset;

        return "lw $t0, " + totalDepl + "($fp)\n";
    }

}
