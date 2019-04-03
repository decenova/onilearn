package com.onilearnapp.onilearnapp.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;


@Entity(indices = {@Index("subjectId")},
        foreignKeys = @ForeignKey(entity = Subject.class,
        parentColumns = "id",
        childColumns = "subjectId"))
public class Task implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int taskId;

    @ColumnInfo(name = "subjectId")
    private int subjectId;
    private Date startTime, endTime;
    private boolean isAlarm;
    private String type;

    @Ignore
    private Subject subject;
    @Ignore
    private Calendar calendar;

    public Task() {
        calendar = Calendar.getInstance();
        this.subjectId = 0;
        this.subject = new Subject("Default","null");
        this.startTime = calendar.getTime();
        this.endTime = calendar.getTime();
        this.isAlarm = true;
        this.type = "";
    }

    @Ignore
    public Task(Subject subject, Date startTime, Date endTime, boolean isAlarm, String type) {
        this.subjectId = subject.getId();
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAlarm = isAlarm;
        this.type = type;
        calendar = Calendar.getInstance();
    }

    public Task(int id, int subjectId, Date startTime, Date endTime, boolean isAlarm, String type) {
        this.taskId = id;
        this.subjectId = subjectId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAlarm = isAlarm;
        this.type = type;
        calendar = Calendar.getInstance();
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int id) {
        this.taskId = id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getStartTimeString(){
        calendar.setTime(startTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(calendar.getTime());
    }

    public String getEndTimeString(){
        calendar.setTime(endTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(calendar.getTime());
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
