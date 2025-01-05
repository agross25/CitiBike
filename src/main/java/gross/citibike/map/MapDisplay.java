package gross.citibike.map;

import gross.citibike.service.CitiBikeFunctions;
import gross.citibike.service.StationResponse;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class MapDisplay {

    private JFrame frame;
    private JXMapViewer mapViewer;
    private RoutePainter routePainter;
    private WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
    private Set<Waypoint> waypoints = new HashSet<>();
    private List<Painter<JXMapViewer>> painters = List.of(routePainter, waypointPainter);
    private JLabel startPoint; // displays start coordinates
    private JLabel endPoint; // displays end coordinates
    private int maxPoints = 2;

    public MapDisplay() {
        frame = new JFrame("CitiBike Map");
        mapViewer = new JXMapViewer();
        mapViewer.setOverlayPainter(waypointPainter);
        startPoint = new JLabel("Start: ");
        endPoint = new JLabel("End: ");
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
    }

    public void display() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Set up map centered around NYC
        GeoPosition nyc = new GeoPosition(40.642703, -74.009441);
        setupMap(nyc);
        mainPanel.add(mapViewer, BorderLayout.CENTER);

        // South panel for text boxes and buttons
        JPanel southPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setupSouthPanel(southPanel, startPoint, endPoint, gbc);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        addButtons(buttonPanel);

        gbcPanelSettings(gbc);
        southPanel.add(buttonPanel, gbc);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        frame.add(mainPanel);
        frame.setVisible(true);

        // Make interactive
        MouseInputListener mil = new PanMouseInputListener(mapViewer);
        setMil(mil);
        addMouseListener();
    }

    public void addPoint(GeoPosition position) {
        if (waypoints.size() < maxPoints) {
            String latLon = position.getLatitude() + ", " + position.getLongitude();
            if (waypoints.size() == 0) {
                startPoint.setText("Start: " + latLon);
            } else { // size == 2
                endPoint.setText("End: " + latLon);
            }
            Waypoint waypoint = new DefaultWaypoint(position);
            waypoints.add(waypoint);
            waypointPainter.setWaypoints(waypoints);
            mapViewer.repaint();
        }
    }

    public void displayRoute() {
        // calculate closest stations to start and end points
        ArrayList<GeoPosition> positions = new ArrayList<>();
        for (Waypoint wp : waypoints) {
            positions.add(wp.getPosition());
        }
        CitiBikeFunctions func = new CitiBikeFunctions();
        GeoPosition pos1 = positions.get(0);
        GeoPosition pos2 = positions.get(1);
        StationResponse.StationInfo startStation = func.findClosestStationWithBikes(pos1.getLatitude(), pos1.getLongitude());
        StationResponse.StationInfo endStation = func.findClosestStationWithSlots(pos2.getLatitude(), pos2.getLongitude());
        maxPoints = 4;

        GeoPosition startPos = new GeoPosition(startStation.lat, startStation.lon);
        GeoPosition endPos = new GeoPosition(endStation.lat, endStation.lon);
        addPoint(startPos);
        addPoint(endPos);

        ArrayList<GeoPosition> track = new ArrayList<>();
        track.add(startPos);
        track.add(endPos);
        routePainter = new RoutePainter(track);
        BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        routePainter.paint(g2d, mapViewer, frame.getHeight(), frame.getWidth());
        maxPoints = 2;
    }

    public void setupMap(GeoPosition position) {
        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);

        // Set the focus
        mapViewer.setZoom(2);
        mapViewer.setAddressLocation(position);
    }

    public void setupSouthPanel(JPanel southPanel, JLabel start, JLabel end, GridBagConstraints gbc) {
        gbc.insets = new Insets(5, 5, 5, 5); // Padding around components

        // Add the start point label
        gbc.gridx = 0; // Column 0
        gbc.gridy = 0; // Row 0
        gbc.anchor = GridBagConstraints.EAST; // Align label to the right
        southPanel.add(start, gbc);

        // Add the end point label
        gbc.gridx = 0; // Column 0
        gbc.gridy = 1; // Row 1
        gbc.fill = GridBagConstraints.NONE; // Reset fill for label
        southPanel.add(end, gbc);
    }

    public void addButtons(JPanel buttonPanel) {
        JButton mapButton = new JButton("Map");
        JButton clearButton = new JButton("Clear");
        mapButton.setPreferredSize(new Dimension(80, 25));
        clearButton.setPreferredSize(new Dimension(80, 25));

        buttonPanel.add(mapButton);
        buttonPanel.add(clearButton);

        mapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (waypoints.size() == 2) {
                    displayRoute();
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                waypoints = new HashSet<>();
                waypointPainter.setWaypoints(waypoints);
                mapViewer.repaint();
                startPoint.setText("Start: ");
                endPoint.setText("End: ");
            }
        });
    }

    public void gbcPanelSettings(GridBagConstraints gbc) {
        gbc.gridx = 0; // Column 0
        gbc.gridy = 2; // Row 2
        gbc.gridwidth = 2; // Span both columns
        gbc.fill = GridBagConstraints.NONE; // No fill for buttons
        gbc.anchor = GridBagConstraints.CENTER; // Center the button panel
    }

    public void setMil(MouseInputListener mia) {
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));
    }

    public void addMouseListener() {
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                Point2D.Double point = new Point2D.Double(x, y);
                GeoPosition position = mapViewer.convertPointToGeoPosition(point);
                addPoint(position);
            }
        });
    }
}

