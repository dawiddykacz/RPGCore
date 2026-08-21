package pl.epicserwer.rpg.core.experience.required;

public class RequiredExperienceService {
    public long calculateRequiredExperience(final int level) {
        return (level * 100L);
    }
}
