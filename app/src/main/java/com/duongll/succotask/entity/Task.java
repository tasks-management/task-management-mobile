package com.duongll.succotask.entity;


import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {

    private Long id;

    private String name;

    private String description;

    private String contentProcess;

    private Date startDate;

    private Date endDate;

    private Date timeComment;

    private String commentContent;

    private float rate;

    private Date created;

    private User creatorId;

    private String image;

    private String sourceHandler;

    private User handlerId;

    private Date lastModified;

    private String taskStatus;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getContentProcess() {
        return contentProcess;
    }

    public void setContentProcess(String contentProcess) {
        this.contentProcess = contentProcess;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getTimeComment() {
        return timeComment;
    }

    public void setTimeComment(Date timeComment) {
        this.timeComment = timeComment;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public User getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(User creatorId) {
        this.creatorId = creatorId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSourceHandler() {
        return sourceHandler;
    }

    public void setSourceHandler(String sourceHandler) {
        this.sourceHandler = sourceHandler;
    }

    public User getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(User handlerId) {
        this.handlerId = handlerId;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
