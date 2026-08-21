package pl.epicserwer.rpg.core.experience;

import pl.epicserwer.rpg.core.experience.required.RequiredExperienceService;
import pl.epicserwer.rpg.core.org.commons.Name;

import javax.annotation.Nonnull;

class AppExperienceService implements ExperienceService{
    private final RequiredExperienceService requiredExperienceService;

    public AppExperienceService(RequiredExperienceService requiredExperienceService) {
        this.requiredExperienceService = requiredExperienceService;
    }

    public boolean addExperience(@Nonnull final String playerId, final long experienceToAdd) {
        final Name playerName = new Name(playerId);
        final Experience experience = new Experience(experienceToAdd);

        //symulacja bazki
        final ExperiencePlayerData experiencePlayerData = new ExperiencePlayerData(new Experience(90L), new Level(1));

        final ExperiencePlayerData newExpData = experiencePlayerData.addExperience(experience, this.requiredExperienceService);

        //save do bazki

        System.out.println("Experience added: " + experience);
        System.out.println("level "+newExpData.getPlayerLevel()+", exp: "+newExpData.getPlayerExperience()+"/"+
                this.requiredExperienceService.calculateRequiredExperience(newExpData.getPlayerLevel().getLevelAsInt()));
        return !experiencePlayerData.getPlayerLevel().equals(newExpData.getPlayerLevel());
    }
}
