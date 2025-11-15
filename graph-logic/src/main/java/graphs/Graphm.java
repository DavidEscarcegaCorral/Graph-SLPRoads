package graphs;

import interfaces.IGraph;

/**
 * Implementación pública de la interfaz IGraph usando una Matriz de Adyacencia.
 */
public class Graphm implements IGraph {
    private int[][] matrix;
    private int edgeCounter;
    public int[] markArray;

    public Graphm(int n) {
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
            if (matrix[v][i] != 0) return i;
        return markArray.length;
    }

    @Override
    public int nextNeighbor(int v, int after) {
        for (int i = after + 1; i < markArray.length; i++)
            if (matrix[v][i] != 0) return i;
        return markArray.length;
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
        if (matrix[i][j] == 0) return 0;
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
