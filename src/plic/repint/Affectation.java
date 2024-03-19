package plic.repint;

public class Affectation extends Instruction {
    private String idf;
    private Expression expression;

    private int index = -1;
    public Affectation(String idf, Expression expression) {
        this.idf = idf;
        this.expression = expression;
    }

    public Affectation(String idf, Expression expression, int index) {
        this.idf = idf;
        this.expression = expression;
        this.index = index;
    }

    @Override
    public String toString() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(idf));
        int depl = symbole.getDeplacement();
        String exprCode = expression.toString();

        if (index != -1) {
            int offset = index * 4;
            int totalDepl = depl - offset;
            return exprCode + "sw $t0, " + totalDepl + "($fp)\n";
        } else {
            return exprCode + "sw $t0, " + depl + "($fp)\n";
        }
    }

}

