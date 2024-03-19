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
            throw new ErreurSyntaxique("idf attendu");
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

        //while (uniteCourante.equals("entier")) {

        //je remplace parce que là il y a aussi le type tableau mtn

        while (this.estType()){
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
            String type = this.analyseType();

            if (type.equals("entier")){
                if (!this.estIdf()) {
                    throw new ErreurSyntaxique(" " + uniteCourante+"  Idf attendu");
                }

                Entree entree = new Entree(uniteCourante);

                Symbole symbole = new Symbole("entier", 0);

                TDS.getInstance().ajouter(entree, symbole);

                this.uniteCourante = this.analex.next();
                this.analyseTerminal(";");
            }
            else {
                //au début je vérifie le []
                this.analyseTerminal("[");

                //ça c'est la taille de mon tableau
                this.estCsteEntiere();

                int taille = Integer.parseInt(this.uniteCourante);

                if (taille<=0) throw new ErreurSyntaxique("taille tableau inférieure à 1");

                this.uniteCourante = this.analex.next();

                this.analyseTerminal("]");

                if (!this.estIdf()) {
                    throw new ErreurSyntaxique(" " + uniteCourante+"  Idf attendu");
                }
                //Le blase de mon tableau
                Entree entree = new Entree(uniteCourante);

                //là je peux créer mon symbole.
                Symbole symbole = new Symbole("tableau", 0, taille);

                TDS.getInstance().ajouter(entree, symbole);

                this.uniteCourante = this.analex.next();
                this.analyseTerminal(";");
            }
        }
    }

    private String analyseType() throws ErreurSyntaxique {
        if (this.uniteCourante.equals("entier")) {
            this.uniteCourante = this.analex.next();
            return "entier";
        }
        else if (this.uniteCourante.equals("tableau")) {
            this.uniteCourante = this.analex.next();
            return "tableau";
        }
        else throw new ErreurSyntaxique("type inconnu : " + this.uniteCourante);
    }

    private Instruction analyseInstruction() throws ErreurSyntaxique, AbsenceDeclaration {
        Instruction instruction;
        if (this.estIdf()) {
            if(!TDS.getInstance().idfexiste(new Entree(this.uniteCourante))) throw new AbsenceDeclaration("Idf inconnu :"+this.uniteCourante);
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

    private Instruction analyseAffectation() throws ErreurSyntaxique, AbsenceDeclaration {

        //bah là je dois aussi verifier si c'est un entier ou un tableau qui existe.

        Affectation affectation = null;

        String idf = this.uniteCourante;

        analyseAcces();

        String type = TDS.getInstance().identifier(new Entree(idf)).getType();

        System.out.println("type = "+type);

        if (type.equals("entier")){
            this.analyseTerminal(":=");
            affectation= new Affectation(idf,this.analyseExpression());
        }
        else {
            this.analyseTerminal("[");
            if (!this.estCsteEntiere()) throw new ErreurSyntaxique("index n'est pas une constante entière");
            int index = Integer.parseInt(uniteCourante);
            if (index<0 || index>=TDS.getInstance().identifier(new Entree(idf)).getTaille()) throw new ErreurSyntaxique("index out of bound");
            System.out.println("index = " + index);
            this.uniteCourante = this.analex.next();
            this.analyseTerminal("]");
            this.analyseTerminal(":=");
            affectation= new Affectation(idf,this.analyseExpression(), index);

        }




        this.analyseTerminal(";");
        return affectation;
    }

    //ça c'est sensé avoir plus d'utilité , peut etre qu'il renvoie un idf maybbeee
    private void analyseAcces() throws ErreurSyntaxique {
        if (!this.estIdf()) {
            throw new ErreurSyntaxique("Idf attendu");
        }
        else if (!TDS.getInstance().idfexiste(new Entree(uniteCourante))) throw new ErreurSyntaxique("idf inconu");
        this.uniteCourante = this.analex.next();
    }


    private Expression analyseExpression() throws ErreurSyntaxique, AbsenceDeclaration {
        if (this.estIdf()) {
            String idfName = this.uniteCourante;
            analyseAcces();

            //bah enfaite là fait que je gère le tab dans un tab
            // AHHHHH ça marche pas avec un index diff de 0 !!!

            // Vérifier si c'est un accès tableau
            if (this.uniteCourante.equals("[")) {
                this.uniteCourante = this.analex.next(); // Avancer pour obtenir l'index

                if (!this.estCsteEntiere()) throw new ErreurSyntaxique("Index entier attendu");
                int index = Integer.parseInt(this.uniteCourante);
                this.uniteCourante = this.analex.next(); // Avancer après l'index
                this.analyseTerminal("]"); // S'attendre à fermer le crochet

                System.out.println("AHHH");
                return new AccesTableau(idfName, index);
            } else {
                // Si ce n'est pas un accès tableau, retourner simplement l'identifiant
                return new Idf(idfName);
            }
        } else if (this.estCsteEntiere()) {
            int val = Integer.parseInt(this.uniteCourante);
            this.uniteCourante = this.analex.next(); // Avancer après le nombre
            return new Nombre(val);
        } else {
            throw new ErreurSyntaxique(this.uniteCourante + " : Caractère inconnue");
        }
    }


    //ça enfaite c'est utile donc faut que je modifie pour pouvoir l'utilsier finalement.
    /*
    private void analyseOperande() throws ErreurSyntaxique {
        //ça c'est pour après wait wait wait
        if (!this.estCsteEntiere() && !this.estIdf()) {
            throw new ErreurSyntaxique("opérande attendu");
        }
        this.uniteCourante = this.analex.next();
    }*/


    //ça bah nn pas utile enfaite
    private boolean estType() {
        return (this.uniteCourante.equals("entier")||this.uniteCourante.equals("tableau"));
    }





}
