package org.swe.model;

public class Guest extends User {

        public Guest(String name, String surname, String passwordHash, String email) {
            super(name, surname, passwordHash, email);
        }
}
