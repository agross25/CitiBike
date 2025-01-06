package gross.citibike.service;

import java.util.List;

public class StatusResponse {
    public Data data;

    public class Data {
        public List<StatusResponse.Status> stations;
        // public Map<String, Status> stations;
    }

    public class Status {
        //CHECKSTYLE:OFF
        public String station_id;
        public int num_ebikes_available;
        public int num_docks_available;
        public int num_bikes_available;
        //CHECKSTYLE:ON
    }
}
