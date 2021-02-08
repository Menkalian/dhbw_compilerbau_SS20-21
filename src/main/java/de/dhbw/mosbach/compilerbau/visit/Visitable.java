package de.dhbw.mosbach.compilerbau.visit;

public interface Visitable {
    void accept (Visitor visitor);
}
