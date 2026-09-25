package pl.epicserwer.rpg.core.experience.required;

import org.commons.experience.Experience;
import org.commons.experience.Level;

import javax.annotation.Nonnull;
import java.util.HashMap;

class AppRequiredExperienceService implements RequiredExperienceService {
    private final HashMap<Level, Experience> experiences;

    public AppRequiredExperienceService(@Nonnull final HashMap<Level, Experience> experiences) {
        this.experiences = experiences;
    }

    @Override
    public Experience calculateRequiredExperience(@Nonnull Level level) {
        return this.experiences.getOrDefault(level, Experience.MAX_EXPERIENCE);
    }
}
