package org.vykhryst.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private final String name;

    public String getName() {
        return name;
    }
}
