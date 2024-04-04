package plic.repint;

public class Ecrire extends Instruction {
    private Expression expression;

    public Ecrire(Expression expression) {
        this.expression = expression;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(expression.toMips());

        sb.append("move $a0, $v0\n");
        sb.append("li $v0, 1\n");
        sb.append("syscall\n");

        sb.append("li $v0, 4\n");
        sb.append("la $a0, next\n");
        sb.append("syscall\n");

        return sb.toString();
    }

    @Override
    public void verifier() throws AbsenceDeclaration {
        if (expression.getType().equals("tableau")){
            ((AccesTableau) expression).verifier();
        }
        else if (expression.getType().equals("idf")){
            ((Idf) expression).verifier();
        }
    }

}
