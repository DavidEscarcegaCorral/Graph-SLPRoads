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

public class MapPanel extends JPanel {
    private static final String IMAGE_PATH = "/img/mapa-estado-san-luis-potosi-low-detail.png";
    private static final String NODES_JSON_PATH = "/view/control/informacionMapa/nodos.json";
    private Image backgroundImage;

    private Dimension mapOriginalSize = null;
    private double mapAspect = 0.0;

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

    public MapPanel() {
        setOpaque(true);
        setPreferredSize(new Dimension(800, 600));

        List<NodeInfo> nodes = loadNodes();
        this.nodesLoaded = nodes;

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
                Dimension imgDim = new Dimension(backgroundImage.getWidth(this), backgroundImage.getHeight(this));
                graphPanel.setPreferredSize(imgDim);
                graphPanel.setMapOriginalSize(imgDim);
                setPreferredSize(imgDim);

                mapOriginalSize = imgDim;
                mapAspect = mapOriginalSize.getWidth() / (double) mapOriginalSize.getHeight();
            } catch (IOException ex) {
                System.err.println("Error al cargar la imagen" + ex.getMessage());
            }
        }

        setLayout(new BorderLayout());
        add(graphPanel, BorderLayout.CENTER);
    }

    public GraphPanel getGraphPanel() {
        return graphPanel;
    }

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
        if (mapOriginalSize != null) {
            return new Dimension(mapOriginalSize);
        }
        if (backgroundImage != null) {
            return new Dimension(backgroundImage.getWidth(this), backgroundImage.getHeight(this));
        } else {
            return new Dimension(400, 300);
        }
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        if (mapOriginalSize != null && mapOriginalSize.height > 0) {
            double aspect = mapAspect;
            int adjW = width;
            int adjH = height;
            if (width / (double) height > aspect) {
                adjW = (int) Math.max(1, Math.round(height * aspect));
            } else {
                adjH = (int) Math.max(1, Math.round(width / aspect));
            }
            int dx = (width - adjW) / 2;
            int dy = (height - adjH) / 2;
            super.setBounds(x + dx, y + dy, adjW, adjH);
        } else {
            super.setBounds(x, y, width, height);
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
                    ni.numero = numToIndex.get(nj.numeroNodo);
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
