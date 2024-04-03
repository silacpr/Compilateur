package plic.repint;

import java.util.ArrayList;

public class Bloc {
    private ArrayList<Instruction> instructions;
    public Bloc() {
        this.instructions = new ArrayList<>();
    }
    public void ajouter(Instruction i){
        this.instructions.add(i);
    }
    public String toMips() {

        StringBuilder mipsCode = new StringBuilder();

        mipsCode.append(".data\n" +
                "next: .asciiz \"\\n\"\n" +
                "\n" +
                ".text\n" +
                ".globl main\n" +
                "main:\n\n" +
                "move $s7, $sp \n" +
                "add $sp, $sp,-24 \n");
        for (Instruction instruction : instructions) {
            mipsCode.append(instruction.toString());
            mipsCode.append("\n");
        }
        mipsCode.append("li $v0, 10\n" +
                "syscall");

        return mipsCode.toString();
    }
}
