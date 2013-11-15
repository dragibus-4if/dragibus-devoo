package model;

public class RoadSection {

	private String roadName;
	private double speed;
	private double length;
	RoadNode roadNodeBegin;
	RoadNode roadNodeEnd;

    public RoadNode getRoadNodeEnd() {
        return roadNodeEnd;
    }
    
    public double getCost() {
        return length / speed;
    }

	public RoadSection() {
		// TODO - implement RoadSection.RoadSection
		throw new UnsupportedOperationException();
	}

}