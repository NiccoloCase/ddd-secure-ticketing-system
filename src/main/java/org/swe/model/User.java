package org.swe.model;

import org.swe.core.utils.PasswordUtility;

public class User {
    private String name;
    private String surname;
    private String passwordHash;
    private String email;
    private int id;

    public User(String name, String surname, String passwordHash, String email, int id) {
        this.name = name;
        this.surname = surname;
        this.passwordHash = passwordHash;
        this.email = email;
        this.id = id;
    }

    public static String hashPassword(String password) {
        return PasswordUtility.hashPassword(password);
    }

    public boolean isPasswordCorrect(String password) {
        return PasswordUtility.checkPassword(password, passwordHash);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
