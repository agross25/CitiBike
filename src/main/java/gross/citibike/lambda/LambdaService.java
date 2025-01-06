package gross.citibike.lambda;

import gross.citibike.service.StationResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LambdaService {
    @POST("https://6yearbnvvznt4yiaqdk2xsucua0gokfz.lambda-url.us-east-2.on.aws/")
    Single<Response> getStationResponse(@Body Request request);
}
