package plic.repint;

import plic.analyse.ErreurSémantique;

public abstract class Instruction {
    public abstract String toString();

    public abstract void verifier() throws AbsenceDeclaration, ErreurSémantique;
}