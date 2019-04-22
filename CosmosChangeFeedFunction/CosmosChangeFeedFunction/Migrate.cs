using System.Collections.Generic;
using System.Net.Http;
using Microsoft.Azure.Documents;
using Microsoft.Azure.WebJobs;
using Microsoft.Azure.WebJobs.Host;
using Microsoft.Extensions.Logging;

namespace CosmosChangeFeedFunction
{
    public static class Migrate
    {        
        [FunctionName("Migrate")]
        public static void  Run(
            [CosmosDBTrigger(
            databaseName: "telemetry",
            collectionName: "EngineTelemetry",
            ConnectionStringSetting = "CosmosDBConnection",
            LeaseCollectionName = "leases",
            CreateLeaseCollectionIfNotExists = true )]IReadOnlyList<Document> input,
      
             [CosmosDB(
                databaseName: "telemetry",
                collectionName: "EngineTelemetry1",
                ConnectionStringSetting = "CosmosDBConnection")]IAsyncCollector<Telemetry> docsToSave, ILogger log)
        {
            foreach (Document doc in input)
            {
                log.LogInformation(doc.ToString());
                Telemetry t = (dynamic)doc;
                  docsToSave.AddAsync(t);
            }
        }
    }
}
