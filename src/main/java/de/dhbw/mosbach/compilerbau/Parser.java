package de.dhbw.mosbach.compilerbau;

import de.dhbw.mosbach.compilerbau.visit.Visitable;

public class Parser {
    private int position;
    private final String eingabe;
    //TODO: 08.02.2021(...)

    public Parser (String eingabe) {
        this.eingabe = eingabe;
        position = 0;
    }

    // TODO: 08.02.2021(...)
    // TODO: 08.02.2021 pro Nichtterminal eine Methode!

    /**
     * Nichtterminal Start<br>
     * - Nur diese Methode ist oeffentlich!!<br>
     * - Nur in dieser Methode auf Eingabeende ueberpruefen !!!
     */
    public Visitable start (Visitable parameter) {
        // TODO: 08.02.2021 (...)
        return null;
    }
    // TODO: 08.02.2021 (...)

    /**
     * Nichtterminal Alphanum
     */
    private Visitable alphanum (Visitable parameter) {
        // TODO: 08.02.2021 (...)
        return null;
    }

    private void match (char symbol) {
        if ((eingabe == null) || ("".equals(eingabe))) {
            throw new RuntimeException("Syntax error !");
        }
        if (position >= eingabe.length()) {
            throw new RuntimeException("End of input reached !");
        }
        if (eingabe.charAt(position) != symbol) {
            throw new RuntimeException("Syntax error !");
        }
        position++;
    }

    /**
     * 1. wird benoetigt bei der Regel Start -> '(' RegExp ')''#'<br>
     * 2. wird benoetigt bei der Regel Start -> '#'<br>
     * 3. wird sonst bei keiner anderen Regel benoetigt
     */
    private void assertEndOfInput () {
        if (position < eingabe.length()) {
            throw new RuntimeException("No end of input reached!");
        }
    }
}
