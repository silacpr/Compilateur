package plic.analyse;

public class ErreurSyntaxique extends Exception {
    private String message;
    public ErreurSyntaxique(String programme_attendu) {
        this.message = programme_attendu;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
