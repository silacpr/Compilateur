package plic.repint;

public abstract class Acces extends Expression {
    @Override
    public String toString() {
        return null;
    }

    public abstract String getAdresse();

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String toMips() {
        return null;
    }


    public void verifier(){

    }


}
