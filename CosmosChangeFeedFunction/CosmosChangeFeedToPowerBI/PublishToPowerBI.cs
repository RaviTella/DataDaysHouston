using System;
using System.Collections.Generic;
using System.Net.Http;
using Microsoft.Azure.Documents;
using Microsoft.Azure.WebJobs;
using Microsoft.Azure.WebJobs.Host;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using System.Threading.Tasks;

namespace CosmosChangeFeedToPowerBI
{
    public static class PublishToPowerBI
    {
        private static HttpClient httpClient = new HttpClient();

        [FunctionName("PublishToPowerBI")]
        public static async Task RunAsync([CosmosDBTrigger(
            databaseName: "telemetry",
            collectionName: "EngineTelemetry",
            ConnectionStringSetting = "CosmosDBConnection",
            LeaseCollectionName = "leases")]IReadOnlyList<Document> input, ILogger log)
        {
            foreach (Document doc in input)
            {
                log.LogInformation(doc.ToString());
                Telemetry t = (dynamic)doc;
                var jsonString = JsonConvert.SerializeObject(t);
                HttpContent content = new StringContent(jsonString);           
                var response = await httpClient.PostAsync(Environment.GetEnvironmentVariable("PowerBIPushUrl"), content);
                log.LogInformation(jsonString);
            }
        }
    }
}
