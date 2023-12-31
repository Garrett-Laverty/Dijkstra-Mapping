import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class Tester {

	Graph g = new Graph("MapInformationXY.txt");
	@Test
	void testGraphConstructor() {
		assertTrue(g != null);
	}
	@Test
	void testVertexAndEdgeConstructors() {
		int vertexCount = 0;
		int edgeCount = 0;
		for(Vertex v : g.getGraph().keySet()) {
			vertexCount++;
			for(Edge e : g.getGraph().get(v)) {
				edgeCount++;
			}
		}
		assertEquals(vertexCount, 20);
		assertEquals(edgeCount, 41);
	}
	@Test
	void testReadFile() {
		assertFalse(g.getGraph().isEmpty());
		assertTrue(g.toString().contains("J has edge J => C with a distance cost of: 16"));
		assertTrue(g.toString().contains("A has edge A => T with a distance cost of: 10 "));
		assertTrue(g.toString().contains("K has edge K => H with a distance cost of: 12"));
	}
	@Test
	void testDijkstra() {
		String start;
		String end;

		start = "J";
		end = "C";
		Path p = Dijkstra.shortestPath(g.getGraph(), start, end);
		assertTrue(p.toString().equals("[C JC 16]"));
		g.useDistCost = false;
		g.useTrafficDensity = true;

		start = "I";
		end = "N";
		p = Dijkstra.shortestPath(g.getGraph(), start, end);
		assertTrue(p.toString().equals("[N IEOPCDN 27]"));
	}

}
