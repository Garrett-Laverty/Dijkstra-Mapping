import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Dijkstra {
	public static Path shortestPath(HashMap<Vertex, Set<Edge>> graph, String start, String end) {
		// Variables for use
		HeapPriorityQueue<Path> notVisited = new HeapPriorityQueue<Path>();
		ArrayList<String> visited = new ArrayList<String>();

		notVisited.add(new Path(start, 0, start));

		while(!notVisited.isEmpty()) {
			// Get the next entry to evaluate
			Path nextEntry = notVisited.remove();

			// If already evaluated, skip
			if(visited.contains(nextEntry.getVertex())) {
				continue;
			} else {
				// This is now being evaluated, if this is the end, return the path
				visited.add(nextEntry.getVertex());
				if(nextEntry.getVertex().equals(end)) {
					return nextEntry;
				} else {
					// Set current values
					String currPath = nextEntry.getPathStr();
					String currVertex = nextEntry.getVertex();
					int currCost = nextEntry.getCost();

					// Set up the next path to be evaluated from every edge
					for(Vertex v : graph.keySet())
						if(v.getInfo().equals(currVertex))
							for(Edge e : graph.get(v)) {
								if(Graph.avoidTolls && e.getIsToll()) continue;
								int nextCost = currCost + (int)((Graph.useTrafficDensity && !Graph.useDistCost)?
																e.getCost() * e.getTrafficDensity() : e.getCost());
								String nextPath = currPath + e.getTo().getInfo();
								Path anotherPath = new Path(e.getTo().getInfo(), nextCost, nextPath);
								notVisited.add(anotherPath);
						}
				}
			}
		}
		// If no path was found, return null
		return null;
	}
}
