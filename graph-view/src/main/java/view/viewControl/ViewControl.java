package view.viewControl;

import algorithms.GraphAlgorithms;
import view.MainFrame;
import view.panels.MainAppPanel;
import view.panels.leftPanels.GraphPanel;
import view.panels.leftPanels.LeftPanel;
import view.panels.leftPanels.ControlsPanel;
import view.panels.rightPanels.*;
import view.panels.rightPanels.header.HeaderComponent;
import view.panels.rightPanels.header.HeaderMenuPanel;
import view.panels.rightPanels.header.OptionsMenuComponent;
import view.panels.rightPanels.mst.MSTMenuComponent;
import view.panels.rightPanels.searchAlgorithms.SearchAlgorithmsComponent;

import javax.swing.*;
import java.awt.*;

public class ViewControl {
    private MainFrame mainFrame;
    private MainAppPanel mainAppPanel;

    // Right Panel
    private RightPanel rightPanel;
    private HeaderMenuPanel headerMenuPanel;

    // Components
    private HeaderComponent headerComponent;
    private OptionsMenuComponent optionsMenuComponent;
    private SearchAlgorithmsComponent searchAlgorithmsComponent;
    private MSTMenuComponent mstMenuComponent;

    // Left Panel
    private LeftPanel leftPanel;
    private ControlsPanel controlsPanel;

    private AlgorithmCategory currentCategory;

    public ViewControl(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
        initListeners();
        showWelcomeView();
    }

    private void initUI() {
        mainAppPanel = new MainAppPanel();

        // Panel Derecho
        headerComponent = new HeaderComponent();
        optionsMenuComponent = new OptionsMenuComponent();
        headerMenuPanel = new HeaderMenuPanel(headerComponent, optionsMenuComponent);

        searchAlgorithmsComponent = new SearchAlgorithmsComponent();
        mstMenuComponent = new MSTMenuComponent();

        rightPanel = new RightPanel(headerMenuPanel);

        // Panel Izquierdo
        controlsPanel = new ControlsPanel();
        leftPanel = new LeftPanel(controlsPanel);

        mainAppPanel.setLeftPanel(leftPanel);
        mainAppPanel.setRightPanel(rightPanel);
        mainFrame.setMainPanel(mainAppPanel);
    }

    private void initListeners() {
        // Menu de Algoritmos
        optionsMenuComponent.getRecorridoBtn().addActionListener(e -> switchView(AlgorithmCategory.SEARCH));

        // Menu de MST
        optionsMenuComponent.getMstBtn().addActionListener(e -> switchView(AlgorithmCategory.MST));

        // Botón Reiniciar
        controlsPanel.getRestartBtn().addActionListener(e -> reiniciarSimulacion());

        // Botón Pausar/Reanudar
        controlsPanel.getPauseBtn().addActionListener(e -> alternarPausa());

        // Botón Iniciar
        controlsPanel.getPlayBtn().addActionListener(e -> startSimulation());

    } // Corregir botones de reproduccion

    private void showWelcomeView() {
        this.currentCategory = null;
        JPanel container = rightPanel.getSecondPanel();
        container.removeAll();

        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<h2>Bienvenido al sistema de visualizador de grafos</h2>"
                + "<p>Por favor seleccione una cateogria<br>del menu para iniciar.</p>"
                + "</div></html>");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        container.add(welcomeLabel, BorderLayout.CENTER);

        controlsPanel.getPlayBtn().setEnabled(false);
        controlsPanel.getPauseBtn().setEnabled(false);
        controlsPanel.getRestartBtn().setEnabled(false);

