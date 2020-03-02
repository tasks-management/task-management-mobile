package duongll.entity;


import java.time.LocalDate;

public class Comment {

    private Long id;

    private String username;

    private String content;

    private float managerRate;

    private LocalDate commentTime;

    private Long workId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getManagerRate() {
        return managerRate;
    }

    public void setManagerRate(float managerRate) {
        this.managerRate = managerRate;
    }

    public LocalDate getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(LocalDate commentTime) {
        this.commentTime = commentTime;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }
}
