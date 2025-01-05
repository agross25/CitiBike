package gross.citibike;

import gross.citibike.citiBikeService.CitiBikeFunctions;
import gross.citibike.citiBikeService.StationResponse;
import gross.citibike.citiBikeService.StatusResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CitiBikeFunctionsTest {

    @Test
    void findStatus() {
        // given
        CitiBikeFunctions request = new CitiBikeFunctions();
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
        CitiBikeFunctions request = new CitiBikeFunctions();
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
        CitiBikeFunctions request = new CitiBikeFunctions();
        double lat = 40.642703;
        double lon = -74.009441;

        // when
        StationResponse.StationInfo closestStation = request.findClosestStationWithSlots(lat, lon);

        // then
        assertNotNull(closestStation);
        assertTrue(request.hasSlotsAvail(closestStation.station_id));
    }

}