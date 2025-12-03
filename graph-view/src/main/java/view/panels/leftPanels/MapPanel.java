package view.panels.leftPanels;

import graphs.GraphM;
import interfaces.IGraph;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Un JPanel personalizado que dibuja una imagen de fondo fija,
 * escalándola para que cubra el componente.
 */
public class MapPanel extends JPanel {
    private static final String IMAGE_PATH = "/img/mapa-estado-san-luis-potosi-low-detail.png";
    private Image backgroundImage;

    private final IGraph graph;
    private final GraphPanel graphPanel;

    // Lista estática y estructurada de aristas del grafo
    private static final EdgeDef[] EDGES = new EdgeDef[] {
            new EdgeDef(0, 1, 192), new EdgeDef(1, 0, 192),
            new EdgeDef(0, 19, 192), new EdgeDef(19, 0, 192),
            new EdgeDef(0, 22, 192), new EdgeDef(22, 0, 192),
            new EdgeDef(0, 4, 77), new EdgeDef(4, 0, 77),
            new EdgeDef(22, 11, 77), new EdgeDef(11, 22, 77),
            new EdgeDef(11, 12, 77), new EdgeDef(12, 11, 77),
            new EdgeDef(11, 1, 77), new EdgeDef(1, 11, 77),
            new EdgeDef(1, 10, 77), new EdgeDef(10, 1, 77),
            new EdgeDef(10, 21, 77), new EdgeDef(21, 10, 77),
            new EdgeDef(21, 3, 77), new EdgeDef(3, 21, 77),
            new EdgeDef(2, 8, 77), new EdgeDef(8, 2, 77),
            new EdgeDef(1, 15, 8), new EdgeDef(15, 1, 8),
            new EdgeDef(3, 2, 103), new EdgeDef(2, 3, 103),
            new EdgeDef(6, 7, 97), new EdgeDef(7, 6, 97),
            new EdgeDef(8, 5, 55), new EdgeDef(5, 8, 55),
            new EdgeDef(8, 18, 57), new EdgeDef(18, 8, 57),
            new EdgeDef(9, 5, 8), new EdgeDef(5, 9, 8),
            new EdgeDef(5, 10, 8), new EdgeDef(10, 5, 8),
            new EdgeDef(11, 12, 8), new EdgeDef(12, 11, 8),
            new EdgeDef(13, 14, 8), new EdgeDef(14, 13, 8),
            new EdgeDef(16, 3, 8), new EdgeDef(3, 16, 8),
            new EdgeDef(16, 17, 8), new EdgeDef(17, 16, 8),
            new EdgeDef(19, 20, 8), new EdgeDef(20, 19, 8),
            new EdgeDef(21, 22, 8), new EdgeDef(22, 21, 8),
            new EdgeDef(8, 23, 8), new EdgeDef(23, 8, 8),
            new EdgeDef(23, 6, 8), new EdgeDef(6, 23, 8),
            new EdgeDef(23, 7, 8), new EdgeDef(7, 23, 8),
            new EdgeDef(13, 6, 8), new EdgeDef(6, 13, 8)
    };

    private static final class EdgeDef {
        final int s, d, w;
        EdgeDef(int s, int d, int w) { this.s = s; this.d = d; this.w = w; }
    }

    /**
     * Constructor que carga la imagen interna.
     */
    public MapPanel() {
        setOpaque(true);
        setPreferredSize(new Dimension(800, 600));

        graph = new GraphM(24);

        // Aplicar la lista estructurada de aristas
        for (EdgeDef e : EDGES) {
            if (e == null) continue;
            if (e.s >= 0 && e.s < graph.vertexCount() && e.d >= 0 && e.d < graph.vertexCount()) {
                graph.setEdge(e.s, e.d, e.w);
            }
        }

        graphPanel = new GraphPanel(graph);

        // Cargar la imagen robustamente comprobando que el recurso existe
        URL imageUrl = getClass().getResource(IMAGE_PATH);
        if (imageUrl != null) {
            try {
                backgroundImage = ImageIO.read(imageUrl);
                if (backgroundImage == null) {
                    System.err.println("La imagen se cargó pero es null: " + IMAGE_PATH);
                }
            } catch (IOException ex) {
                System.err.println("Error de E/S al cargar la imagen: " + ex.getMessage());
            }
        } else {
            System.err.println("Recurso de imagen no encontrado en classpath: " + IMAGE_PATH);
        }

        add(graphPanel);
    }

    public IGraph getGraph() {
        return graph;
    }

    public GraphPanel getGraphPanel() {
        return graphPanel;
    }

    /**
     * Sobrescribe el metodo paintComponent para dibujar la imagen de fondo.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            int targetWidth = getWidth();
            int targetHeight = getHeight();

            Image scaledImage = backgroundImage.getScaledInstance(
                    targetWidth,
                    targetHeight,
                    Image.SCALE_SMOOTH
            );

            g.drawImage(scaledImage, 0, 0, this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (backgroundImage != null) {
            return new Dimension(backgroundImage.getWidth(this), backgroundImage.getHeight(this));
        } else {
            return new Dimension(400, 300);
        }
    }
}
