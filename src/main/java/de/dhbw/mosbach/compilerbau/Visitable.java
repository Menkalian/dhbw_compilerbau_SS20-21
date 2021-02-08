package de.dhbw.mosbach.compilerbau;

public interface Visitable {
    void accept (Visitor visitor);
}
