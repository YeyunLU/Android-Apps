#include <Arduino.h>
#include <SPI.h>
#include "Adafruit_BLE.h"
#include "pitches.h"
#include "Adafruit_BluefruitLE_SPI.h"
#include "Adafruit_BluefruitLE_UART.h"
#include <Adafruit_CircuitPlayground.h>
#include "BluefruitConfig.h"

#if SOFTWARE_SERIAL_AVAILABLE
  #include <SoftwareSerial.h>
#endif

# define Second 1000

// Strings to compare incoming BLE messages
String start = "start";
String green = "green";
String blue = "blue";
String red = "red";
String readtemp = "readtemp";
String stp = "stop";
String on = "BUZZON";
String off = "BUZZOFF";
int idx = 0;
int durTime =10; // default 10s for timer
int colors[3][3] = {{255,0,0},
                 {0,255,0},
                 {0,0,255}
                };
 
int  sensorTemp = 0;
boolean buzz = false;
int melody[] = {                            // specific notes in the melody
NOTE_E7, NOTE_E7, 0, NOTE_E7,
  0, NOTE_C7, NOTE_E7, 0,
  NOTE_G7, 0, 0,  0,
  NOTE_G6, 0, 0, 0,
 
  NOTE_C7, 0, 0, NOTE_G6,
  0, 0, NOTE_E6, 0,
  0, NOTE_A6, 0, NOTE_B6,
  0, NOTE_AS6, NOTE_A6, 0,
 
  NOTE_G6, NOTE_E7, NOTE_G7,
  NOTE_A7, 0, NOTE_F7, NOTE_G7,
  0, NOTE_E7, 0, NOTE_C7,
  NOTE_D7, NOTE_B6, 0, 0,
 
  NOTE_C7, 0, 0, NOTE_G6,
  0, 0, NOTE_E6, 0,
  0, NOTE_A6, 0, NOTE_B6,
  0, NOTE_AS6, NOTE_A6, 0,
 
  NOTE_G6, NOTE_E7, NOTE_G7,
  NOTE_A7, 0, NOTE_F7, NOTE_G7,
  0, NOTE_E7, 0, NOTE_C7,
  NOTE_D7, NOTE_B6, 0, 0  
 };
int numNotes; // Number of notes in the melody

int noteDurations[] = {     // note durations
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
 
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
 
  9, 9, 9,
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
 
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
 
  9, 9, 9,
  12, 12, 12, 12,
  12, 12, 12, 12,
  12, 12, 12, 12,
};

/*=========================================================================
    APPLICATION SETTINGS
    -----------------------------------------------------------------------*/
    #define FACTORYRESET_ENABLE         0
    #define MINIMUM_FIRMWARE_VERSION    "0.6.6"
    #define MODE_LED_BEHAVIOUR          "MODE"
/*=========================================================================*/

// Create the bluefruit object, either software serial...uncomment these lines

Adafruit_BluefruitLE_UART ble(BLUEFRUIT_HWSERIAL_NAME, BLUEFRUIT_UART_MODE_PIN);

/* ...hardware SPI, using SCK/MOSI/MISO hardware SPI pins and then user selected CS/IRQ/RST */
// Adafruit_BluefruitLE_SPI ble(BLUEFRUIT_SPI_CS, BLUEFRUIT_SPI_IRQ, BLUEFRUIT_SPI_RST);

/* ...software SPI, using SCK/MOSI/MISO user-defined SPI pins and then user selected CS/IRQ/RST */
//Adafruit_BluefruitLE_SPI ble(BLUEFRUIT_SPI_SCK, BLUEFRUIT_SPI_MISO,
//                             BLUEFRUIT_SPI_MOSI, BLUEFRUIT_SPI_CS,
//                             BLUEFRUIT_SPI_IRQ, BLUEFRUIT_SPI_RST);


// A small helper
void error(const __FlashStringHelper*err) {
  Serial.println(err);
  while (1);
}

