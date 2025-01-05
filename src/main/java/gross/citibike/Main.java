package gross.citibike;

import gross.citibike.map.MapDisplay;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        MapDisplay map = new MapDisplay();
        map.display();
    }
}
