package model;

import java.io.File;

public class RoadNetwork {

        RoadNode root;

	public RoadNetwork() {
		this.root = null;
	}

	/**
	 * 
     * @param node
	 */
	public void setRoot(RoadNode node) {
		this.root = node;
	}

	/**
	 * 
	 * @param Long
     * @return 
	 */
	public RoadNode getNodeById(int Long) {
		// TODO - implement RoadNetwork.getNodeById
		throw new UnsupportedOperationException();
	}

    public RoadNode getRoot() {
        return root;
    }

	/**
	 * 
	 * @param file
     * @return 
	 */
	public int loadFromXML(File file) {
		// TODO - implement RoadNetwork.loadFromXml
		throw new UnsupportedOperationException();
	}

}