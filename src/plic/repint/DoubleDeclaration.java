package plic.repint;

public class DoubleDeclaration extends Exception{
    private String message;
    public DoubleDeclaration(String probleme) {
        this.message = probleme;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
