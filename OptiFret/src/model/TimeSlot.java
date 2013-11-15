package model;

import java.util.Date;

public class TimeSlot {

    private Date begin;
    private Long duration;

    public TimeSlot() {
        // TODO - implement TimeSlot.TimeSlot
        throw new UnsupportedOperationException();
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

}
