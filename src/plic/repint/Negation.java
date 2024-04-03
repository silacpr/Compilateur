package plic.repint;

public class Negation extends Expression {
    private Expression expression;

    public Negation(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toMips() {
        StringBuilder sb = new StringBuilder();
        sb.append(expression.toMips());
        sb.append("sub $v0, $zero, $v0\n");

        return sb.toString();
    }

    @Override
    public String getType() {
        return "entier";
    }
}
