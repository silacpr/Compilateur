package plic.repint;

public class Affectation extends Instruction {
    private Acces acces;
    private Expression expression;

    public Affectation(Acces acces, Expression expression) {
        this.acces = acces;
        this.expression = expression;
    }

    @Override
    public String toString() {
        String codeAdresse = acces.getAdresse();
        String codeValeur = expression.toMips();

        return codeValeur +
                "sub $sp, $sp, 4\n" +
                "sw $v0, 0($sp)\n" +
                codeAdresse +
                "lw $v0, 0($sp)\n" +
                "add $sp, $sp, 4\n" +
                "sw $v0, 0($a0)\n";
    }

}
