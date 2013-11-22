package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RoadNode {

    private final Long id;
    private final Set<RoadSection> sections = new HashSet<>();
    private final Set<RoadNode> nodes = new HashSet<>();
    private int x;
    private int y;

    public RoadNode(long id) {
        this(new Long(id));
    }

    public RoadNode(Long id) {
        if (id == null) {
            throw new NullPointerException("'id' ne doit pas être nul");
        } 
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Collection<RoadSection> getSections() {
        return sections;
    }

    public final Collection<RoadNode> getNeighbors() {
        Collection<RoadNode> ls = new ArrayList<>(sections.size());
        for (RoadSection section : getSections()) {
            ls.add(section.getRoadNodeEnd());
        }
        return ls;
    }
    
    public final Collection<RoadNode> getNodes() {
        return nodes;
    }

    public void addNeighbor(RoadNode section, double speed, double length) {
        addNeighbor(new RoadSection(this, section, speed, length));
    }
    
    public void addNeighbor(RoadNode section, double speed, double length, String roadName) {
        addNeighbor(new RoadSection(this, section, speed, length, roadName));
    }
    
    public void addNeighbor(RoadSection section) {
        if (section == null) {
            throw new NullPointerException("'section' ne doit pas être nul");
        } else if (section.getRoadNodeEnd() == null) {
            throw new NullPointerException("'section' ne doit pas avoir une arrivée nulle");
        } else if (section.getRoadNodeBegin() != this) {
            throw new IllegalArgumentException("'section' ajoutée doit avoir l'objet courant comme départ");
        } else {
            sections.add(section);
            nodes.add(section.getRoadNodeEnd());
            section.getRoadNodeEnd().nodes.add(this);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + id.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        } else {
            final RoadNode other = (RoadNode) obj;
            return id.equals(other.id);
        }
    }
    
    public String toString(){ //to remove later
        return ( this.id.toString());
    }

}
