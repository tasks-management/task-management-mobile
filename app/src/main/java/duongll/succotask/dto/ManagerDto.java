package duongll.succotask.dto;

import java.io.Serializable;


public class ManagerDto implements Serializable {
    private String username;
    private String password;
    private String fullName;
    private String teamName;

    public ManagerDto(String username, String password, String fullName, String teamName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.teamName = teamName;
    }

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
