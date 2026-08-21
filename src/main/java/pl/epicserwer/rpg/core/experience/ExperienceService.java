package pl.epicserwer.rpg.core.experience;

import javax.annotation.Nonnull;

public interface ExperienceService {
    boolean addExperience(@Nonnull final String playerId, final long experienceToAdd);
}
