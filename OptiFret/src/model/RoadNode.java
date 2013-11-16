package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RoadNode {

    private final Long id;
    private final Set<RoadSection> sections = new HashSet<>();
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

    public Collection<RoadNode> getNeighbors() {
        Collection<RoadNode> ls = new ArrayList<>(sections.size());
        for (RoadSection section : getSections()) {
            ls.add(section.getRoadNodeEnd());
        }
        return ls;
    }

    public void addNeighbor(RoadSection section) {
        if (section == null) {
            throw new NullPointerException("'section' ne doit pas être nul");
        } else if (section.getRoadNodeEnd() == null) {
            throw new NullPointerException("'section' ne doit pas avoir une arrivée nulle");
        } else if (section.getRoadNodeBegin() != this) {
            throw new IllegalArgumentException("'section' ajoutée doit avoir l'objet courant comme départ");
        } else {
            int size = sections.size();
            sections.add(section);
            if (sections.size() == size) {
                throw new IllegalArgumentException("'section' ajoutée ne doit pas avoir déjà été ajoutée");
            }
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

}
