import com.microsoft.azure.cosmosdb.*;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import rx.Subscription;

import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Stopwatch;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MultiMasterLatencyTest {
    private static final String serviceEndpoint = "<>";
    private static final String masterKeyOrResourceToken = "<>";

    private final String databaseName = "telemetry";
    private final String collectionName = "EngineTelemetry";
    private static MultiMasterLatencyTest ct;
    private static List<String> docIDs;
    private static AsyncDocumentClient client;
    private static String masterLocation= "Central US";
    private static String clientLocation= "Central US";

    public static void main (String [] args){

        MultiMasterLatencyTest mmlt = new MultiMasterLatencyTest();
        client = getClient();
        Telemetry telemetry = new Telemetry("2019-04-20T22:00:44.1223597Z","device_aus","door",10,80,75,43.5,12.67,"aus","melbourne","Victoria","m0097",120);
        List<String> docIDs = new ArrayList<String>();
        for(int i=0; i<20; i++){
            Stopwatch stopwatch = Stopwatch.createStarted();
            String id = mmlt.createTelemetry(telemetry);
            stopwatch.stop();
            long millis = stopwatch.elapsed(MILLISECONDS);
            System.out.println("Client Region: "+clientLocation+ " | " +"  Cosmos Read-Write Region: "+ masterLocation + " | " +"  Time elapsed for write: " + stopwatch);
            docIDs.add(id);
        }

         for (String id : docIDs ) {
             Stopwatch stopwatch = Stopwatch.createStarted();
             mmlt.geTelemetryId(id);
             long millis = stopwatch.elapsed(MILLISECONDS);
             System.out.println("Client Region: "+clientLocation+ " | " +"  Cosmos Read-Write Region: "+ masterLocation + " | " +"  Time elapsed for read: " + stopwatch);

         }
        System.exit(0);
    }

    private static AsyncDocumentClient getClient() {

        ConnectionPolicy policy = new ConnectionPolicy();
        policy.setConnectionMode(ConnectionMode.Direct);
        policy.setUsingMultipleWriteLocations(true);
        List<String> locations =new ArrayList<String>();
        locations.add(masterLocation);
        policy.setPreferredLocations(locations);
        policy.setConnectionMode(ConnectionMode.Direct);

        return new AsyncDocumentClient.Builder()
                .withServiceEndpoint(serviceEndpoint)
                .withMasterKeyOrResourceToken(
                        masterKeyOrResourceToken)
                .withConnectionPolicy(policy)
                .withConsistencyLevel(ConsistencyLevel.ConsistentPrefix)
                .build();
    }

    private String createTelemetry(Telemetry telemetry) {
        String collectionLink = String.format("/dbs/%s/colls/%s", databaseName, collectionName);
        ResourceResponse<Document> s = client
                .createDocument(collectionLink, telemetry, null, false)
                .toBlocking().single();
       return s.getResource().getId();


    }

    private void geTelemetryId(String id) {
        FeedOptions queryOptions = new FeedOptions();
        queryOptions.setMaxItemCount(10);
        queryOptions.setEnableCrossPartitionQuery(true);
        String collectionLink = String.format("/dbs/%s/colls/%s", databaseName, collectionName);
        String query = "SELECT * FROM EngineTelemetry r WHERE r.id = " + "'" + id + "'" + " and " + "r.device_id = "+ "'" + "device_aus" + "'" +"";
        FeedResponse<Document> s = getClient().queryDocuments(collectionLink, query, queryOptions).toBlocking().single();
      }

}
