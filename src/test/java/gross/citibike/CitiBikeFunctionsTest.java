package gross.citibike;

import gross.citibike.service.CitiBikeFunctions;
import gross.citibike.service.StationResponse;
import gross.citibike.service.StationsCache;
import gross.citibike.service.StatusResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CitiBikeFunctionsTest {

    @Test
    void findStatus() {
        // given
        StationsCache cache = new StationsCache();
        CitiBikeFunctions request = new CitiBikeFunctions(cache);
        String stationId = "4efda436-cd19-4ec3-acc7-1a622f723264";

        // when
        StatusResponse.Status status = request.findStatus(stationId);

        // then
        assertNotNull(status);
        assertEquals(stationId, status.station_id);
    }

    @Test
    void findClosestStationWithBikes() {
        // given
        StationsCache cache = new StationsCache();
        CitiBikeFunctions request = new CitiBikeFunctions(cache);
        double lat = 40.642703;
        double lon = -74.009441;

        // when
        StationResponse.StationInfo closestStation = request.findClosestStationWithBikes(lat, lon);

        // then
        assertNotNull(closestStation);
        assertTrue(request.hasBikesAvail(closestStation.station_id));
    }

    @Test
    void findClosestStationWithSlots() {
        // given
        StationsCache cache = new StationsCache();
        CitiBikeFunctions request = new CitiBikeFunctions(cache);
        double lat = 40.642703;
        double lon = -74.009441;

        // when
        StationResponse.StationInfo closestStation = request.findClosestStationWithSlots(lat, lon);

        // then
        assertNotNull(closestStation);
        assertTrue(request.hasSlotsAvail(closestStation.station_id));
    }

}