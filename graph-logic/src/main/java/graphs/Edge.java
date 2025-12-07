package graphs;

/**
 * Representación de una arista para la lista de adyacencia.
 */
public class Edge {
    private int vert;
    private int weight;

    public Edge(int v, int w) {
        vert = v;
        weight = w;
    }

    public int getVert() {
        return vert;
    }

    public int getWeight() {
        return weight;
    }
}
