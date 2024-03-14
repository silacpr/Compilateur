package plic.repint;

import java.util.HashMap;

public class Entree {
    private String idf;
    public Entree(String idf) {
        this.idf = idf;
    }
    @Override
    public int hashCode() {
        return this.idf.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        return getClass()==obj.getClass();
    }
    public String getIdf() {
        return idf;
    }
}
