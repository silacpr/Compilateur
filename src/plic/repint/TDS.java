package plic.repint;
import java.util.HashMap;
import java.util.Map;
public class TDS {
    private static TDS instance = null;
    private Map<Entree, Symbole> table;
    private int cptDepl;
    public TDS() {
        this.table = new HashMap<>();
        this.cptDepl = 0;
    }
    public static TDS getInstance() {
        if (instance == null) {
            instance = new TDS();
        }
        return instance;
    }
    public void ajouter(Entree e, Symbole s) throws DoubleDeclaration {
        if (table.containsKey(e)) throw new DoubleDeclaration("Double déclaration : "+e.getIdf());
        if (table.containsKey(e)) throw new DoubleDeclaration("Double déclaration : "+e.getIdf());
        if (s.getType().equals("entier")){
            table.put(e, new SymboleEntier(s.getType(), cptDepl * -4));
            cptDepl++;
        }
        else {
            Symbole symbole = new SymboleTableau(s.getType(), cptDepl * -4, ((SymboleTableau) s).getTaille());
            table.put(e, symbole);
            cptDepl = cptDepl + ((SymboleTableau) s).getTaille();
        }
    }
    public Symbole identifier(Entree e) {
        return this.table.get(e);
    }
    public boolean idfexiste(Entree e){
        return this.table.containsKey(e);
    }
}
