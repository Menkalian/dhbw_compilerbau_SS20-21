package de.dhbw.mosbach.compilerbau;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class DummyTest {
    @Test
    public void test () {
        assertDoesNotThrow((Executable) Dummy::new);
    }
}