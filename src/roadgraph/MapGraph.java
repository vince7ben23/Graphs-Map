package roadgraph;


import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;
import java.lang.IllegalArgumentException;

import geography.GeographicPoint;
import util.GraphLoader;


/**
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between
 *  
 * @author Cheng-Ying, Chen
 */

public class MapGraph {
	// Add member variables here in WEEK 3
	private Map<GeographicPoint, MapNode> graph;
	private int verticesNum;
	private Set<GeographicPoint> vertices;
	private int edgesNum;
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		// Implement in this constructor in WEEK 3
		this.graph = new HashMap<>();
		this.vertices = new HashSet<>();
		this.verticesNum = 0;
		this.edgesNum = 0;
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		// Implement this method in WEEK 3
		return verticesNum;
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		// Implement this method in WEEK 3
		return vertices;
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		// Implement this method in WEEK 3
		return edgesNum;
	}

	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		// Implement this method in WEEK 3
		if (graph.containsKey(location) || null == location) {
			return false;
		}
		graph.put(location, new MapNode(location));
		vertices = graph.keySet();
		verticesNum += 1;
		return true;
		
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {

		// Implement this method in WEEK 3
		if (!checkBeforeAddEdge(from, to, length)) {
			throw new IllegalArgumentException("check from/to points in the graph and arguments are valid");
		}
		List<MapEdge> edges = graph.get(from).getEdges();
		MapEdge newEdge = new MapEdge(from, to, roadName, roadType, length);
		edges.add(newEdge);
		edgesNum += 1;
	}
	
	private boolean checkBeforeAddEdge(GeographicPoint from, GeographicPoint to, double length) {
		if (
				!graph.containsKey(from) ||
				!graph.containsKey(to) ||
				null==from ||
				null==to ||
				length < 0
			) {
			return false;
		}
		return true;
	}
	

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, 
			 					     Consumer<GeographicPoint> nodeSearched
			 					    )
	{
		// Implement this method in WEEK 3
		
		MapNode startNode = this.graph.get(start);
		MapNode goalNode = this.graph.get(goal);
		
		if (startNode == null || goalNode == null) {
			throw new IllegalArgumentException("start or goal point is null.");
		}
		
		// initialize structures that BFS needs
		HashMap<MapNode, MapNode> parentMap = new HashMap<>();
		
		boolean found = bfsSearch(startNode, goalNode, parentMap, nodeSearched);
		
		if (!found) {
			return null;
		}
		return constructPath(startNode, goalNode, parentMap);
	}
	
	private boolean bfsSearch(
			MapNode startNode,
			MapNode goalNode, 
			HashMap<MapNode, MapNode> parentMap,
			Consumer<GeographicPoint> nodeSearched
			) {
		// initialize structures that BFS needs
		Queue<MapNode> toExplore = new LinkedList<>();
		HashSet<MapNode> seenNodes = new HashSet<>();
		boolean found = false;
	
		toExplore.add(startNode);	
		while (!toExplore.isEmpty()) {
			MapNode currNode = toExplore.remove();
			if (currNode == goalNode) {
				found = true;
				break;
			}
			List<MapEdge> edges = currNode.getEdges();
			for (MapEdge edge : edges) {
				MapNode neighborNode =  this.graph.get(edge.getEnd());
				if (!seenNodes.contains(neighborNode)) {
					seenNodes.add(neighborNode);
					parentMap.put(neighborNode, currNode);
					toExplore.add(neighborNode);
					nodeSearched.accept(neighborNode.getLocation());
				}
			}
 		}
		
		return found;
	}
	
	private List<GeographicPoint> constructPath(
			MapNode startNode, 
			MapNode goalNode, 
			HashMap<MapNode, MapNode> parentMap
			) {
		LinkedList<GeographicPoint> path = new LinkedList<>();
		MapNode currNode = goalNode;
		while (currNode != startNode) {
			path.addFirst(currNode.getLocation());
			currNode = parentMap.get(currNode);
		}
		path.addFirst(currNode.getLocation());
		return path;
	}

	private void resetAllNodesDist() {
		graph.forEach(
				(geoPoint, node)
				-> {
					node.setDistanceFromStart(Double.POSITIVE_INFINITY);
					node.setDistanceToGoal(Double.POSITIVE_INFINITY);
				}
		);
	}

	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(
			GeographicPoint start,
			GeographicPoint goal,
			Consumer<GeographicPoint> nodeSearched
	)
	{
		// Implement this method in WEEK 4
		MapNode startNode = this.graph.get(start);
		MapNode goalNode = this.graph.get(goal);

		if (startNode == null || goalNode == null) {
			throw new IllegalArgumentException("start or goal point is null.");
		}

		// Initialize vars
		boolean found = false;
		PriorityQueue<MapNode> toExplore = new PriorityQueue<>();
		HashSet<MapNode> visitedNodes = new HashSet<>();
		HashMap<MapNode, MapNode> childToParent = new HashMap<>();
		HashMap<MapNode, Double> nodeDistance = new HashMap<>();

		int dequeueTimes = 0;
		startNode.setDistanceFromStart(0.0);
		startNode.setDistanceToGoal(0.0);
		toExplore.add(startNode);
		while (!toExplore.isEmpty()) {
			/*
			System.out.println("#################");
			System.out.println("toExplore");
			for (MapNode node : toExplore) {
				System.out.println("node: " + node.getLocation() + ", dis: " + node.getDistance());
			}
			*/
			MapNode currNode = toExplore.poll();
			// Hook for visualization.  See writeup.
			nodeSearched.accept(currNode.getLocation());
			dequeueTimes += 1;
			System.out.println("number " + dequeueTimes + ". " + "dequeue currNode: " + currNode.getLocation());

			visitedNodes.add(currNode);

			if (currNode == goalNode) {
				found = true;
				break;
			}

			List<MapEdge> edges = currNode.getEdges();
			for (MapEdge edge : edges) {
				MapNode neighborNode =  this.graph.get(edge.getEnd());
				if (visitedNodes.contains(neighborNode)) {
					continue;
				}

				Double neighborDist = currNode.getDistanceFromStart() + edge.getLength();

				if (!childToParent.containsKey(neighborNode) ||
						childToParent.containsKey(neighborNode) &&
								neighborDist < nodeDistance.get(neighborNode)
				) {
					neighborNode.setDistanceFromStart(neighborDist);
					neighborNode.setDistanceToGoal(0.0);
					childToParent.put(neighborNode, currNode);
					nodeDistance.put(neighborNode, neighborNode.getDistanceFromStart());
					toExplore.add(neighborNode);
					/*
					System.out.println("childToParent");
					for (MapNode node : childToParent.keySet()) {
						System.out.println("node: " + node.getLocation() + ", dis: " + node.getDistance());
					}
					System.out.println();
					System.out.println("nodeDistance");
					for (MapNode node : nodeDistance.keySet()) {
						System.out.println("node: " + node.getLocation() + ", dis: " + node.getDistance());
					}
					System.out.println();
					*/
				}
			}
		}

		resetAllNodesDist();
		if (!found) {
			return null;
		}
		return constructPath(startNode, goalNode, childToParent);
	}

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(
			GeographicPoint start,
			GeographicPoint goal,
			Consumer<GeographicPoint> nodeSearched
	)
	{
		// Implement this method in WEEK 4
		MapNode startNode = this.graph.get(start);
		MapNode goalNode = this.graph.get(goal);

		if (startNode == null || goalNode == null) {
			throw new IllegalArgumentException("start or goal point is null.");
		}

		// Initialize vars
		boolean found = false;
		PriorityQueue<MapNode> toExplore = new PriorityQueue<>();
		HashSet<MapNode> visitedNodes = new HashSet<>();
		HashMap<MapNode, MapNode> childToParent = new HashMap<>();
		HashMap<MapNode, Double> nodeDistance = new HashMap<>();

		int dequeueTimes = 0;
		startNode.setDistanceFromStart(0.0);
		startNode.setDistanceToGoal(goal.distance(start));
		toExplore.add(startNode);
		while (!toExplore.isEmpty()) {
			/*
			System.out.println("#################");
			System.out.println("toExplore");
			for (MapNode node : toExplore) {
				System.out.println("node: " + node.getLocation() +
						", dis: " + node.getDistance()
				);
			}
			*/
			MapNode currNode = toExplore.poll();
			// Hook for visualization.  See writeup.
			nodeSearched.accept(currNode.getLocation());
			dequeueTimes += 1;
			System.out.println("number " + dequeueTimes + ". " + "dequeue currNode: " + currNode.getLocation());

			visitedNodes.add(currNode);

			if (currNode == goalNode) {
				found = true;
				break;
			}

			List<MapEdge> edges = currNode.getEdges();
			for (MapEdge edge : edges) {
				MapNode neighborNode =  this.graph.get(edge.getEnd());
				if (visitedNodes.contains(neighborNode)) {
					continue;
				}

				Double fromStartDist = currNode.getDistanceFromStart() + edge.getLength();
				Double toGoalDist = goalNode.getLocation().distance(neighborNode.getLocation());
				Double totalDist = fromStartDist + toGoalDist;

				if (!childToParent.containsKey(neighborNode) ||
						childToParent.containsKey(neighborNode) &&
								totalDist < nodeDistance.get(neighborNode)
				) {
					neighborNode.setDistanceFromStart(fromStartDist);
					neighborNode.setDistanceToGoal(toGoalDist);
					childToParent.put(neighborNode, currNode);
					nodeDistance.put(neighborNode, totalDist);
					toExplore.add(neighborNode);
					/*
					System.out.println("childToParent");
					for (MapNode node : childToParent.keySet()) {
						System.out.println("node: " + node.getLocation() + ", dis: " + node.getDistance());
					}
					System.out.println();
					System.out.println("nodeDistance");
					for (MapNode node : nodeDistance.keySet()) {
						System.out.println("node: " + node.getLocation() + ", dis: " + node.getDistance());
					}
					System.out.println();
					*/
				}
			}
		}

		resetAllNodesDist();
		if (!found) {
			return null;
		}
		return constructPath(startNode, goalNode, childToParent);
	}

	
	
	public static void main(String[] args)
	{

		System.out.print("Making a new map...");
		MapGraph firstMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
		System.out.println("DONE.");
		
		// You can use this method for testing.  
		
		
		/* Here are some test cases you should try before you attempt 
		 * the Week 3 End of Week Quiz, EVEN IF you score 100% on the 
		 * programming assignment.
		 */

		MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		System.out.println("Call Dijkstra");
		List<GeographicPoint> testRoute = simpleTestMap.dijkstra(testStart,testEnd);
		System.out.println(testRoute);
		System.out.println("Call A Star");
		List<GeographicPoint> testRoute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		System.out.println(testRoute2);

		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		System.out.println("Call Dijkstra");
		testRoute = testMap.dijkstra(testStart,testEnd);
		System.out.println(testRoute);
		System.out.println("Call A Star");
		testRoute2 = testMap.aStarSearch(testStart,testEnd);
		System.out.println(testRoute2);



		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		System.out.println("Call Dijkstra");
		testRoute = testMap.dijkstra(testStart,testEnd);
		System.out.println(testRoute);
		System.out.println("Call A Star");
		testRoute2 = testMap.aStarSearch(testStart,testEnd);
		System.out.println(testRoute2);

		
		/* Use this code in Week 3 End of Week Quiz */

		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);

		System.out.println("Call Dijkstra");
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		System.out.println(route);
		System.out.println("Call A Star");
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
		System.out.println(route2);


		
	}
	
}
