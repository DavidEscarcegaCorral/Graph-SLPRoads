package interfaces;

/**
 * Interfaz pública que define las operaciones básicas de un Grafo.
 * Cualquier clase de grafo (Graphm, Graphl) debe implementar esto.
 */
public interface IGraph {

    /**
     * Inicializa el grafo con un número específico de vértices.
     */
    public void init(int n);

    /**
     * @return El número total de vértices en el grafo.
     */
    public int vertexCount();

    /**
     * @return El número total de aristas en el grafo.
     */
    public int edgeCount();

    /**
     * @return El primer vecino del vértice 'v'.
     */
    public int firstNeighbor(int v);

    /**
     * @return El siguiente vecino de 'v' después del vecino 'after'.
     */
    public int nextNeighbor(int v, int after);

    /**
     * Establece (o actualiza) una arista de 'i' a 'j' con un peso 'weight'.
     */
    public void setEdge(int i, int j, int weight);

    /**
     * Elimina la arista de 'i' a 'j'.
     */
    public void deleteEdge(int i, int j);

    /**
     * @return true si existe una arista de 'i' a 'j', false si no.
     */
    public boolean isEdge(int i, int j);

    /**
     * @return El peso de la arista (i, j).
     */
    public int weight(int i, int j);

    /**
     * Establece la marca (valor) para un vértice 'v'.
     */
    public void setMark(int v, int value);

    /**
     * @return La marca (valor) actual del vértice 'v'.
     */
    public int getMark(int v);
}
