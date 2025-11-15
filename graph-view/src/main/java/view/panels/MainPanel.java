package view.panels;

import algorithms.GraphAlgorithms;
import graphs.Graphm;
import interfaces.IGraph;

import javax.swing.*;
import java.awt.*;

/**
 * Panel principal.
 * Contiene el GraphPanel y el controlPanel.
 * Maneja todos los eventos de los botones.
 */
public class MainPanel extends JPanel {

    private GraphPanel graphPanel;
    private IGraph myGraph;

    private JButton startButton;
    private JButton resetButton;
    private JButton pauseButton;
    private JTextField startField;

    public MainPanel() {
        this.setLayout(new BorderLayout());

        myGraph = new Graphm(5);
        myGraph.setEdge(0, 1, 10);
        myGraph.setEdge(0, 2, 10);
        myGraph.setEdge(1, 3, 10);
        myGraph.setEdge(2, 4, 10);
        myGraph.setEdge(4, 1, 10); // Arista de 4 a 1
        myGraph.setEdge(3, 0, 10); // Arista de 3 a 0

        graphPanel = new GraphPanel(myGraph);

        startButton = new JButton("Iniciar");
        resetButton = new JButton("Reiniciar");
        pauseButton = new JButton("Pausar");
        pauseButton.setEnabled(false);
        startField = new JTextField("0", 3);
        JLabel startLabel = new JLabel("Nodo de inicio: ");

        JPanel controlPanel = new JPanel();
        controlPanel.add(startLabel);
        controlPanel.add(startField);
        controlPanel.add(startButton);
        controlPanel.add(pauseButton);
        controlPanel.add(resetButton);


        this.add(graphPanel, BorderLayout.CENTER);
        this.add(controlPanel, BorderLayout.SOUTH);

        // Botón Reiniciar
        resetButton.addActionListener(e -> {
            graphPanel.resetVisuals();
            if (GraphAlgorithms.isPaused()) {
                GraphAlgorithms.resumeAlgorithm();
            }
            pauseButton.setText("Pausar");
            pauseButton.setEnabled(false);
            startButton.setEnabled(true);
        });

        // Botón Pausar/Reanudar
        pauseButton.addActionListener(e -> {
            if (GraphAlgorithms.isPaused()) {
                GraphAlgorithms.resumeAlgorithm();
                pauseButton.setText("Pausar");
            } else {
                GraphAlgorithms.pauseAlgorithm();
                pauseButton.setText("Reanudar");
            }
        });

        startButton.addActionListener(e -> {
            try {
                int startNode = Integer.parseInt(startField.getText());
                if (startNode < 0 || startNode >= myGraph.vertexCount()) {
                    JOptionPane.showMessageDialog(this, "Nodo invalido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                startButton.setEnabled(false);
                resetButton.setEnabled(false);
                pauseButton.setEnabled(true);
                pauseButton.setText("Pausar");

                new Thread(() -> {
                    GraphAlgorithms.runDFSFromNode(graphPanel, startNode);

                    SwingUtilities.invokeLater(() -> {
                        startButton.setEnabled(true);
                        resetButton.setEnabled(true);
                        pauseButton.setEnabled(false);
                    });
                }).start();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor ingresa un numero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
