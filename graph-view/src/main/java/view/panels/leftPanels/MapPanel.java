package view.panels.leftPanels;

import graphs.GraphM;
import interfaces.IGraph;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.text.Normalizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.InputStreamReader;

/**
 * Un JPanel personalizado que dibuja una imagen de fondo fija,
 * escalándola para que cubra el componente.
 */
public class MapPanel extends JPanel {
    private static final String IMAGE_PATH = "/img/mapa-estado-san-luis-potosi-low-detail.png";
    private static final String NODES_JSON_PATH = "/view/control/informacionMapa/nodos.json";
    private Image backgroundImage;

    private final IGraph graph;
    private final GraphPanel graphPanel;

    private static final class EdgeDef {
        final int s, d, w;

        EdgeDef(int s, int d, int w) {
            this.s = s;
            this.d = d;
            this.w = w;
        }
    }

    private static final class NodeInfo {
        int numero;
        String nombre;
        int x, y;
        List<EdgeDef> aristas = new ArrayList<>();
    }

    private List<NodeInfo> nodesLoaded = null;

    /**
     * Constructor que carga la imagen interna y el JSON de nodos.
     */
    public MapPanel() {
        setOpaque(true);
        setPreferredSize(new Dimension(800, 600));

        List<NodeInfo> nodes = loadNodes();
        this.nodesLoaded = nodes; // guardar para búsquedas por nombre

        int nodeCount;
        if (nodes != null && !nodes.isEmpty()) {
            int maxNum = -1;
            for (NodeInfo ni : nodes) if (ni != null) maxNum = Math.max(maxNum, ni.numero);
            nodeCount = Math.max(nodes.size(), maxNum + 1);
        } else {
            nodeCount = 24;
        }

        graph = new GraphM(nodeCount);

        if (nodes != null && !nodes.isEmpty()) {
            Point[] positions = new Point[nodeCount];

            for (NodeInfo ni : nodes) {
                if (ni.numero >= 0 && ni.numero < nodeCount) {
                    positions[ni.numero] = new Point(ni.x, ni.y);
                    for (EdgeDef e : ni.aristas) {
                        if (e.s >= 0 && e.s < nodeCount && e.d >= 0 && e.d < nodeCount) {
                            graph.setEdge(e.s, e.d, e.w);
                        } else {
                            System.err.println("Arista fuera de rango: " + e.s + "->" + e.d + " (peso=" + e.w + ")");
                        }
                    }
                } else {
                    System.err.println("Nodo fuera de rango: " + ni.numero);
                }
            }

            for (int i = 0; i < nodeCount; i++) if (positions[i] == null) positions[i] = new Point(100, 100);

            graphPanel = new GraphPanel(graph, positions);
        } else {
            System.out.println("Error: no se encontro el archivo de nodos.");
            graphPanel = new GraphPanel(graph);
        }

        URL imageUrl = getClass().getResource(IMAGE_PATH);
        if (imageUrl != null) {
            try {
                backgroundImage = ImageIO.read(imageUrl);
                if (backgroundImage == null) {
                    System.err.println("La imagen se cargó pero es null: " + IMAGE_PATH);
                } else {
                    // Si cargamos la imagen con éxito, ajustamos el preferredSize del graphPanel
                    Dimension imgDim = new Dimension(backgroundImage.getWidth(this), backgroundImage.getHeight(this));
                    graphPanel.setPreferredSize(imgDim);
                    // pasar el tamaño original del mapa al graphPanel para permitir escalado
                    if (graphPanel instanceof GraphPanel) {
                        ((GraphPanel) graphPanel).setMapOriginalSize(imgDim);
                    }
                    // También ajustar este panel para reflejar la imagen
                    setPreferredSize(imgDim);
                }
            } catch (IOException ex) {
                System.err.println("Error al cargar la imagen" + ex.getMessage());
            }
        } else {
            System.err.println("Imagen no encontrado en classpath: " + IMAGE_PATH);
        }

        setLayout(new BorderLayout());
        add(graphPanel, BorderLayout.CENTER);
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

    private List<NodeInfo> loadNodes() {
        InputStream in = getClass().getResourceAsStream(NODES_JSON_PATH);
        if (in == null) return null;
        try (InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().create();
            NodeJson[] arr = gson.fromJson(isr, NodeJson[].class);
            List<NodeInfo> nodes = new ArrayList<>();
            if (arr != null) {
                java.util.Map<Integer, Integer> numToIndex = new java.util.HashMap<>();
                for (int i = 0; i < arr.length; i++) {
                    numToIndex.put(arr[i].numeroNodo, i);
                }

                for (NodeJson nj : arr) {
                    NodeInfo ni = new NodeInfo();
                    ni.numero = numToIndex.get(nj.numeroNodo); // índice compacto
                    ni.nombre = nj.nombre;
                    ni.x = nj.x;
                    ni.y = nj.y;
                    if (nj.aristas != null) {
                        for (AristaJson aj : nj.aristas) {
                            Integer destMapped = numToIndex.get(aj.destino);
                            if (destMapped != null) {
                                ni.aristas.add(new EdgeDef(ni.numero, destMapped, aj.peso));
                            } else {
                                System.err.println("Arista con destino faltante " + aj.destino + " en nodo " + nj.numeroNodo);
                            }
                        }
                    }
                    nodes.add(ni);
                }

                StringBuilder dbg = new StringBuilder("Nodos cargados: ");
                for (NodeInfo ninfo : nodes) dbg.append(ninfo.numero).append("=").append(ninfo.nombre).append(", ");
                System.out.println(dbg);
            }
            return nodes;
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("No se pudo parsear nodos.json: " + e.getMessage());
            return null;
        }
    }

    private static final class NodeJson {
        String nombre;
        int numeroNodo;
        int x;
        int y;
        AristaJson[] aristas;
    }

    private static final class AristaJson {
        int destino;
        int peso;
    }

    public static final class NodeSummary {
        private final String nombre;
        private final int numeroNodo;
        private final List<String> conexiones;

        public NodeSummary(String nombre, int numeroNodo, java.util.List<String> conexiones) {
            this.nombre = nombre;
            this.numeroNodo = numeroNodo;
            this.conexiones = conexiones;
        }

        public String getNombre() { return nombre; }
        public int getNumeroNodo() { return numeroNodo; }
        public java.util.List<String> getConexiones() { return conexiones; }
    }

    /**
     * Devuelve un listado con nombre, número y nombres de ciudades conectadas para cada nodo.
     * Retorna null si no se cargaron nodos.
     */
    public java.util.List<NodeSummary> getNodeSummaries() {
        if (nodesLoaded == null || nodesLoaded.isEmpty()) return null;
        java.util.Map<Integer, String> numToName = new java.util.HashMap<>();
        for (NodeInfo ni : nodesLoaded) {
            numToName.put(ni.numero, ni.nombre);
        }

        java.util.List<NodeSummary> out = new ArrayList<>();
        for (NodeInfo ni : nodesLoaded) {
            java.util.List<String> conns = new ArrayList<>();
            for (EdgeDef e : ni.aristas) {
                String destName = numToName.get(e.d);
                if (destName != null) conns.add(destName + " (" + e.d + ")");
                else conns.add(String.valueOf(e.d));
            }
            out.add(new NodeSummary(ni.nombre, ni.numero, conns));
        }
        return out;
    }

    /**
     * Busca el índice de un nodo por nombre.
     * Devuelve -1 si no se encuentra.
     */
    public int findNodeByName(String nombre) {
        if (nombre == null || nodesLoaded == null) return -1;
        String q = normalize(nombre);
        if (q.isEmpty()) return -1;

        for (NodeInfo ni : nodesLoaded) {
            if (ni == null || ni.nombre == null) continue;
            if (normalize(ni.nombre).equals(q)) return ni.numero;
        }

        String[] qTokens = q.split("\\s+");
        for (NodeInfo ni : nodesLoaded) {
            if (ni == null || ni.nombre == null) continue;
            String normName = normalize(ni.nombre);
            boolean all = true;
            for (String t : qTokens) {
                if (!normName.contains(t)) {
                    all = false;
                    break;
                }
            }
            if (all) return ni.numero;
        }

        for (NodeInfo ni : nodesLoaded) {
            if (ni == null || ni.nombre == null) continue;
            if (normalize(ni.nombre).contains(q)) return ni.numero;
        }

        return -1;
    }

    private static String normalize(String s) {
        if (s == null) return "";
        String t = s.trim().toLowerCase();

        t = Normalizer.normalize(t, Normalizer.Form.NFD);
        t = t.replaceAll("\\p{M}", "");
        t = t.replaceAll("[^a-z0-9\\s]", " ");
        t = t.replaceAll("\\s+", " ").trim();
        return t;
    }
}
