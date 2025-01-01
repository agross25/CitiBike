package gross.citibike;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CitiBikeServiceTest {

    @Test
    void getStationResponse() {
        // given
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        // when
        StationResponse response = service.getStationResponse().blockingGet();
        // then
        StationResponse.StationInfo stationInfo = response.data.stations.get(0);
        assertNotNull(stationInfo.name);
        assertNotNull(stationInfo.lat);
        assertNotNull(stationInfo.lon);
        assertNotNull(stationInfo.station_type);
        assertNotNull(stationInfo.station_id);
        assertNotNull(stationInfo.capacity);
        System.out.println(response.data.stations.size());
    }

    @Test
    void getStatusResponse() {
        // given
        CitiBikeService service = new CitiBikeServiceFactory().getService();
        // when
        StatusResponse response = service.getStatusResponse().blockingGet();
        // then
        StatusResponse.Status stationStatus = response.data.stations.get(0);
        assertNotNull(stationStatus.station_id);
        assertNotNull(stationStatus.num_bikes_available);
        assertNotNull(stationStatus.num_docks_available);
        assertNotNull(stationStatus.num_ebikes_available);
        System.out.println(response.data.stations.size());
    }
}