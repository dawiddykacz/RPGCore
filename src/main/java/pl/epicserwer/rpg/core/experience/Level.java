package pl.epicserwer.rpg.core.experience;

import java.util.Objects;

class Level {
    private final int level;

    public Level(int level) {
        if (level <= 0) throw new IllegalArgumentException("level must be greater than 0");

        this.level = level;
    }

    public Level increaseLevel() {
        return new Level(this.level + 1);
    }

    public int getLevelAsInt() {
        return level;
    }

    @Override
    public String toString() {
        return level + "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Level level1 = (Level) o;
        return level == level1.level;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(level);
    }
}
