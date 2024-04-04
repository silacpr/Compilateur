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

    /*

    bon alors mtn il faut que j'ajoute les instructions comme pour si et lire
     */


    public AnalyseurSyntaxique(File file) throws FileNotFoundException {
        this.analex = new AnalyseurLexical(file);
    }

    public Bloc analyse() throws ErreurSyntaxique, DoubleDeclaration, AbsenceDeclaration{

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


    private Bloc analyseBloc() throws ErreurSyntaxique, DoubleDeclaration, AbsenceDeclaration  {
        this.analyseTerminal("{");

        Bloc bloc = new Bloc();

        while (uniteCourante.equals("entier")||uniteCourante.equals("tableau")) {
            this.analyseDeclaration();
        }

        while (!uniteCourante.equals("}")) {
            bloc.ajouter(this.analyseInstruction());
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

            String type = this.analyseType();
            Symbole symbole = null;
            Entree entree = null;


            if (type.equals("entier")){
                if (!this.estIdf()) {
                    throw new ErreurSyntaxique(" " + uniteCourante+"  Idf attendu");
                }
                symbole = new SymboleEntier("entier", 0);
                entree = new Entree(this.uniteCourante);

            }
            else if (type.equals("tableau")){
                this.analyseTerminal("[");
                int taille;
                try{
                    taille = Integer.parseInt(this.uniteCourante);
                    if (taille<0) throw new ErreurSyntaxique("taille tableau invalide");
                }catch (Exception e){
                    throw new ErreurSyntaxique("taille tableau invalide");
                }
                this.uniteCourante = this.analex.next();
                this.analyseTerminal("]");
                if (!this.estIdf()) {
                    throw new ErreurSyntaxique(" " + uniteCourante+"  Idf attendu");
                }
                symbole = new SymboleTableau("tableau", 0, taille);
                entree = new Entree(this.uniteCourante);
            }
            else {
                throw new ErreurSyntaxique(" " + uniteCourante+"  Idf attendu");
            }

            TDS.getInstance().ajouter(entree, symbole);

            this.uniteCourante = this.analex.next();
            this.analyseTerminal(";");

    }

    private String analyseType() throws ErreurSyntaxique {
        if (!this.uniteCourante.equals("entier")&&!this.uniteCourante.equals("tableau")) {
            throw new ErreurSyntaxique("Type 'entier' ou 'tableau' attendu");
        }
        String type = uniteCourante;
        this.uniteCourante = this.analex.next();
        return type;
    }

    private Instruction analyseInstruction() throws ErreurSyntaxique, AbsenceDeclaration{
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

    private Instruction analyseAffectation() throws ErreurSyntaxique{

        Acces acces = analyseAcces();


        this.analyseTerminal(":=");


        Expression expression = this.analyseExpression();


        Affectation instruction = new Affectation(acces,expression);

        this.analyseTerminal(";");
        return instruction;
    }

    private Acces analyseAcces() throws ErreurSyntaxique {
        Acces acces = null;
        if (!this.estIdf()) {
            throw new ErreurSyntaxique("Idf attendu");
        }

        String idf = this.uniteCourante;

        this.uniteCourante = this.analex.next();

        if (this.uniteCourante.equals("[")){
            this.uniteCourante = this.analex.next();

            Expression expression = this.analyseExpression();

            this.analyseTerminal("]");

            acces = new AccesTableau(new Idf(idf), expression);

        }else {
            acces = new Idf(idf);
        }

        return acces;

    }


    private Expression analyseExpression() throws ErreurSyntaxique {
        Expression result = analyseOperande();

        while (estOperateur()) {
            String operateur = uniteCourante;
            this.uniteCourante = this.analex.next();
            Expression droite = analyseOperande();

            switch (operateur) {
                case "+":
                    result = new Somme(result, droite);
                    break;
                case "-":
                    result = new Soustraction(result, droite);
                    break;
                case "*":
                    result = new Multiplication(result, droite);
                    break;
                case "et":
                    result = new Et(result, droite);
                    break;
                case "ou":
                    result = new Ou(result, droite);
                    break;
                case "<":
                    result = new Inferieur(result, droite);
                    break;
                case ">":
                    result = new Superieur(result, droite);
                    break;
                case "=":
                    result = new Egal(result, droite);
                    break;
                case "#":
                    result = new NonEgal(result, droite);
                    break;
                case ">=":
                    result = new SuperieurOuEgal(result, droite);
                    break;
                case "<=":
                    result = new InferieurOuEgal(result, droite);
                    break;
            }
        }

        return result;
    }


    private Expression analyseOperande() throws ErreurSyntaxique {
        if (estCsteEntiere()) {
            int valeur = Integer.parseInt(uniteCourante);
            this.uniteCourante = this.analex.next();
            return new Nombre(valeur);
        } else if (estIdf()) {
            return analyseAcces();
        } else if (uniteCourante.equals("(")) {
            this.uniteCourante = this.analex.next();
            Expression expr = analyseExpression();
            analyseTerminal(")");
            return expr;
        } else if (uniteCourante.equals("-")) {
            this.uniteCourante = this.analex.next();
            Expression expr = analyseExpression();
            return new Negation(expr);
        } else if (uniteCourante.equals("non")) {
            this.uniteCourante = this.analex.next();
            Expression expr = analyseExpression();
            return new Non(expr);
        }


        throw new ErreurSyntaxique("Opérande attendu");
    }


    private boolean estOperateur() {
        return Arrays.asList("+", "-", "*", "/", "et", "ou", "<", ">", "=", "#", "<=", ">=").contains(uniteCourante);
    }

}
