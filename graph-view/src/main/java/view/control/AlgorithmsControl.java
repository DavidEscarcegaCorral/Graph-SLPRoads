package view.control;

import algorithms.GraphAlgorithms;
import view.MainFrame;
import view.panels.leftPanels.ControlsPanel;
import view.panels.leftPanels.GraphPanel;
import view.panels.leftPanels.MapPanel;
import view.panels.rightPanels.mst.MSTMenuComponent;
import view.panels.rightPanels.searchAlgorithms.SearchAlgorithmsComponent;
import view.panels.rightPanels.shortestPath.ShortestPathComponent;

import javax.swing.*;

public class AlgorithmsControl {
    private final MainFrame mainFrame;
    private final GraphPanel graphPanel;
    private final ControlsPanel controlsPanel;
    private final MapPanel mapPanel;

    private final SearchAlgorithmsComponent searchPanel;
    private final MSTMenuComponent mstPanel;
    private final ShortestPathComponent shortestPathComponent;

    public AlgorithmsControl(MainFrame mainFrame,
                             GraphPanel graphPanel,
                             ControlsPanel controlsPanel,
                             MapPanel mapPanel,
                             SearchAlgorithmsComponent searchPanel,
                             MSTMenuComponent mstPanel,
                             ShortestPathComponent shortestPathComponent) {
        this.mainFrame = mainFrame;
        this.graphPanel = graphPanel;
        this.controlsPanel = controlsPanel;
        this.mapPanel = mapPanel;
        this.searchPanel = searchPanel;
        this.mstPanel = mstPanel;
        this.shortestPathComponent = shortestPathComponent;
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
        int endNode = -1;
        boolean isValid = true;

        switch (currentCategory) {
            case SEARCH:
                startNode = validateAndGetStartNode(searchPanel.getTextField());
                if (startNode == -1) isValid = false;
                break;

            case MST:
                if (mstPanel.isPrimSelected()) {
                    startNode = validateAndGetStartNode(mstPanel.getTextField());
                    if (startNode == -1) isValid = false;
                }
                break;

            case SHORTEST_PATH:
                startNode = validateAndGetStartNode(shortestPathComponent.getTextFieldOrigen());
                endNode = validateAndGetStartNode(shortestPathComponent.getTextFieldDestino());

                if (startNode == -1 || endNode == -1) isValid = false;
                break;
        }

        if (!isValid) return;

        controlsPanel.getPlayBtn().setEnabled(false);
        controlsPanel.getRestartBtn().setEnabled(false);
        controlsPanel.getPauseBtn().setEnabled(true);
        controlsPanel.getPauseBtn().setText("⏹");

        mstPanel.setWeight(-1);

        final int finalStartNode = startNode;
        final int finalEndNode = endNode;

        new Thread(() -> {
            try {
                int finalWeight = runLogic(currentCategory, finalStartNode, finalEndNode);
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

    private int runLogic(AlgorithmCategory category, int startNode, int endNode) {
        int resultWeight = -1;

        switch (category) {
            case SEARCH:
                if (searchPanel.isBFSSelected())
                    GraphAlgorithms.runBFSFromNode(graphPanel, startNode);
                else if (searchPanel.isDFSSelected())
                    GraphAlgorithms.runDFSFromNode(graphPanel, startNode);
                break;

            case MST:
                if (mstPanel.isKruskalSelected()) {
                    resultWeight = GraphAlgorithms.runKruskal(graphPanel);
                } else if (mstPanel.isPrimSelected()) {
                    resultWeight = GraphAlgorithms.runPrim(graphPanel, startNode);
                } else if (mstPanel.isBoruvkaSelected()) {
                    resultWeight = GraphAlgorithms.runBoruvka(graphPanel);
                }
                break;

            case SHORTEST_PATH:
                if (shortestPathComponent.isBellmanFordSelected()) {
                    resultWeight = GraphAlgorithms.runBellmanFord(graphPanel, startNode, endNode);
                } else if(shortestPathComponent.isDijkstraSelected()){
                    resultWeight = GraphAlgorithms.runDijkstra(graphPanel,startNode,endNode);
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
            try {
                int node = Integer.parseInt(text);
                if (node < 0 || node >= maxVertices) {
                    JOptionPane.showMessageDialog(mainFrame, "Nodo fuera de rango (0-" + (maxVertices - 1) + ")");
                    return -1;
                }
                return node;
            } catch (NumberFormatException ex) {
                if (mapPanel != null) {
                    int idx = mapPanel.findNodeByName(text);
                    if (idx >= 0) return idx;
                }
                JOptionPane.showMessageDialog(mainFrame, "Ingrese un nodo válido (número o nombre de localidad).\nEj: 0 o 'San Luis Potosí'");
                return -1;
            }
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
