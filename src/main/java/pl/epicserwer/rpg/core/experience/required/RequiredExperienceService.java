package pl.epicserwer.rpg.core.experience.required;

import org.commons.experience.Experience;
import org.commons.experience.Level;

import javax.annotation.Nonnull;
import java.util.HashMap;

public interface RequiredExperienceService {
    class Builder{
        private final HashMap<Level, Experience> experiences;

        public Builder(){
            experiences = new HashMap<>();
        }

        public Builder add(int levelInt, long exp){
            Level level = new Level(levelInt);
            if(experiences.containsKey(level)){
                throw new IllegalArgumentException("Experience already exists");
            }


            Experience requiredExperience = calculateRequiredExperience(level);
            Experience newExp = requiredExperience.add(new Experience(exp));
            experiences.put(level, newExp);
            return this;
        }

        public RequiredExperienceService build(){
            return new AppRequiredExperienceService(experiences);
        }

        private Experience calculateRequiredExperience(Level level){
            if(level.equals(new Level())) return new Experience();

            return experiences.getOrDefault(new Level(level.getLevelAsInt() - 1),
                    new Experience());
        }
    }

    Experience calculateRequiredExperience(@Nonnull final Level level);
}
