package interfaces;

/**
 * Contrato mínimo de un grafo dirigido ponderado utilizado por los algoritmos.
 *
 * Convenciones generales:
 * - Los vértices se indexan en el rango [0, V-1].
 * - Un peso 0 equivale a “sin arista” (ausencia de conexión).
 * - Las implementaciones pueden representar grafos dirigidos; si se desea no dirigido,
 *   se espera que el llamador establezca ambas direcciones (i->j y j->i).
 */
public interface IGraph {

    /**
     * Inicializa/reinicia la estructura interna para un número dado de vértices.
     * @param n número de vértices del grafo (debe ser > 0)
     */
    void init(int n);

    /**
     * @return número total de vértices en el grafo.
     */
    int vertexCount();

    /**
     * @return número total de aristas actualmente almacenadas.
     */
    int edgeCount();

    /**
     * Obtiene el primer vecino del vértice v, o V si no tiene vecinos.
     * @param v vértice de origen
     * @return índice del primer vecino, o {@code vertexCount()} si no hay
     */
    int firstNeighbor(int v);

    /**
     * Obtiene el vecino siguiente a {@code after} para el vértice v, o V si no hay más.
     * @param v vértice de origen
     * @param after vecino de referencia a partir del cual continuar
     * @return índice del siguiente vecino, o {@code vertexCount()} si no hay
     */
    int nextNeighbor(int v, int after);

    /**
     * Crea o actualiza una arista (i, j) con un peso dado.
     * Un peso 0 NO crea ni actualiza la arista; para eliminar use {@link #deleteEdge(int, int)}.
     * @param i vértice origen
     * @param j vértice destino
     * @param weight peso asociado (> 0 significa arista presente)
     */
    void setEdge(int i, int j, int weight);

    /**
     * Elimina la arista (i, j) si existe.
     * @param i vértice origen
     * @param j vértice destino
     */
    void deleteEdge(int i, int j);

    /**
     * Verifica si existe la arista (i, j).
     * @param i vértice origen
     * @param j vértice destino
     * @return true si hay arista; false en caso contrario
     */
    boolean isEdge(int i, int j);

    /**
     * Devuelve el peso de la arista (i, j) o 0 si no existe.
     * @param i vértice origen
     * @param j vértice destino
     * @return peso de la arista, o 0 si no hay arista
     */
    int weight(int i, int j);

    /**
     * Asigna una marca/estado para el vértice v (usado por algoritmos: WHITE/GRAY/BLACK, etc.).
     * @param v vértice a marcar
     * @param value valor de marca
     */
    void setMark(int v, int value);

    /**
     * Obtiene la marca/estado del vértice v.
     * @param v vértice
     * @return valor de marca actual del vértice
     */
    int getMark(int v);
}
