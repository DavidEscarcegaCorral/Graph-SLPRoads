package algorithms;

import interfaces.IGraph;
import interfaces.IVisualizer;

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

    /**
     * Metodo que revisa si el algoritmo debe pausarse.
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
}