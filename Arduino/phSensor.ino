#include <SoftwareSerial.h> 

SoftwareSerial BT(9, 10); 

void setup()  
{
  Serial.begin(9600); 
  // set digital pin to control as an output
  pinMode(LED_BUILTIN, OUTPUT);
  // set the data rate for the SoftwareSerial port
  BT.begin(9600);
  
}

char command; // stores incoming character from other device

void loop() 
{
  // if text arrived in from BT serial...
  if (BT.available())
  {
    command = (BT.read());
    if (command == '1')
    {
      // read the input on analog pin 0:
      float total = 0.0; 
      int analog = 0; 
      
      // average 100 reads 
      for(int i = 0; i < 100; i++)
      {
        analog = analogRead(A0); 
        total = total + analog;
        Serial.println(analog); 
        delay(50); 
      }
      int sensorValue = total / 100; 
      BT.print(sensorValue);
    }
  }
}
