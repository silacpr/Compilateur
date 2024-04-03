package plic.analyse;

import plic.repint.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class AnalyseurSyntaxique {

    /*
    deja reussir à écrire un bon analyseur syntaxique

    coder pour generer avec les tableaux

    modifier pour accepter les operations

    coder pour generer avec des operations.

    jpp de ce cours il est horrible.
     */

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


    //analyse bloc à modifier???
    //je pense pas.
    private Bloc analyseBloc() throws ErreurSyntaxique, DoubleDeclaration, AbsenceDeclaration {
        this.analyseTerminal("{");

        Bloc bloc = new Bloc();

        // Ajoutez une boucle pour gérer les déclarations initiales si elles existent.
        while (uniteCourante.equals("entier")||uniteCourante.equals("tableau")) {
            this.analyseDeclaration();
        }

        // Ajoutez une boucle pour lire et ajouter toutes les instructions jusqu'à ce que vous rencontriez "}".
        while (!uniteCourante.equals("}")) {
            bloc.ajouter(this.analyseInstruction()); // Il faut ajouter cette instruction dans la boucle
        }
        this.analyseTerminal("}");
        return bloc;
    }


    //ça bouge pas
    private void analyseTerminal(String terminal) throws ErreurSyntaxique {
        if (!this.uniteCourante.equals(terminal)) {
            throw new ErreurSyntaxique(terminal + " attendu ");
        }
        this.uniteCourante = this.analex.next();
    }




    //à modifier???
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

    //bon bon bon
    private String analyseType() throws ErreurSyntaxique {
        if (!this.uniteCourante.equals("entier")&&!this.uniteCourante.equals("tableau")) {
            throw new ErreurSyntaxique("Type 'entier' ou 'tableau' attendu");
        }
        String type = uniteCourante;
        this.uniteCourante = this.analex.next();
        return type;
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


        //là c'est censé être acces pas besoin de tout ça..

        //String idf = this.uniteCourante;
        Acces acces = analyseAcces();


        this.analyseTerminal(":=");

        //System.out.println("bahhh" + this.uniteCourante);

        //this.uniteCourante = this.analex.next();


        //une affectation prend un acces pas juste un idf enfaite

        Affectation instruction = new Affectation(acces,this.analyseExpression());
        this.analyseTerminal(";");
        return instruction;
    }

    /*
    private void analyseAcces() throws ErreurSyntaxique {
        if (!this.estIdf()) {
            throw new ErreurSyntaxique("Idf attendu");
        }
        this.uniteCourante = this.analex.next();
    }
    */


    /*
    IL FAUT QUE J'AJOUTE CA PARCE QU'IL N'Y EST PAS DANS MA VERSION PRECEDENTE DONC
    JE DOIS L'AJOUTER POUR AVOIR UN VRAI CODE FONCTIONNEL
*/
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

            //this.uniteCourante = this.analex.next();
            this.analyseTerminal("]");

            acces = new AccesTableau(new Idf(idf), expression);

        }else {
            acces = new Idf(idf);
        }

        return acces;

    }



    //je dois aussi modifier ça

    //bah en fait là c'est juste l'expression en globale
    private Expression analyseExpression() throws ErreurSyntaxique {
        Expression result = analyseOperande(); // Analyse le premier opérande

        // Gérer les opérations binaires
        while (estOperateur()) {
            String operateur = uniteCourante;
            this.uniteCourante = this.analex.next();
            Expression droite = analyseOperande(); // Analyse le deuxième opérande

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


                // Ajouter d'autres cas pour "*", "/", opérateurs relationnels, etc.
            }
        }

        return result;
    }



    //qu'est ce que c'était que ça
    //ahh faut que je check ça mtn
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
        } else if (uniteCourante.equals("-")) { // Opération unaire négation
            this.uniteCourante = this.analex.next();
            Expression expr = analyseExpression();
            return new Negation(expr);
        } /*else if (uniteCourante.equals("non")) { // Opération logique unaire NON
            this.uniteCourante = this.analex.next();
            Expression expr = analyseExpression();
            return new Non(expr);
        }*/

        throw new ErreurSyntaxique("Opérande attendu");
    }


    private boolean estOperateur() {
        return Arrays.asList("+", "-", "*", "/", "et", "ou", "<", ">", "=", "#", "<=", ">=").contains(uniteCourante);
    }





}
