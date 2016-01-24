/*
 * Filename: smart-object.ino
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

/**
 *
 *
 */
 // the LED.h include requires Wiring API at build
 // Also to avoid compilation error " WProgram.h not found"
 // replace the corresponding include with Arduino.h in the LED.h file
  #include <LED.h>
  #include <SPI.h>
  #include "nRF24L01.h"
  #include "RF24.h"
  #include "printf.h"
  #include <NewPing.h>
  #include <positioning2D.h>
  #define CHANGE_DETECT_RES_US 28 // in micro seconds, equivalent to 0.5 cm

  //
  // Hardware configuration
  //
  NewPing x_sonar(7,8);
  NewPing y_sonar(2,4);
  LED statusLed = LED(3);
  uint32_t x_us = 0;
  uint32_t y_us = 0;

  // Set up nRF24L01 radio on SPI bus plus pins 9 & 10
  RF24 radio(9,10);
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
  role_e role = role_pong_back;

  byte currGwPingCnt = 0;

  void setup(void)
  {
    //
    // Print preamble
    //

    Serial.begin(57600);
    printf_begin();
    printf("\n\rStarting Smart Object\n\r");
    printf("ROLE: %s\n\r",role_friendly_name[role]);
    //printf("*** PRESS 'T' to begin transmitting to the other node\n\r");

    //
    // Setup and configure rf radio
    //

    radio.begin();

    // optionally, increase the delay between retries & # of retries
    radio.setRetries(15,15);
    radio.openReadingPipe(1,pipes[1]);
    radio.startListening();

    //
    // Dump the configuration of the rf unit for debugging
    //

    radio.printDetails();
  }

  void loop(void)
  {
    //
    // Ping out role.  Repeatedly send the current time
    //
    if (currGwPingCnt == MAX_GW_PING_CNT)
    {
      // Allow enough time for gateway to switch back to reciever role
      statusLed.blink(500,4);
      delay(1500);
      changeToTransmitter();
      currGwPingCnt = 0;
    }
    if (role == role_ping_out)
    {
        boolean changedPos = isPositionChanged();
        if (changedPos)
        {
            Header payloadAck;
            Coordinates payload;
            payload.x_uS = x_us;
            payload.y_uS = y_us;
            // First, stop listening so we can talk.
            radio.stopListening();

            // Take the time, and send it.  This will block until complete
            //unsigned long time = millis();
            printf("Now sending X = %lu uS, Y = %lu uS \n\r",payload.x_uS, payload.y_uS);
            bool ok = radio.write( &payload, sizeof(payload) );

            if (ok)
            {
                printf("ok...");
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
                radio.read( &payloadAck, sizeof(payloadAck) );

                // Spew it
                printf("Got payload response %u\n\r",payloadAck.id);
                statusLed.blink(140,2);
            }

        }

      // Try again 1s later
      delay(1000);
    }

    //
    // Pong back role.  Receive each packet, dump it out, and send it back
    //

    if ( role == role_pong_back )
    {
        Header pingd, ack;
        // if there is data ready
        if ( radio.available() )
        {
            // Dump the payloads until we've gotten everything
            //unsigned long got_time;
            bool done = false;
            while (!done)
            {
                // Fetch the payload, and see if this was the last one.
                done = radio.read( &pingd, sizeof(pingd) );

                // Spew it
                printf("Got ping ACK %u...",pingd.id);
                currGwPingCnt ++;
              	// Delay just a little bit to let the other unit
              	// make the transition to receiver
              	delay(20);
            }

            // First, stop listening so we can talk
            radio.stopListening();
            ack.id = HDR_PAIRING_PING_RX_ACK;
            // Send the final one back.
            radio.write( &ack, sizeof(ack) );
            printf("Sent ping ACK.\n\r");

            // Now, resume listening so we catch the next packets.
            radio.startListening();
        }
    }

    //
    // Change roles
    //

//    if ( Serial.available() )
//    {
//      char c = toupper(Serial.read());
//      if ( c == 'T' && role == role_pong_back )
//      {
//          changeToTransmitter();
//      }
//      else if ( c == 'R' && role == role_ping_out )
//      {
//          changeToReciever();
//      }
//    }
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
  boolean isPositionChanged()
  {
        boolean isChanged = false;
        uint32_t x = x_sonar.ping_median();
        uint32_t y = y_sonar.ping_median();
        uint8_t dX = abs(x - x_us);
        uint8_t dY = abs(y - y_us);
        if (dX > CHANGE_DETECT_RES_US)
        {
            isChanged = true;
            x_us = x;
        }
        if (dY > CHANGE_DETECT_RES_US)
        {
            isChanged = true;
            y_us = y;
        }
        return isChanged;
  }
