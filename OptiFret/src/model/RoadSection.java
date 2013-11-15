package model;

public class RoadSection {

    private String roadName;
    private double speed;
    private double length;
    private RoadNode roadNodeBegin;
    private RoadNode roadNodeEnd;

    public RoadSection() {
        // TODO - implement RoadSection.RoadSection
        throw new UnsupportedOperationException();
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

    public void setRoadNodeBegin(RoadNode roadNodeBegin) {
        this.roadNodeBegin = roadNodeBegin;
    }

    public RoadNode getRoadNodeEnd() {
        return roadNodeEnd;
    }

    public void setRoadNodeEnd(RoadNode roadNodeEnd) {
        this.roadNodeEnd = roadNodeEnd;
    }

}
