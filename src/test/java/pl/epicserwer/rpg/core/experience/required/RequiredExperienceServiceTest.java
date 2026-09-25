package pl.epicserwer.rpg.core.experience.required;

import org.commons.experience.Experience;
import org.commons.experience.Level;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExperienceServiceBuilderTest {
    @Test
    public void shouldCorrectCalculateExperience() {
        RequiredExperienceService requiredExperienceService = new RequiredExperienceService.Builder()
                .add(1,300)
                .add(2, 700)
                .build();
        assertEquals(requiredExperienceService.calculateRequiredExperience(new Level(1)),new Experience(300));
        assertEquals(requiredExperienceService.calculateRequiredExperience(new Level(2)),new Experience(1_000));
        assertEquals(requiredExperienceService.calculateRequiredExperience(new Level(3)),Experience.MAX_EXPERIENCE);
    }
}

class RequiredExperienceServiceBuilderTest {

    @Test
    void shouldAddLevelExperienceAndBuildSuccessfully() {
        RequiredExperienceService.Builder builder = new RequiredExperienceService.Builder();

        builder.add(1, 100L);
        builder.add(2, 250L);
        RequiredExperienceService service = builder.build();

        assertNotNull(service, "Built service should not be null");
        assertInstanceOf(AppRequiredExperienceService.class, service,
                "Built service should be an instance of AppRequiredExperienceService");
    }

    @Test
    void shouldThrowExceptionWhenAddingExperienceForExistingLevel() {
        RequiredExperienceService.Builder builder = new RequiredExperienceService.Builder();
        builder.add(5, 1000L);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> builder.add(5, 1500L)
        );

        assertEquals("Experience already exists", exception.getMessage());
    }

    @Test
    void shouldAllowAddingMultipleDifferentLevelsWithoutException() {
        RequiredExperienceService.Builder builder = new RequiredExperienceService.Builder();

        assertDoesNotThrow(() -> {
            builder.add(10, 5000L);
            builder.add(11, 6000L);
            builder.add(12, 7500L);
        }, "Adding different levels should not throw any exception");

        assertNotNull(builder.build());
    }

    @Test
    void shouldBuildSuccessfullyEvenIfNoExperienceIsAdded() {
        RequiredExperienceService.Builder builder = new RequiredExperienceService.Builder();

        RequiredExperienceService service = builder.build();

        assertNotNull(service, "Should be able to build service with empty configuration");
        assertInstanceOf(AppRequiredExperienceService.class, service);
    }
}