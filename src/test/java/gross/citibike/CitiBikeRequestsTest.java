package gross.citibike;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CitiBikeRequestsTest {

    @Test
    void findStatus() {
        // given
        CitiBikeRequests request = new CitiBikeRequests();
        String station_id = "4efda436-cd19-4ec3-acc7-1a622f723264";

        // when
        StatusResponse.Status status = request.findStatus(station_id);

        // then
        assertNotNull(status);
        assertEquals(station_id, status.station_id);
    }

    @Test
    void findClosestStationWithBikes() {
        // given
        CitiBikeRequests request = new CitiBikeRequests();
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
        CitiBikeRequests request = new CitiBikeRequests();
        double lat = 40.642703;
        double lon = -74.009441;

        // when
        StationResponse.StationInfo closestStation = request.findClosestStationWithSlots(lat, lon);

        // then
        assertNotNull(closestStation);
        assertTrue(request.hasSlotsAvail(closestStation.station_id));
    }

}