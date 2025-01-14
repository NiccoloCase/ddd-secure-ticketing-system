package org.swe.domain;

public class User {
    private String name;
    private String surname;
    private String passwordHash;
    private String email;

    public User(String name, String surname, String passwordHash, String email) {
        this.name = name;
        this.surname = surname;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
