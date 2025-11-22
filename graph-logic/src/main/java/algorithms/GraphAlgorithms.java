package algorithms;

import algorithms.mst.EdgeContext;
import algorithms.mst.UnionFind;
import interfaces.IGraph;
import interfaces.IVisualizer;

import java.util.*;

/**
 * Clase pública de utilidad que contiene los algoritmos de recorrido.
 * Solo opera sobre las interfaces IGraph e IVisualizer.
 */
public class GraphAlgorithms {

    public static final int WHITE = 0;
    public static final int GRAY = 1;
    public static final int BLACK = 2;

    private static volatile boolean isPaused = false;
    private static final Object lock = new Object();

    /**
     * Revisa si el algoritmo está actualmente en pausa.
     */
    public static boolean isPaused() {
        return isPaused;
    }

    /**
     * Activa la bandera de pausa.
     */
    public static void pauseAlgorithm() {
        isPaused = true;
    }

    /**
     * Desactiva la bandera de pausa y activa al hilo en espera.
     */
    public static void resumeAlgorithm() {
        isPaused = false;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    /**
     * Revisa si el algoritmo debe pausarse.
     */
    private static void checkPause() {
        try {
            synchronized (lock) {
                while (isPaused) {
                    System.out.println("Algoritmo pausado.");
                    lock.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            isPaused = false;
        }
    }

    /**
     * Inicia un recorrido DFS desde un nodo específico.
     *
     * @param visual    El objeto de la GUI que implementa IVisualizer.
     * @param startNode El vértice donde comenzará el recorrido.
     */
    public static void runDFSFromNode(IVisualizer visual, int startNode) {
        if (isPaused) resumeAlgorithm();

        IGraph graph = visual.getGraph();
        int vertex;

        for (vertex = 0; vertex < graph.vertexCount(); vertex++) {
            graph.setMark(vertex, WHITE);
        }
        visual.resetVisuals();

        visual.pauseAndRedraw("Estado inicial. Iniciando en " + startNode, 1000);

        if (graph.getMark(startNode) == WHITE) {
            DFS(visual, startNode);
        }
        System.out.println("Recorrido desde " + startNode + " completado.");
    }

    /**
     * Metodo recursivo para el algoritmo DFS.
     *
     * @param visual El objeto de la GUI que implementa IVisualizer.
     * @param vertex El vértice actual que está siendo explorado.
     */
    private static void DFS(IVisualizer visual, int vertex) {
        IGraph graph = visual.getGraph();

        graph.setMark(vertex, GRAY);
        visual.pauseAndRedraw("Descubriendo (GRIS): " + vertex, 800);
        checkPause();

        for (int neighbor = graph.firstNeighbor(vertex); neighbor < graph.vertexCount(); neighbor = graph.nextNeighbor(vertex, neighbor)) {
            if (graph.getMark(neighbor) == WHITE) {
                visual.markEdge(vertex, neighbor, true);
                visual.pauseAndRedraw("Arista (AZUL): " + vertex + " -> " + neighbor, 500);
                checkPause();

                DFS(visual, neighbor);
            }
        }

        graph.setMark(vertex, BLACK);
        visual.pauseAndRedraw("Terminando (NEGRO): " + vertex, 800);
        checkPause();
    }

    public static void runBFSFromNode(IVisualizer visual, int startNode) {
        if (isPaused) resumeAlgorithm();

        IGraph graph = visual.getGraph();

        for (int v = 0; v < graph.vertexCount(); v++) {
            graph.setMark(v, WHITE);
        }
        visual.resetVisuals();
        visual.pauseAndRedraw("Estado inicial (BFS). Inicio: " + startNode, 1000);

        BFS(visual, startNode);

        System.out.println("BFS desde " + startNode + " completado.");
    }

    private static void BFS(IVisualizer visual, int startNode) {
        IGraph graph = visual.getGraph();

        Queue<Integer> q = new LinkedList<>();

        q.add(startNode);
        graph.setMark(startNode, GRAY);
        visual.pauseAndRedraw("Encolando inicio: " + startNode, 800);
        checkPause();

        while (!q.isEmpty()) {
            int v = q.poll();

            for (int w = graph.firstNeighbor(v); w < graph.vertexCount(); w = graph.nextNeighbor(v, w)) {

                if (graph.getMark(w) == WHITE) {

                    graph.setMark(w, GRAY);
                    visual.markEdge(v, w, true);
                    visual.pauseAndRedraw("Visitando vecino: " + w + " (por " + v + ")", 500);
                    checkPause();

                    q.add(w);
                }
            }

            graph.setMark(v, BLACK);
            visual.pauseAndRedraw("Nodo procesado completamente: " + v, 500);
            checkPause();
        }
    }

    public static void runKruskal(IVisualizer visual) {
        if (isPaused) resumeAlgorithm();
        IGraph graph = visual.getGraph();

        resetForMST(visual, graph);
        visual.pauseAndRedraw("Iniciando Kruskal. Obteniendo todas las aristas...", 1000);

        //Obtener todas las aristas y ordenarlas
        List<EdgeContext> allEdges = new ArrayList<>();
        for (int i = 0; i < graph.vertexCount(); i++) {
            for (int j = i + 1; j < graph.vertexCount(); j++) {
                if (graph.isEdge(i, j)) {
                    allEdges.add(new EdgeContext(i, j, graph.weight(i, j)));
                }
            }
        }
        Collections.sort(allEdges);

        // Inicializar Union-Find
        UnionFind uf = new UnionFind(graph.vertexCount());
        int mstWeight = 0;
        int edgesCount = 0;
        List<String> selectedEdges = new ArrayList<>();

        // Procesar aristas
        for (EdgeContext edge : allEdges) {
            visual.pauseAndRedraw("Analizando arista: " + edge, 500);
            checkPause();

            // Intentar unir los conjuntos
            if (uf.union(edge.source, edge.dest)) {

                // Si se unen (no crean ciclo) se añaden
                visual.markEdge(edge.source, edge.dest, true);
                graph.setMark(edge.source, BLACK);
                graph.setMark(edge.dest, BLACK);

                mstWeight += edge.weight;
                edgesCount++;
                selectedEdges.add(edge.toString());

                visual.pauseAndRedraw("¡Arista seleccionada! (Peso acumulado: " + mstWeight + ")", 800);
            } else {
                visual.pauseAndRedraw("Arista descartada (crea ciclo): " + edge, 200);
            }
        }

        System.out.println("=== KRUSKAL FINALIZADO ===");
        System.out.println("Peso Total: " + mstWeight);
        System.out.println("Aristas: " + selectedEdges);
        visual.pauseAndRedraw("Kruskal Terminado. Peso Total: " + mstWeight, 0);

    }

    public static void runPrim(IVisualizer visual, int startNode) {
        if (isPaused) resumeAlgorithm();
        IGraph graph = visual.getGraph();

        resetForMST(visual, graph);
        visual.pauseAndRedraw("Iniciando Prim desde nodo: " + startNode, 1000);

        // Elegir la arista menos costosa
        PriorityQueue<EdgeContext> pq = new PriorityQueue<>();

        // Arreglo para saber quien esta en el mst
        boolean[] inMST = new boolean[graph.vertexCount()];

        int mstWeight = 0;
        List<String> selectedEdges = new ArrayList<>();

        // Agregar nodo inicial
        inMST[startNode] = true;
        graph.setMark(startNode, BLACK);
        addEdgesToPQ(graph, startNode, pq, inMST);

        while (!pq.isEmpty()) {
            EdgeContext edge = pq.poll();

            if (inMST[edge.dest]) continue;

            inMST[edge.dest] = true;
            mstWeight += edge.weight;
            selectedEdges.add(edge.toString());

            // Visualización
            graph.setMark(edge.dest, BLACK);
            visual.markEdge(edge.source, edge.dest, true);
            visual.pauseAndRedraw("Agregando nodo " + edge.dest + " vía arista peso " + edge.weight, 800);
            checkPause();

            // Añadir vecinos del nuevo nodo
            addEdgesToPQ(graph, edge.dest, pq, inMST);
        }

        System.out.println("=== PRIM FINALIZADO ===");
        System.out.println("Peso Total: " + mstWeight);
        visual.pauseAndRedraw("Prim Terminado. Peso Total: " + mstWeight, 0);
    }

    private static void addEdgesToPQ(IGraph graph, int u, PriorityQueue<EdgeContext> pq, boolean[] inMST) {
        for (int v = graph.firstNeighbor(u); v < graph.vertexCount(); v = graph.nextNeighbor(u, v)) {
            if (!inMST[v]) {
                pq.add(new EdgeContext(u, v, graph.weight(u, v)));
            }
        }
    }

    private static void resetForMST(IVisualizer visual, IGraph graph) {
        for (int v = 0; v < graph.vertexCount(); v++) {
            graph.setMark(v, WHITE);
        }
        visual.resetVisuals();
    }

}