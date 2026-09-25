package pl.epicserwer.rpg.core.experience;

import pl.epicserwer.rpg.core.experience.required.RequiredExperienceService;

public class ChooseExperienceService {
    private final RequiredExperienceService requiredExperienceService;

    public ChooseExperienceService(RequiredExperienceService requiredExperienceService) {
        this.requiredExperienceService = requiredExperienceService;
    }

    public ExperienceService choose() {
        return new AppExperienceService(this.requiredExperienceService);
    }

}
