
public class Vertex {
	// ===================================================== Private Variables
	private String symbol;
	private String address;
	private int xPos;
	private int yPos;
	// ===================================================== Constructor
	public Vertex(String symbol, String address, int x, int y) {
		this.symbol = symbol;
		this.address = address;
		xPos = x;
		yPos = y;
	}
	
	// ===================================================== Methods
	public String getSymbol() {
		return symbol;
	}
	public String getAddress() {
		return address;
	}
	public String getInfo() {
		return Graph.returnAddress ? address : symbol;
	}

	public int getxPos() {
		return xPos;
	}
	public int getyPos() {
		return yPos;
	}
}
