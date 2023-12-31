
public class Edge {
	//===================================================== Private Variables
	private Vertex from;
	private Vertex to;
	private int timeCost;
	private int distCost;
	private boolean isToll;
	private double trafficDensity;

	//===================================================== Constructor
	public Edge(Vertex from, Vertex to, int timeCost, int distCost, boolean isToll, double trafficDensity) {
		this.from = from;
		this.to = to;
		this.timeCost = timeCost;
		this.distCost = distCost;
		this.isToll = isToll;
		this.trafficDensity = trafficDensity;
	}

	//===================================================== Methods
	public Vertex getFrom() {
		return from;
	}
	public Vertex getTo() {
		return to;
	}
	public int getCost() {
		return Graph.useDistCost ? distCost : timeCost;
	}

	public Boolean getIsToll() {
		return isToll;
	}

	public double getTrafficDensity() {
		return trafficDensity;
	}
}
