package view.control;

import algorithms.GraphAlgorithms;
import view.MainFrame;
import view.panels.leftPanels.ControlsPanel;
import view.panels.leftPanels.GraphPanel;
import view.panels.rightPanels.mst.MSTMenuComponent;
import view.panels.rightPanels.searchAlgorithms.SearchAlgorithmsComponent;
import view.panels.rightPanels.shortestPath.ShortestPathComponent;

import javax.swing.*;

/**
 * Controlador principal que coordina la ejecución de algoritmos desde la capa de vista.
 *
 * Responsabilidades:
 * - Valida entradas de la UI y enruta a los algoritmos correspondientes (búsqueda, MST, caminos).
 * - Gestiona el estado de los botones (play/pausa/reinicio) y muestra resultados (p. ej., peso de MST).
 * - Ejecuta la lógica en un hilo en segundo plano y actualiza la UI de forma segura mediante SwingUtilities.
 *
 * Concurrencia:
 * - Los algoritmos se ejecutan en un hilo aparte; toda actualización de UI se programa en la EDT.
 * - Usa los métodos estáticos de GraphAlgorithms para pausa/reanudación.
 */
public class AlgorithmsControl {
    private MainFrame mainFrame;
    private GraphPanel graphPanel;
    private ControlsPanel controlsPanel;

    private SearchAlgorithmsComponent searchPanel;
    private MSTMenuComponent mstPanel;
    private ShortestPathComponent shortestPathPanel;

    public AlgorithmsControl(MainFrame mainFrame,
                             GraphPanel graphPanel,
                             ControlsPanel controlsPanel,
                             SearchAlgorithmsComponent searchPanel,
                             MSTMenuComponent mstPanel,
                             ShortestPathComponent shortestPathaPanel) {
        this.mainFrame = mainFrame;
        this.graphPanel = graphPanel;
        this.controlsPanel = controlsPanel;
        this.searchPanel = searchPanel;
        this.mstPanel = mstPanel;
        this.shortestPathPanel = shortestPathaPanel;
    }

    public void onRestart() {
        graphPanel.resetVisuals();
        if (GraphAlgorithms.isPaused()) {
            GraphAlgorithms.resumeAlgorithm();
        }
        resetControlButtons();
    }

    public void onTogglePause() {
        if (GraphAlgorithms.isPaused()) {
            GraphAlgorithms.resumeAlgorithm();
            controlsPanel.getPauseBtn().setText("⏹");
        } else {
            GraphAlgorithms.pauseAlgorithm();
            controlsPanel.getPauseBtn().setText("⏯");
        }
    }

    /**
     * Inicia la simulación basada en la categoría actual que le pasa el ViewControl.
     */
    public void startSimulation(AlgorithmCategory currentCategory) {
        // Validaciones
        if (currentCategory == null) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Por favor seleccione una categoría del menú primero.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int startNode = 0;
        boolean isValid = true;

        switch (currentCategory) {
            case SEARCH:
                startNode = validateAndGetStartNode(searchPanel.getTextField());
                if (startNode == -1) isValid = false;
                break;
            case MST:
                // Solo Prim necesita validación
                if (mstPanel.isPrimSelected()) {
                    startNode = validateAndGetStartNode(mstPanel.getTextField());
                    if (startNode == -1) isValid = false;
                }
                break;
        }

        if (!isValid) return;

        controlsPanel.getPlayBtn().setEnabled(false);
        controlsPanel.getRestartBtn().setEnabled(false);
        controlsPanel.getPauseBtn().setEnabled(true);
        controlsPanel.getPauseBtn().setText("⏹");

        mstPanel.setWeight(-1);

        // Correr los hilos
        final int finalNode = startNode;
        new Thread(() -> {
            try {
                int finalWeight = runLogic(currentCategory, finalNode);
                System.out.println("DEBUG CONTROLADOR: El algoritmo retornó peso = " + finalWeight);

                if (currentCategory == AlgorithmCategory.MST) {
                    SwingUtilities.invokeLater(() -> {
                        System.out.println("DEBUG CONTROLADOR: Enviando a la vista...");
                        mstPanel.setWeight(finalWeight);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(mainFrame, "Error inesperado: " + e.getMessage())
                );
            } finally {
                SwingUtilities.invokeLater(this::restoreControlButtons);
            }
        }).start();
    }

    private int runLogic(AlgorithmCategory category, int startNode) {
        int resultWeight = -1;

        switch (category) {
            case SEARCH:
                if (searchPanel.isBFSSelected()) GraphAlgorithms.runBFSFromNode(graphPanel, startNode);
                else if (searchPanel.isDFSSelected()) GraphAlgorithms.runDFSFromNode(graphPanel, startNode);
                break;

            case MST:
                if (mstPanel.isKruskalSelected()) {
                    resultWeight = GraphAlgorithms.runKruskal(graphPanel);
                } else if (mstPanel.isPrimSelected()) {
                    resultWeight = GraphAlgorithms.runPrim(graphPanel, startNode);
                } else if (mstPanel.isBoruvkaSelected()) {
                    JOptionPane.showMessageDialog(mainFrame, "Aun no implementado sory.");
                }
                break;
            case SHORTEST_PATH:
                if(shortestPathPanel.isBellmanFordSelected()){

                }
                break;
        }
        return resultWeight;
    }

    private int validateAndGetStartNode(JTextField field) {
        int maxVertices = graphPanel.getGraph().vertexCount();
        try {
            String text = field.getText();
            if (text.isEmpty()) throw new NumberFormatException();
            int node = Integer.parseInt(text);

            if (node < 0 || node >= maxVertices) {
                JOptionPane.showMessageDialog(mainFrame, "Nodo fuera de rango (0-" + (maxVertices-1) + ")");
                return -1;
            }
            return node;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainFrame, "Ingrese un nodo válido.");
            return -1;
        }
    }

    private void resetControlButtons() {
        controlsPanel.getPauseBtn().setText("⏹");
        controlsPanel.getPauseBtn().setEnabled(false);
        controlsPanel.getPlayBtn().setEnabled(true);
        controlsPanel.getRestartBtn().setEnabled(true);
    }

    private void restoreControlButtons() {
        controlsPanel.getPlayBtn().setEnabled(true);
        controlsPanel.getRestartBtn().setEnabled(true);
        controlsPanel.getPauseBtn().setEnabled(false);
        controlsPanel.getPauseBtn().setText("⏸");
        JOptionPane.showMessageDialog(mainFrame, "Algoritmo finalizado.");
    }
}