/**************************************************************************/
/*!
    @brief  Sets up the HW an the BLE module (this function is called
            automatically on startup)
*/
/**************************************************************************/
void setup(void)
{
  CircuitPlayground.begin();
  numNotes = sizeof(melody)/sizeof(int);  // number of notes we are playing
  CircuitPlayground.clearPixels();

  Serial.begin(115200);
  Serial.println(F("Adafruit Bluefruit Command <-> Data Mode Example"));
  Serial.println(F("------------------------------------------------"));

  /* Initialise the module */
  Serial.print(F("Initialising the Bluefruit LE module: "));

  if ( !ble.begin(VERBOSE_MODE) )
  {
    error(F("Couldn't find Bluefruit, make sure it's in CoMmanD mode & check wiring?"));
  }
  Serial.println( F("OK!") );

  if ( FACTORYRESET_ENABLE )
  {
    /* Perform a factory reset to make sure everything is in a known state */
    Serial.println(F("Performing a factory reset: "));
    if ( ! ble.factoryReset() ){
      error(F("Couldn't factory reset"));
    }
  }

  /* Disable command echo from Bluefruit */
  ble.echo(false);

  Serial.println("Requesting Bluefruit info:");
  /* Print Bluefruit information */
  ble.info();

  Serial.println(F("Please use Adafruit Bluefruit LE app to connect in UART mode"));
  Serial.println(F("Then Enter characters to send to Bluefruit"));
  Serial.println();

  ble.verbose(false);  // debug info is a little annoying after this point!

  /* Wait for connection */
  while (! ble.isConnected()) {
      delay(500);
  }

  Serial.println(F("******************************"));

  // LED Activity command is only supported from 0.6.6
  if ( ble.isVersionAtLeast(MINIMUM_FIRMWARE_VERSION) )
  {
    // Change Mode LED Activity
    Serial.println(F("Change LED activity to " MODE_LED_BEHAVIOUR));
    ble.sendCommandCheckOK("AT+HWModeLED=" MODE_LED_BEHAVIOUR);
  }

  // Set module to DATA mode
  Serial.println( F("Switching to DATA mode!") );
  ble.setMode(BLUEFRUIT_MODE_DATA);

  Serial.println(F("******************************"));

  CircuitPlayground.setPixelColor(0,255,0,0);
 
  delay(1000);
}
/**************************************************************************/
/*!
    @brief  Constantly poll for new command or response data
*/
/**************************************************************************/
void loop(void)
{
  // Save received data to string
  String received = "";
  while ( ble.available() )
  {
    int c = ble.read();
    Serial.print((char)c);
    received += (char)c;
        delay(50);
  }
  
  if (CircuitPlayground.rightButton()){
    timer(durTime);
  }
 
  if(red == received){
    Serial.println("RECEIVED RED!!!!"); 
       for(int i = 0; i < 10; i++){
      CircuitPlayground.setPixelColor(i,255, 0, 0);
    }
    idx = 0;
    delay(50);
  }
  
  else if(green == received){
      Serial.println("RECEIVED GREEN!!!!"); 
       for(int i = 0; i < 10; i++){
      CircuitPlayground.setPixelColor(i,0, 255, 0);
    }
    idx = 1;
    delay(50);  
  }

  else if(blue == received){
    Serial.println("RECEIVED BLUE!!!!"); 
       for(int i = 0; i < 10; i++){
      CircuitPlayground.setPixelColor(i,0, 0, 255);
    }
    idx = 2;
    delay(50);    
  }
  
  else if(readtemp == received){
        
    sensorTemp = CircuitPlayground.temperature(); // returns a floating point number in Centigrade
    Serial.println("Read temperature sensor"); 
    delay(10);

   //Send data to Android Device
    char output[8];
    String data = "";
    data += sensorTemp;
    Serial.println(data);
    data.toCharArray(output,8);
    ble.print("\nTemperature:\n");
    ble.print(data);
  }

  else if (on == received){
    buzz = true;
  }

  else if (off == received){
    buzz = false;
  }
 
  else if (stp == received){
      CircuitPlayground.clearPixels();
    }

  else if(start==received) {   // play when we press the right button
    // light all the LEDs one by one
    received="";
    delay(50);
    while(1){
      while (ble.available()){
        int c = ble.read();
        Serial.print((char)c);
        received += (char)c;
      }
      durTime = received.toInt();
      timer(durTime);
      break;
    }   
  }

}
void start_buzz(){
  for (int thisNote = 0; thisNote < numNotes; thisNote++) { // play notes of the melody
      // to calculate the note duration, take one second divided by the note type.
      int noteDuration = 1000 / noteDurations[thisNote];
      CircuitPlayground.playTone(melody[thisNote], noteDuration);

      // to distinguish the notes, set a minimum time between them.
      //   the note's duration + 30% seems to work well:
      int pauseBetweenNotes = noteDuration * 1.30;
      delay(pauseBetweenNotes);
    }
}
void timer(int sec){
    CircuitPlayground.clearPixels();
    int duration = Second*sec;
    for (int i =0; i<10; i++){
      CircuitPlayground.setPixelColor(i,colors[idx][0],colors[idx][1],colors[idx][2]);
      delay(duration);
    }
    for (int i=0;i<2;i++){
      CircuitPlayground.clearPixels();
      delay(100);
      for (int i =0; i<10; i++){
        CircuitPlayground.setPixelColor(i,colors[idx][0],colors[idx][1],colors[idx][2]);
      }
      delay(100);
      CircuitPlayground.clearPixels();
    }
    if(buzz==true){
      start_buzz();
    }
  
}

 
