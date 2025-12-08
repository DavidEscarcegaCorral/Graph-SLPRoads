package view.panels.leftPanels;

import algorithms.GraphAlgorithms;
import interfaces.IGraph;
import interfaces.IVisualizer;
import view.styles.Colors;
import view.styles.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayDeque;
import java.util.Queue;
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

    private boolean[] allowedNodes = null;

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

    private int[] centerX;
    private int[] centerY;
    private volatile boolean scaledDirty = true;

    private final Font nodeFont;
    private final BasicStroke edgeStroke = new BasicStroke(2.5f);
    private final BasicStroke nodeStroke = new BasicStroke(2.0f);
    private static final Color NODE_SHADOW = new Color(255, 255, 255, 200);
    private static final Color NODE_FILL_WHITE = Color.WHITE;
    private static final Color EDGE_VISITED = Color.BLUE;
    private static final Color EDGE_DEFAULT = Color.DARK_GRAY;


    public GraphPanel(IGraph g) {
        this(g, null);
    }


    public GraphPanel(IGraph g, Point[] positions) {
        setOpaque(false);
        this.graph = g;
        int nodeCount = graph.vertexCount();

        this.positions = new Point[nodeCount];
        this.visitedEdges = new boolean[nodeCount][nodeCount];

        setBackground(Color.white);
        setPreferredSize(new Dimension(775, 705));

        if (positions != null && positions.length >= nodeCount) {
            System.arraycopy(positions, 0, this.positions, 0, nodeCount);
        } else {
            for (int i = 0; i < nodeCount; i++) {
                if (i < NODE_LOCATIONS.length) {
                    this.positions[i] = NODE_LOCATIONS[i];
                } else {
                    this.positions[i] = new Point(100, 100);
                }
            }
        }

        this.nodeFont = FontUtil.loadFont(14, "Inter_Thin_Italic");

        this.centerX = new int[nodeCount];
        this.centerY = new int[nodeCount];

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                markScaledDirty();
            }
        });
    }

    private void markScaledDirty() {
        scaledDirty = true;
    }

    public void setMapOriginalSize(Dimension orig) {
        this.originalMapSize = orig;
        markScaledDirty();
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
        this.allowedNodes = null;
        markScaledDirty();
        repaint();
    }

    @Override
    public void markEdge(int source, int destination, boolean visited) {
        if (source < 0 || destination < 0) return;
        if (source >= visitedEdges.length || destination >= visitedEdges.length) return;
        this.visitedEdges[source][destination] = visited;
        if (graph.isEdge(destination, source)) {
            this.visitedEdges[destination][source] = visited;
        }
    }

    public synchronized void setAllowedComponent(int startNode) {
        int n = graph.vertexCount();
        if (startNode < 0 || startNode >= n) {
            this.allowedNodes = null;
            repaint();
            return;
        }

        boolean[] seen = new boolean[n];
        Queue<Integer> q = new ArrayDeque<>();
        seen[startNode] = true;
        q.add(startNode);

        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v = graph.firstNeighbor(u); v < graph.vertexCount(); v = graph.nextNeighbor(u, v)) {
                if (!seen[v]) {
                    seen[v] = true;
                    q.add(v);
                }
            }
        }

        this.allowedNodes = seen;
        markScaledDirty();
        repaint();
    }

    public synchronized void clearAllowedComponent() {
        this.allowedNodes = null;
        repaint();
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

        if (scaledDirty) {
            recomputeScaledPositions();
        }

        int n = graph.vertexCount();
        int[] cx = this.centerX;
        int[] cy = this.centerY;
        boolean[][] localVisited = this.visitedEdges;
        boolean[] allowed = this.allowedNodes;

        g2.setStroke(edgeStroke);
        for (int source = 0; source < n; source++) {
            for (int dest = 0; dest < n; dest++) {
                if (graph.isEdge(source, dest)) {
                    if (source <= dest || !graph.isEdge(dest, source)) {
                        boolean bothAllowed = (allowed == null) || (allowed[source] && allowed[dest]);
                        boolean markVisited = localVisited[source][dest] && bothAllowed;
                        g2.setColor(markVisited ? EDGE_VISITED : EDGE_DEFAULT);
                        g2.drawLine(cx[source], cy[source], cx[dest], cy[dest]);
                    }
                }
            }
        }

        g2.setFont(nodeFont);
        g2.setStroke(nodeStroke);

        for (int v = 0; v < n; v++) {
            boolean nodeAllowed = (allowed == null) || allowed[v];

            Color fill = NODE_FILL_WHITE;
            Color borderColor = Colors.COLOR_BUTTON.darker();
            Color textColor = Colors.COLOR_BUTTON.darker();

            if (nodeAllowed) {
                int mark = graph.getMark(v);
                if (mark == GraphAlgorithms.BLACK) {
                    fill = Colors.COLOR_BUTTON;
                    borderColor = Colors.COLOR_BUTTON.darker();
                    textColor = Color.WHITE;
                } else if (mark == GraphAlgorithms.GRAY) {
                    fill = Color.LIGHT_GRAY;
                    borderColor = Colors.COLOR_BUTTON.darker();
                    textColor = Colors.COLOR_BUTTON.darker();
                }
            }

            int x = cx[v] - NODE_RADIUS;
            int y = cy[v] - NODE_RADIUS;

            g2.setColor(NODE_SHADOW);
            g2.fillOval(x - 3, y - 3, NODE_DIAMETER + 6, NODE_DIAMETER + 6);

            g2.setColor(fill);
            g2.fillOval(x, y, NODE_DIAMETER, NODE_DIAMETER);

            g2.setColor(borderColor);
            g2.drawOval(x, y, NODE_DIAMETER, NODE_DIAMETER);

            String text = String.valueOf(v);
            FontMetrics fm = g2.getFontMetrics();
            int textX = x + (NODE_DIAMETER - fm.stringWidth(text)) / 2;
            int textY = y + (NODE_DIAMETER + fm.getAscent()) / 2 - 2;

            g2.setColor(textColor);
            g2.drawString(text, textX, textY);
        }
    }

    private synchronized void recomputeScaledPositions() {
        int n = graph.vertexCount();
        if (centerX == null || centerX.length != n) {
            centerX = new int[n];
            centerY = new int[n];
        }

        double sx = 1.0, sy = 1.0;
        if (originalMapSize != null && originalMapSize.width > 0 && originalMapSize.height > 0) {
            sx = (double) getWidth() / originalMapSize.width;
            sy = (double) getHeight() / originalMapSize.height;
        }

        for (int i = 0; i < n; i++) {
            centerX[i] = (int) Math.round((positions[i].x + NODE_RADIUS) * sx);
            centerY[i] = (int) Math.round((positions[i].y + NODE_RADIUS) * sy);
        }

        scaledDirty = false;
    }
}
