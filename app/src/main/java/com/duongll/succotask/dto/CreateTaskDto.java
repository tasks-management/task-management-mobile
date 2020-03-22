package com.duongll.succotask.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreateTaskDto implements Serializable {

    private String name;
    private String description;
    private String process;
    private String status;
    private String startDate;
    private String endDate;
    private Long creator;
    private Long handler;

    public CreateTaskDto() {
    }

    public CreateTaskDto(String name, String description, String process, String status, String startDate, String endDate, Long creator, Long handler) {
        this.name = name;
        this.description = description;
        this.process = process;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.creator = creator;
        this.handler = handler;
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

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getHandler() {
        return handler;
    }

    public void setHandler(Long handler) {
        this.handler = handler;
    }
}
