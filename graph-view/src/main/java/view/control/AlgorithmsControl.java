package view.control;

import algorithms.GraphAlgorithms;
import algorithms.GraphAlgorithms.ShortestPathResult;
import view.MainFrame;
import view.panels.leftPanels.ControlsPanel;
import view.panels.leftPanels.GraphPanel;
import view.panels.leftPanels.MapPanel;
import view.panels.rightPanels.mst.MSTMenuComponent;
import view.panels.rightPanels.searchAlgorithms.SearchAlgorithmsComponent;
import view.panels.rightPanels.shortestPath.ShortestPathComponent;
import view.utils.ConsoleTee;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

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


    public void startSimulation(AlgorithmCategory currentCategory) {
        int startNode = 0;
        int endNode = -1;
        boolean isValid = true;

        switch (currentCategory) {
            case SEARCH:
                startNode = validateAndGetStartNode(searchPanel.getTextField());
                if (startNode == -1)
                    isValid = false;
                break;

            case MST:
                if (mstPanel.isPrimSelected()) {
                    startNode = validateAndGetStartNode(mstPanel.getTextField());
                    if (startNode == -1)
                        isValid = false;
                }
                break;

            case SHORTEST_PATH:
                startNode = validateAndGetStartNode(shortestPathComponent.getTextFieldOrigen());
                endNode = validateAndGetStartNode(shortestPathComponent.getTextFieldDestino());

                if (startNode == -1 || endNode == -1)
                    isValid = false;
                break;
        }

        if (!isValid) return;

        graphPanel.resetVisuals();

        if ((currentCategory == AlgorithmCategory.SEARCH) ||
                (currentCategory == AlgorithmCategory.SHORTEST_PATH) ||
                (currentCategory == AlgorithmCategory.MST && mstPanel.isPrimSelected())) {

            graphPanel.setAllowedComponent(startNode);
        } else {
            graphPanel.clearAllowedComponent();
        }

        controlsPanel.getPlayBtn().setEnabled(false);
        controlsPanel.getRestartBtn().setEnabled(false);
        controlsPanel.getPauseBtn().setEnabled(true);
        controlsPanel.getPauseBtn().setText("⏹");

        mstPanel.setWeight(-1);

        ConsoleTee.getInstance().setActiveChannel(currentCategory);
        ConsoleTee.getInstance().clearChannel(currentCategory);

        final int finalStart = startNode;
        final int finalEnd = endNode;

        new Thread(() -> {
            try {
                int weight = runLogic(currentCategory, finalStart, finalEnd);

                if (currentCategory == AlgorithmCategory.MST) {
                    SwingUtilities.invokeLater(() -> mstPanel.setWeight(weight));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(mainFrame, "Error inesperado: " + ex.getMessage())
                );
            } finally {
                SwingUtilities.invokeLater(() -> {
                    restoreControlButtons();
                    ConsoleTee.getInstance().setActiveChannel(null);
                    graphPanel.clearAllowedComponent();
                });
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
                    ShortestPathResult res = GraphAlgorithms.runBellmanFordWithEvolution(graphPanel, startNode, endNode);
                    resultWeight = res.finalDistance;
                    final ShortestPathResult finalRes = res;
                    SwingUtilities.invokeLater(() -> {
                        shortestPathComponent.setTotalDistanceText("Distancia total: " + (finalRes.finalDistance == Integer.MAX_VALUE ? "∞" : finalRes.finalDistance));
                    });
                } else if(shortestPathComponent.isDijkstraSelected()){
                    ShortestPathResult res = GraphAlgorithms.runDijkstraWithEvolution(graphPanel, startNode, endNode);
                    resultWeight = res.finalDistance;
                    final ShortestPathResult finalRes = res;
                    SwingUtilities.invokeLater(() -> {
                        shortestPathComponent.setTotalDistanceText("Distancia total: " + (finalRes.finalDistance == Integer.MAX_VALUE ? "∞" : finalRes.finalDistance));
                    });
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
            JOptionPane.showMessageDialog(mainFrame, "Ingrese un nodo válido.", "Error", JOptionPane.WARNING_MESSAGE);
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
