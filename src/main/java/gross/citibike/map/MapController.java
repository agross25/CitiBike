package gross.citibike.map;

import org.jxmapviewer.viewer.GeoPosition;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MapController {
    private MapFrame frame;
    private int maxPoints = 2;

    public MapController() {
        frame = new MapFrame();
        setupListeners();
        frame.setVisible(true);
    }
    private void setupListeners() {
        frame.getMapComponent().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                GeoPosition position = frame.getMapComponent()
                        .convertPointToGeoPosition(e.getPoint());
                addPoint(position);
            }
        });

        frame.getMapButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frame.getMapComponent().getWaypoints().size() == 2) {
                    displayRoute();
                }
            }
        });

        frame.getClearButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getMapComponent().clearWaypoints();
                frame.getStartPointLabel().setText("Start: ");
                frame.getEndPointLabel().setText("End: ");
            }
        });
    }

    private void addPoint(GeoPosition position) {
        if (frame.getMapComponent().getWaypoints().size() < maxPoints) {
            frame.getMapComponent().addWaypoint(position);
            if (frame.getMapComponent().getWaypoints().size() == 1) {
                frame.getStartPointLabel().setText("Start: " + position);
            } else {
                frame.getEndPointLabel().setText("End: " + position);
            }
        }
    }

    private void displayRoute() {
        // Add logic to calculate and display routes here
        System.out.println("Display best route.");
    }
}
