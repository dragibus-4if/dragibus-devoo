package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Structure de données représentant un noeud dans un graphe.
 *
 * @author Pierre
 * @author Jean-Marie
 */
public class RoadNode {

    private final Long id;
    private final Set<RoadSection> sections = new HashSet<>();
    private final Set<RoadNode> nodes = new HashSet<>();
    private int x;
    private int y;

    /**
     * Surcharge prenant un type primitif {@code long}.
     *
     * @param id
     */
    public RoadNode(long id) {
        this(new Long(id));
    }

    /**
     * Constructeur mettant l'ID du noeud à la valeur spécifiée.
     *
     * @param id
     */
    public RoadNode(Long id) {
        if (id == null) {
            throw new NullPointerException("'id' ne doit pas être nul");
        } 
        this.id = id;
    }

    /**
     * Accesseur en lecture de l'id du noeud.
     *
     * @return l'id
     */
    public Long getId() {
        return id;
    }

    /**
     * Accesseur en lecture de l'abscisse du noeud.
     *
     * @return l'abscisse
     */
    public int getX() {
        return x;
    }

    /**
     * Accesseur en écriture de l'abscisse du noeud.
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Accesseur en lecture de l'ordonnée du noeud.
     *
     * @return l'ordonnée
     */
    public int getY() {
        return y;
    }

    /**
     * Accesseur en écriture de l'ordonnée du noeud.
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Accesseur en lecture de la liste des sections voisines du noeud.
     *
     * @return liste des sections voisines
     */
    public Collection<RoadSection> getSections() {
        return sections;
    }

    /**
     * Accesseur en lecture de la liste des noeuds voisin du noeud.
     * Un noeud a est voisin d'un autre b si il existe une section orientée
     * entre le noeud b et a.
     *
     * @return liste des noeuds voisin.
     */
    public Collection<RoadNode> getNeighbors() {
        Collection<RoadNode> ls = new ArrayList<>();
        for (RoadSection section : getSections()) {
            ls.add(section.getRoadNodeEnd());
        }
        return ls;
    }
    
    /**
     * Accesseur en lecture de la liste des noeuds autours du noeud.
     * Un noeud a est aux alentours d'un autre b si il existe une section
     * orientée entre a et b ou entre b et a.
     *
     * @return l'id
     */
    public final Collection<RoadNode> getNodes() {
        return nodes;
    }

    /**
     * @param section
     * @param speed
     * @param length
     * @see addNeihbor(RoadSection)
     */
    public void addNeighbor(RoadNode section, double speed, double length) {
        addNeighbor(new RoadSection(this, section, speed, length));
    }
    
    /**
     * Ajoute une section orienté partant du noeud.
     *
     * @param section
     */
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
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RoadNode ")
                .append(this.getId())
                .append(" with coordinates x = ")
                .append(this.getX())
                .append(" and y = ")
                .append(this.getY());
        return sb.toString();
    }

}
