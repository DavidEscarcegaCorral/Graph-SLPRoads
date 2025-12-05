package view.panels.leftPanels;

import algorithms.GraphAlgorithms;
import interfaces.IGraph;
import interfaces.IVisualizer;
import view.styles.Colors;
import view.styles.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;

/**
 * Panel que representa visualmente el estado del grafo.
 * Implementa IVisualizer para comunicarse con la lógica.
 */
public class GraphPanel extends JPanel implements IVisualizer {
    private final IGraph graph;
    private final Point[] positions;
    private boolean[][] visitedEdges;
    private Dimension originalMapSize = null;

    private static final int NODE_DIAMETER = 30;
    private static final int NODE_RADIUS = NODE_DIAMETER / 2;

    private static final Point[] NODE_LOCATIONS = {
            new Point(254, 472), // Nodo 0
            new Point(316, 174),  // Nodo 1
            new Point(443, 530), // Nodo 2
            new Point(378, 442), // Nodo 3
            new Point(182, 515), // Nodo 4
            new Point(528, 418), // Nodo 5
            new Point(664, 496), // Nodo 6
            new Point(634, 612), // Nodo 7
            new Point(517, 545),  // Nodo 8
            new Point(612, 392), // Nodo 9
            new Point(368, 311),   // Nodo 10
            new Point(224, 251),  // Nodo 11
            new Point(131, 241),  // Nodo 12
            new Point(716, 466), // Nodo 13
            new Point(739, 422), // Nodo 14
            new Point(268, 158), // Nodo 15
            new Point(439, 459), // Nodo 16
            new Point(388, 388), // Nodo 17
            new Point(513, 612), // Nodo 18
            new Point(302, 491), // Nodo 19
            new Point(272, 515), // Nodo 20
            new Point(307, 401), // Nodo 21
            new Point(239, 358), // Nodo 22
            new Point(597, 510)  // Nodo 23
    };

    /**
     * Constructor del panel.
     *
     * @param g El grafo a visualizar.
     */
    public GraphPanel(IGraph g) {
        this(g, null);
    }

    /**
     * Nuevo constructor que recibe las posiciones de los nodos (en coordenadas del mapa).
     */
    public GraphPanel(IGraph g, Point[] positions) {
        setOpaque(false);
        this.graph = g;
        int nodeCount = graph.vertexCount();

        this.positions = new Point[nodeCount];
        this.visitedEdges = new boolean[nodeCount][nodeCount];

        setBackground(Color.white);
        setPreferredSize(new Dimension(775, 705));

        if (positions != null && positions.length >= nodeCount) {
            for (int i = 0; i < nodeCount; i++) this.positions[i] = positions[i];
        } else {
            for (int i = 0; i < nodeCount; i++) {
                if (i < NODE_LOCATIONS.length) {
                    this.positions[i] = NODE_LOCATIONS[i];
                } else {
                    this.positions[i] = new Point(100, 100);
                }
            }
        }
    }

    public void setMapOriginalSize(Dimension orig) {
        this.originalMapSize = orig;
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
        int count = graph.vertexCount();
        this.visitedEdges = new boolean[count][count];
        repaint();
    }

    @Override
    public void markEdge(int source, int destination, boolean visited) {
        this.visitedEdges[source][destination] = visited;
        if (source >= 0 && destination >= 0 && source < visitedEdges.length && destination < visitedEdges.length) {
            if (graph.isEdge(destination, source)) {
                this.visitedEdges[destination][source] = visited;
            }
        }
    }

    @Override
    public void pauseAndRedraw(String message, int milliseconds) {
        if (SwingUtilities.isEventDispatchThread()) {
            if (message != null) System.out.println(message);
            this.repaint();
            try { Thread.sleep(Math.max(0, milliseconds)); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
            return;
        }

        final CountDownLatch latch = new CountDownLatch(1);

        SwingUtilities.invokeLater(() -> {
            if (message != null) System.out.println(message);
            this.repaint();

            if (milliseconds <= 0) {
                latch.countDown();
            } else {
                Timer t = new Timer(milliseconds, ev -> {
                    ((Timer) ev.getSource()).stop();
                    latch.countDown();
                });
                t.setRepeats(false);
                t.start();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2.5f));

        double sx = 1.0, sy = 1.0;
        if (originalMapSize != null && originalMapSize.width > 0 && originalMapSize.height > 0) {
            sx = (double) getWidth() / originalMapSize.width;
            sy = (double) getHeight() / originalMapSize.height;
        }

        for (int source = 0; source < graph.vertexCount(); source++) {
            for (int dest = 0; dest < graph.vertexCount(); dest++) {
                if (graph.isEdge(source, dest)) {
                    if (source <= dest || !graph.isEdge(dest, source)) {
                        drawSimpleEdge(g2, source, dest, sx, sy);
                    }
                }
            }
        }

        for (int v = 0; v < graph.vertexCount(); v++) {
            drawNode(g2, v, sx, sy);
        }
    }

    private void drawSimpleEdge(Graphics2D g2, int source, int dest, double sx, double sy) {
        if (visitedEdges[source][dest]) {
            g2.setColor(Color.BLUE);
        } else {
            g2.setColor(Color.DARK_GRAY);
        }

        int x1 = (int) Math.round((positions[source].x + NODE_RADIUS) * sx);
        int y1 = (int) Math.round((positions[source].y + NODE_RADIUS) * sy);
        int x2 = (int) Math.round((positions[dest].x + NODE_RADIUS) * sx);
        int y2 = (int) Math.round((positions[dest].y + NODE_RADIUS) * sy);

        g2.drawLine(x1, y1, x2, y2);
    }

    private void drawNode(Graphics2D g2, int v, double sx, double sy) {
        if (graph.getMark(v) == GraphAlgorithms.BLACK) {
            g2.setColor(Colors.COLOR_BUTTON);
        } else if (graph.getMark(v) == GraphAlgorithms.GRAY) {
            g2.setColor(Color.GRAY);
        } else {
            g2.setColor(new Color(255, 255, 255, 230));
        }

        int x = (int) Math.round(positions[v].x * sx);
        int y = (int) Math.round(positions[v].y * sy);

        g2.setColor(new Color(255, 255, 255, 200));
        g2.fillOval(x - 3, y - 3, NODE_DIAMETER + 6, NODE_DIAMETER + 6);

        if (graph.getMark(v) == GraphAlgorithms.BLACK) {
            g2.setColor(Colors.COLOR_BUTTON);
        } else if (graph.getMark(v) == GraphAlgorithms.GRAY) {
            g2.setColor(Color.LIGHT_GRAY);
        } else {
            g2.setColor(Color.WHITE);
        }
        g2.fillOval(x, y, NODE_DIAMETER, NODE_DIAMETER);

        g2.setColor(Colors.COLOR_BUTTON.darker());
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawOval(x, y, NODE_DIAMETER, NODE_DIAMETER);

        if (graph.getMark(v) == GraphAlgorithms.BLACK) {
            g2.setColor(Color.WHITE);
        } else {
            g2.setColor(Colors.COLOR_BUTTON.darker());
        }

        g2.setFont(FontUtil.loadFont( 14, "Inter_Thin_Italic"));

        String text = String.valueOf(v);
        FontMetrics fm = g2.getFontMetrics();

        int textX = x + (NODE_DIAMETER - fm.stringWidth(text)) / 2;
        int textY = y + (NODE_DIAMETER + fm.getAscent()) / 2 - 2;

        g2.drawString(text, textX, textY);
    }
}
