package view.control;

import view.MainFrame;
import view.panels.MainAppPanel;
import view.panels.leftPanels.LeftPanel;
import view.panels.leftPanels.ControlsPanel;
import view.panels.rightPanels.*;
import view.panels.rightPanels.header.HeaderComponent;
import view.panels.rightPanels.header.HeaderMenuPanel;
import view.panels.rightPanels.header.OptionsMenuComponent;
import view.panels.rightPanels.mst.MSTMenuComponent;
import view.panels.rightPanels.searchAlgorithms.SearchAlgorithmsComponent;
import view.panels.rightPanels.shortestPath.ShortestPathComponent;

import javax.swing.*;
import java.awt.*;

public class ViewControl {
    private final MainFrame mainFrame;
    private MainAppPanel mainAppPanel;

    // Right Panel
    private RightPanel rightPanel;
    private HeaderMenuPanel headerMenuPanel;

    // Components
    private HeaderComponent headerComponent;
    private OptionsMenuComponent optionsMenuComponent;
    private SearchAlgorithmsComponent searchAlgorithmsComponent;
    private MSTMenuComponent mstMenuComponent;
    private ShortestPathComponent shortestPathComponent;

    // Left Panel
    private LeftPanel leftPanel;
    private ControlsPanel controlsPanel;

    private AlgorithmCategory currentCategory;
    private final AlgorithmsControl algorithmsControl;

    public ViewControl(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
        this.algorithmsControl = new AlgorithmsControl(
                mainFrame,
                leftPanel.getMapPanel().getGraphPanel(),
                controlsPanel,
                searchAlgorithmsComponent,
                mstMenuComponent,
                shortestPathComponent
        );

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
        shortestPathComponent = new ShortestPathComponent();

        rightPanel = new RightPanel(headerMenuPanel);

        // Panel Izquierdo
        controlsPanel = new ControlsPanel();
        leftPanel = new LeftPanel(controlsPanel);

        mainAppPanel.setLeftPanel(leftPanel);
        mainAppPanel.setRightPanel(rightPanel);
        mainFrame.setMainPanel(mainAppPanel);
    }

    private void initListeners() {
        optionsMenuComponent.getRecorridoBtn().addActionListener(e -> switchView(AlgorithmCategory.SEARCH));
        optionsMenuComponent.getMstBtn().addActionListener(e -> switchView(AlgorithmCategory.MST));
        optionsMenuComponent.getRutaCortaBtn().addActionListener(e -> {
            switchView(AlgorithmCategory.SHORTEST_PATH);
        });

        controlsPanel.getPlayBtn().addActionListener(e ->
                algorithmsControl.startSimulation(currentCategory)
        );

        // Botón Reiniciar
        controlsPanel.getRestartBtn().addActionListener(e ->
                algorithmsControl.onRestart()
        );

        // Botón Pausa
        controlsPanel.getPauseBtn().addActionListener(e ->
                algorithmsControl.onTogglePause()
        );
    }

    private void showWelcomeView() {
        this.currentCategory = null;
        JPanel container = rightPanel.getSecondPanel();
        container.removeAll();

        JLabel welcomeLabel = new JLabel("<html><center><h2>Bienvenido al Visualizador</h2><p>Por favor seleccione una categoría de algoritmo<br>en el menú para comenzar..</p></center></html>");
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
                container.add(shortestPathComponent);
                break;

        }

        controlsPanel.getPlayBtn().setEnabled(true);
        controlsPanel.getRestartBtn().setEnabled(true);

        container.revalidate();
        container.repaint();
    }
}
