package org.vykhryst.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Program {
    private long id;
    private long clientId;
    private List<ProgramAdvertising> advertising;

    public Program() {
        advertising = new ArrayList<>();
    }

    public Program(Builder builder) {
        this.id = builder.id;
        this.clientId = builder.clientId;
        this.advertising = builder.advertising;
    }

    public void addAdvertising(ProgramAdvertising advertising) {
        this.advertising.add(advertising);
    }

    public static class Builder {
        private long id;
        private long clientId;
        private final List<ProgramAdvertising> advertising = new ArrayList<>();




        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder clientId(long clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder addAdvertising(ProgramAdvertising advertising) {
            this.advertising.add(advertising);
            return this;
        }

        public Program build() {
            return new Program(this);
        }
    }
}
