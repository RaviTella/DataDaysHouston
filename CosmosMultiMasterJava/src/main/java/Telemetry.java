public class Telemetry {
  public String id;
  public String processed;
  public String device_id;
  public String event_type;
  public double temperature;
  public double humidity;
  public double battery;
  public double light_meter;
  public double pressure;
  public String country;
  public String city;
  public String state;
  public String model;
  public int periodsInService;

  public Telemetry(
      String processed,
      String device_id,
      String event_type,
      double temperature,
      double humidity,
      double battery,
      double light_meter,
      double pressure,
      String country,
      String city,
      String state,
      String model,
      int periodsInService) {
    this.processed = processed;
    this.device_id = device_id;
    this.event_type = event_type;
    this.temperature = temperature;
    this.humidity = humidity;
    this.battery = battery;
    this.light_meter = light_meter;
    this.pressure = pressure;
    this.country = country;
    this.city = city;
    this.state = state;
    this.model = model;
    this.periodsInService = periodsInService;
  }
}
