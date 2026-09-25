package pl.epicserwer.rpg.core.experience;

import org.commons.experience.Experience;
import org.commons.experience.Level;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.epicserwer.rpg.core.experience.required.RequiredExperienceService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperiencePlayerDataTest {

    @Mock
    private RequiredExperienceService experienceService;

    @Test
    void shouldCreatePlayerDataCorrectly() {
        Experience initialExp = new Experience(0L);
        Level initialLevel = new Level(1);

        ExperiencePlayerData data = new ExperiencePlayerData(initialExp, initialLevel);

        assertEquals(initialExp, data.getPlayerExperience());
        assertEquals(initialLevel, data.getPlayerLevel());
    }

    @Test
    void shouldAddExperienceWithoutLevelUp() {
        ExperiencePlayerData data = new ExperiencePlayerData(new Experience(0L), new Level(1));
        Experience addedExp = new Experience(50L);

        when(experienceService.calculateRequiredExperience(new Level(1)))
                .thenReturn(new Experience(100L));

        ExperiencePlayerData updatedData = data.addExperience(addedExp, experienceService);

        assertEquals(new Experience(50L), updatedData.getPlayerExperience());
        assertEquals(new Level(1), updatedData.getPlayerLevel(), "Level should not increase (50 < 100)");
        assertNotSame(data, updatedData, "Method should return a new instance (immutability)");
    }

    @Test
    void shouldAddExperienceAndLevelUpOnce() {
        ExperiencePlayerData data = new ExperiencePlayerData(new Experience(80L), new Level(1));
        Experience addedExp = new Experience(40L);

        when(experienceService.calculateRequiredExperience(new Level(1)))
                .thenReturn(new Experience(100L));
        when(experienceService.calculateRequiredExperience(new Level(2)))
                .thenReturn(new Experience(300L));

        ExperiencePlayerData updatedData = data.addExperience(addedExp, experienceService);

        assertEquals(new Experience(120L), updatedData.getPlayerExperience());
        assertEquals(new Level(2), updatedData.getPlayerLevel(),
                "Level should increase to 2 because total exp (120) is >= 100");
    }

    @Test
    void shouldAddExperienceAndLevelUpMultipleTimes() {
        ExperiencePlayerData data = new ExperiencePlayerData(new Experience(0L), new Level(1));
        Experience addedExp = new Experience(450L);

        when(experienceService.calculateRequiredExperience(new Level(1)))
                .thenReturn(new Experience(100L));
        when(experienceService.calculateRequiredExperience(new Level(2)))
                .thenReturn(new Experience(300L));
        when(experienceService.calculateRequiredExperience(new Level(3)))
                .thenReturn(new Experience(600L));

        ExperiencePlayerData updatedData = data.addExperience(addedExp, experienceService);

        assertEquals(new Experience(450L), updatedData.getPlayerExperience());
        assertEquals(new Level(3), updatedData.getPlayerLevel(),
                "Level should jump from 1 to 3 (450 bypasses 100 and 300, but is less than 600)");
    }

    @Test
    void shouldBeEqualIfPropertiesAreTheSame() {
        ExperiencePlayerData data1 = new ExperiencePlayerData(new Experience(150L), new Level(2));
        ExperiencePlayerData data2 = new ExperiencePlayerData(new Experience(150L), new Level(2));

        assertEquals(data1, data2);
        assertEquals(data1.hashCode(), data2.hashCode());
    }

    @Test
    void shouldNotBeEqualIfPropertiesAreDifferent() {
        ExperiencePlayerData baseData = new ExperiencePlayerData(new Experience(150L), new Level(2));
        ExperiencePlayerData differentExp = new ExperiencePlayerData(new Experience(200L), new Level(2));
        ExperiencePlayerData differentLevel = new ExperiencePlayerData(new Experience(150L), new Level(3));

        assertNotEquals(baseData, differentExp);
        assertNotEquals(baseData, differentLevel);
    }
}