package model;

import java.util.Date;

public class TimeSlot {
    private Date begin;
    private Long duration;

    public TimeSlot(Date begin, Long duration) {
        this.begin = begin;
        this.duration = duration;
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

    public Date getEnd() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
