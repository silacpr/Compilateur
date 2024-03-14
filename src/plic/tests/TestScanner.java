package plic.tests;

import plic.analyse.AnalyseurLexical;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestScanner {


    public static void main(String[] args) throws FileNotFoundException {

        String file_name = "src/plic/sources/PLIC0.mips";


        //AnalyseurLexical test = new AnalyseurLexical(new File(file_name));

/*
        while (test.hasNext()){
            System.out.println(test.nextLine());
        }
*/

        /*
        String bam = test.next();
        while (!bam.equals("EOF")){
            System.out.println(bam);
            bam = test.next();
        }
        System.out.println(test.next());



        String mot = "monte2tamoi";

        for (char c : mot.toCharArray()){
            if(!Character.isLetter(c)) System.out.println("AHH c'est pas bon");
        }

         */

    }
}
