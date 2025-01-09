package gross.citibike.service;

import com.google.gson.Gson;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;

public class StationsCache {
    private final String bucketName = "gross.citibike";
    private final String keyName = "station_information.json";
    private S3Client s3Client;
    private StationResponse stations;
    private Instant lastModified;
    private CitiBikeService service;
    private Gson gson;

    public StationsCache() {
        Region region = Region.US_EAST_2;
        s3Client = S3Client.builder().region(region).build();
        service = new CitiBikeServiceFactory().getService();
        gson = new Gson();
    }

    public StationResponse getStations() {
        if (stations != null) {
            // if stations is updated, return it
            if (lastModified.isAfter(Instant.now().minus(Duration.ofHours(1)))) {
                return stations;
            } else { // if stations need to be updated, reload and update lastModified
                stations = service.getStationResponse().blockingGet();
                lastModified = Instant.now();
                writeToS3(stations);
            }
        } else { // read stations info from S3 and read last modified
            if (lastModified.isAfter(Instant.now().minus(Duration.ofHours(1)))) {
                stations = readFromS3();
                lastModified = readLastModified();
            } else { // if stations need to be updated, reload and update lastModified
                stations = service.getStationResponse().blockingGet();
                lastModified = Instant.now();
                writeToS3(stations);
            }
        }
        return stations;
    }

    public void writeToS3(StationResponse stations) {
        try {
            String content = gson.toJson(stations);
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromString(content));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StationResponse readFromS3() {
        StationResponse response = new StationResponse();
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();
            InputStream in = s3Client.getObject(getObjectRequest);
            response = gson.fromJson(new InputStreamReader(in), StationResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Instant readLastModified() {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();
            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
            return headObjectResponse.lastModified();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
