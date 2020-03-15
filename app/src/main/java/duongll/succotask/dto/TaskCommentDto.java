package duongll.succotask.dto;

import java.io.Serializable;
import java.util.Date;

public class TaskCommentDto implements Serializable {

    private String comment;
    private int rate;
    private String status;

    public TaskCommentDto(String comment, int rate, String status) {
        this.comment = comment;
        this.rate = rate;
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
