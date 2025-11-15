package interfaces;

/**
 * Interfaz pública que define el "contrato" de lo que el algoritmo
 * necesita que la GUI (View) haga por él.
 * Esto rompe la dependencia circular.
 */
public interface IVisualizer {

    /**
     * Obtiene el grafo lógico que se está visualizando.
     */
    IGraph getGraph();

    /**
     * Reinicia visualmente el panel a su estado inicial.
     */
    void resetVisuals();

    /**
     * Marca una arista como visitada (para pintarla de otro color).
     */
    void markEdge(int source, int destination, boolean visited);

    /**
     * Pausa el hilo del algoritmo y fuerza un redibujado de la GUI.
     * La implementación (con Thread.sleep y repaint) vive en la clase View.
     */
    void pauseAndRedraw(String message, int milliseconds);
}
