package algorithms;

import algorithms.mst.EdgeContext;
import algorithms.mst.UnionFind;
import interfaces.IGraph;
import interfaces.IVisualizer;

import java.util.*;

/**
 * Utilidades estáticas para ejecutar y visualizar algoritmos de grafos.
 * <p>
 * Alcance y dependencias:
 * - Opera exclusivamente sobre las interfaces {@link interfaces.IGraph} e {@link interfaces.IVisualizer}.
 * - La comunicación con la capa de vista se realiza mediante {@link IVisualizer#pauseAndRedraw(String, int)}
 * y métodos de marcado de nodos/aristas.
 * <p>
 * Control de ejecución:
 * - Soporta pausa/reanudación con un candado interno; las actualizaciones de UI deben ocurrir en la EDT.
 * - Los métodos no gestionan hilos por sí mismos; se espera que la capa de control los ejecute en un hilo de fondo.
 * <p>
 * Algoritmos implementados:
 * - DFS y BFS de recorrido.
 * - MST: Kruskal y Prim.
 * - Camino más corto: Dijkstra (parcial: sin resaltado final del camino en este momento).
 */
public class GraphAlgorithms {

    public static final int WHITE = 0;
    public static final int GRAY = 1;
    public static final int BLACK = 2;

    private static volatile boolean isPaused = false;
    private static final Object lock = new Object();

    /**
     * Indica si el algoritmo está actualmente en pausa.
     *
     * @return true si se ha solicitado pausa; false en caso contrario
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
     * Punto de cooperación para la pausa de los algoritmos.
     * Si la bandera interna está en pausa, espera en un monitor hasta reanudación.
     * Debe llamarse periódicamente dentro de los bucles de los algoritmos.
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
     * Prepara marcas y visuales y delega la recursión en {@link #DFS(IVisualizer, int)}.
     *
     * @param visual    vista que implementa {@link IVisualizer}
     * @param startNode nodo de inicio (0..V-1)
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
        visual.pauseAndRedraw("Descubriendo: " + vertex, 800);
        checkPause();

        for (int neighbor = graph.firstNeighbor(vertex); neighbor < graph.vertexCount(); neighbor = graph.nextNeighbor(vertex, neighbor)) {
            if (graph.getMark(neighbor) == WHITE) {
                visual.markEdge(vertex, neighbor, true);
                visual.pauseAndRedraw("Arista: " + vertex + " -> " + neighbor, 500);
                checkPause();

                DFS(visual, neighbor);
            }
        }

        graph.setMark(vertex, BLACK);
        visual.pauseAndRedraw("Terminando: " + vertex, 800);
        checkPause();
    }

    /**
     * Inicia un recorrido BFS desde un nodo específico.
     * Prepara marcas y visuales y delega la iteración en {@link #BFS(IVisualizer, int)}.
     *
     * @param visual    vista que implementa {@link IVisualizer}
     * @param startNode nodo de inicio (0..V-1)
     */
    public static void runBFSFromNode(IVisualizer visual, int startNode) {
        if (isPaused) resumeAlgorithm();

        IGraph graph = visual.getGraph();

        for (int v = 0; v < graph.vertexCount(); v++) {
            graph.setMark(v, WHITE);
        }
        visual.resetVisuals();
        visual.pauseAndRedraw("Estado inicial. Inicio: " + startNode, 1000);

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

    /**
     * Ejecuta el algoritmo de Kruskal para obtener un Árbol de Expansión Mínima (MST).
     * <p>
     * Requisitos y notas:
     * - Se asume grafo no dirigido y ponderado (si es no dirigido, debe existir simetría i->j y j->i).
     * - El algoritmo recorre todas las aristas, las ordena por peso y selecciona aquellas que no forman ciclo
     * usando {@link algorithms.mst.UnionFind}.
     * - Devuelve el peso total del MST construido (o del bosque, si el grafo está desconectado).
     * <p>
     * Complejidad: O(E log E), dominada por el ordenamiento de aristas.
     * <p>
     * Efectos de UI: resetea visuales, marca aristas seleccionadas y muestra mensajes de progreso.
     *
     * @param visual vista que implementa {@link IVisualizer}
     * @return peso total del MST resultante
     */
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

    public static int runDijkstra(IVisualizer visual, int startNode, int endNode) {
        if (isPaused)
            resumeAlgorithm();

        IGraph graph = visual.getGraph();
        int n = graph.vertexCount();

        resetVisualsForPath(visual, graph);
        visual.pauseAndRedraw("Iniciando Dijkstra desde " + startNode + " hasta " + endNode, 1000);

        int[] dist = new int[n];
        int[] parent = new int[n];
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }
        dist[startNode] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        pq.add(new int[]{startNode, 0});

        System.out.println("\nTabla de distancias inicial");

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];

            if (visited[u]) continue;
            visited[u] = true;

            graph.setMark(u, BLACK);
            visual.pauseAndRedraw("Procesando nodo " + u + ". Distancia actual: " + dist[u], 500);
            checkPause();


            if (u == endNode) break;


            for (int v = graph.firstNeighbor(u); v < n; v = graph.nextNeighbor(u, v)) {
                int weight = graph.weight(u, v);

                // Relajación
                if (!visited[v] && dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    parent[v] = u;
                    pq.add(new int[]{v, dist[v]});

                    graph.setMark(v, GRAY);
                    visual.markEdge(u, v, true);
                    visual.pauseAndRedraw("Actualizando distancia a " + v + " : " + dist[v], 300);
                    checkPause();
                }
            }
//            printDistanceTable(dist);
        }

//        highlightPath(visual, parent, endNode);
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

    private static void resetVisualsForPath(IVisualizer visual, IGraph graph) {
        for (int v = 0; v < graph.vertexCount(); v++) graph.setMark(v, WHITE);
        visual.resetVisuals();
    }

}