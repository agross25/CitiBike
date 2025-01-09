package gross.citibike.lambda;

import org.jxmapviewer.viewer.GeoPosition;

public class Request {
    public Location from;
    public Location to;

    public Request(GeoPosition from, GeoPosition to) {
        this.from = new Location(from.getLatitude(), from.getLongitude());
        this.to = new Location(to.getLatitude(), to.getLongitude());
    }

}
