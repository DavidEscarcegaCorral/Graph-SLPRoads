package view.control;

import algorithms.GraphAlgorithms;
import view.MainFrame;
import view.panels.leftPanels.ControlsPanel;
import view.panels.leftPanels.GraphPanel;
import view.panels.rightPanels.mst.MSTMenuComponent;
import view.panels.rightPanels.searchAlgorithms.SearchAlgorithmsComponent;

import javax.swing.*;

public class AlgorithmControl {
    private MainFrame mainFrame;
    private GraphPanel graphPanel;
    private ControlsPanel controlsPanel;

    private SearchAlgorithmsComponent searchPanel;
    private MSTMenuComponent mstPanel;

    public AlgorithmControl(MainFrame mainFrame,
                               GraphPanel graphPanel,
                               ControlsPanel controlsPanel,
                               SearchAlgorithmsComponent searchPanel,
                               MSTMenuComponent mstPanel) {
        this.mainFrame = mainFrame;
        this.graphPanel = graphPanel;
        this.controlsPanel = controlsPanel;
        this.searchPanel = searchPanel;
        this.mstPanel = mstPanel;
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

        final int finalNode = startNode;
        new Thread(() -> {
            try {
                runLogic(currentCategory, finalNode);
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

    private void runLogic(AlgorithmCategory category, int startNode) {
        switch (category) {
            case SEARCH:
                if (searchPanel.isBFSSelected()) GraphAlgorithms.runBFSFromNode(graphPanel, startNode);
                else if (searchPanel.isDFSSelected()) GraphAlgorithms.runDFSFromNode(graphPanel, startNode);
                break;

            case MST:
                if (mstPanel.isKruskalSelected()) GraphAlgorithms.runKruskal(graphPanel);
                else if (mstPanel.isPrimSelected()) GraphAlgorithms.runPrim(graphPanel, startNode);
                else if (mstPanel.isBoruvkaSelected()) JOptionPane.showMessageDialog(mainFrame, "Aun no implementado srry.");;
                break;
        }
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
