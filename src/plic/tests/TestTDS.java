package plic.tests;

import org.junit.jupiter.api.Test;
import plic.repint.DoubleDeclaration;
import plic.repint.Entree;
import plic.repint.Symbole;
import plic.repint.TDS;

import java.util.HashMap;
import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TDSTest {

    @Test
    void ajout_normal() throws DoubleDeclaration {

        TDS tds = new TDS();
        tds.ajouter(new Entree("i"), new Symbole("entier", 0));
        tds.ajouter(new Entree("j"), new Symbole("entier", 0));

    }


}
