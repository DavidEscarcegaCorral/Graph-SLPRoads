package view.panels.leftPanels;

import graphs.GraphM;
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
    private static final String IMAGE_PATH = "/img/mapa-estado-san-luis-potosi-low-detail.png";
    private Image backgroundImage;

    private IGraph graph;
    private GraphPanel graphPanel;

    /**
     * Constructor que carga la imagen interna.
     */
    public MapPanel() {
        setOpaque(true);
        setPreferredSize(new Dimension(800, 600));

        graph = new GraphM(23);
        graph.setEdge(0, 1, 192);
        graph.setEdge(1, 0, 192);

        graph.setEdge(0, 4, 77);
        graph.setEdge(4, 0, 77);

        graph.setEdge(1, 15, 8);
        graph.setEdge(15, 1, 8);

        graph.setEdge(3, 2, 103);
        graph.setEdge(2, 3, 103);

        graph.setEdge(6, 7, 97);
        graph.setEdge(7, 6, 97);

        graph.setEdge(8, 5, 55);
        graph.setEdge(5, 8, 55);

        graph.setEdge(8, 18, 57);
        graph.setEdge(18, 8, 57);

        graph.setEdge(9, 10, 8);
        graph.setEdge(10, 9, 8);

        graph.setEdge(11, 12, 8);
        graph.setEdge(12, 11, 8);

        graph.setEdge(13, 14, 8);
        graph.setEdge(14, 13, 8);

        graph.setEdge(16, 3, 8);
        graph.setEdge(3, 16, 8);

        graph.setEdge(16, 17, 8);
        graph.setEdge(17, 16, 8);

        graph.setEdge(19, 20, 8);
        graph.setEdge(20, 19, 8);

        graph.setEdge(21, 22, 8);
        graph.setEdge(22, 21, 8);



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
