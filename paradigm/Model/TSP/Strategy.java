package Model.TSP;

import Model.*;

public interface Strategy {

	/**
	 * 
	 * @param nodes
	 */
	RoadNode[] execute(RoadNode[] nodes);

}