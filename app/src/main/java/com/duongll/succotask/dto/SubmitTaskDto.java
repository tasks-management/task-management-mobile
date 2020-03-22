package com.duongll.succotask.dto;

import java.io.Serializable;

public class SubmitTaskDto implements Serializable {
    private String image;
    private String status;

    public SubmitTaskDto() {
    }

    public SubmitTaskDto(String image, String status) {
        this.image = image;
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