        container.revalidate();
        container.repaint();
    }

    private void switchView(AlgorithmCategory newCategory) {
        this.currentCategory = newCategory;
        JPanel container = rightPanel.getSecondPanel();
        container.removeAll();

        switch (newCategory) {
            case SEARCH:
                container.add(searchAlgorithmsComponent);
                break;
            case MST:
                container.add(mstMenuComponent);
                break;
            case SHORTEST_PATH:
                break;

        }

        controlsPanel.getPlayBtn().setEnabled(true);
        controlsPanel.getRestartBtn().setEnabled(true);

        container.revalidate();
        container.repaint();
    }

    private void startSimulation() {
        if (currentCategory == null) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Por favor seleccione una categoría del menú primero.",
                    "Ninguna selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        controlsPanel.getPlayBtn().setEnabled(false);
        controlsPanel.getRestartBtn().setEnabled(false);
        controlsPanel.getPauseBtn().setEnabled(true);
        controlsPanel.getPauseBtn().setText("Detener");

        new Thread(() -> {
            try {
                switch (currentCategory) {
                    case SEARCH:
                        runSearchLogic();
                        break;
                    case MST:
                        runMSTLogic();
                        break;
                    // case SHORTEST_PATH: ...
                    default:
                        System.err.println("Categoría no implementada aún.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(mainFrame,
                                "Ocurrió un error: " + e.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE)
                );
            } finally {
                SwingUtilities.invokeLater(this::restoreControlButtons);
            }
        }).start();
    }

    private void runSearchLogic() {
        SearchAlgorithmsComponent panel = searchAlgorithmsComponent;
        GraphPanel graphPanel = leftPanel.getMapPanel().getGraphPanel();

        int startNode = validateAndGetStartNode(panel.getTextField(), graphPanel.getGraph().vertexCount());
        if (startNode == -1)
            return;

        if (panel.isBFSSelected()) {
            GraphAlgorithms.runBFSFromNode(graphPanel, startNode);
        } else if (panel.isDFSSelected()) {
            GraphAlgorithms.runDFSFromNode(graphPanel, startNode);
        }
    }

    private void runMSTLogic() {
        MSTMenuComponent panel = mstMenuComponent;
        GraphPanel graphPanel = leftPanel.getMapPanel().getGraphPanel();

        int startNode = 0;

        if (panel.isPrimSelected()) {
            startNode = validateAndGetStartNode(panel.getTextField(), graphPanel.getGraph().vertexCount());
            if (startNode == -1) return;
        }

        if (panel.isKruskalSelected()) {
            GraphAlgorithms.runKruskal(graphPanel);
        } else if (panel.isPrimSelected()) {
            GraphAlgorithms.runPrim(graphPanel, startNode);
        } else if (panel.isBoruvkaSelected()) {
//            GraphAlgorithms.runBoruvka(graphPanel);
        }
    }

    /**
     * Valida el campo de texto para obtener un nodo de inicio válido.
     * Retorna -1 si hay error (y muestra popup), o el número si es válido.
     */
    private int validateAndGetStartNode(JTextField field, int maxVertices) {
        try {
            String text = field.getText();
            if (text.isEmpty()) throw new NumberFormatException();

            int node = Integer.parseInt(text);
            if (node < 0 || node >= maxVertices) {
                JOptionPane.showMessageDialog(mainFrame,
                        "Nodo fuera de rango (0 - " + (maxVertices - 1) + ")",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return -1;
            }
            return node;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Por favor ingrese un número válido en el campo de inicio.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    private void reiniciarSimulacion() {
        // Resetear visuales
        leftPanel.getMapPanel().getGraphPanel().resetVisuals();

        // Asegurar que el algoritmo no esté pausado
        if (GraphAlgorithms.isPaused()) {
            GraphAlgorithms.resumeAlgorithm();
        }

        // Resetear estado de botones
        controlsPanel.getPauseBtn().setText("⏸");
        controlsPanel.getPauseBtn().setEnabled(false);
        controlsPanel.getPlayBtn().setEnabled(true);
    }

    private void alternarPausa() {
        if (GraphAlgorithms.isPaused()) {
            GraphAlgorithms.resumeAlgorithm();
            controlsPanel.getPauseBtn().setText("⏸");
        } else {
            GraphAlgorithms.pauseAlgorithm();
            controlsPanel.getPauseBtn().setText("►");
        }
    }

    private void restoreControlButtons() {
        if (currentCategory != null) {
            controlsPanel.getPlayBtn().setEnabled(true);
            controlsPanel.getRestartBtn().setEnabled(true);
            controlsPanel.getPauseBtn().setEnabled(false);
            controlsPanel.getPauseBtn().setText("⏸");
        }
    }


}
