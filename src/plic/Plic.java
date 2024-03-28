package plic;

import plic.analyse.AnalyseurSyntaxique;
import plic.analyse.ErreurSyntaxique;
import plic.repint.AbsenceDeclaration;
import plic.repint.Bloc;
import plic.repint.DoubleDeclaration;

import java.io.File;
import java.io.FileNotFoundException;

public class Plic {
    public static void main(String[] args) throws ErreurSyntaxique {

        System.out.println(args[0]);
//        args[0]="src/plic/sources/test1.plic";
//        System.out.println(args.length);

        try {

            if (args.length != 1) {
                throw new IllegalArgumentException("ERREUR: Fichier source absent");
            }
            String nomFichier = args[0].contains("sources") ? args[0].substring(args[0].indexOf("sources")) : "";
            if (!nomFichier.endsWith(".plic")) {
                throw new IllegalArgumentException("ERREUR: Suffixe incorrect");
            }
            nomFichier="src/plic/"+nomFichier;
            System.out.println(nomFichier);
            File file = new File(nomFichier);
            if (!file.exists()) {
                throw new FileNotFoundException("ERREUR: Fichier source absent");
            }

            new Plic(nomFichier);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ErreurSyntaxique e) {
            System.out.println("ERREUR: " + e.getMessage());
        }catch (DoubleDeclaration e) {
            System.out.println("ERREUR: "+e.getMessage());
        }catch (AbsenceDeclaration e){
            System.out.println("ERREUR: "+ e.getMessage());
        }


    }

    public Plic(String nomFichier) throws FileNotFoundException, ErreurSyntaxique, DoubleDeclaration, AbsenceDeclaration {
        File file = new File(nomFichier);

        AnalyseurSyntaxique as = new AnalyseurSyntaxique(file);
        Bloc bloc = as.analyse();
        String mdr = bloc.toMips();
        System.out.println("\n\n"+mdr);


    }
}
