import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 
 * @author 
 * 		Garrett Laverty
 * 
 * Date:
 * 		5/6/2022
 * 
 * Description:
 * 		Displays everything necessary for the GPS application
 *
 */

public class GUI extends JPanel {
	// ============================================================================================ Variables
	// Frame
	JFrame window = new JFrame();
	// Lists
	DefaultListModel<String> l1 = new DefaultListModel<>();      
	DefaultListModel<String> l2 = new DefaultListModel<>(); 
	// Everything needed for finding and displaying paths
	Path p;
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	boolean canChange = false;
	static Graph g;
	Image imgMap = Toolkit.getDefaultToolkit().getImage("FinalProjectGraph_Final_400x400.png");

	Stroke s1 = new BasicStroke(7.5f,
			BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_MITER,
			10.0f,
			new float[] {20},
			0.0f
			);	
	Stroke s2 = new BasicStroke(4.0f);

	// ============================================================================================ Constructor
	public GUI(Graph graph) {

		g = graph;
		// ============================================================ Setup lists
		addValsToList();

		// ============================================================ Setup initial window values
		window.setTitle("GPS");
		window.setBounds(50, 50, 900, 460);
		window.setAlwaysOnTop(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.getContentPane().add(this);
		window.setVisible(true);

		// ============================================================ Create lists and scroll bars
		JList<String> list1 = new JList<>(l1);
		JPanel listPanel = new JPanel(new GridLayout(1, 2, 20, 0));
		listPanel.setBounds(40,30, 350, 130); 
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(list1);
		list1.setLayoutOrientation(JList.VERTICAL);
		scrollPane.setSize(10, 10);
		listPanel.add(scrollPane);

		JList<String> list2 = new JList<>(l2);
		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setViewportView(list2);
		list2.setLayoutOrientation(JList.VERTICAL);
		scrollPane2.setSize(10, 10);
		listPanel.add(scrollPane2);

		// ============================================================ Create labels for lists
		JPanel labelPanel = new JPanel(new GridLayout(1, 2));
		JLabel startingLabel = new JLabel("Starting Location:");
		JLabel endinglabel = new JLabel("Ending Location:");
		labelPanel.setBounds(40, 10, 375, 20);
		labelPanel.add(startingLabel);
		labelPanel.add(endinglabel);

		// ============================================================ Create text output
		JPanel textPanel = new JPanel(new BorderLayout());
		JTextArea text = new JTextArea();      
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		textPanel.add(text);
		textPanel.setBounds(40, 295, 340, 115); 

		// ============================================================ Create button to generate paths
		JPanel generatePanel = new JPanel(new BorderLayout());
		JButton generateButton=new JButton("Generate Path");  
		generateButton.setBounds(40,260,140,30);  
		generatePanel.add(generateButton);
		generatePanel.setBounds(40,260,140,30);

		// ============================================================ Create radio buttons for Graph boolean values
		JRadioButton UseDistButton = new JRadioButton("Use Distance Cost");
		UseDistButton.setActionCommand("Use Distance Cost");

		JRadioButton UseTimeButton = new JRadioButton("Use Time Cost");
		UseTimeButton.setActionCommand("Use Time Cost");

		ButtonGroup group = new ButtonGroup();
		group.add(UseDistButton);
		group.add(UseTimeButton);

		// ============================================================ Group 2 of buttons
		JRadioButton UseAddresses = new JRadioButton("Use Addresses");
		UseAddresses.setActionCommand("Use Distance Cost");

		JRadioButton UseSymbols = new JRadioButton("Use Symbols");
		UseSymbols.setActionCommand("Use Time Cost");

		ButtonGroup group2 = new ButtonGroup();
		group2.add(UseAddresses);
		group2.add(UseSymbols);

		// ============================================================ Add buttons and checkboxes
		JPanel radioButtonsPanel = new JPanel(new GridLayout(3, 2));
		radioButtonsPanel.add(UseDistButton);
		radioButtonsPanel.add(UseTimeButton);
		radioButtonsPanel.add(UseAddresses);
		radioButtonsPanel.add(UseSymbols);
		radioButtonsPanel.setBounds(40, 160, 370, 50);

		JPanel checkBoxPanel = new JPanel(new GridLayout(2, 1));
		JCheckBox AvoidTollsButton = new JCheckBox("Avoid Toll Roads", false);
		JCheckBox TrafficButton = new JCheckBox("Factor Traffic Into Time Cost", false);
		TrafficButton.setEnabled(false);
		checkBoxPanel.add(AvoidTollsButton);
		checkBoxPanel.add(TrafficButton);
		checkBoxPanel.setBounds(40, 200, 200, 50);

		// ============================================================ Set initial selected values
		list1.setSelectedIndex(0);
		list2.setSelectedIndex(0);
		UseDistButton.setSelected(true);
		UseSymbols.setSelected(true);

		// ============================================================ Set layout and add all panels
		window.setLayout(null);
		window.add(listPanel); 
		window.add(textPanel);
		window.add(generatePanel);
		window.add(radioButtonsPanel);
		window.add(checkBoxPanel);
		window.add(labelPanel);
		window.setVisible(true);

		// ============================================================ Button Actions
		generateButton.addActionListener(new ActionListener() {  
			public void actionPerformed(ActionEvent e) {  
				if(list1.getSelectedIndex() != list2.getSelectedIndex()) {
					canChange = true;
					createPath(text, list1, list2);
				}
				else {
					text.setText("Please choose two unique locations");
				}
			}  
		});  
		list1.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				canChange = false;
				edges = null;
				revalidate();
				repaint(400, 10, 500, 490);
			}
		});
		list2.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				canChange = false;
				edges = null;
				revalidate();
				repaint(400, 10, 500, 490);
			}
		});
		UseDistButton.addActionListener(new ActionListener() {  
			public void actionPerformed(ActionEvent e) {  
				Graph.useDistCost =  true;
				Graph.useTrafficDensity = false;
				TrafficButton.setSelected(false);
				TrafficButton.setEnabled(false);
				createPath(text, list1, list2);
			}  
		});
		UseTimeButton.addActionListener(new ActionListener() {  
			public void actionPerformed(ActionEvent e) {  
				Graph.useDistCost = false;
				TrafficButton.setEnabled(true);
				createPath(text, list1, list2);
			}  
		});
		UseAddresses.addActionListener(new ActionListener() {  
			public void actionPerformed(ActionEvent e) {  
				Graph.returnAddress =  true;
				l1.clear();
				l2.clear();
				addValsToList();

				list1.setSelectedIndex(0);
				list2.setSelectedIndex(0);
			}  
		});
		UseSymbols.addActionListener(new ActionListener() {  
			public void actionPerformed(ActionEvent e) {  
				Graph.returnAddress = false;
				l1.clear();
				l2.clear();
				addValsToList();

				list1.setSelectedIndex(0);
				list2.setSelectedIndex(0);
			}  
		});
		AvoidTollsButton.addActionListener(new ActionListener() {  
			public void actionPerformed(ActionEvent e) {  
				Graph.avoidTolls = (AvoidTollsButton.isSelected())? true : false;
				createPath(text, list1, list2);
			}  
		});
		TrafficButton.addActionListener(new ActionListener() {  
			public void actionPerformed(ActionEvent e) {  
				Graph.useTrafficDensity = (TrafficButton.isSelected())? true : false;
				createPath(text, list1, list2);
			}  
		});
	}
	// ============================================================================================ Methods

	private void createPath(JTextArea text, JList<String> list1, JList<String> list2) {
		// If the path can be changed then update text box, set up edges, and always repaint over the image
		if(canChange) {
			text.setText(callDijkstra(g.getGraph(),
					(String)list1.getSelectedValue(),
					(String)list2.getSelectedValue()));
			edges = (p != null) ? getEdges() : null;
		}
		revalidate();
		repaint(400, 10, 500, 490);
	}

	private void addValsToList() {
		// Use heap priority queue to add all the Vertices into both lists in order
		// This is only really relevant if using symbols
		HeapPriorityQueue<String> elements1 = new HeapPriorityQueue<String>();

		for(Vertex v : g.getGraph().keySet())
			elements1.add(v.getInfo());
		for(int i = elements1.size(); i > 0; i--)
			l1.addElement((String)elements1.remove());

		l2 = l1;
	}

	private String callDijkstra(HashMap<Vertex, Set<Edge>> graph, String startStr, String endStr) {
		p = Dijkstra.shortestPath(graph, startStr, endStr);
		return (p == null) ? "There is no path from " + startStr + " to " + endStr : 
			"Start Destination: " + startStr + "\n" + 
			"End Destination: " + endStr + "\n" + p.toString();
	}

	private ArrayList<Edge> getEdges() {
		// Split the path string to get an array of locations, send each pair to getEdgeOfVertex() (only if symbols)
		ArrayList<Edge> ret = new ArrayList<Edge>();
		String[] locations = p.getPathStr().split("");
		if(!g.returnAddress)
			for(int i = 0; i < locations.length-1; i++)
				ret.add(g.getEdgeOfVertex(locations[i], locations[(i+1)]));

		return ret;
	}

	// ============================================================================================ Painting Methods
	private Color setEdgeColor(Edge edge, Graphics g2, boolean isOpaque) {
		// Set the color of the edge depending on traffic density and make it opaque if
		// that edge is in the path
		Color myC;
		if(isOpaque) {
			if(edge.getTrafficDensity() > 1.5)		myC = Color.RED;
			else if(edge.getTrafficDensity() > 1.0) myC = Color.YELLOW;
			else									myC = Color.GREEN;
		}else {
			if(edge.getTrafficDensity() > 1.5)		myC = new Color(255, 0, 0, 75);
			else if(edge.getTrafficDensity() > 1.0) myC = new Color(180, 150, 0, 65);
			else									myC = new Color(0, 255, 0, 60);
		}
		return myC;
	}

	private void drawTrafficOrTolls(Graphics g2) {
		// Go through every edge, create an x if it is a toll road
		// and draw a line with a color depending on that edges traffic density
		for(Vertex v : this.g.getGraph().keySet()) {
			for(Edge edge: this.g.getGraph().get(v)) {
				int startX = 	(int) edge.getFrom().getxPos() + 450;
				int startY = 	(int) edge.getFrom().getyPos() + 10;
				int endX = 		(int) edge.getTo().getxPos() + 450;
				int endY = 		(int) edge.getTo().getyPos() + 10;
				if(Graph.useTrafficDensity) {
					g2.setColor(setEdgeColor(edge, g2, false));
					g2.drawLine(startX, startY, endX, endY);
				}
				if(edge.getIsToll()) {
					if(Graph.avoidTolls) {
						int lineLength = 10;
						g2.setColor(Color.RED);
						g2.drawLine((startX + endX) / 2 + lineLength, (startY + endY) / 2 + lineLength,
								(startX + endX) / 2 - lineLength, (startY + endY) / 2 - lineLength);
						g2.drawLine((startX + endX) / 2 - lineLength, (startY + endY) / 2 + lineLength,
								(startX + endX) / 2 + lineLength, (startY + endY) / 2 - lineLength);
					}
				}
			}
		}
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Add Map Image
		g2.drawImage(imgMap, 450, 10, window);
		g2.setColor(Color.CYAN);

		// Draw traffic density or cross out toll roads
		g2.setStroke(s2);
		if(Graph.avoidTolls || Graph.useTrafficDensity)
			drawTrafficOrTolls(g2);

		// Create the Path (This code was shown to me by Prof. Salman who is allowing me to use it)
		if(edges != null) {
			int state = 0;
			int factor = 1;
			for(Edge edge: edges) {
				int startX = 	(int) edge.getFrom().getxPos() + 450;
				int startY = 	(int) edge.getFrom().getyPos() + 10;
				int endX = 		(int) edge.getTo().getxPos() + 450;
				int endY = 		(int) edge.getTo().getyPos() + 10;
				g2.setColor(Color.CYAN);
				g2.setStroke(s1);
				// Set color of path to that roads traffic density
				if(Graph.useTrafficDensity) {
					g2.setColor(setEdgeColor(edge, g2, true));
					g2.drawLine(startX, startY, endX, endY);
				} else {
					g2.drawLine(startX, startY, endX, endY);
				}
				// If starting vertex create special icon, otherwise create generic icon
				g2.setColor((state == 0? Color.GRAY: Color.CYAN));
				factor = (state == 0? 2: 1);
				g2.fillOval(startX - (5 * factor), startY - (5 * factor), 10 * factor, 10 * factor);
				if(state == 0) {
					factor = 1;
					g2.setColor(Color.WHITE);
					g2.fillOval(startX - (5 * factor), startY - (5 * factor), 10 * factor, 10 * factor);
				}
				// If ending vertex create special icon, otherwise create generic icon
				g2.setColor((state == (edges.size() - 1)? Color.ORANGE: Color.CYAN));
				factor = (state == (edges.size() - 1)? 2: 1);
				g2.fillOval(endX - (5 * factor), endY - (5 * factor), 10 * factor, 10 * factor);
				state++;
			}
		}
	}

	public static void main(String[] args) { 
		Graph g = new Graph("MapInformationXY.txt");
		new GUI(g); 
	}
}
