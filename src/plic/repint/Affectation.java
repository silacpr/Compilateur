package plic.repint;

public class Affectation extends Instruction {
    private String idf;
    private Expression expression;
    public Affectation(String idf, Expression expression) {
        this.idf = idf;
        this.expression = expression;
    }

    public void verifier(){

    }

    @Override
    public String toString() {
        Symbole symbole = null;
        try {
            symbole = TDS.getInstance().identifier(new Entree(idf));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int depl = symbole.getDeplacement();
        String exprCode = expression.toString();
        String mipsCode = exprCode + "sw $t0, " + depl + "($fp)\n";
        return mipsCode;
    }
}