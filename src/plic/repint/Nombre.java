package plic.repint;

public class Nombre extends Expression{
    private int val;
    public Nombre(int val) {
        this.val = val;
    }
    @Override
    public String toString() {
        return "li $t0, " + val + "\n";
    }
}
