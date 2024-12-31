package gross.citibike;

import java.util.List;

public class StatusResponse {
    public Data data;

    public class Data {
        public List<Status> stations;
    }

    public class Status {
        public String station_id;
        public int num_ebikes_available;
        public int num_docks_available;
        public int num_bikes_available;
    }
}
