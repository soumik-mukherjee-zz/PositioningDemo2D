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

#include <SPI.h>
#include "nRF24L01.h"
#include "RF24.h"
#include "printf.h"

//
// Hardware configuration
//

// Set up nRF24L01 radio on SPI bus plus pins 9 & 10 

RF24 radio(9,10);


typedef struct payload_t
{
  unsigned long x;
  unsigned long y;
} Payload;

//
// Topology
//

// Radio pipe addresses for the 2 nodes to communicate.
const uint64_t pipe = 0xABCDABCD71LL;


void setup(void)
{
  //
  // Print preamble
  //

  Serial.begin(57600);
  printf_begin();
  printf("\n\rStarting Position Reciever\n\r");
  
  //
  // Setup and configure rf radio
  //

  radio.begin();

  // optionally, increase the delay between retries & # of retries
  radio.setRetries(15,15);

  // optionally, reduce the payload size.  seems to
  // improve reliability
  //radio.setPayloadSize(8);

 radio.openReadingPipe(1,pipe);
  
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
  
// if there is data ready
    if ( radio.available() )
    {
        // Dump the payloads until we've gotten everything
        Payload payload;
        bool done = false;
        while (!done)
        {
          // Fetch the payload, and see if this was the last one.
          done = radio.read( &payload, sizeof(Payload) );
  
          // Spew it
          printf("Got payload! X: %lu cm, Y: %lu cm",payload.x, payload.y);
  
    
        }
      }
    // Delay just a little bit to let the other unit
    // make the transition to receiver
    delay(1000);
}


// vim:cin:ai:sts=2 sw=2 ft=cpp
