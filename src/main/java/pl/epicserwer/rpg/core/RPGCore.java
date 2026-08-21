package pl.epicserwer.rpg.core;

import pl.epicserwer.rpg.core.experience.ChooseExperienceService;
import pl.epicserwer.rpg.core.experience.ExperienceService;
import pl.epicserwer.rpg.core.experience.required.RequiredExperienceService;

public class RPGCore {

    public static void main(String[] args) {
        RequiredExperienceService requiredExperienceService = new RequiredExperienceService();
        ExperienceService experienceService = new ChooseExperienceService(requiredExperienceService).choose();

        boolean a = experienceService.addExperience(null,1);
        System.out.println(a);
    }
}
