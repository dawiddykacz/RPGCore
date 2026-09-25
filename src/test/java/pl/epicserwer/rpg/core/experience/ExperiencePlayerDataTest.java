package pl.epicserwer.rpg.core.experience;

import org.commons.experience.Experience;
import org.commons.experience.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.epicserwer.rpg.core.experience.required.RequiredExperienceService;

import static org.junit.jupiter.api.Assertions.*;

class ExperiencePlayerDataTest {

    private RequiredExperienceService experienceService;

    @BeforeEach
    void setUp() {
        experienceService = new RequiredExperienceService.Builder()
                .add(1, 100L)
                .add(2, 300L)
                .add(3, 600L)
                .build();
    }

    @Test
    void shouldCreatePlayerDataCorrectly() {
        // given
        Experience initialExp = new Experience(0L);
        Level initialLevel = new Level(1);

        // when
        ExperiencePlayerData data = new ExperiencePlayerData(initialExp, initialLevel);

        // then
        assertEquals(initialExp, data.getPlayerExperience());
        assertEquals(initialLevel, data.getPlayerLevel());
    }

    @Test
    void shouldAddExperienceWithoutLevelUp() {
        // given
        ExperiencePlayerData data = new ExperiencePlayerData(new Experience(0L), new Level(1));
        Experience addedExp = new Experience(50L);

        // when
        ExperiencePlayerData updatedData = data.addExperience(addedExp, experienceService);

        // then
        assertEquals(new Experience(50L), updatedData.getPlayerExperience());
        assertEquals(new Level(1), updatedData.getPlayerLevel(), "Poziom nie powinien wzrosnąć (50 < 100)");
        assertNotSame(data, updatedData, "Metoda powinna zwrócić nowy obiekt (niemutowalność)");
    }

    @Test
    void shouldAddExperienceAndLevelUpOnce() {
        // given
        ExperiencePlayerData data = new ExperiencePlayerData(new Experience(80L), new Level(1));
        Experience addedExp = new Experience(40L); // 80 + 40 = 120

        // when
        ExperiencePlayerData updatedData = data.addExperience(addedExp, experienceService);

        // then
        assertEquals(new Experience(120L), updatedData.getPlayerExperience());
        assertEquals(new Level(2), updatedData.getPlayerLevel(),
                "Poziom powinien wzrosnąć do 2, ponieważ całkowite exp (120) >= 100");
    }

    @Test
    void shouldAddExperienceAndLevelUpMultipleTimes() {
        // given
        ExperiencePlayerData data = new ExperiencePlayerData(new Experience(0L), new Level(1));
        Experience addedExp = new Experience(450L);

        // when
        ExperiencePlayerData updatedData = data.addExperience(addedExp, experienceService);

        // then
        assertEquals(new Experience(450L), updatedData.getPlayerExperience());
        assertEquals(new Level(3), updatedData.getPlayerLevel(),
                "Poziom powinien przeskoczyć z 1 na 3 (450 przeskakuje wymóg 100 oraz 300, ale nie dobija do 600)");
    }

    @Test
    void shouldBeEqualIfPropertiesAreTheSame() {
        // given
        ExperiencePlayerData data1 = new ExperiencePlayerData(new Experience(150L), new Level(2));
        ExperiencePlayerData data2 = new ExperiencePlayerData(new Experience(150L), new Level(2));

        // then
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