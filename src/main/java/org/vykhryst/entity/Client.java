package org.vykhryst.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private long id;
    private String username;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String email;
    private String password;

    @ToString.Exclude
    private List<ClientMemento> mementos = new ArrayList<>();

    public Client(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.password = builder.password;
    }

    @Getter
    @ToString
    public class ClientMemento {
        private long id;
        private String username;
        private String firstname;
        private String lastname;
        private String phoneNumber;
        private String email;
        private String password;

        public ClientMemento(Client client) {
            this.id = client.id;
            this.username = client.username;
            this.firstname = client.firstname;
            this.lastname = client.lastname;
            this.phoneNumber = client.phoneNumber;
            this.email = client.email;
            this.password = client.password;
        }

        public ClientMemento save() {
            ClientMemento memento = new ClientMemento(Client.this);
            mementos.add(memento);
            return memento;
        }

        public void restore(ClientMemento memento) {
            this.id = memento.id;
            this.username = memento.username;
            this.firstname = memento.firstname;
            this.lastname = memento.lastname;
            this.phoneNumber = memento.phoneNumber;
            this.email = memento.email;
            this.password = memento.password;
        }

        public ClientMemento undo() {
            if (!mementos.isEmpty()) {
                ClientMemento memento = mementos.remove(mementos.size() - 1);
                restore(memento);
            } else {
                System.out.println("Can't undo: no more mementos");
            }
            return this;
        }

        public static Client fromMemento(ClientMemento memento) {
            return new Client.Builder()
                    .id(memento.id)
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

        public Client build() {
            return new Client(this);
        }
    }
}