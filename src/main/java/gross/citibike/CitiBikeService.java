package gross.citibike;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface CitiBikeService {
    @GET("https://gbfs.citibikenyc.com/gbfs/en/station_information.json")
    Single<StationResponse> getStationResponse();

    @GET("https://gbfs.citibikenyc.com/gbfs/en/station_status.json")
    Single<StatusResponse> getStatusResponse();
}
