package plic.repint;

public class Soustraction extends Expression {
    private Expression gauche;
    private Expression droite;

    public Soustraction(Expression gauche, Expression droite) {
        this.gauche = gauche;
        this.droite = droite;
    }

    @Override
    public String toMips() {
        StringBuilder sb = new StringBuilder();
        sb.append(gauche.toMips());
        sb.append("sw $v0, 0($sp)\n");
        sb.append("addiu $sp, $sp, -4\n");
        sb.append(droite.toMips());
        sb.append("lw $t1, 4($sp)\n");
        sb.append("addiu $sp, $sp, 4\n");
        sb.append("sub $v0, $t1, $v0\n");

        return sb.toString();
    }

    @Override
    public String getType() {
        return "entier";
    }
}
