#include <Arduino.h>
#include "Thing.h"
#include "WebThingAdapter.h"

const char* ssid = "Livebox-4BDE";
const char* password = "AD21AC2EFE22949996C7D33976";

const char* thingType[] = {"WirePilot", nullptr};
WebThingAdapter* adapter;
ThingDevice* heater;

ThingProperty heaterMode("heaterMode", "The actual mode of the heater", STRING, "RunModeProperty");
ThingProperty humidity("humidity", "The humidity level from 0 to 100", NUMBER, "HumidityProperty");


bool lastOn = false;

void setup(void){
  Serial.begin(115200);
  Serial.println("");
  Serial.print("Connecting to \"");
  Serial.print(ssid);
  Serial.println("\"");
#if defined(ESP8266) || defined(ESP32)
  WiFi.mode(WIFI_STA);
#endif
  WiFi.begin(ssid, password);
  Serial.println("");

  // Wait for connection
  bool blink = true;
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
    blink = !blink;
  }

  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  String hostname = WiFi.hostname();
  adapter = new WebThingAdapter("w25", WiFi.localIP());
   heater = new ThingDevice("led", "Heater", thingType);
   heater->addProperty(&humidity);
   adapter->addDevice(heater);
   adapter->begin();
  Serial.println("HTTP server started");
  Serial.print("http://");
  Serial.print(WiFi.localIP());
  Serial.print("/things/");
  Serial.println(heater->id);
}

void loop(void){
  adapter->update();
}
