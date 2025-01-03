package gross.citibike;

import java.util.List;
import java.util.Map;

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
        public String station_type;
        public int capacity;
        //CHECKSTYLE:ON
    }
}
