package gross.citibike;

import java.util.List;

public class StationResponse {
    public Data data;

    public class Data {
        public List<StationInfo> stations;
    }

    public class StationInfo {
        public String name;
        public double lat;
        public double lon;
        public String station_type;
        public String station_id;
        public int capacity;
    }
}
