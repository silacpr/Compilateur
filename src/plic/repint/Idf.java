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

    @Override
    public void verifier() throws AbsenceDeclaration {

        if (!TDS.getInstance().idfexiste(new Entree(this.getNom()))) throw new AbsenceDeclaration(this.getNom()+" non déclaré");

    }


}