#include <ESP8266WiFi.h>          //ESP8266 Core WiFi Library (you most likely already have this in your sketch)
#include <DNSServer.h>            //Local DNS Server used for redirecting all requests to the configuration portal
#include <ESP8266WebServer.h>     //Local WebServer used to serve the configuration portal
#include <WiFiManager.h>          //https://github.com/tzapu/WiFiManager WiFi Configuration Magic

#include <Adafruit_Sensor.h>
#include <DHT.h> 

#define DHTPIN 14     // what digital pin we're connected to
#define DHTTYPE DHT22


// Create an instance of the server
// specify the port to listen on as an argument
WiFiServer server(80);

DHT dht(DHTPIN, DHTTYPE);

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
  WiFiManager wifiManager;

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
 
  
  // Start the server
  server.begin();
  Serial.println("Server started");
  
  // Print the IP address
  Serial.println(WiFi.localIP());


   dht.begin();
}

void loop() {
  // Check if a client has connected
  WiFiClient client = server.available();
  if (!client) {
    return;
  }
  
  // Wait until the client sends some data
  Serial.println("new client");
  while(!client.available()){
    delay(1);
  }
  
  // Read the first line of the request
  String req = client.readStringUntil('\r');
  Serial.println(req);
  client.flush();
  
  // Match the request
  
  if (req.indexOf("/mode/confort") != -1){        // Confort
    digitalWrite(12,0);
    digitalWrite(13,0);
    client.flush();
    client.print("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<!DOCTYPE HTML>\r\n<html>\r\nRadiateur en mode CONFORT </html>\n");
  }
  else if (req.indexOf("/mode/horsgel") != -1){        // hors-gel
    digitalWrite(12,0);
    digitalWrite(13,1); // only negative
    client.flush();
    client.print("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<!DOCTYPE HTML>\r\n<html>\r\nRadiateur en mode HORSGEL </html>\n");
  }
  else if (req.indexOf("/mode/arret") != -1){        // arret
    digitalWrite(12,1);// only positive
    digitalWrite(13,0);
    client.flush();
    client.print("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<!DOCTYPE HTML>\r\n<html>\r\nRadiateur en mode ARRET </html>\n");
  }
  else if (req.indexOf("/mode/eco") != -1){        // eco
    digitalWrite(12,1);
    digitalWrite(13,1);
    client.flush();
    client.print("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<!DOCTYPE HTML>\r\n<html>\r\nRadiateur en mode ECO </html>\n");
  }
  else if (req.indexOf("/infos/") != -1){        // infos
    float t = dht.readTemperature();
    char t_buff[4];
    String t_string = "";
    dtostrf(t, 2, 2, t_buff);
    t_string += t_buff;

    float h = dht.readHumidity();
    char h_buff[4];
    String h_string = "";
    dtostrf(h, 2, 2, h_buff);
    h_string += h_buff;

    String mode_string;
    int pin12 = digitalRead(12);
    int pin13 = digitalRead(13);
    
    if (pin12 == 0) {
      if (pin13 == 0) {
        mode_string =  "confort";
      } else {
        mode_string =  "horsgel";
      }
    } else {
      if (pin13 == 0) {
        mode_string =  "arret";
      } else {
        mode_string =  "eco";
      }
    }
    
    client.flush();
    client.print("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n{'temperature' :"+t_string+", 'humidity' :"+h_string+", 'mode' : '"+mode_string+"'}\n");
  }
  else {
    Serial.println("invalid request");
    client.stop();
    return;
  }

  delay(1);
  Serial.println("Client disonnected");

}
