package pl.epicserwer.rpg.core.experience;

import org.commons.experience.Experience;
import org.commons.experience.Level;
import pl.epicserwer.rpg.core.experience.required.RequiredExperienceService;

import javax.annotation.Nonnull;
import java.util.Objects;

class ExperiencePlayerData {
    private final Experience playerExperience;
    private final Level playerLevel;

    public ExperiencePlayerData(@Nonnull Experience playerExperience, @Nonnull Level playerLevel) {
        this.playerExperience = playerExperience;
        this.playerLevel = playerLevel;
    }

    public ExperiencePlayerData addExperience(@Nonnull final Experience experience,
                                 @Nonnull final RequiredExperienceService requiredExperienceService) {
        Experience newExp = this.playerExperience.add(experience);
        Level newLevel = this.playerLevel;

        while (!newExp.isLowerThan(
                requiredExperienceService.calculateRequiredExperience(newLevel))) {
            newLevel = newLevel.increaseLevel();
        }

        return new ExperiencePlayerData(newExp, newLevel);
    }

    public Experience getPlayerExperience() {
        return playerExperience;
    }

    public Level getPlayerLevel() {
        return playerLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExperiencePlayerData that = (ExperiencePlayerData) o;
        return Objects.equals(playerExperience, that.playerExperience) && Objects.equals(playerLevel, that.playerLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerExperience, playerLevel);
    }
}
