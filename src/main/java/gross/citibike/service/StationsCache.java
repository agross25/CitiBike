package gross.citibike.service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;

public class StationsCache {

    private S3Client s3Client;
    private StationResponse stations;
    private Instant lastModified;

    public StationsCache() {
        Region region = Region.US_EAST_2;
        s3Client = S3Client.builder().region(region).build();
        // to read from S3
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket("gross.citibike").key("").build();
        InputStream in = s3Client.getObject(getObjectRequest);
        MyJsonClass myJsonObject = gson.fromJson(new InputStreamReader(in), MyJsonClass.java);

        // to write to S3
    }

    public StationResponse getStations() {
        if (stations != null) {
            // if stations is updated, return it
            if (lastModified.isAfter(Instant.now().minus(Duration.ofHours(1)))) {
                return stations;
            } else { // if stations need to be updated, reload and update lastModified
                stations = new CitiBikeServiceFactory().getService().getStationResponse().blockingGet();
                lastModified = Instant.now();
                // upload stations to S3
            }
        } else {
            if (lastModified.isAfter(Instant.now().minus(Duration.ofHours(1))) {
                // read stations from S3
                // update lastModified to lastModified from S3
            } else { // if stations need to be updated, reload and update lastModified
                stations = new CitiBikeServiceFactory().getService().getStationResponse().blockingGet();
                lastModified = Instant.now();
                // upload stations to S3
            }
        }
        return stations;
    }

}
