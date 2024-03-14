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
                "li $sp, 0x7ffffffc\n" +
                "move $fp, $sp\n");
        for (Instruction instruction : instructions) {
            mipsCode.append(instruction.toString());
            mipsCode.append("\n");
        }
        mipsCode.append("li $v0, 10\n" +
                "syscall");

        return mipsCode.toString();
    }
}
