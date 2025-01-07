package gross.citibike.map;

import gross.citibike.lambda.LambdaService;
import gross.citibike.lambda.LambdaServiceFactory;
import gross.citibike.lambda.Request;
import gross.citibike.lambda.Response;
import gross.citibike.service.StationResponse;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MapFrame extends JFrame {
    private MapComponent mapComponent;
    private MapController mapController;
    private JLabel startPointLabel;
    private JLabel endPointLabel;
    private JButton mapButton;
    private JButton clearButton;

    public MapFrame() {
        super("CitiBike Map");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        mapController = new MapController();
        mapComponent = mapController.getComponent();
        setupLayout();
        setVisible(true);
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(mapComponent, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        startPointLabel = new JLabel("Start: ");
        endPointLabel = new JLabel("End: ");

        JPanel southPanel = new JPanel(new GridBagLayout());
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

        addActionListeners();
    }

    public void addActionListeners() {
        mapComponent.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                GeoPosition position = mapComponent
                        .convertPointToGeoPosition(e.getPoint());
                mapController.addPoint(position);
                if (mapComponent.getWaypoints().size() == 1) {
                    startPointLabel.setText("Start: " + position);
                } else {
                    endPointLabel.setText("End: " + position);
                }
            }
        });
        mapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mapComponent.getWaypoints().size() == 2) {
                    mapController.findBestRoute();
                    repaint();
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapComponent.clearWaypoints();
                startPointLabel.setText("Start: ");
                endPointLabel.setText("End: ");
                mapComponent.setupMap(new GeoPosition(40.642703, -74.009441));
                // Remove the painted route by resetting route painter
                mapComponent.setRoutePainter(new RoutePainter(new ArrayList<>()));
                repaint();
            }
        });
    }
}

