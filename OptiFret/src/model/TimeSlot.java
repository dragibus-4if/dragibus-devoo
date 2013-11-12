package model;

import java.util.Date;

/**
 *
 * @author jmcomets
 */
class TimeSlot {
    private Date begin;
    private Date end;

    public TimeSlot(Date begin, Date end) {
        this.begin = begin;
        this.end = end;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
