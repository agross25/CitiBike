package gross.citibike.map;



import gross.citibike.map.MapComponent;

import javax.swing.*;
import java.awt.*;
public class MapFrame extends JFrame {
    private MapComponent mapComponent;
    private JLabel startPointLabel;
    private JLabel endPointLabel;
    private JButton mapButton;
    private JButton clearButton;

    public MapFrame() {
        super("CitiBike Map");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);

        mapComponent = new MapComponent();
        setupLayout();
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(mapComponent, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        startPointLabel = new JLabel("Start: ");
        endPointLabel = new JLabel("End: ");

        gbc.gridx = 0;
        gbc.gridy = 0;
        southPanel.add(startPointLabel, gbc);

        gbc.gridy = 1;
        southPanel.add(endPointLabel, gbc);

        mapButton = new JButton("Map");
        clearButton = new JButton("Clear");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(mapButton);
        buttonPanel.add(clearButton);

        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        southPanel.add(buttonPanel, gbc);

        mainPanel.add(southPanel, BorderLayout.SOUTH);
        this.add(mainPanel);
    }

    public MapComponent getMapComponent() {
        return mapComponent;
    }

    public JLabel getStartPointLabel() {
        return startPointLabel;
    }

    public JLabel getEndPointLabel() {
        return endPointLabel;
    }

    public JButton getMapButton() {
        return mapButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }
}

