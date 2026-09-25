package org.commons.experience;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    @Test
    void shouldCreateLevelWhenValueIsPositive() {
        Level level = new Level(1);

        assertEquals(1, level.getLevelAsInt());
    }

    @Test
    void shouldCreateDefaultLevel() {
        Level defaultLevel = new Level();
        Level level = new Level(1);

        assertEquals(defaultLevel, level);
    }

    @Test
    void shouldThrowExceptionWhenLevelIsZero() {
        // when
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Level(0)
        );

        assertEquals("level must be greater than 0", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLevelIsNegative() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Level(-5)
        );
    }

    @Test
    void shouldIncreaseLevelAndReturnNewInstance() {
        Level initialLevel = new Level(5);

        Level increasedLevel = initialLevel.increaseLevel();

        assertEquals(6, increasedLevel.getLevelAsInt());
        assertEquals(5, initialLevel.getLevelAsInt(), "Original level should remain unchanged (immutability)");
        assertNotSame(initialLevel, increasedLevel);
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        Level level = new Level(42);

        String stringValue = level.toString();

        assertEquals("42", stringValue);
    }

    @Test
    void shouldBeEqualIfValuesAreTheSame() {
        Level level1 = new Level(10);
        Level level2 = new Level(10);

        assertEquals(level1, level2);
        assertEquals(level1.hashCode(), level2.hashCode());
    }

    @Test
    void shouldNotBeEqualIfValuesAreDifferent() {
        Level level1 = new Level(10);
        Level level2 = new Level(11);

        assertNotEquals(level1, level2);
        assertNotEquals(level1.hashCode(), level2.hashCode());
    }

    @Test
    void shouldNotBeEqualToNullOrDifferentClass() {
        Level level = new Level(5);

        assertNotEquals(null, level);
        assertNotEquals("5", level);
    }
}