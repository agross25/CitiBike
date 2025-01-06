package gross.citibike.map;

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
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapComponent extends JXMapViewer {
    private WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
    private Set<Waypoint> waypoints = new HashSet<>();
    private CompoundPainter<JXMapViewer> painter;

    public MapComponent() {
        setupMap(new GeoPosition(40.642703, -74.009441)); // NYC coordinates
        waypointPainter.setWaypoints(waypoints);
        painter = new CompoundPainter<>(waypointPainter);
        this.setOverlayPainter(painter);
    }

    public void setupMap(GeoPosition position) {
        DefaultTileFactory tileFactory = new DefaultTileFactory(new OSMTileFactoryInfo());
        this.setTileFactory(tileFactory);
        tileFactory.setThreadPoolSize(8);
        this.setZoom(2);
        this.setAddressLocation(position);
        addMouseListeners();
    }

    public void addMouseListeners() {
        MouseInputListener mil = new PanMouseInputListener(this);
        this.addMouseListener(mil);
        this.addMouseMotionListener(mil);
        this.addMouseListener(new CenterMapListener(this));
        this.addMouseWheelListener(new ZoomMouseWheelListenerCursor(this));
        this.addKeyListener(new PanKeyListener(this));
    }

    public void addWaypoint(GeoPosition position) {
        Waypoint waypoint = new DefaultWaypoint(position);
        waypoints.add(waypoint);
        waypointPainter.setWaypoints(waypoints);
        repaint();
    }

    public void clearWaypoints() {
        waypoints.clear();
        waypointPainter.setWaypoints(waypoints);
        repaint();
    }

    public Set<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setRoutePainter(Painter<JXMapViewer> routePainter) {
        painter.setPainters(waypointPainter, routePainter);
        repaint();
    }
}
