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
        mainAppPanel = new MainAppPanel();

        // Panel Derecho
        menuButtonsPanel = new MenuButtonsPanel();
        recorridoButtonsPanel = new RecorridoButtonsPanel();
        rightPanel = new RightPanel(menuButtonsPanel);

        // Panel Izquierdo
        controlsPanel = new ControlsPanel();
        leftPanel = new LeftPanel(controlsPanel);

        // Main App Panel
        mainAppPanel.setLeftPanel(leftPanel);
        mainAppPanel.setRightPanel(rightPanel);

        // Main Frame
        mainFrame.setMainPanel(mainAppPanel);


        // Manu de Algoritmos
        menuButtonsPanel.getRecorridoBtn().addActionListener(e -> {
            rightPanel.getSecondPanel().add(recorridoButtonsPanel);
            rightPanel.getSecondPanel().revalidate();
            rightPanel.getSecondPanel().repaint();
        });

        // Botón Reiniciar
        controlsPanel.getRestartBtn().addActionListener(e -> {
            leftPanel.getMapPanel().getGraphPanel().resetVisuals();
            if (GraphAlgorithms.isPaused()) {
                GraphAlgorithms.resumeAlgorithm();
            }
            controlsPanel.getPauseBtn().setText("Detener");
            controlsPanel.getPauseBtn().setEnabled(false);
            controlsPanel.getPauseBtn().setEnabled(true);
        });

        // Botón Pausar/Reanudar
        controlsPanel.getPauseBtn().addActionListener(e -> {
            if (GraphAlgorithms.isPaused()) {
                GraphAlgorithms.resumeAlgorithm();
                controlsPanel.getPauseBtn().setText("Detener");
            } else {
                GraphAlgorithms.pauseAlgorithm();
                controlsPanel.getPauseBtn().setText("Reanudar");
            }
        });

        // Botón Iniciar
        controlsPanel.getPlayBtn().addActionListener(e -> {
            try {
                int startNode = Integer.parseInt(recorridoButtonsPanel.getTextField().getText());
                // Agregar validacion para que este seleccionado un radioButton
                // Agregar validacion para ciudades tmb
                if (startNode < 0 || startNode >= leftPanel.getMapPanel().getGraph().vertexCount()) {
                    JOptionPane.showMessageDialog(mainFrame, "Nodo invalido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                controlsPanel.getPlayBtn().setEnabled(false);
                controlsPanel.getRestartBtn().setEnabled(false);
                controlsPanel.getPauseBtn().setEnabled(true);
                controlsPanel.getPauseBtn().setText("Detener");

                new Thread(() -> {
                    GraphAlgorithms.runDFSFromNode(leftPanel.getMapPanel().getGraphPanel(), startNode);

                    SwingUtilities.invokeLater(() -> {
                        controlsPanel.getPlayBtn().setEnabled(true);
                        controlsPanel.getRestartBtn().setEnabled(true);
                        controlsPanel.getPauseBtn().setEnabled(false);
                    });
                }).start();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Por favor ingresa un numero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


    }

}
