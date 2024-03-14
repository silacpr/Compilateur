package plic.repint;

public class Ecrire extends Instruction {
    private Expression expression;
    public Ecrire(Expression expression) {
        this.expression = expression;
    }
    @Override
    public String toString(){
        String mipsCode;
        if (expression instanceof Idf) {
            Idf idf = (Idf) expression;
            Symbole symbole = null;

            symbole = TDS.getInstance().identifier(new Entree(idf.getNom()));

            int depl = symbole.getDeplacement();
            mipsCode = "lw $a0, " + depl + "($fp)\n";
        } else {
            mipsCode = expression.toString();
        }
        mipsCode += "li $v0, 1\nsyscall\n";
        mipsCode += "li $v0, 4\nla $a0, next\nsyscall\n";
        return mipsCode;
    }
}