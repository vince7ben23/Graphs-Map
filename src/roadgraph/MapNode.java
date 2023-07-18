package roadgraph;
import java.util.List;
import java.util.ArrayList;
import geography.GeographicPoint;

public class MapNode {
	
	 
	private GeographicPoint location;
	private List<MapEdge> edges;
	
	
	public MapNode(GeographicPoint point) {
		this.location = point;
		this.edges = new ArrayList<MapEdge>();
	}
	
	public GeographicPoint getLocation() {
		return this.location;
	}
	
	public List<MapEdge> getEdges() {
		return edges;
	}
	
	public void addEdge(MapEdge edge) {
		edges.add(edge);
	}
	
	
	
}
