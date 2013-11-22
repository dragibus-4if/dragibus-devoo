package test;

import java.util.Date;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import junit.framework.TestCase;
import model.TimeSlot;

public class TimeSlotTest extends TestCase {

    public void testConstructionFromDateAndDuration() {
        // Test des cas valides
        Date d = new Date();
        TimeSlot t = new TimeSlot(new Date(), new Long(0));
        assertNotNull(t);
        assertNotNull(t.getBegin());
        assertNotNull(t.getEnd());
        assertNotNull(t.getDuration());
        assertEquals(t.getDuration(), new Long(0));
        assertEquals(t.getBegin().getTime(), d.getTime());
        assertEquals(t.getEnd().getTime(), t.getBegin().getTime());

        // Test des cas invalides
        try {
            new TimeSlot(new Date(), new Long(-1));
            fail("Construction avec une durée négative");
        } catch (IllegalArgumentException e) {
        }

        try {
            new TimeSlot(new Date(), (Long) null);
            fail("Construction avec une durée nulle");
        } catch (IllegalArgumentException | NullPointerException e) {
        }

        try {
            new TimeSlot(null, new Long(0));
            fail("Construction avec une date nulle");
        } catch (IllegalArgumentException | NullPointerException e) {
        }
    }

    public void testConstructionFromTwoDates() {
        // Test des cas valides
        TimeSlot t = new TimeSlot(new Date(), new Date());
        assertNotNull(t);
        assertNotNull(t.getBegin());
        assertNotNull(t.getEnd());
        assertNotNull(t.getDuration());
        assertEquals(t.getDuration(), new Long(0));
        assertEquals(t.getBegin(), new Date());
        assertEquals(t.getEnd(), t.getBegin());

        // Test des cas invalides
        try {
            new TimeSlot(new Date(1), new Date(0));
            fail("Construction avec un intervalle inversé");
        } catch (IllegalArgumentException e) {
        }

        try {
            new TimeSlot(new Date(), (Date) null);
            new TimeSlot((Date) null, new Date());
            fail("Construction avec une date nulle");
        } catch (IllegalArgumentException | NullPointerException e) {
        }
    }

    public void testSetters() {
        // Test avec une donnée null ou invalide
        TimeSlot t = new TimeSlot(new Date(), new Long(0));

        try {
            t.setBegin(null);
            fail("Assignement d'un pointeur nul comme début");
        } catch (NullPointerException e) {
        }

        try {
            t.setDuration(null);
            fail("Assignement d'un pointeur nul comme duration");
        } catch (NullPointerException e) {
        }

        try {
            t.setDuration(new Long(-1));
            fail("Assignement d'une valeur négative comme duration");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testGetters() {
        // Test avec une donnée null ou invalide
        TimeSlot t = new TimeSlot(new Date(), new Long(0));

        // Set puis get le début
        long timestamp = 133742;
        t.setBegin(new Date(timestamp));
        assertEquals(t.getBegin().getTime(), timestamp);

        // Set puis get la durée
        Long duration = new Long(10);
        t.setDuration(duration);
        assertEquals(t.getDuration(), duration);

        // Vérifie la fin
        assertEquals(t.getBegin().getTime() + t.getDuration(), t.getEnd().getTime());
        assertEquals(t.getEnd().getTime(), timestamp + duration);
    }
};
