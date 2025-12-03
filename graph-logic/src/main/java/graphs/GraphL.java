package graphs;

import interfaces.IGraph;

import java.util.LinkedList;

/**
 * Implementación de {@link IGraph} mediante lista de adyacencia.
 *
 * Características:
 * - Almacena para cada vértice una lista de aristas salientes ({@link graphs.Edge}).
 * - Iteración de vecinos eficiente en grafos dispersos; consultas de peso son O(grado(v)).
 * - Puede representar grafos dirigidos; para no dirigidos, insertar aristas en ambos sentidos.
 */
public class GraphL implements IGraph {
    private LinkedList<Edge>[] adjacencylist;
    private int numVertex;
    private int numEdge;
    private int[] Mark;
    private final int UNREACHABLE = Integer.MAX_VALUE;

    /**
     * Constructor
     */
    public GraphL(int n) {
        init(n);
    }

    /**
     * Inicializa el grafo
     */
    @Override
    public void init(int n) {
        numVertex = n;
        numEdge = 0;

        adjacencylist = new LinkedList[n];
        Mark = new int[n];

        for (int i = 0; i < n; i++) {
            adjacencylist[i] = new LinkedList<Edge>();
        }
    }

    @Override
    public int vertexCount() {
        return numVertex;
    }

    @Override
    public int edgeCount() {
        return numEdge;
    }

    @Override
    public int firstNeighbor(int v) {
        if (adjacencylist[v].size() == 0) {
            return numVertex;
        }
        return adjacencylist[v].get(0).getVert();
    }

    @Override
    public int nextNeighbor(int v, int after) {
        for (int i = 0; i < adjacencylist[v].size(); i++) {
            if (adjacencylist[v].get(i).getVert() == after) {
                if (i + 1 < adjacencylist[v].size()) {
                    return adjacencylist[v].get(i + 1).getVert();
                } else {
                    return numVertex;
                }
            }
        }
        return numVertex;
    }

    @Override
    public void setEdge(int i, int j, int weight) {
        if (weight == 0)
            return;

        for (Edge curr : adjacencylist[i]) {
            if (curr.getVert() == j) {
                return;
            }
        }

        adjacencylist[i].add(new Edge(j, weight));
        numEdge++;
        // if (esNoDirigido) { adjacencylist[j].add(new Edge(i, weight)); }

    }

    @Override
    public void deleteEdge(int i, int j) {
        for (int k = 0; k < adjacencylist[i].size(); k++) {
            if (adjacencylist[i].get(k).getVert() == j) {
                adjacencylist[i].remove(k);
                numEdge--;
                return;
            }
        }
    }

    @Override
    public boolean isEdge(int i, int j) {
        return weight(i, j) != 0;
    }

    @Override
    public int weight(int i, int j) {
        for (Edge curr : adjacencylist[i]) {
            if (curr.getVert() == j) {
                return curr.getWeight();
            }
        }
        return 0;
    }

    @Override
    public void setMark(int v, int value) {
        Mark[v] = value;
    }

    @Override
    public int getMark(int v) {
        return Mark[v];
    }
}
