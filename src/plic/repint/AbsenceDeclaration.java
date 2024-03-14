package plic.repint;

public class AbsenceDeclaration extends Exception{
    private String message;
    public AbsenceDeclaration(String probleme) {
        this.message = probleme;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
