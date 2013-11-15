package tsp;

import model.*;

public interface Strategy {

	/**
	 * 
	 * @param nodes
	 */
	List<RoadNode> execute(List<RoadNode> nodes);

}