package gross.citibike;

import java.util.List;
import java.util.Map;

public class CitiBikeRequests {

    private List<StationResponse.StationInfo> stations;
    private List<StatusResponse.Status> statuses;

    public CitiBikeRequests() {
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        stations = service.getStationResponse().blockingGet().data.stations;
        statuses = service.getStatusResponse().blockingGet().data.stations;
    }

    public CitiBikeRequests(CitiBikeService cbs) {
        CitiBikeService service = cbs;
        stations = service.getStationResponse().blockingGet().data.stations;
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
        // set distance and retVal according to first station in list
        StationResponse.StationInfo retVal = null;
        double distance = Double.MAX_VALUE;

        for (StationResponse.StationInfo station : stations) {
            double currentDistance = findDistance(lat, lon, station.lat, station.lon);
            if (distance > currentDistance && hasBikesAvail(station.station_id)) {
                distance = currentDistance;
                retVal = station;
            }
        }
        return retVal;
    }

    // find closest station with available slots to given location
    public StationResponse.StationInfo findClosestStationWithSlots(double lat, double lon) {
        // set distance and retVal according to first station in list
        StationResponse.StationInfo retVal = null;
        double distance = Double.MAX_VALUE;

        for (StationResponse.StationInfo station : stations) {
            double currentDistance = findDistance(lat, lon, station.lat, station.lon);
            if (distance > currentDistance && hasSlotsAvail(station.station_id)) {
                distance = currentDistance;
                retVal = station;
            }
        }
        return retVal;
    }

    // calculate distance between 2 sets of lat-lon points
    public double findDistance(double lat1, double lon1, double lat2, double lon2) {
        // Difference in coordinates
        double diffLat = lat2 - lat1;
        double diffLon = lon2 - lon1;

        // Adjust longitude based on latitude
        double avg_lat = (lat1 + lat2) / 2;
        double dx = diffLon * Math.cos(Math.toRadians(avg_lat));  // Convert avg_lat to radians for cosine
        double dy = diffLat;

        //Calculate distance using Pythagorean theorem
        double distance_km = 111.32 * Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0));
        return distance_km;
    }

    // check if station has bikes available
    public boolean hasBikesAvail(String id) {
        for (StatusResponse.Status status : statuses) {
            if (status.station_id.equals(id)) {
                if ((status.num_bikes_available > 0) || (status.num_ebikes_available > 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    // check if station has slots available
    public boolean hasSlotsAvail(String id) {
        for (StatusResponse.Status status : statuses) {
            if (status.station_id.equals(id) && (status.num_docks_available > 0)) {
                return true;
            }
        }
        return false;
    }

}