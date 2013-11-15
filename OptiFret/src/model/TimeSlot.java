package model;

import java.util.Date;

public class TimeSlot {

    private Date begin;
    private Long duration;

    public TimeSlot(Date begin, Long duration) {
        if (begin == null) {
            throw new NullPointerException("'begin' cannot be null");
        }
        this.begin = begin;

        if (duration == null) {
            throw new NullPointerException("'duration' cannot be null");
        } else if (duration < 0) {
            throw new IllegalArgumentException("'duration' cannot be negative");
        }
        this.duration = duration;
    }

    public TimeSlot(Date begin, Date end) {
        if (begin == null) {
            throw new NullPointerException("'begin' cannot be null");
        }
        this.begin = begin;

        if (end == null) {
            throw new NullPointerException("'end' cannot be null");
        } else if (end.before(begin)) {
            throw new IllegalArgumentException("'end' must be after 'begin'");
        }
        this.duration = new Long(end.getTime() - begin.getTime());
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        if (begin == null) {
            throw new NullPointerException("'begin' cannot be null");
        }
        this.begin = begin;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("'duration' cannot be negative");
        }
        this.duration = duration;
    }

    public Date getEnd() {
        Date end = (Date) begin.clone();
        end.setTime(begin.getTime() + duration);
        return end;
    }

}
