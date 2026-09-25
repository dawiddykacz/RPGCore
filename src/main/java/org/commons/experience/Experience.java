package org.commons.experience;

import java.util.Objects;

public class Experience {
    public static Experience MAX_EXPERIENCE = new Experience(Long.MAX_VALUE - 10_000);
    private final long experience;

    public Experience(){
        this(0);
    }

    public Experience(long experience) {
        if (experience < 0) throw new IllegalArgumentException("experience must be >= 0");

        this.experience = experience;
    }

    public Experience add(Experience experience) {
        return new Experience(this.experience + experience.experience);
    }

    public boolean isLowerThan(Experience experience) {
        return this.experience < experience.experience;
    }

    public double getExperienceAsDouble() {
        return experience;
    }

    public long getExperienceAsLong() {
        return experience;
    }

    @Override
    public String toString() {
        return experience + "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Experience that = (Experience) o;
        return experience == that.experience;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(experience);
    }
}
