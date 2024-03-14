package plic.repint;

public class Idf extends Expression {
    private String nom;
    public Idf(String nom) {
        this.nom = nom;
    }
    @Override
    public String toString() {
        Symbole symbole = null;
        try {
            symbole = TDS.getInstance().identifier(new Entree(nom));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int depl = symbole.getDeplacement();
        return "lw $t0, " + depl + "($fp)\n";
    }
    public String getNom() {
        return this.nom;
    }
}