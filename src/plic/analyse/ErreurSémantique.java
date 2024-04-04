package plic.analyse;

public class ErreurSémantique extends Exception {
    private String message;
    public ErreurSémantique(String programme_attendu) {
        this.message = programme_attendu;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
