using System;
using System.Collections.Generic;
using System.Text;

namespace CosmosChangeFeedToPowerBI
{
    public class Telemetry
    {
        public string Id { get; set;}
        public string Processed { get; set; }
        public string Device_id { get; set; }
        public string Event_type { get; set; }
        public string Temperature { get; set; }
        public double Humidity { get; set; }
        public double Battery { get; set; }
        public double Light_meter { get; set; }
        public double Pressure { get; set; }
        public string Country { get; set; }
        public string City { get; set; }
        public string State { get; set; }
        public string Model { get; set; }
        public int PeriodsInService { get; set; }
    }
}
