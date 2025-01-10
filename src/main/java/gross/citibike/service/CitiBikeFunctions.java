package gross.citibike.service;

import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;

public class CitiBikeFunctions {
    private StationsCache stationsCache;
    private final String bucketName = "gross.citibike";
    private List<StationResponse.StationInfo> stations;
    private List<StatusResponse.Status> statuses;

    public CitiBikeFunctions(StationsCache cache) {
        stationsCache = cache;
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        stations = stationsCache.getStations().data.stations;
        statuses = service.getStatusResponse().blockingGet().data.stations;
    }

    // find status of a station given its id
    public StatusResponse.Status findStatus(String id) {
        for (StatusResponse.Status status : statuses) {
            if (status.station_id.equals(id)) {
                return status;
            }
        }
        return null;
    }

    // find closest station with available bikes to given location
    public StationResponse.StationInfo findClosestStationWithBikes(double lat, double lon) {
        // set distance and closestStation according to first station in list
        StationResponse.StationInfo closestStation = null;
        double distance = Double.MAX_VALUE;

        for (StationResponse.StationInfo station : stations) {
            double currentDistance = findDistance(lat, lon, station.lat, station.lon);
            if (distance > currentDistance && hasBikesAvail(station.station_id)) {
                distance = currentDistance;
                closestStation = station;
            }
        }
        return closestStation;
    }

    // find closest station with available slots to given location
    public StationResponse.StationInfo findClosestStationWithSlots(double lat, double lon) {
        // set distance and closestStation according to first station in list
        StationResponse.StationInfo closestStation = null;
        double distance = Double.MAX_VALUE;

        for (StationResponse.StationInfo station : stations) {
            double currentDistance = findDistance(lat, lon, station.lat, station.lon);
            if (distance > currentDistance && hasSlotsAvail(station.station_id)) {
                distance = currentDistance;
                closestStation = station;
            }
        }
        return closestStation;
    }

    // calculate distance between 2 sets of lat-lon points
    public double findDistance(double lat1, double lon1, double lat2, double lon2) {
        // Difference in coordinates
        double diffLat = lat2 - lat1;
        double diffLon = lon2 - lon1;

        // Adjust longitude based on latitude
        double avgLat = (lat1 + lat2) / 2;
        double dx = diffLon * Math.cos(Math.toRadians(avgLat));  // Convert avgLat to radians for cosine
        double dy = diffLat;

        //Calculate distance using Pythagorean theorem
        double distance = 111.32 * Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0));
        return distance;
    }

    // check if station has bikes available
    public boolean hasBikesAvail(String id) {
        StatusResponse.Status status = findStatus(id);
        return status.num_bikes_available > 0 || status.num_ebikes_available > 0;
    }

    // check if station has slots available
    public boolean hasSlotsAvail(String id) {
        StatusResponse.Status status = findStatus(id);
        return status.num_docks_available > 0;
    }

}
