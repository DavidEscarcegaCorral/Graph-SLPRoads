package graphs;

import interfaces.IGraph;

/**
 * Implementación de {@link IGraph} mediante matriz de adyacencia.
 *
 * Características:
 * - Acceso O(1) para {@link #isEdge(int, int)} y {@link #weight(int, int)}.
 * - Coste O(V) para iterar vecinos con {@link #firstNeighbor(int)} / {@link #nextNeighbor(int, int)}.
 * - Adecuado para grafos densos; para grafos dispersos considerar {@link graphs.GraphL}.
 */
public class GraphM implements IGraph {
    private int[][] matrix;
    private int edgeCounter;
    public int[] markArray;

    /**
     * Crea un grafo con n vértices y sin aristas.
     * @param n número de vértices (>0)
     */
    public GraphM(int n) {
        init(n);
    }

    @Override
    public void init(int n) {
        markArray = new int[n];
        matrix = new int[n][n];
        edgeCounter = 0;
    }

    @Override
    public int vertexCount() {
        return markArray.length;
    }

    @Override
    public int edgeCount() {
        return edgeCounter;
    }

    @Override
    public int firstNeighbor(int v) {
        for (int i = 0; i < markArray.length; i++)
            if (matrix[v][i] != 0)
                return i;
        return -1;
    }

    @Override
    public int nextNeighbor(int v, int after) {
        for (int i = after + 1; i < markArray.length; i++)
            if (matrix[v][i] != 0)
                return i;
        return -1;
    }

    @Override
    public void setEdge(int i, int j, int weight) {
        if (weight == 0) return;
        if (matrix[i][j] == 0) edgeCounter++;
        matrix[i][j] = weight;
    }

    @Override
    public void deleteEdge(int i, int j) {
        if (matrix[i][j] != 0) {
            matrix[i][j] = 0;
            edgeCounter--;
        }
    }

    @Override
    public boolean isEdge(int i, int j) {
        return matrix[i][j] != 0;
    }

    @Override
    public int weight(int i, int j) {
        if (i < 0 || j < 0 || i >= matrix.length || j >= matrix.length)
            return Integer.MAX_VALUE;

        return matrix[i][j];
    }

    @Override
    public void setMark(int v, int value) {
        markArray[v] = value;
    }

    @Override
    public int getMark(int v) {
        return markArray[v];
    }
}
