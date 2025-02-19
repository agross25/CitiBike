package gross.citibike.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;
import gross.citibike.service.StationResponse;
import gross.citibike.service.CitiBikeFunctions;
import gross.citibike.service.StationsCache;

public class CitiBikeRequestHandler implements RequestHandler<APIGatewayProxyRequestEvent, Response> {

    private StationsCache cache = new StationsCache();

    @Override
    public Response handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String body = event.getBody();
        Gson gson = new Gson();
        Request request = gson.fromJson(body, Request.class);
        CitiBikeFunctions func = new CitiBikeFunctions(cache);
        StationResponse.StationInfo start = func.findClosestStationWithBikes(request.from.lat, request.from.lon);
        StationResponse.StationInfo end = func.findClosestStationWithSlots(request.to.lat, request.to.lon);
        return new Response(request.from, start, end, request.to);
    }
}
