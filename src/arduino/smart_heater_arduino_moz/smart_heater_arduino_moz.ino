#include <ESP8266WiFi.h>          //ESP8266 Core WiFi Library (you most likely already have this in your sketch)
#include <DNSServer.h>            //Local DNS Server used for redirecting all requests to the configuration portal
#include <ESPAsyncWebServer.h>     //Local WebServer used to serve the configuration portal
#include <ESPAsyncWiFiManager.h>   //https://github.com/tzapu/WiFiManager WiFi Configuration Magic

#include <Adafruit_Sensor.h>
#include <DHT.h> 

#include <Thing.h>
#include <WebThingAdapter.h>

#define DHTPIN 14     // what digital pin we're connected to
#define DHTTYPE DHT22

// Create an instance of the server
// specify the port to listen on as an argument
AsyncWebServer server(8080);
DNSServer dns;

DHT dht(DHTPIN, DHTTYPE);
String temp;
String hum;
String currMode;


const char* thingType[] = {"WirePilot", nullptr};
WebThingAdapter* adapter;
ThingDevice* heater;

ThingProperty heaterMode("heaterMode", "The actual mode of the heater", STRING, "RunModeProperty");
ThingProperty temperature("temperature", "The temperature in celcius", NUMBER, "TemperatureProperty");
ThingProperty humidity("humidity", "The humidity level from 0 to 100%", NUMBER, "HumidityProperty");


void setup() {
  Serial.begin(115200);
  delay(10);

  // prepare GPIOs
  pinMode(12, OUTPUT);
  pinMode(13, OUTPUT);
  digitalWrite(12, 1);
  digitalWrite(13, 0);
  
  // Connect to WiFi network
  Serial.println();
  AsyncWiFiManager wifiManager(&server,&dns);

  // timeout for power failure
  wifiManager.setTimeout(180);
  //first parameter is name of access point, second is the password
  Serial.println("trying autoConnect");
  if (!wifiManager.autoConnect("RadiateurIntelligent", "radiateur")) {
    Serial.println("failed to connect and hit timeout");
    delay(3000);
    //reset and try again maybe the accesspoint is up now
    ESP.reset();
    delay(5000);
  }  

   // Print the IP address and hostname
  Serial.println(WiFi.localIP());
  String hostname = WiFi.hostname();
  Serial.println("Wifi hostname :" + hostname);

  // Prepare dht22
  dht.begin();
  readInfos();

  // Start the server
  server.begin();
  Serial.println("Server started");

 // set requests
    server.on("/mode/confort", HTTP_GET, [](AsyncWebServerRequest *request){ // Confort
        digitalWrite(12,0);
        digitalWrite(13,0);
        //---
        updateCurrentMode();
        //---
        request->send(200, "text/html", "<!DOCTYPE HTML>\r\n<html>\r\nRadiateur en mode CONFORT </html>\n");
    });


    server.on("/mode/horsgel", HTTP_GET, [](AsyncWebServerRequest *request){ // hors-gel
        digitalWrite(12,0);
        digitalWrite(13,1); // only negative
        //---
        updateCurrentMode();
        //---
        request->send(200, "text/html", "<!DOCTYPE HTML>\r\n<html>\r\nRadiateur en mode HORSGEL </html>\n");
    });

    server.on("/mode/arret", HTTP_GET, [](AsyncWebServerRequest *request){ // arret
        digitalWrite(12,1);// only positive
        digitalWrite(13,0);
        //---
        updateCurrentMode();
        //---
        request->send(200, "text/html", "<!DOCTYPE HTML>\r\n<html>\r\nRadiateur en mode ARRET </html>\n");
    });

    server.on("/mode/eco", HTTP_GET, [](AsyncWebServerRequest *request){ // eco
        digitalWrite(12,1);
        digitalWrite(13,1); 
        //---
        updateCurrentMode();
        //---
        request->send(200, "text/html", "<!DOCTYPE HTML>\r\n<html>\r\nRadiateur en mode ECO </html>\n");
    });

    server.on("/infos/", HTTP_GET, [](AsyncWebServerRequest *request){ // infos
        request->send(200, "application/json", "{'temperature' :"+temp+", 'humidity' :"+hum+", 'mode' : '"+currMode+"'}\n");
    });


  


   //----- web thing
   adapter = new WebThingAdapter("w25", WiFi.localIP());
   heater = new ThingDevice("smartheater", "Heater", thingType);
   heater->addProperty(&heaterMode);
   heater->addProperty(&temperature);
   heater->addProperty(&humidity);
   adapter->addDevice(heater);
   adapter->begin();
   
}

void loop() {
        readInfos();
        //---
        adapter->update();
        Serial.print("webthing mode :");
        Serial.println(*(heaterMode.getValue().string));
        Serial.print("webthing temperature :");
        Serial.println(temperature.getValue().number);
        Serial.print("webthing humidity :");
        Serial.println(humidity.getValue().number);

        if (*(heaterMode.getValue().string) != currMode) {
          changeMode( heaterMode.getValue().string);
        }
        
        //---
        delay(2000);
}


void readInfos() {
  // read infos from the sensor and store them as string in global variable
    float t = dht.readTemperature();
    ThingPropertyValue newTemperatureValue;
    newTemperatureValue.number = t;
    temperature.setValue(newTemperatureValue);
    String* t_string = new String(t);
    temp = *t_string;

    float h = dht.readHumidity();
    ThingPropertyValue newHumidityValue;
    newHumidityValue.number = h;
    humidity.setValue(newHumidityValue);
    String* h_string = new String(h);
    hum = *h_string;
  
    updateCurrentMode();
  
}


void updateCurrentMode() {
  
    int pin12 = digitalRead(12);
    int pin13 = digitalRead(13);
    
    if (pin12 == 0) {
      if (pin13 == 0) {
        currMode =  "confort";
      } else {
        currMode =  "horsgel";
      }
    } else {
      if (pin13 == 0) {
        currMode =  "arret";
      } else {
        currMode =  "eco";
      }
    }

    ThingPropertyValue newModeValue;
    newModeValue.string = &currMode;
    heaterMode.setValue(newModeValue);
  
}

void changeMode(String* mode) {
  
    int pin12 = digitalRead(12);
    int pin13 = digitalRead(13);

    if (*mode == "confort") {
        digitalWrite(12,0);
        digitalWrite(13,0);
    }else if (*mode == "horsgel") {
        digitalWrite(12,0);
        digitalWrite(13,1);
    } else  if (*mode == "arret") {
        digitalWrite(12,1);
        digitalWrite(13,0);
    } else if (*mode == "eco") {
        digitalWrite(12,1);
        digitalWrite(13,1);
    }
  
}
