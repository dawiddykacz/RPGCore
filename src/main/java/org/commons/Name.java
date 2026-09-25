package org.commons;

import javax.annotation.Nonnull;

public class Name {
    private final String name;

    public Name(@Nonnull final String name) {
        if(name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty");

        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
