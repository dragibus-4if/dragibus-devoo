package model;

public class RoadSection {

    private String roadName;
    private double speed;
    private double length;
    private final RoadNode roadNodeBegin;
    private final RoadNode roadNodeEnd;

    public RoadSection(RoadNode roadNodeBegin, RoadNode roadNodeEnd, double speed, double length, String roadName) {
        this(roadNodeBegin, roadNodeEnd, speed, length);
        this.roadName = roadName;
    }
    
    public RoadSection(RoadNode roadNodeBegin, RoadNode roadNodeEnd, double speed, double length) {
        if (roadNodeBegin == null) {
            throw new NullPointerException("'roadNodeBegin' ne doit pas être nul");
        }
        this.roadNodeBegin = roadNodeBegin;

        if (roadNodeBegin == null) {
            throw new NullPointerException("'roadNodeEnd' ne doit pas être nul");
        }
        this.roadNodeEnd = roadNodeEnd;

        this.speed = speed;
        this.length = length;
    }

    public double getCost() {
        return length / speed;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public RoadNode getRoadNodeBegin() {
        return roadNodeBegin;
    }

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

}
