import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * 
 * @author 
 * 		Garrett Laverty
 * 
 * Date:
 * 		5/6/2022
 * 
 * Description:
 * 		A HashMap of Vertices and Edges used in the GPS application
 *
 */

public class Graph {
	//===================================================== Public Variables
	public static boolean useDistCost = true;
	public static boolean returnAddress = false;
	public static boolean avoidTolls = false;
	public static boolean useTrafficDensity = false;

	//===================================================== Private Variables
	private HashMap<Vertex, Set<Edge>> graph = new HashMap<Vertex, Set<Edge>>();

	//===================================================== Constructor
	public Graph(String fileName) {
		readFile(fileName);
	}

	//===================================================== Methods
	public void readFile(String fileName) {
        try {
            
            // Creates a scanner
            Scanner file = new Scanner(new File(fileName));
            String line = file.nextLine();
            
            Map<String, Vertex> vertices = new HashMap<String, Vertex>();
            
            // Skips lines until the Nodes are reached
            while (!line.equals("<Nodes>")) { line = file.nextLine(); }
            
            // Skips two lines of header text in the file
            file.nextLine();
            line = file.nextLine();
            
            // Creates Vertex objects (each of which contains a symbol and an address property)
            while (!line.equals("</Nodes>")) {
                String[] s = line.split("\t");
                vertices.put(s[0], new Vertex(s[0], s[1], Integer.parseInt(s[2]), Integer.parseInt(s[3])));
                line = file.nextLine();
            }
            
            // Skips lines until Edges are reached
            while (!line.equals("<Edges>")) { line = file.nextLine(); }
            file.nextLine();
            
            // Creates Edge objects (each of which contains a source, destination, time cost, 
            // and distance cost property) and adds them to a set
            line = file.nextLine();
            String[] s = line.split("\t");
            while (!line.equals("</Edges>")) {
                Vertex v = new Vertex(s[0], vertices.get(s[0]).getAddress(), vertices.get(s[0]).getxPos(), vertices.get(s[0]).getyPos());
                Set<Edge> edges = new HashSet<Edge>();

                do {
                    Vertex destination = new Vertex(s[1], vertices.get(s[1]).getAddress(), vertices.get(s[1]).getxPos(), vertices.get(s[1]).getyPos());
                    edges.add(new Edge(v, destination, Integer.parseInt(s[2]), Integer.parseInt(s[3]),
                    				   s[4].equals("T")? true : false, Double.parseDouble(s[5])));
                    line = file.nextLine();
                    s = line.split("\t");
                } while (s[0].equals(v.getSymbol()));
                
                // When the next line contains a different source Vertex, the current set of Edges are
                // added to the graph Map as values of the corresponding Vertex key (the source Vertex)
                graph.put(v, edges);
                
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception f) {
            f.printStackTrace();
        }
    }
	public Edge getEdgeOfVertex(String start, String end) {
		// Find the starting vertex, find the edge that has the ending parameter
		for(Vertex v : graph.keySet())
			if(v.getInfo().equals(start))
				for(Edge edge : graph.get(v))
					if(edge.getTo().getInfo().equals(end))
						return edge;
		return null;
	}
	@Override
	public String toString() {
		String ret = "";
		for(Vertex v : graph.keySet())
			for(Edge edge : graph.get(v))
				ret += v.getInfo() + " has edge " + edge.getFrom().getInfo() + " => " + edge.getTo().getInfo() +
						" with a " + ((Graph.useDistCost)? "distance" : "time") + " cost of: " + edge.getCost() + "  \n";
		return ret;
	}
	
	public HashMap<Vertex, Set<Edge>> getGraph() {
		return graph;
	}
	
	public static void main(String[] args) throws IOException {
		Graph g = new Graph("MapInformationXY.txt");
		GUI gui = new GUI(g);
	}
}
