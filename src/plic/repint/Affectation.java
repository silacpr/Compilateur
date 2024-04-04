package plic.repint;

import plic.analyse.ErreurSémantique;

public class Affectation extends Instruction {
    private Acces acces;
    private Expression expression;

    public Affectation(Acces acces, Expression expression) {
        this.acces = acces;
        this.expression = expression;
    }

    @Override
    public String toString() {
        String codeAdresse = acces.getAdresse();
        String codeValeur = expression.toMips();

        return codeValeur +
                "sub $sp, $sp, 4\n" +
                "sw $v0, 0($sp)\n" +
                codeAdresse +
                "lw $v0, 0($sp)\n" +
                "add $sp, $sp, 4\n" +
                "sw $v0, 0($a0)\n";
    }

    @Override
    public void verifier() throws AbsenceDeclaration, ErreurSémantique {
        if (expression.getType().equals("tableau")){
            ((AccesTableau) expression).verifier();
        }
        else if (expression.getType().equals("idf")){
            ((Idf) expression).verifier();
        }
        else if (expression.getType().equals("boolean")){
            throw new ErreurSémantique("erreur sémantique");

        }
    }

}
