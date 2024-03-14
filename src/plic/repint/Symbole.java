package plic.repint;

public class Symbole {
    private String type;
    private int deplacement;
    private int taille = 0;

    public Symbole(String type, int deplacement) {
        this.type = type;
        this.deplacement = deplacement;
    }

    public Symbole(String type, int deplacement, int taille) {
        this.type = type;
        this.deplacement = deplacement;
        this.taille = taille;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getDeplacement() {
        return deplacement;
    }
    public void setDeplacement(int deplacement) {
        this.deplacement = deplacement;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }
}
