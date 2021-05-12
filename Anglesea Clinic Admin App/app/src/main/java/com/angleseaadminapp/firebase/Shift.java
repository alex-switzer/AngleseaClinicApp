package com.angleseaadminapp.firebase;

import java.time.LocalTime;

public class Shift {
    private String id;
    private long start;
    private long end;

    public Shift(){ }

    public Shift(String id, LocalTime start, LocalTime end) {
        this.id = id;
        this.start = start.toSecondOfDay();
        this.end = end.toSecondOfDay();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public LocalTime getStartDate() {
        return LocalTime.ofSecondOfDay(start);
    }

    public void setStartDate(LocalTime start) {
        this.start = start.toSecondOfDay();
    }

    public LocalTime getEndDate() {
        return LocalTime.ofSecondOfDay(end);
    }

    public void setEndDate(LocalTime end) {
        this.end = end.toSecondOfDay();
    }
}
