package gross.citibike.lambda;

import gross.citibike.service.StationResponse;

public class Response {

    public StationResponse.StationInfo start;
    public StationResponse.StationInfo end;
    public Location to;
    public Location from;

    public Response(Location from, StationResponse.StationInfo start, StationResponse.StationInfo end, Location to) {
        this.from = from;
        this.start = start;
        this.end = end;
        this.to = to;
    }

}
