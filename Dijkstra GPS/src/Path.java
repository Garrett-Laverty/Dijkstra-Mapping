
public class Path implements Comparable<Path> { 
	// ===================================================== Variables
	private String vertex;
	private String pathStr;
	private int cost;
	
	// ===================================================== Constructor
	public Path(String vertex, int cost, String pathStr) {
		this.vertex = vertex;
		this.cost = cost;
		this.pathStr = pathStr;
	}
	// ===================================================== Methods
	public int compareTo(Path other) {
		return cost - other.cost;
	}
	public String getVertex() {
		return vertex;
	}
	public String getPathStr() {
		return pathStr;
	}
	public int getCost() {
		return cost;
	}
	@Override
	public String toString() {
		return "[" + vertex + " " + pathStr + " " + cost + "]";
	}
}