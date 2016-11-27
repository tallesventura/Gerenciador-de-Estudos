package com.talles.android.daa_ti_tallesoliveira.model;

/**
 * Created by talles on 11/26/16.
 */

public class TaskModel {

    private String day;
    private String start_time;
    private String end_time;
    private String type;
    private String subject;
    private String description;
    private String supervisor_email;

    public TaskModel(){}

    public TaskModel(String day, String start_time, String end_time, String type, String subject, String description, String supervisor_email) {
        this.day = day;
        this.start_time = start_time;
        this.end_time = end_time;
        this.type = type;
        this.subject = subject;
        this.description = description;
        this.supervisor_email = supervisor_email;
    }

    public String getDay() {
        return day;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getType() {
        return type;
    }

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public String getSupervisor_email() {
        return supervisor_email;
    }
}
