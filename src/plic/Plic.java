package plic;

import plic.analyse.AnalyseurSyntaxique;
import plic.analyse.ErreurSyntaxique;
import plic.analyse.ErreurSémantique;
import plic.repint.AbsenceDeclaration;
import plic.repint.Bloc;
import plic.repint.DoubleDeclaration;
import plic.repint.TDS;

import java.io.File;
import java.io.FileNotFoundException;

public class Plic {
    public static void main(String[] args) throws ErreurSyntaxique {
        try {

            args = new String[]{"src/plic/sources/test13.plic"};

            if (args.length != 1) {
                throw new IllegalArgumentException("ERREUR: Fichier source absent");
            }
            String nomFichier = args[0].contains("sources") ? args[0].substring(args[0].indexOf("sources")) : "";
            if (!nomFichier.endsWith(".plic")) {
                throw new IllegalArgumentException("ERREUR: Suffixe incorrect");
            }
            nomFichier="src/plic/"+nomFichier;
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
        catch (ErreurSémantique e){
            System.out.println("ERREUR: "+ e.getMessage());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    public Plic(String nomFichier) throws DoubleDeclaration, ErreurSémantique, ErreurSyntaxique, FileNotFoundException, AbsenceDeclaration {
        File file = new File(nomFichier);

        AnalyseurSyntaxique as = new AnalyseurSyntaxique(file);
        Bloc bloc = as.analyse();
        String mips = bloc.toMips();


        System.out.println(mips);




    }
}
