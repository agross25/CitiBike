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
    private MapComponent mapComponent;
    private int maxPoints = 2;

    public MapController() {
        mapComponent = new MapComponent();
    }

    public MapComponent getComponent() {
        return mapComponent;
    }

    public void addPoint(GeoPosition position) {
        if (mapComponent.getWaypoints().size() < maxPoints) {
            mapComponent.addWaypoint(position);
        }
    }

    public void findBestRoute() {
        LambdaService lambdaService = new LambdaServiceFactory().getService();
        List<GeoPosition> positions = new ArrayList<>();
        for (Waypoint wp : mapComponent.getWaypoints()) {
            GeoPosition gp = wp.getPosition();
            positions.add(gp);
        }
        Request request = new Request(positions.get(0), positions.get(1));
        lambdaService.getStationResponse(request)
                .subscribe(
                        response -> {
                            StationResponse.StationInfo startStation = response.start;
                            StationResponse.StationInfo endStation = response.end;
                            GeoPosition start = new GeoPosition(startStation.lat, startStation.lon);
                            GeoPosition end = new GeoPosition(endStation.lat, endStation.lon);
                            displayRoute(positions.get(0), start, end, positions.get(1));
                        },
                        throwable -> {
                            // Handle error here
                            throwable.printStackTrace();
                        }
                );
    }

    public void displayRoute(GeoPosition from, GeoPosition start, GeoPosition end, GeoPosition to) {
        List<GeoPosition> positions = new ArrayList<>();
        // add start and end to waypoints to frame
        positions.add(start);
        positions.add(end);
        mapComponent.addWaypoint(start);
        mapComponent.addWaypoint(end);
        // paint route between start and end
        RoutePainter routePainter = new RoutePainter(positions);
        mapComponent.setRoutePainter(routePainter);
        mapComponent.zoomToBestFit(Set.of(from, start, end, to), 1.0);
    }
}
