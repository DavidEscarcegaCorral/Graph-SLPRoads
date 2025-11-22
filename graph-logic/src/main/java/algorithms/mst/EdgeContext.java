package algorithms.mst;

/**
 * Clase auxiliar para manejar una arista con su contexto (origen y destino).
 * Implementa Comparable para poder ordenar por peso automáticamente.
 */
public class EdgeContext implements Comparable<EdgeContext>{
    public int source;
    public int dest;
    public int weight;

    public EdgeContext(int source, int dest, int weight) {
        this.source = source;
        this.dest = dest;
        this.weight = weight;
    }

    @Override
    public int compareTo(EdgeContext o) {
        return Integer.compare(this.weight, o.weight);
    }

    @Override
    public String toString() {
        return String.format("(%d - %d : %d)", source, dest, weight);
    }
}
