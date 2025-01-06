package gross.citibike.map;

import gross.citibike.lambda.LambdaService;
import gross.citibike.lambda.LambdaServiceFactory;
import gross.citibike.lambda.Request;
import gross.citibike.lambda.Response;
import gross.citibike.service.StationResponse;
import io.reactivex.rxjava3.core.Single;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.annotation.Repeatable;
import java.util.ArrayList;
import java.util.List;

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
                    LambdaService lambdaService = LambdaServiceFactory.getService();
                    List<GeoPosition> positions = new ArrayList<>();
                    for (Waypoint wp : frame.getMapComponent().getWaypoints()) {
                        GeoPosition gp = wp.getPosition();
                        positions.add(gp);
                    }
                    Request request = new Request(positions.get(0), positions.get(1));
                    Response response = lambdaService.getStationResponse(request).blockingGet();
                    StationResponse.StationInfo start = response.start;
                    StationResponse.StationInfo end = response.end;
                    displayRoute(start, end);
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

    private void displayRoute(StationResponse.StationInfo start, StationResponse.StationInfo end) {
        List<GeoPosition> positions = new ArrayList<GeoPosition>();
        positions.add(new GeoPosition(start.lat, start.lon));
        positions.add(new GeoPosition(end.lat, end.lon));
        RoutePainter routePainter = new RoutePainter(positions);
        frame.getMapComponent().setRoutePainter(routePainter);
        frame.repaint();
    }
}
