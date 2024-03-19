package plic.repint;

public class Ecrire extends Instruction {
    private Expression expression;

    public Ecrire(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        String mipsCode;
        mipsCode = expression.toString();

        mipsCode += "move $a0, $t0\n";

        mipsCode += "li $v0, 1\n";
        mipsCode += "syscall\n";

        mipsCode += "li $v0, 4\n";
        mipsCode += "la $a0, next\n";
        mipsCode += "syscall\n";

        return mipsCode;
    }
}
