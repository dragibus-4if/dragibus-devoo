package model;

import javax.management.RuntimeErrorException;

public class RoadSection {

    private String roadName;
    private double speed = 0;
    private double length = 0;
    private final RoadNode roadNodeBegin;
    private final RoadNode roadNodeEnd;

    /**
     * Constructeur d'une section liant un premier noeud à un autre.
     *
     * @param roadNodeBegin
     * @param roadNodeEnd
     */
    public RoadSection(RoadNode roadNodeBegin, RoadNode roadNodeEnd) {
        if (roadNodeBegin == null) {
            throw new NullPointerException("'roadNodeBegin' ne doit pas être nul");
        }
        this.roadNodeBegin = roadNodeBegin;

        if (roadNodeEnd == null) {
            throw new NullPointerException("'roadNodeEnd' ne doit pas être nul");
        }
        this.roadNodeEnd = roadNodeEnd;
    }
    /**
     * Constructeur définissant le noeud de début, celui de fin et la vitesse et
     * la longueur.
     * 
     * @param roadNodeBegin
     * @param roadNodeEnd
     * @param speed
     * @param length 
     */
    public RoadSection(RoadNode roadNodeBegin, RoadNode roadNodeEnd, double speed, double length) {
        this(roadNodeBegin, roadNodeEnd);
        this.speed = speed;
        this.length = length;
    }

    /**
     * Constructeur définissant totalement une section de route.
     * 
     * @param roadNodeBegin
     * @param roadNodeEnd
     * @param speed
     * @param length
     * @param roadName 
     */
    public RoadSection(RoadNode roadNodeBegin, RoadNode roadNodeEnd, double speed, double length, String roadName) {
        this(roadNodeBegin, roadNodeEnd, speed, length);
        this.roadName = roadName;
    }

    /**
     * Calcul le temps de parcours de la section.
     * Utilise la formule t = d / v. Ce temps peut être assimilé à un cout de
     * trajet.
     * 
     * @return  le temps pour parcourir la section.
     */
    public double getCost() {
        if (speed == 0) {
            throw new RuntimeErrorException(new Error("Coût indéfini, vitesse de 0"));
        }
        return length / speed;
    }

    /**
     * Accesseur en lecture du nom de la route.
     * 
     * @return  nom de la route
     */
    public String getRoadName() {
        return roadName;
    }

    /**
     * Accesseur en écriture du nom de la route.
     * 
     * @param roadName 
     */
    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    /**
     * Accesseur en lecture de la vitesse de parcours de la route.
     * 
     * @return vitesse de parcours
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Accesseur en écriture de la vitesse de parcours de la route.
     * @param speed 
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Accesseur en lecture de la longueur de la route.
     * 
     * @return  longueur de la route
     */
    public double getLength() {
        return length;
    }

    /**
     * Accesseur en écriture de la longueur de la route.
     * 
     * @param length 
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * Accesseur en lecture du noeud de début de la route.
     * 
     * @return le noeud de début
     */
    public RoadNode getRoadNodeBegin() {
        return roadNodeBegin;
    }

    /**
     * Accesseur en lecture du noeud de fin de la route.
     * 
     * @return le noeud de fin
     */
    public RoadNode getRoadNodeEnd() {
        return roadNodeEnd;
    }

    //@Override
    //public boolean equals(Object obj) {
    //    if (obj == null) {
    //        return false;
    //    } else if (getClass() != obj.getClass()) {
    //        return false;
    //    } else {
    //        final RoadSection other = (RoadSection) obj;
    //        return roadNodeBegin.equals(other.roadNodeBegin);
    //    }
    //}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + roadNodeBegin.hashCode();
        hash = 67 * hash + roadNodeEnd.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return "RoadSection with begin = " + roadNodeBegin + " and end = " + roadNodeEnd;
    }

}
