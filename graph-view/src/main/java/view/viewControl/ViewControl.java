package view.viewControl;

import algorithms.GraphAlgorithms;
import view.MainFrame;
import view.panels.MainAppPanel;
import view.panels.leftPanels.LeftPanel;
import view.panels.rightPanels.ControlsPanel;
import view.panels.rightPanels.MenuButtonsPanel;
import view.panels.rightPanels.RecorridoButtonsPanel;
import view.panels.rightPanels.RightPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ViewControl {
    private MainFrame mainFrame;
    private MainAppPanel mainAppPanel;

    private RightPanel rightPanel;
    private MenuButtonsPanel menuButtonsPanel;
    private RecorridoButtonsPanel recorridoButtonsPanel;

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
        menuButtonsPanel = new MenuButtonsPanel();
        recorridoButtonsPanel = new RecorridoButtonsPanel();
        rightPanel = new RightPanel(menuButtonsPanel);

        // Panel Izquierdo
        controlsPanel = new ControlsPanel();
        leftPanel = new LeftPanel(controlsPanel);

        mainAppPanel.setLeftPanel(leftPanel);
        mainAppPanel.setRightPanel(rightPanel);
        mainFrame.setMainPanel(mainAppPanel);
    }

    private void initListeners() {
        // Menu de Algoritmos
        menuButtonsPanel.getRecorridoBtn().addActionListener(this::mostrarMenuRecorrido);

        // Botón Reiniciar
        controlsPanel.getRestartBtn().addActionListener(e -> reiniciarSimulacion());

        // Botón Pausar/Reanudar
        controlsPanel.getPauseBtn().addActionListener(e -> alternarPausa());

        // Botón Iniciar
        controlsPanel.getPlayBtn().addActionListener(e -> iniciarSimulacion());

    }

    private void mostrarMenuRecorrido(ActionEvent e) {
        rightPanel.getSecondPanel().add(recorridoButtonsPanel);
        rightPanel.getSecondPanel().revalidate();
        rightPanel.getSecondPanel().repaint();
    }

    private void reiniciarSimulacion() {
        // Resetear visuales
        leftPanel.getMapPanel().getGraphPanel().resetVisuals();

        // Asegurar que el algoritmo no esté pausado internamente
        if (GraphAlgorithms.isPaused()) {
            GraphAlgorithms.resumeAlgorithm();
        }

        // Resetear estado de botones
        controlsPanel.getPauseBtn().setText("Detener");
        controlsPanel.getPauseBtn().setEnabled(false);
        controlsPanel.getPlayBtn().setEnabled(true);
    }

    private void alternarPausa() {
        if (GraphAlgorithms.isPaused()) {
            GraphAlgorithms.resumeAlgorithm();
            controlsPanel.getPauseBtn().setText("Detener");
        } else {
            GraphAlgorithms.pauseAlgorithm();
            controlsPanel.getPauseBtn().setText("Reanudar");
        }
    }

    private void iniciarSimulacion() {
        try {
            String inputText = recorridoButtonsPanel.getTextField().getText();
            int startNode = Integer.parseInt(inputText);

            if (startNode < 0 || startNode >= leftPanel.getMapPanel().getGraph().vertexCount()) {
                mostrarError("Nodo inválido: " + startNode);
                return;
            }

            //
            // Validaciones pendientes (RadioButtons, Ciudades, etc.)
            //

            // Configurar botones antes de iniciar
            controlsPanel.getPlayBtn().setEnabled(false);
            controlsPanel.getRestartBtn().setEnabled(false);
            controlsPanel.getPauseBtn().setEnabled(true);
            controlsPanel.getPauseBtn().setText("Detener");

            // Iniciar algoritmo en un hilo separado
            new Thread(() -> ejecutarAlgoritmo(startNode)).start();

        } catch (NumberFormatException ex) {
            mostrarError("Por favor ingresa un número válido.");
        }
    }

    /**
     * Lógica que corre en el hilo secundario para no congelar la UI
     */
    private void ejecutarAlgoritmo(int startNode) {
        // Agregar los demas Algoritmos aqui
        GraphAlgorithms.runDFSFromNode(leftPanel.getMapPanel().getGraphPanel(), startNode);

        // Restaurar la UI
        SwingUtilities.invokeLater(() -> {
            controlsPanel.getPlayBtn().setEnabled(true);
            controlsPanel.getRestartBtn().setEnabled(true);
            controlsPanel.getPauseBtn().setEnabled(false);
            mostrarMensaje("Recorrido completado.");
        });
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(mainFrame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(mainFrame, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

}
