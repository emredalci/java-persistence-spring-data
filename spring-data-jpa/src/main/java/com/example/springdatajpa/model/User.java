package com.example.springdatajpa.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private LocalDate registrationDate;

    private String email;

    private int level;

    private boolean active;

    public User() {
    }

    public User(String username, LocalDate registrationDate) {
        this.username = username;
        this.registrationDate = registrationDate;
    }

    public User(String username, LocalDate registrationDate, String email, int level, boolean active) {
        this.username = username;
        this.registrationDate = registrationDate;
        this.email = email;
        this.level = level;
        this.active = active;
    }

    public long id() {
        return id;
    }

    public String username() {
        return username;
    }

    public LocalDate registrationDate() {
        return registrationDate;
    }

    public String email() {
        return email;
    }

    public int level() {
        return level;
    }

    public boolean active() {
        return active;
    }
}
