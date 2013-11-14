package test;

import java.util.Date;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import junit.framework.TestCase;
import model.TimeSlot;

public class TimeSlotTest extends TestCase {
    public void testConstruction() {
        // Test de la construction
        TimeSlot t = new TimeSlot(new Date(), new Long(0));
        assertNotNull(t);
        assertEquals(t.getDuration(), new Long(0));
        assertEquals(t.getBegin(), new Date());
        assertEquals(t.getEnd(), t.getBegin());
        
        // Levée d'une exception dans le cas ou on donne des paramètres
        // invalides.
        try {
            new TimeSlot(new Date(), new Long(-1));
            fail("Construction avec une durée négative");
        }
        catch(IllegalArgumentException e) {
        }
        
        try {
            new TimeSlot(new Date(), null);
            fail("Construction avec une durée null");
        }
        catch(NullPointerException e) {
        }
        
        try {
            new TimeSlot(null, new Long(0));
            fail("Construction avec une date null");
        }
        catch(NullPointerException e) {
        }
    }
    
    public void testSetter() {
        // Test avec une donnée null ou invalide
        TimeSlot t = new TimeSlot(new Date(), new Long(0));
        
        try {
            t.setBegin(null);
            fail("Assignement d'un pointeur null comme début");
        }
        catch(NullPointerException e) {
        }
        
        try {
            t.setDuration(null);
            fail("Assignement d'un pointeur null comme duration");
        }
        catch(NullPointerException e) {
        }
        
        try {
            t.setDuration(new Long(-1));
            fail("Assignement d'une valeur négative comme duration");
        }
        catch(NullPointerException e) {
        }
    }
    
    public void testGetter() {
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
        assertEquals(t.getBegin().getTime() + t.getDuration(), t.getEnd());
        assertEquals(t.getEnd(), timestamp + duration);
    }
};
