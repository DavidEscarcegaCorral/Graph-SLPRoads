package view.viewControl;

import algorithms.GraphAlgorithms;
import view.MainFrame;
import view.panels.MainAppPanel;
import view.panels.leftPanels.LeftPanel;
import view.panels.leftPanels.ControlsPanel;
import view.panels.rightPanels.*;
import view.panels.rightPanels.header.HeaderComponent;
import view.panels.rightPanels.header.HeaderMenuPanel;
import view.panels.rightPanels.header.OptionsMenuComponent;
import view.panels.rightPanels.mst.MSTMenuComponent;
import view.panels.rightPanels.searchAlgorithms.SearchlAlgorithmsComponent;

import javax.swing.*;
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
    private SearchlAlgorithmsComponent searchlAlgorithmsComponent;
    private MSTMenuComponent mstMenuComponent;

    // Left Panel
    private LeftPanel leftPanel;
    private ControlsPanel controlsPanel;

    public ViewControl(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        initUI();
        initListeners();
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

        searchlAlgorithmsComponent = new SearchlAlgorithmsComponent();
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
                rightPanel.getSecondPanel().add(searchlAlgorithmsComponent);
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
        try {
            String inputText = searchlAlgorithmsComponent.getTextField().getText();
            int startNode = Integer.parseInt(inputText);

            if (startNode < 0 || startNode >= leftPanel.getMapPanel().getGraph().vertexCount()) {
                mostrarError("Nodo inválido: " + startNode);
                return;
            }

            // Validaciones pendientes (RadioButtons, Ciudades, etc.)
            boolean esBFS = searchlAlgorithmsComponent.isBFSSelected();

            // Configurar botones antes de iniciar
            controlsPanel.getPlayBtn().setEnabled(false);
            controlsPanel.getRestartBtn().setEnabled(false);
            controlsPanel.getPauseBtn().setEnabled(true);
            controlsPanel.getPauseBtn().setText("⏸");

            // Iniciar algoritmo en un hilo separado
            new Thread(() -> ejecutarAlgoritmo(startNode, esBFS)).start();

        } catch (NumberFormatException ex) {
            mostrarError("Por favor ingresa un número válido.");
        }
    }

    /**
     * Metodo para correr el algoritmo en un hilo secundario
     */
    private void ejecutarAlgoritmo(int startNode, boolean esBFS) {


        if (esBFS) {
            GraphAlgorithms.runBFSFromNode(leftPanel.getMapPanel().getGraphPanel(), startNode);
        } else {
            GraphAlgorithms.runDFSFromNode(leftPanel.getMapPanel().getGraphPanel(), startNode);
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
