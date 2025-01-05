package gross.citibike.Lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;
import gross.citibike.CitiBikeService.StationResponse;
import gross.citibike.CitiBikeService.citiBikeFunctions;

public class CitiBikeRequestHandler implements RequestHandler<APIGatewayProxyRequestEvent, Response> {
    @Override
    public Response handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String body = event.getBody();
        Gson gson = new Gson();
        Request request = gson.fromJson(body, Request.class);
        citiBikeFunctions func = new citiBikeFunctions();
        StationResponse.StationInfo start = func.findClosestStationWithBikes(request.from.lat, request.from.lon);
        StationResponse.StationInfo end = func.findClosestStationWithSlots(request.to.lat, request.to.lon);
        return new Response(request.from, start, end, request.to);
    }
}
