package plic.repint;

public class Non extends Expression {
    private Expression expr;

    public Non(Expression expr) {
        this.expr = expr;
    }

    @Override
    public String toMips() {
        StringBuilder sb = new StringBuilder();
        sb.append(expr.toMips());
        sb.append("xori $v0, $v0, 0x1\n");
        return sb.toString();
    }

    @Override
    public String getType() {
        return "boolean";
    }
}
