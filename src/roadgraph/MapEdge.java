package roadgraph;
import geography.GeographicPoint;

public class MapEdge {
	
	private GeographicPoint start;
	private GeographicPoint end;
	private String roadName;
	private String roadType;
	private Double length;
	
	public MapEdge(GeographicPoint start, GeographicPoint end, String roadName, String roadType, Double length) {
		this.start = start;
		this.end = end;
		this.roadName = roadName;
		this.roadType = roadType;
		this.length = length;
	}
	
	public GeographicPoint getStart() {
		return this.start;
	}
	
	public GeographicPoint getEnd() {
		return this.end;
	}
	
	public String getroadName() {
		return this.roadName;
	}
	
	public String getroadType() {
		return this.roadType;
	}
	
	public Double getLength() {
		return this.length;
	}
	
}
