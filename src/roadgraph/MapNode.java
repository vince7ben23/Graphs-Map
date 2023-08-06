package roadgraph;
import java.util.List;
import java.util.ArrayList;
import geography.GeographicPoint;

public class MapNode implements Comparable<MapNode> {
	
	 
	private GeographicPoint location;
	private Double distanceFromStart;
	private Double distanceToGoal;
	private List<MapEdge> edges;

	public MapNode(GeographicPoint point) {
		this.location = point;
		this.distanceFromStart = Double.POSITIVE_INFINITY;
		this.distanceToGoal = Double.POSITIVE_INFINITY;
		this.edges = new ArrayList<MapEdge>();

	}

	public GeographicPoint getLocation() {
		return this.location;
	}
	
	public List<MapEdge> getEdges() {
		return this.edges;
	}
	
	public void addEdge(MapEdge edge) {
		this.edges.add(edge);
	}

	public Double getDistanceFromStart() {
		return this.distanceFromStart;
	}
	public void setDistanceFromStart(Double distance) {
		this.distanceFromStart = distance;
	}
	public Double getDistanceToGoal() {
		return this.distanceToGoal;
	}
	public void setDistanceToGoal(Double distance) {
		this.distanceToGoal = distance;
	}
	public Double getDistance() {
		return this.distanceFromStart + this.distanceToGoal;
	}


	@Override
	public int compareTo(MapNode anotherNode) {
		if (this.getDistance() < anotherNode.getDistance()) return -1;
		if (this.getDistance() > anotherNode.getDistance()) return 1;
		return 0;
	}
}
