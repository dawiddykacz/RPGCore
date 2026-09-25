package pl.epicserwer.rpg.core.experience.required;

import org.commons.experience.Experience;
import org.commons.experience.Level;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppRequiredExperienceServiceTest {

    @Test
    void shouldReturnRequiredExperienceForExistingLevel() {
        Level level1 = new Level(1);
        Level level2 = new Level(2);
        Experience expForLevel1 = new Experience(100L);
        Experience expForLevel2 = new Experience(250L);

        HashMap<Level, Experience> experiencesMap = new HashMap<>();
        experiencesMap.put(level1, expForLevel1);
        experiencesMap.put(level2, expForLevel2);

        AppRequiredExperienceService service = new AppRequiredExperienceService(experiencesMap);

        Experience calculatedExp = service.calculateRequiredExperience(new Level(1));

        assertEquals(expForLevel1, calculatedExp, "Should return exact experience defined for level 1");
    }

    @Test
    void shouldReturnMaxExperienceWhenLevelIsNotInTheMap() {
        Level configuredLevel = new Level(1);
        Experience configuredExp = new Experience(100L);

        HashMap<Level, Experience> experiencesMap = new HashMap<>();
        experiencesMap.put(configuredLevel, configuredExp);

        AppRequiredExperienceService service = new AppRequiredExperienceService(experiencesMap);

        Level unconfiguredLevel = new Level(99);

        Experience calculatedExp = service.calculateRequiredExperience(unconfiguredLevel);

        assertEquals(Experience.MAX_EXPERIENCE, calculatedExp,
                "Should return MAX_EXPERIENCE fallback for undefined levels");
    }

    @Test
    void shouldReturnMaxExperienceWhenMapIsEmpty() {
        HashMap<Level, Experience> emptyMap = new HashMap<>();
        AppRequiredExperienceService service = new AppRequiredExperienceService(emptyMap);

        Experience calculatedExp = service.calculateRequiredExperience(new Level(5));

        assertEquals(Experience.MAX_EXPERIENCE, calculatedExp,
                "Should return MAX_EXPERIENCE when the configuration map is completely empty");
    }
}