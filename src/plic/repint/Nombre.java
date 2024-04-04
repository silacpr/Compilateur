package plic.repint;

public class Nombre extends Expression{
    private int val;
    public Nombre(int val) {
        this.val = val;
    }
    @Override
    public String getType() {
        return "nombre";
    }

    public int getVal() {
        return val;
    }

    @Override
    public String toMips() {
        return "li $v0, " + val + "\n";
    }


}
