package view.panels;

import algorithms.GraphAlgorithms;
import interfaces.IGraph;
import interfaces.IVisualizer;

import javax.swing.*;
import java.awt.*;

/**
 * Panel que representa visualmente el estado del grafo.
 * Implementa IVisualizer para comunicarse con la lógica.
 */
public class GraphPanel extends JPanel implements IVisualizer {

    private IGraph graph;
    private Point[] positions;
    private boolean[][] visitedEdges;

    private static final int NODE_DIAMETER = 30;
    private static final int NODE_RADIUS = NODE_DIAMETER / 2;

    public GraphPanel(IGraph g) {
        this.graph = g;
        this.positions = new Point[graph.vertexCount()];
        this.visitedEdges = new boolean[graph.vertexCount()][graph.vertexCount()];

        setBackground(Color.white);
        setPreferredSize(new Dimension(850, 500));

        // Posiciones de los fokin nodos
        int nodeCount = graph.vertexCount();
        if (nodeCount > 0) positions[0] = new Point(100, 225); // Nodo 0
        if (nodeCount > 1) positions[1] = new Point(300, 100); // Nodo 1
        if (nodeCount > 2) positions[2] = new Point(300, 350); // Nodo 2
        if (nodeCount > 3) positions[3] = new Point(500, 100); // Nodo 3
        if (nodeCount > 4) positions[4] = new Point(500, 350); // Nodo 4

        for (int i = 0; i < nodeCount; i++) {
            if (positions[i] == null) {
                positions[i] = new Point(0, 0);
            }
        }
    }

    @Override
    public IGraph getGraph() {
        return this.graph;
    }

    @Override
    public void resetVisuals() {
        for (int v = 0; v < graph.vertexCount(); v++) {
            graph.setMark(v, GraphAlgorithms.WHITE);
        }
        this.visitedEdges = new boolean[graph.vertexCount()][graph.vertexCount()];
        System.out.println("Grafo reseteado.");
        repaint();
    }

    @Override
    public void markEdge(int source, int destination, boolean visited) {
        this.visitedEdges[source][destination] = visited;
    }

    @Override
    public void pauseAndRedraw(String message, int milliseconds) {
        try {
            System.out.println(message);
            this.repaint();
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));

        final int ARROW_SIZE = 15;
        final double ARROW_ANGLE = Math.toRadians(20);

        for (int source = 0; source < graph.vertexCount(); source++) {
            for (int destination = 0; destination < graph.vertexCount(); destination++) {

                if (graph.isEdge(source, destination)) {
                    if (visitedEdges[source][destination]) {
                        g2.setColor(Color.BLUE);
                    } else {
                        g2.setColor(Color.DARK_GRAY);
                    }

                    int x1 = positions[source].x + NODE_RADIUS;
                    int y1 = positions[source].y + NODE_RADIUS;
                    int x2 = positions[destination].x + NODE_RADIUS;
                    int y2 = positions[destination].y + NODE_RADIUS;

                    double dx = x2 - x1, dy = y2 - y1;
                    double angle = Math.atan2(dy, dx);

                    int tipX = (int) (x2 - (NODE_RADIUS) * Math.cos(angle));
                    int tipY = (int) (y2 - (NODE_RADIUS) * Math.sin(angle));

                    g2.drawLine(x1, y1, tipX, tipY);

                    double angle1 = angle + Math.PI + ARROW_ANGLE;
                    double angle2 = angle + Math.PI - ARROW_ANGLE;
                    int x_wing1 = (int) (tipX + ARROW_SIZE * Math.cos(angle1));
                    int y_wing1 = (int) (tipY + ARROW_SIZE * Math.sin(angle1));
                    int x_wing2 = (int) (tipX + ARROW_SIZE * Math.cos(angle2));
                    int y_wing2 = (int) (tipY + ARROW_SIZE * Math.sin(angle2));

                    g2.drawLine(tipX, tipY, x_wing1, y_wing1);
                    g2.drawLine(tipX, tipY, x_wing2, y_wing2);
                }
            }
        }

        for (int v = 0; v < graph.vertexCount(); v++) {
            switch (graph.getMark(v)) {
                case GraphAlgorithms.GRAY:
                    g.setColor(Color.GRAY);
                    break;
                case GraphAlgorithms.BLACK:
                    g.setColor(Color.BLACK);
                    break;
                case GraphAlgorithms.WHITE:
                default:
                    g.setColor(Color.WHITE);
                    break;
            }
            g.fillOval(positions[v].x, positions[v].y, NODE_DIAMETER, NODE_DIAMETER);

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString(String.valueOf(v), positions[v].x + 10, positions[v].y + 20);
        }
    }
}
