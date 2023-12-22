package org.vykhryst.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long id;
    private Role role;
    private String username;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String email;
    private String password;

    @ToString.Exclude
    private List<UserMemento> mementos = new ArrayList<>();

    public User(Builder builder) {
        this.id = builder.id;
        this.role = builder.role;
        this.username = builder.username;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.password = builder.password;
    }

    @Getter
    @ToString
    public class UserMemento {
        private long id;
        private Role role;
        private String username;
        private String firstname;
        private String lastname;
        private String phoneNumber;
        private String email;
        private String password;

        public UserMemento(User user) {
            this.id = user.id;
            this.role = user.role;
            this.username = user.username;
            this.firstname = user.firstname;
            this.lastname = user.lastname;
            this.phoneNumber = user.phoneNumber;
            this.email = user.email;
            this.password = user.password;
        }

        public UserMemento save() {
            UserMemento memento = new UserMemento(User.this);
            mementos.add(memento);
            return memento;
        }

        public void restore(UserMemento memento) {
            this.id = memento.id;
            this.role = memento.role;
            this.username = memento.username;
            this.firstname = memento.firstname;
            this.lastname = memento.lastname;
            this.phoneNumber = memento.phoneNumber;
            this.email = memento.email;
            this.password = memento.password;
        }

        public UserMemento undo() {
            if (!mementos.isEmpty()) {
                UserMemento memento = mementos.remove(mementos.size() - 1);
                restore(memento);
            } else {
                System.out.println("Can't undo: no more mementos");
            }
            return this;
        }

        public static User fromMemento(UserMemento memento) {
            return new User.Builder()
                    .id(memento.id)
                    .role(memento.role)
                    .username(memento.username)
                    .firstname(memento.firstname)
                    .lastname(memento.lastname)
                    .phoneNumber(memento.phoneNumber)
                    .email(memento.email)
                    .password(memento.password)
                    .build();
        }
    }


    public static class Builder {
        private long id;
        public Role role;
        private String username;
        private String firstname;
        private String lastname;
        private String phoneNumber;
        private String email;
        private String password;


        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}