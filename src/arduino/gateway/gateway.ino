/*
 * Filename: gateway.ino
 *
 * Copyright (C) 2016 Soumik Mukherjee <miky.mukherjee@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

  #include <SPI.h>
  #include "nRF24L01.h"
  #include "RF24.h"
  #include "printf.h"
  #include <positioning2D.h>
  //
  // Hardware configuration
  //

  // Set up nRF24L01 radio on SPI bus plus pins 9 & 10

  RF24 radio(9,10);

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


    const uint8_t payloadSize = sizeof(Coordinates);
    uint8_t payloadBuffer[payloadSize];
    byte currGwPingCnt = 0;

  void setup(void)
  {
    //
    // Print preamble
    //

    Serial.begin(57600);
    printf_begin();
    printf("\n\r Starting Gateway Reciever\n\r");

    //
    // Setup and configure rf radio
    //

    radio.begin();
    radio.setRetries(15,15);
    radio.openReadingPipe(1,pipes[1]);
    radio.startListening();
    //radio.printDetails();
  }

  void loop(void)
  {
      //
      // Ping out role.  Repeatedly send the current time
      //
      if (currGwPingCnt == MAX_GW_PING_CNT)
      {
          changeToReciever ();
          currGwPingCnt = 0;
      }
      if (role == role_ping_out)
      {
          Header pingD, ack;
          // First, stop listening so we can talk.
          radio.stopListening();

          // Take the time, and send it.  This will block until complete
          pingD.id = HDR_PAIRING_PING_TX;
          printf("Now sending ping %u...",pingD.id);
          bool ok = radio.write( &pingD, sizeof(pingD) );

          if (ok)
          printf("ok...");
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
              radio.read( &ack, sizeof(ack) );

              // Spew it
              printf("Got ping ACK %u\n\r",ack.id);
          }
          currGwPingCnt ++;
          // Try again 1s later
          delay(1000);
      }

      //
      // Pong back role.  Receive each packet, dump it out, and send it back
      //

      if ( role == role_pong_back )
      {
          Coordinates payload;
          Header payloadAck;
          // if there is data ready
          if ( radio.available() )
          {
              // Dump the payloads until we've gotten everything
              //unsigned long got_time;
              bool done = false;
              while (!done)
              {
                  // Fetch the payload, and see if this was the last one.
                  done = radio.read( &payload, sizeof(payload) );

                  // Spew it
                  printf("Got payload, X = %lu uS, Y = %lu uS",payload.x_uS,payload.y_uS);

                	// Delay just a little bit to let the other unit
                	// make the transition to receiver
                	delay(20);
              }
              payloadAck.id = HDR_COORD_RX_ACK;
              // First, stop listening so we can talk
              radio.stopListening();

              // Send the final one back.
              radio.write( &payloadAck, sizeof(payloadAck) );
              printf("Sent payload ACK.\n\r");

              // Now, resume listening so we catch the next packets.
              radio.startListening();
          }
      }

      //
      // Change roles
      //

      if ( Serial.available() )
      {
          char c = toupper(Serial.read());
          if ( c == 'T' && role == role_pong_back )
          {
              printf("*** CHANGING TO TRANSMIT ROLE -- PRESS 'R' TO SWITCH BACK\n\r");

              // Become the primary transmitter (ping out)
              role = role_ping_out;
              radio.openWritingPipe(pipes[0]);
              radio.openReadingPipe(1,pipes[1]);
          }
          else if ( c == 'R' && role == role_ping_out )
          {
              printf("*** CHANGING TO RECEIVE ROLE -- PRESS 'T' TO SWITCH BACK\n\r");

              // Become the primary receiver (pong back)
              role = role_pong_back;
              radio.openWritingPipe(pipes[1]);
              radio.openReadingPipe(1,pipes[0]);
          }
      }
  }
  void changeToReciever()
  {
      printf("*** CHANGING TO RECEIVE ROLE -- PRESS 'T' TO SWITCH BACK\n\r");

      // Become the primary receiver (pong back)
      role = role_pong_back;
      radio.openWritingPipe(pipes[1]);
      radio.openReadingPipe(1,pipes[0]);
  }
  void changeToTransmitter()
  {
      printf("*** CHANGING TO TRANSMIT ROLE -- PRESS 'R' TO SWITCH BACK\n\r");

      // Become the primary transmitter (ping out)
      role = role_ping_out;
      radio.openWritingPipe(pipes[0]);
      radio.openReadingPipe(1,pipes[1]);
  }
