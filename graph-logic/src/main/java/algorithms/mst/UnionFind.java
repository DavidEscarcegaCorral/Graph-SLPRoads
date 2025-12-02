package algorithms.mst;

/**
 * Estructura de datos Union-Find (Disjoint Set Union, DSU) con:
 * - Compresión de caminos en {@link #find(int)}
 * - Unión por rango en {@link #union(int, int)}
 *
 * Uso típico: algoritmos de componentes disjuntas y, en este proyecto,
 * la detección de ciclos durante Kruskal para el Árbol de Expansión Mínima (MST).
 *
 * Complejidad: amortizada casi constante por operación, O(α(n)),
 * donde α es la inversa de la función de Ackermann.
 */
public class UnionFind {
    private int[] parent;
    private int[] rank;

    /**
     * Crea una estructura de N conjuntos disjuntos, cada elemento en su propio conjunto.
     * @param n número de elementos (0..n-1)
     */
    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    /**
     * Encuentra el representante (raíz) del conjunto que contiene a i.
     * Aplica compresión de caminos.
     * @param i elemento
     * @return índice de la raíz del conjunto
     */
    public int find(int i) {
        if (parent[i] != i) {
            parent[i] = find(parent[i]); // Compresión de caminos
        }
        return parent[i];
    }

    /**
     * Une los conjuntos que contienen i y j usando unión por rango.
     * @param i elemento del primer conjunto
     * @param j elemento del segundo conjunto
     * @return true si la unión ocurrió (eran distintos); false si ya estaban en el mismo conjunto
     */
    public boolean union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);

        if (rootI != rootJ) {
            // Unir por rango
            if (rank[rootI] < rank[rootJ]) {
                parent[rootI] = rootJ;
            } else if (rank[rootI] > rank[rootJ]) {
                parent[rootJ] = rootI;
            } else {
                parent[rootJ] = rootI;
                rank[rootI]++;
            }
            return true;
        }
        return false;
    }
}
