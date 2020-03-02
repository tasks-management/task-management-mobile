package duongll.entity;


import java.time.LocalDate;

public class History {

    private Long Id;

    private LocalDate From;

    private LocalDate To;

    private Enum<WorkStatus> workStatus;

    private String username;

    private String content;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public LocalDate getFrom() {
        return From;
    }

    public void setFrom(LocalDate from) {
        From = from;
    }

    public LocalDate getTo() {
        return To;
    }

    public void setTo(LocalDate to) {
        To = to;
    }

    public Enum<WorkStatus> getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Enum<WorkStatus> workStatus) {
        this.workStatus = workStatus;
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

    private enum  WorkStatus {PENDING, DONE, INPROGESS}
}
