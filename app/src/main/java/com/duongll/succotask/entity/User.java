package com.duongll.succotask.entity;

import java.io.Serializable;

public class User implements Serializable {

    private Long id;

    private String username;

    private String password;

    private String role;

    private String name;

    private String firebaseToken;

    private Team team;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public Team getTeamId() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
