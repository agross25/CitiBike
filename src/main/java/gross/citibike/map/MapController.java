package gross.citibike.map;

import gross.citibike.lambda.LambdaService;
import gross.citibike.lambda.LambdaServiceFactory;
import gross.citibike.lambda.Request;
import gross.citibike.lambda.Response;
import gross.citibike.service.StationResponse;
import io.reactivex.rxjava3.core.Single;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.annotation.Repeatable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jxmapviewer.viewer.Waypoint;

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
                    LambdaService lambdaService = new LambdaServiceFactory().getService();
                    List<GeoPosition> positions = new ArrayList<>();
                    for (Waypoint wp : frame.getMapComponent().getWaypoints()) {
                        GeoPosition gp = wp.getPosition();
                        positions.add(gp);
                    }
                    Request request = new Request(positions.get(0), positions.get(1));
                    Response response = lambdaService.getStationResponse(request).blockingGet();
                    StationResponse.StationInfo startStation = response.start;
                    StationResponse.StationInfo endStation = response.end;
                    // Convert start and end to GeoPosition to display route
                    GeoPosition start = new GeoPosition(startStation.lat, startStation.lon);
                    GeoPosition end = new GeoPosition(endStation.lat, endStation.lon);
                    displayRoute(positions.get(0), start, end, positions.get(1));
                }
            }
        });

        frame.getClearButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getMapComponent().clearWaypoints();
                frame.getStartPointLabel().setText("Start: ");
                frame.getEndPointLabel().setText("End: ");
                frame.getMapComponent().setupMap(new GeoPosition(40.642703, -74.009441));
                // Remove the route painter by setting it to null or an empty painter
                frame.getMapComponent().setRoutePainter(new RoutePainter(new ArrayList<>()));
                frame.repaint();
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

    private void displayRoute(GeoPosition from, GeoPosition start, GeoPosition end, GeoPosition to) {
        List<GeoPosition> positions = new ArrayList<>();
        // add start and end to waypoints to frame
        positions.add(start);
        positions.add(end);
        frame.getMapComponent().addWaypoint(start);
        frame.getMapComponent().addWaypoint(end);
        // paint route between start and end
        RoutePainter routePainter = new RoutePainter(positions);
        frame.getMapComponent().setRoutePainter(routePainter);
        frame.getMapComponent().zoomToBestFit(Set.of(from, start, end, to), 1.0);
        frame.repaint();
    }
}
