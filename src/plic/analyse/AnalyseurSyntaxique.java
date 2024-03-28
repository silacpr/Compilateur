package plic.analyse;

import plic.repint.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class AnalyseurSyntaxique {

    private AnalyseurLexical analex;
    private String uniteCourante;


    public AnalyseurSyntaxique(File file) throws FileNotFoundException {
        this.analex = new AnalyseurLexical(file);

    }

    public Bloc analyse() throws ErreurSyntaxique, DoubleDeclaration, AbsenceDeclaration {
        this.uniteCourante = this.analex.next();
        Bloc bloc =  this.analyseProg();
        if(!this.uniteCourante.equals("EOF")) throw new ErreurSyntaxique("EOF attendu");
        return bloc;
    }

    private Bloc analyseProg() throws ErreurSyntaxique, DoubleDeclaration, AbsenceDeclaration {
        if (!this.uniteCourante.equals("programme")) {
            throw new ErreurSyntaxique("programme attendu");
        }

        this.uniteCourante = this.analex.next();

        if(!this.estIdf()){
            throw new ErreurSyntaxique("idf attendu 1");
        }

        this.uniteCourante = this.analex.next();

        return this.analyseBloc();
    }

    private boolean estIdf() {
        List<String> motsCles = Arrays.stream(new String[]{"programme", "entier", "ecrire", "lire",
                "si", "alors", "sinon", "pour", "dans", "repeter", "tant que", "et", "ou", "non"}).toList();

        for (char c : uniteCourante.toCharArray()){
            if(!Character.isLetter(c)) return false;
        }

        for (int i=0; i<motsCles.size(); i++){
            if (motsCles.get(i).equals(uniteCourante)) return false;
        }

        return true;
    }

    private boolean estCsteEntiere(){
        if (uniteCourante.isEmpty()) return false;
        for (char c : uniteCourante.toCharArray()){
            if(!Character.isDigit(c)) return false;
        }
        return true;
    }

    private Bloc analyseBloc() throws ErreurSyntaxique, DoubleDeclaration, AbsenceDeclaration {
        this.analyseTerminal("{");

        Bloc bloc = new Bloc();

        // Ajoutez une boucle pour gérer les déclarations initiales si elles existent.
        while (uniteCourante.equals("entier")) {
            this.analyseDeclaration();
        }

        // Ajoutez une boucle pour lire et ajouter toutes les instructions jusqu'à ce que vous rencontriez "}".
        while (!uniteCourante.equals("}")) {
            bloc.ajouter(this.analyseInstruction()); // Il faut ajouter cette instruction dans la boucle
        }
        this.analyseTerminal("}");
        return bloc;
    }

    private void analyseTerminal(String terminal) throws ErreurSyntaxique {
        if (!this.uniteCourante.equals(terminal)) {
            throw new ErreurSyntaxique(terminal + " attendu ");
        }
        this.uniteCourante = this.analex.next();
    }



    private void analyseDeclaration() throws ErreurSyntaxique, DoubleDeclaration {
        while (this.estType()) {
            this.analyseType();
            if (!this.estIdf()) {
                throw new ErreurSyntaxique(" " + uniteCourante+"  Idf attendu");
            }

            Entree entree = new Entree(uniteCourante);

            Symbole symbole = new SymboleEntier("entier", 0);

            TDS.getInstance().ajouter(entree, symbole);

            this.uniteCourante = this.analex.next();
            this.analyseTerminal(";");
        }
    }

    private void analyseType() throws ErreurSyntaxique {
        if (!this.uniteCourante.equals("entier")) {
            throw new ErreurSyntaxique("Type 'entier' attendu");
        }
        this.uniteCourante = this.analex.next();
    }

    private Instruction analyseInstruction() throws ErreurSyntaxique, AbsenceDeclaration {
        Instruction instruction;

        if (this.estIdf()) {
            if(TDS.getInstance().idfexiste(new Entree(this.uniteCourante))==false) throw new AbsenceDeclaration("Idf inconnu :"+this.uniteCourante);
            instruction = this.analyseAffectation();
        } else if (this.uniteCourante.equals("ecrire")) {
            instruction = this.analyseEcrire();
        } else {
            throw new ErreurSyntaxique("caractère inconnue : " + this.uniteCourante);
        }
        return instruction;
    }

    private Instruction analyseEcrire() throws ErreurSyntaxique, AbsenceDeclaration {
        this.uniteCourante = this.analex.next();
        if (this.estIdf()){
            if (!TDS.getInstance().idfexiste(new Entree(this.uniteCourante))) throw new AbsenceDeclaration("Idf inconnu : "+this.uniteCourante);
        }
        Ecrire ecrire = new Ecrire(this.analyseExpression());
        this.analyseTerminal(";");
        return ecrire;
    }

    private Instruction analyseAffectation() throws ErreurSyntaxique {

        String idf = this.uniteCourante;

        analyseAcces();

        if (!this.uniteCourante.equals(":=")) {
            throw new ErreurSyntaxique(":= attendu");
        }

        this.uniteCourante = this.analex.next();

        Affectation instruction = new Affectation(idf,this.analyseExpression());
        this.analyseTerminal(";");
        return instruction;
    }

    private void analyseAcces() throws ErreurSyntaxique {
        if (!this.estIdf()) {
            throw new ErreurSyntaxique("Idf attendu");
        }
        this.uniteCourante = this.analex.next();
    }


    private Expression analyseExpression() throws ErreurSyntaxique {
        if (this.estIdf()) {
            Idf idf = new Idf(this.uniteCourante);
            analyseAcces();
            return  idf;
        } else if (this.estCsteEntiere()) {
            Nombre nombre = new Nombre(Integer.parseInt(this.uniteCourante));
            this.uniteCourante = this.analex.next();
            return nombre;
        } else {
           throw new ErreurSyntaxique( this.uniteCourante + " : Caractère inconnue");
        }
    }


    private void analyseOperande() throws ErreurSyntaxique {
        if (!this.estCsteEntiere() && !this.estIdf()) {
            throw new ErreurSyntaxique("opérande attendu");
        }
        this.uniteCourante = this.analex.next();
    }

    private boolean estType() {
        return this.uniteCourante.equals("entier");
    }






}
