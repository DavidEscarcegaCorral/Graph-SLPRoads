package view.viewControl;

import algorithms.GraphAlgorithms;
import interfaces.IGraph;
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
import view.panels.rightPanels.searchAlgorithms.SearchlAlgorithmsComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ViewControl {
    private MainFrame mainFrame;
    private MainAppPanel mainAppPanel;

    // Right Panel
    private RightPanel rightPanel;
    private HeaderMenuPanel headerMenuPanel;

    // Components
    private HeaderComponent headerComponent;
    private OptionsMenuComponent optionsMenuComponent;
    private SearchlAlgorithmsComponent searchAlgorithmsComponent;
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

    private void showWelcomeView() {
        this.currentCategory = null; // Ensure state is null

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

        }

        controlsPanel.getPlayBtn().setEnabled(true);
        controlsPanel.getRestartBtn().setEnabled(true);

        container.revalidate();
        container.repaint();
    }

    /**
     * Se encarga de crear los paneles y organizarlos en el Frame.
     */
    private void initUI() {
        mainAppPanel = new MainAppPanel();

        // Panel Derecho
        headerComponent = new HeaderComponent();
        optionsMenuComponent = new OptionsMenuComponent();
        headerMenuPanel = new HeaderMenuPanel(headerComponent, optionsMenuComponent);

        searchAlgorithmsComponent = new SearchlAlgorithmsComponent();
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
        optionsMenuComponent.getRecorridoBtn().addActionListener(e ->
                mostrarMenu(e, 1));

        // Menu de MST
        optionsMenuComponent.getMstBtn().addActionListener(e ->
                mostrarMenu(e, 2));

        // Botón Reiniciar
        controlsPanel.getRestartBtn().addActionListener(e -> reiniciarSimulacion());

        // Botón Pausar/Reanudar
        controlsPanel.getPauseBtn().addActionListener(e -> alternarPausa());

        // Botón Iniciar
        controlsPanel.getPlayBtn().addActionListener(e -> iniciarSimulacion());

    }

    private void mostrarMenu(ActionEvent e, int option) {
        rightPanel.getSecondPanel().removeAll();
        switch (option) {
            case 1:
                rightPanel.getSecondPanel().add(searchAlgorithmsComponent);
                break;
            case 2:
                rightPanel.getSecondPanel().add(mstMenuComponent);
                break;

        }
        rightPanel.getSecondPanel().revalidate();
        rightPanel.getSecondPanel().repaint();
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

    private void iniciarSimulacion() {
        SearchlAlgorithmsComponent buttonsSearchAl = searchAlgorithmsComponent;
        MSTMenuComponent buttonsMST = mstMenuComponent;
        GraphPanel panel = leftPanel.getMapPanel().getGraphPanel();
        IGraph graph = panel.getGraph();

        int startNode = 0;
        boolean necesitaNodoInicio = buttonsSearchAl.isDFSSelected() || buttonsSearchAl.isBFSSelected() || buttonsMST.isPrimSelected();

        if (necesitaNodoInicio) {
            try {
                String text = buttonsSearchAl.getTextField().getText();
                if (text.isEmpty()) {
                    mostrarError("Por favor ingresa un nodo de inicio.");
                    return;
                }
                startNode = Integer.parseInt(text);

                if (startNode < 0 || startNode >= graph.vertexCount()) {
                    mostrarError("Nodo inválido. Rango permitido: 0 a " + (graph.vertexCount() - 1));
                    return;
                }

            } catch (NumberFormatException ex) {
                mostrarError("Por favor ingresa un número entero válido.");
                return;
            }
        }

        controlsPanel.getPlayBtn().setEnabled(false);
        controlsPanel.getRestartBtn().setEnabled(false);
        controlsPanel.getPauseBtn().setEnabled(true);
        controlsPanel.getPauseBtn().setText("⏸");

        final int finalStartNode = startNode;

        new Thread(() -> ejecutarAlgoritmo(finalStartNode)).start();
    }

    /**
     * Metodo para correr el algoritmo en un hilo secundario
     */
    private void ejecutarAlgoritmo(int startNode) {
        SearchlAlgorithmsComponent buttonsSearchAl = searchAlgorithmsComponent;
        MSTMenuComponent buttonsMST = mstMenuComponent;
        GraphPanel panel = leftPanel.getMapPanel().getGraphPanel();

        if (buttonsSearchAl.isBFSSelected()) {
            GraphAlgorithms.runBFSFromNode(panel, startNode);

        } else if (buttonsSearchAl.isDFSSelected()) {
            GraphAlgorithms.runDFSFromNode(panel, startNode);

        } else if (buttonsMST.isKruskalSelected()) {
            GraphAlgorithms.runKruskal(panel);

        }else if(buttonsMST.isPrimSelected()){
            GraphAlgorithms.runPrim(panel, startNode);

        }else if(buttonsMST.isBoruvkaSelected()){

        }

        SwingUtilities.invokeLater(() -> {
            controlsPanel.getPlayBtn().setEnabled(true);
            controlsPanel.getRestartBtn().setEnabled(true);
            controlsPanel.getPauseBtn().setEnabled(false);
            mostrarMensaje("Recorrido completado :D.");
        });
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(mainFrame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(mainFrame, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

}
