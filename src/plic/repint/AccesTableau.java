package plic.repint;

public class AccesTableau extends Acces {
    private Idf idf;
    private Expression index;

    public AccesTableau(Idf idf, Expression index) {
        this.idf = idf;
        this.index = index;
    }

    @Override
    public String toMips() {
        return getAdresse() + "lw $v0, 0($a0)\n";
    }

    @Override
    public String getAdresse() {
        StringBuilder sb = new StringBuilder();

        sb.append(index.toMips());
        sb.append("sll $v0, $v0, 2\n");

        sb.append(idf.getAdresse());
        sb.append("add $a0, $a0, $v0\n");

        return sb.toString();
    }

    @Override
    public String getType() {
        return "tableau";
    }
}
