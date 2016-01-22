/*
 Copyright (C) 2011 J. Coliz <maniacbug@ymail.com>

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 version 2 as published by the Free Software Foundation.
 */

/**
 * Example for Getting Started with nRF24L01+ radios. 
 *
 * This is an example of how to use the RF24 class.  Write this sketch to two 
 * different nodes.  Put one of the nodes into 'transmit' mode by connecting 
 * with the serial monitor and sending a 'T'.  The ping node sends the current 
 * time to the pong node, which responds by sending the value back.  The ping 
 * node can then see how long the whole cycle took.
 */
 // the LED.h include requires Wiring API at compilation
 // Also to avoid compilation error " WProgram.h not found"
 // replace the corresponding include with Arduino.h in the LED.h file
#include <LED.h>
#include <SPI.h>
#include <NewPing.h>
#include "nRF24L01.h"
#include "RF24.h"
#include "printf.h"

//
// Hardware configuration
//

// Set up nRF24L01 radio on SPI bus plus pins 9 & 10 

RF24 radio(9,10);


NewPing x_sonar(7,8);
NewPing y_sonar(2,4);

LED txOkLed = LED(3);

const unsigned long changeDetectionResCm =  1;

typedef struct payload_t
{
  uint32_t x_uS;
  uint32_t y_uS;
} Payload;

const uint8_t payloadSize = sizeof(payload_t);
uint8_t payloadBuffer[payloadSize];

unsigned long x_cm = 0;
unsigned long y_cm = 0;
//
// Topology
//

// Radio pipe addresses for the 2 nodes to communicate.
const uint64_t pipes[2] = { 0xF0F0F0F0E1LL, 0xF0F0F0F0D2LL };

//
// Role management
//
// Set up role.  This sketch uses the same software for all the nodes
// in this system.  Doing so greatly simplifies testing.  
//

// The various roles supported by this sketch
typedef enum { role_ping_out = 1, role_pong_back } role_e;

// The debug-friendly names of those roles
const char* role_friendly_name[] = { "invalid", "Ping out", "Pong back"};

// The role of the current running sketch
role_e role = role_ping_out;

void setup(void)
{
  //
  // Print preamble
  //

  Serial.begin(57600);
  printf_begin();
  printf("\n\rRF24/examples/GettingStarted/\n\r");
  printf("ROLE: %s\n\r",role_friendly_name[role]);
  printf("*** PRESS 'T' to begin transmitting to the other node\n\r");

  //
  // Setup and configure rf radio
  //

  radio.begin();

  // optionally, increase the delay between retries & # of retries
  radio.setRetries(15,15);

  // optionally, reduce the payload size.  seems to
  // improve reliability
  //radio.setPayloadSize(8);

  //
  // Open pipes to other nodes for communication
  //

  // This simple sketch opens two pipes for these two nodes to communicate
  // back and forth.
  // Open 'our' pipe for writing
  // Open the 'other' pipe for reading, in position #1 (we can have up to 5 pipes open for reading)

  //if ( role == role_ping_out )
  {
    //radio.openWritingPipe(pipes[0]);
    radio.openReadingPipe(1,pipes[1]);
  }
  //else
  {
    //radio.openWritingPipe(pipes[1]);
    //radio.openReadingPipe(1,pipes[0]);
  }

  //
  // Start listening
  //

  radio.startListening();

  //
  // Dump the configuration of the rf unit for debugging
  //

  radio.printDetails();
}

void loop(void)
{
  
  bool posChanged = isPositionChanged();
  if(posChanged){
    radio.stopListening();

    // Take the time, and send it.  This will block until complete
    //unsigned long time = millis();
    printf("Now sending X: %lu cm, Y: %lu cm \n\r",x_cm, y_cm);
    Payload pld = {x_cm, y_cm};
    bool ok = radio.write( &pld, sizeof(payload_t) );
    
    if (ok){
      printf("ok...");
      // this following call is blocking (140 ms), need to improve this later
      txOkLed.blink(140,2);
    }
    else
      printf("failed.\n\r");

    // Now, continue listening
    radio.startListening();

    // Wait here until we get a response, or timeout (250ms)
    unsigned long started_waiting_at = millis();
    bool timeout = false;
    while ( ! radio.available() && ! timeout )
      if (millis() - started_waiting_at > 200 )
        timeout = true;

    // Describe the results
    if ( timeout )
    {
      printf("Failed, response timed out.\n\r");
    }
    else
    {
      // Grab the response, compare, and send to debugging spew
      //unsigned long got_time;
      Payload ackPld;
      radio.read( &ackPld, sizeof(payload_t) );

      // Spew it
      printf("Got response with payload, X: %lu cm, Y: %lu cm\n\r",ackPld.x,ackPld.y);
      }
  }
  delay(1000);
}

boolean isPositionChanged(){
  boolean isChanged = false;
  unsigned long x = x_sonar.ping_median();
  unsigned long y = y_sonar.ping_median();
  x = x/US_ROUNDTRIP_CM;
  y = y/US_ROUNDTRIP_CM;
  int dX = abs(x - x_cm);
  int dY = abs(y - y_cm);
  if (dX > changeDetectionResCm){
    isChanged = true;
    x_cm = x;
  }
  if (dY > changeDetectionResCm){
    isChanged = true;
    y_cm = y;
  }

  return isChanged;
  
}
// vim:cin:ai:sts=2 sw=2 ft=cpp
