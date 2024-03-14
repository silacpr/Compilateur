package plic.analyse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AnalyseurLexical {
    private boolean commentaire;
    private Scanner scanner;
    private int index;
    private ArrayList<String> tab;


    public AnalyseurLexical(File source) throws FileNotFoundException {
        this.scanner = new Scanner(source);
        this.commentaire = false;
        tab = new ArrayList<>();
        init_tab();

        index =-1;

    }

    public void init_tab(){
        while (scanner.hasNext()){
            String[] line = scanner.nextLine().split(" ");
            while (line[0].contains("//")&&scanner.hasNext()) line = scanner.nextLine().split(" ");

            for (int i = 0; i< line.length; i++){
                if(!line[i].contains("//")) {
                    if(!line[i].isEmpty()) tab.add(line[i]);
                }
                else break;
            }
        }
        //tab.stream().forEach((e)-> System.out.print(e+" "));
    }

    public String next(){
        if (tab.size()-1<=index){
            return "EOF";
        }
        else {
            index++;
            return tab.get(index);
        }
    }

}
