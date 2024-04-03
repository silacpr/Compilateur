package plic.repint;

public class Idf extends Acces {
    private String nom;
    public Idf(String nom) {
        this.nom = nom;
    }
    @Override
    public String getAdresse() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(nom));

        int depl = symbole.getDeplacement();
        return "la $a0, " + depl + "($s7)\n";
    }


    @Override
    public String toMips() {
        return getAdresse() + "lw $v0, 0($a0)\n";
    }



    public String getNom() {
        return this.nom;
    }


    @Override
    public String getType() {
        return "idf";
    }


}