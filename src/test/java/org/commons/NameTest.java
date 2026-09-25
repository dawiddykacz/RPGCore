package org.commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NameTest {

    @Test
    void shouldCreateNameWhenValueIsNotEmpty() {
        Name name = new Name("Jan Kowalski");

        assertEquals("Jan Kowalski", name.toString());
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Name("")
        );

        assertEquals("Name cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenNameIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> new Name(null)
        );
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        Name name = new Name("Alicja");

        String stringValue = name.toString();

        assertEquals("Alicja", stringValue);
    }
}