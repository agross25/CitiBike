package gross.citibike.service;

import java.util.List;

public class StationResponse {
    public Data data;

    public class Data {
        public List<StationInfo> stations;
    }

    public class StationInfo {
        //CHECKSTYLE:OFF
        public String station_id;
        public String name;
        public double lat;
        public double lon;
        //CHECKSTYLE:ON
    }
}
