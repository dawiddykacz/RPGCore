package pl.epicserwer.rpg.core.experience;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExperienceTest {

    @Test
    void shouldCreateExperienceWithPositiveValue() {
        long validExperience = 100L;

        Experience experience = new Experience(validExperience);

        assertEquals(100L, experience.getExperienceAsLong());
    }

    @Test
    void shouldCreateExperienceWithZero() {
        long zeroExperience = 0L;

        Experience experience = new Experience(zeroExperience);

        assertEquals(0L, experience.getExperienceAsLong());
    }

    @Test
    void shouldThrowExceptionWhenExperienceIsNegative() {
        long negativeExperience = -1L;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Experience(negativeExperience)
        );

        assertEquals("experience must be >= 0", exception.getMessage());
    }

    @Test
    void shouldReturnExperienceAsDouble() {
        Experience experience = new Experience(42L);

        double result = experience.getExperienceAsDouble();

        assertEquals(42.0, result, 0.0001);
    }

    @Test
    void shouldReturnExperienceAsLong() {
        Experience experience = new Experience(9999999999L);

        long result = experience.getExperienceAsLong();

        assertEquals(9999999999L, result);
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        Experience experience = new Experience(500L);

        String result = experience.toString();

        assertEquals("500", result);
    }
}