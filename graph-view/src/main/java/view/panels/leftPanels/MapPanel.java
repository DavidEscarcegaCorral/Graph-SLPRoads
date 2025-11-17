package view.panels.leftPanels;

import graphs.Graphm;
import interfaces.IGraph;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Un JPanel personalizado que dibuja una imagen de fondo fija,
 * escalándola para que cubra el componente.
 */
public class MapPanel extends JPanel {
    private static final String IMAGE_PATH = "/img/SLP_Img.jpg";
    private Image backgroundImage;

    private IGraph graph;
    private GraphPanel graphPanel;

    /**
     * Constructor que carga la imagen interna.
     */
    public MapPanel() {
        setOpaque(true);
        graph = new Graphm(5);
        graph.setEdge(0, 1, 10);
        graph.setEdge(0, 2, 10);
        graph.setEdge(1, 3, 10);
        graph.setEdge(2, 4, 10);
        graph.setEdge(4, 1, 10);
        graph.setEdge(3, 0, 10);

        graphPanel = new GraphPanel(graph);

        try {
            backgroundImage = ImageIO.read(getClass().getResource(IMAGE_PATH));

            if (backgroundImage == null) {
                System.err.println("Error: No se pudo cargar la imagen desde la ruta: " + IMAGE_PATH);
                System.err.println("Verifica que el archivo exista en la carpeta 'resources' y que la ruta sea correcta.");
            }
        } catch (IOException e) {
            System.err.println("Error de E/S al cargar la imagen: " + e.getMessage());
            e.printStackTrace();
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
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
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
