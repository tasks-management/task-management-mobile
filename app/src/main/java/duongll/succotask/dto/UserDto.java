package duongll.succotask.dto;

import java.io.Serializable;

public class UserDto implements Serializable {
    private String username;
    private String password;
    private String fullName;
    private String teamId;

    public UserDto(String username, String password, String fullName, String teamId) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.teamId = teamId;
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

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
