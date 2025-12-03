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

    public static int runKruskal(IVisualizer visual) {
        if (isPaused) resumeAlgorithm();
        IGraph graph = visual.getGraph();

        resetForMST(visual, graph);
        visual.pauseAndRedraw("Iniciando Kruskal. Obteniendo todas las aristas...", 1000);

        List<EdgeContext> allEdges = new ArrayList<>();
        for (int i = 0; i < graph.vertexCount(); i++) {
            for (int j = i + 1; j < graph.vertexCount(); j++) {
                if (graph.isEdge(i, j)) {
                    allEdges.add(new EdgeContext(i, j, graph.weight(i, j)));
                }
            }
        }
        Collections.sort(allEdges);

        UnionFind uf = new UnionFind(graph.vertexCount());
        int mstWeight = 0;
        int edgesCount = 0;
        List<String> selectedEdges = new ArrayList<>();

        for (EdgeContext edge : allEdges) {
            visual.pauseAndRedraw("Analizando arista: " + edge, 500);
            checkPause();

            if (uf.union(edge.source, edge.dest)) {

                visual.markEdge(edge.source, edge.dest, true);
                graph.setMark(edge.source, BLACK);
                graph.setMark(edge.dest, BLACK);

                mstWeight += edge.weight;
                edgesCount++;
                selectedEdges.add(edge.toString());

                visual.pauseAndRedraw("Arista agregada (Peso acumulado: " + mstWeight + ")", 800);
            } else {
                visual.pauseAndRedraw("Arista descartada (crea ciclo): " + edge, 200);
            }
        }

        System.out.println("FINALIZADO");
        System.out.println("Peso Total: " + mstWeight);
        System.out.println("Aristas: " + selectedEdges);
        visual.pauseAndRedraw("Kruskal Terminado. Peso Total: " + mstWeight, 0);

        return mstWeight;

    }

    public static int runPrim(IVisualizer visual, int startNode) {
        if (isPaused) resumeAlgorithm();
        IGraph graph = visual.getGraph();

        resetForMST(visual, graph);
        visual.pauseAndRedraw("Iniciando Prim desde nodo: " + startNode, 1000);

        PriorityQueue<EdgeContext> pq = new PriorityQueue<>();

        boolean[] inMST = new boolean[graph.vertexCount()];

        int mstWeight = 0;
        List<String> selectedEdges = new ArrayList<>();

        inMST[startNode] = true;
        graph.setMark(startNode, BLACK);
        addEdgesToPQ(graph, startNode, pq, inMST);

        while (!pq.isEmpty()) {
            EdgeContext edge = pq.poll();

            if (inMST[edge.dest]) continue;

            inMST[edge.dest] = true;
            mstWeight += edge.weight;
            selectedEdges.add(edge.toString());

            graph.setMark(edge.dest, BLACK);
            visual.markEdge(edge.source, edge.dest, true);
            visual.pauseAndRedraw("Agregando nodo " + edge.dest + " vía arista peso " + edge.weight, 800);
            checkPause();

            addEdgesToPQ(graph, edge.dest, pq, inMST);
        }

        System.out.println("FINALIZADO");
        System.out.println("Peso Total: " + mstWeight);
        visual.pauseAndRedraw("Prim Terminado. Peso Total: " + mstWeight, 0);

        return mstWeight;
    }

    public static int runBoruvka(IVisualizer visual) {
        if (isPaused) resumeAlgorithm();
        IGraph graph = visual.getGraph();

        resetForMST(visual, graph);
        visual.pauseAndRedraw("Iniciando Boruvka...", 1000);

        int n = graph.vertexCount();
        UnionFind uf = new UnionFind(n);

        List<EdgeContext> allEdges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (graph.isEdge(i, j)) {
                    allEdges.add(new EdgeContext(i, j, graph.weight(i, j)));
                }
            }
        }

        int mstWeight = 0;
        int components = n;
        List<String> selectedEdges = new ArrayList<>();

        while (components > 1) {
            EdgeContext[] cheapest = new EdgeContext[n];

            for (EdgeContext e : allEdges) {
                int setU = uf.find(e.source);
                int setV = uf.find(e.dest);
                if (setU == setV) continue;

                if (cheapest[setU] == null || e.weight < cheapest[setU].weight) {
                    cheapest[setU] = e;
                }
                if (cheapest[setV] == null || e.weight < cheapest[setV].weight) {
                    cheapest[setV] = e;
                }
            }

            boolean anyMerged = false;

            for (int i = 0; i < n; i++) {
                EdgeContext e = cheapest[i];
                if (e == null) continue;
                visual.pauseAndRedraw("Mejor arista para comp. " + i + ": " + e, 400);
                checkPause();

                if (uf.union(e.source, e.dest)) {
                    anyMerged = true;
                    mstWeight += e.weight;
                    components--;
                    selectedEdges.add(e.toString());

                    graph.setMark(e.source, BLACK);
                    graph.setMark(e.dest, BLACK);
                    visual.markEdge(e.source, e.dest, true);

                    visual.pauseAndRedraw("Arista agregada (Peso acumulado: " + mstWeight + ")", 800);
                    checkPause();
                } else {
                    visual.pauseAndRedraw("Arista descartada (misma componente): " + e, 200);
                    checkPause();
                }
            }
            if (!anyMerged) {
                visual.pauseAndRedraw("No se pudieron unir más componentes. Grafo posiblemente desconectado.", 500);
                break;
            }
        }

        System.out.println("FINALIZADO BORUVKA");
        System.out.println("Peso Total: " + mstWeight);
        System.out.println("Aristas seleccionadas: " + selectedEdges);
        visual.pauseAndRedraw("Boruvka Terminado. Peso Total: " + mstWeight, 0);

        return mstWeight;
    }


    static class NodeDist implements Comparable<NodeDist> {
        int node;
        int dist;

        NodeDist(int node, int dist) {
            this.node = node;
            this.dist = dist;
        }

        @Override
        public int compareTo(NodeDist other) {
            return Integer.compare(this.dist, other.dist);
        }
    }

    public static int runDijkstra(IVisualizer visual, int startNode, int endNode) {
        if (isPaused) resumeAlgorithm();

        IGraph graph = visual.getGraph();
        int n = graph.vertexCount();

        visual.pauseAndRedraw(
                " Iniciando Dijkstra: calculando la ruta más corta desde el nodo " + startNode + " hasta el nodo " + endNode, 800);

        int[] dist = new int[n];
        int[] parent = new int[n];
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
            parent[i] = -1;
            visited[i] = false;
        }

        dist[startNode] = 0;

        PriorityQueue<NodeDist> pq = new PriorityQueue<>();
        pq.add(new NodeDist(startNode, 0));

        while (!pq.isEmpty()) {
            NodeDist current = pq.poll();
            int u = current.node;

            if (visited[u]) continue;
            visited[u] = true;

            graph.setMark(u, BLACK);
            visual.pauseAndRedraw(
                    " Procesando nodo " + u
                            + ". Distancia conocida hasta este nodo: " + dist[u], 500);
            checkPause();

            if (u == endNode) break;

            for (int v = graph.firstNeighbor(u); v < n; v = graph.nextNeighbor(u, v)) {

                int weight = graph.weight(u, v);
                if (weight < 0) continue;

                visual.pauseAndRedraw(
                        " Evaluando vecino " + v + " desde nodo " + u
                                + ". Peso de la arista: " + weight, 300);

                if (!visited[v] && dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    parent[v] = u;

                    pq.add(new NodeDist(v, dist[v]));

                    graph.setMark(v, GRAY);
                    visual.markEdge(u, v, true);
                    visual.pauseAndRedraw(
                            "Distancia mejorada a nodo " + v
                                    + " vía nodo " + u + ". Nueva distancia mínima: " + dist[v], 300);
                    checkPause();
                }
            }

            visual.pauseAndRedraw(
                    "Nodo " + u + " completamente procesado. Todos sus vecinos han sido evaluados.", 500);
        }

        visual.pauseAndRedraw(
                "Dijkstra completado. Distancia más corta desde nodo "
                        + startNode + " hasta nodo " + endNode + " = " + dist[endNode], 0);

        return dist[endNode];
    }


    public static int runBellmanFord(IVisualizer visual, int startNode, int endNode) {
        if (isPaused) resumeAlgorithm();

        IGraph graph = visual.getGraph();
        int n = graph.vertexCount();

        resetForMST(visual, graph);

        visual.pauseAndRedraw(
                "Iniciando Bellman-Ford: calculando distancias mínimas desde nodo "
                        + startNode + " hasta nodo " + endNode, 1000);

        int[] dist = new int[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[startNode] = 0;

        List<EdgeContext> edges = new ArrayList<>();
        for (int u = 0; u < n; u++) {
            for (int v = graph.firstNeighbor(u); v < n; v = graph.nextNeighbor(u, v)) {
                edges.add(new EdgeContext(u, v, graph.weight(u, v)));
            }
        }

        for (int i = 1; i < n; i++) {
            visual.pauseAndRedraw("Iteración " + i + " de relajación de todas las aristas...", 700);
            checkPause();

            boolean changed = false;

            for (EdgeContext e : edges) {
                int u = e.source;
                int v = e.dest;
                int w = e.weight;

                if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    parent[v] = u;
                    changed = true;

                    graph.setMark(u, GRAY);
                    graph.setMark(v, GRAY);
                    visual.markEdge(u, v, true);

                    visual.pauseAndRedraw(
                            "Arista " + u + " → " + v
                                    + " relajada. Nueva distancia mínima a nodo " + v + ": " + dist[v], 500);
                    checkPause();
                }
            }

            if (!changed) {
                visual.pauseAndRedraw(" No se actualizaron distancias en esta iteración. Terminando temprano.", 600);
                break;
            }
        }

        for (EdgeContext e : edges) {
            int u = e.source;
            int v = e.dest;
            int w = e.weight;

            if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v]) {
                visual.pauseAndRedraw("Ciclo negativo detectado en la arista " + u + " → " + v + ". No existe solución válida.", 1500);
                return Integer.MIN_VALUE;
            }
        }

        int node = endNode;
        while (node != -1) {
            graph.setMark(node, BLACK);
            node = parent[node];
        }

        visual.pauseAndRedraw(
                "Bellman-Ford completado. Distancia mínima desde nodo "
                        + startNode + " hasta nodo " + endNode + " = " + dist[endNode], 0);

        return dist[endNode];
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