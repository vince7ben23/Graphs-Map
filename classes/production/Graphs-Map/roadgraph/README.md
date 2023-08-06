# Class `MapGraph`
- use `MapNode` and `MapEdge` to construct MapGraph
- three search algorithms can search the shortest path
  - BFS (only for un-weighted graphs)
  - Dijkstra Search
  - A-Star Search

## Class `MapNode`
- store location and its edges
- store additional data for Dijkstra and A-Star search
  - actual distance from start node
  - predict distance to gaol node   

## Class `MapEdge`
- store the edge of start point and end point
- store the length from start point and end point

