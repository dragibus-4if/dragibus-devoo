package model;

import java.util.Date;

/**
 * Modèle correspondant à un créneau horaire.
 */
public class TimeSlot {

    private Date begin;
    private Long duration;

    /**
     * Constructeur par attributs, mettant en place la valeur de ceux-ci.
     *
     * @param begin
     * @param duration
     * @throws NullPointerException si begin ou duration sont nuls
     * @throws IllegalArgumentException si duration est négatif
     */
    public TimeSlot(Date begin, Long duration) {
        if (begin == null) {
            throw new NullPointerException("'begin' ne doit pas être nul");
        }
        this.begin = begin;

        if (duration == null) {
            throw new NullPointerException("'duration' ne doit pas être nul");
        } else if (duration < 0) {
            throw new IllegalArgumentException("'duration' ne doit pas être négatif");
        }
        this.duration = duration;
    }

    /**
     * Surcharge du constructeur, prenant deux dates.
     *
     * @param begin
     * @param end
     * @throws NullPointerException si begin ou end sont nuls
     * @throws IllegalArgumentException si duration est négatif
     */
    public TimeSlot(Date begin, Date end) {
        this(begin, new Long(end.getTime() - begin.getTime()));
    }

    /**
     * Accesseur en lecture de l'heure de début du créneau horaire.
     *
     * @return l'heure de début du créneau horaire
     */
    public Date getBegin() {
        return begin;
    }

    /**
     * Accesseur en écriture de l'heure de début du créneau horaire.
     *
     * @param begin
     * @throws NullPointerException si begin est null
     */
    public void setBegin(Date begin) {
        if (begin == null) {
            throw new NullPointerException("'begin' ne doit pas être nul");
        }
        this.begin = begin;
    }

    /**
     * Accesseur en lecture de la durée du créneau horaire.
     *
     * @return la durée du créneau horaire
     */
    public Long getDuration() {
        return duration;
    }

    /**
     * Accesseur en écriture de la durée du créneau horaire.
     *
     * @param duration
     * @throws NullPointerException si la durée est null
     */
    public void setDuration(Long duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("'duration' ne doit pas être négatif");
        }
        this.duration = duration;
    }

    /**
     * Accesseur en lecture de l'heure de fin du créneau horaire.
     *
     * <p>
     * Cette méthode calcule l'heure de fin à partir de l'heure de début et de
     * la durée du créneau horaire.</p>
     *
     * @return l'heure de fin du créneau horaire
     */
    public Date getEnd() {
        Date end = (Date) begin.clone();
        end.setTime(begin.getTime() + duration);
        return end;
    }

    @Override
    public String toString() {
        return "TimeSlot{" + "begin=" + begin + ", duration=" + duration + '}';
    }

}
